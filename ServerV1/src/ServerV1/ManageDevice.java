package ServerV1;

import java.util.ArrayList;
/**
 * Είναι μια κλάση που μας βοηθάει στο να διαχειριζόμαστε ένα DeviceClient που
 * είναι συνδεδεμένο στο server μας όπως και τα UserClient που έχουν να κάνουν
 * με τη συγκεκριμένο DeviceClient και είναι και αυτά ταυτόχρονα συνδεδεμένα στο Server μας.
 * @author Michael Galliakis
 */
public class ManageDevice {
    
    static public ArrayList<ManageDevice> listWithConnectedDevices = new ArrayList<>() ;
    static public ArrayList<String> listWithConnectedCDeviceIDs = new ArrayList<>();
    
    public ArrayList<ManageSocket> listWithConnectedUserClientInDevice = new ArrayList<>();
    public ArrayList<String> listWithConnectedUserClientIDsInDevice = new ArrayList<>();
    
    public Device device ;
    
    public ManageSocket deviceManSocket ;
    
    
    ManageSocketMessage msm = new ManageSocketMessage() ;
    boolean running ;
    ArrayList<ListenFromTheClient> lftcThreads = new ArrayList<>() ;
    
    
    /**
     * χρησιμοποιείται στην δημιουργία ενός ManageDevice και μόνο από ένα socket
     * ενός DeviceClient, με σκοπό να δημιουργηθεί ένα "κανάλι επικοινωνίας" ανάμεσα
     * στο ίδιο το DeviceClient και στα μελλοντικά UserClient που θα κάνουν
     * εποπτεία πάνω του .
     * @param ms Ένα {@link ManageSocket} ενός νέου DeviceClient .
     */
    ManageDevice(ManageSocket ms) {
        try {
            deviceManSocket = ms ;
            
            if (initControllersProccess().equals("OK"))
            {
                listWithConnectedDevices.add(this);
                listWithConnectedCDeviceIDs.add(ms.SID) ;
                ListenFromTheClient tmpLftcTread = new ListenFromTheClient(deviceManSocket,this) ;
                tmpLftcTread.start() ;
                lftcThreads.add(tmpLftcTread);
                ClientServiceThread.sendAllUserNewDevice(deviceManSocket,deviceManSocket.DBID);
            }
            else
            {
                Tools.Debug.print("Device Client |"+deviceManSocket.clientSystemMessage+ " initControllersProccess Failed!",Tools.MSGERROREMO);
            }
        } catch (Exception ex)
        {
            Tools.Debug.printError(ex);
        }
    }
    /**
     * Μέθοδος που προσθέτει ένα socket ενός UserClient στο ίδιο "κανάλι επικοινωνίας"
     * με αυτό του DeviceClient, για να μπορεί να πραγματοποιηθεί εποπτεία .
     * @param ms Ένα {@link ManageSocket} ενός νέου UserClient .
     */
    public void add(ManageSocket ms) {
        try
        {
            if (sendUserControllersProccess(ms).equals("OK"))
            {
                listWithConnectedUserClientInDevice.add(ms);
                ListenFromTheClient tmpLftcTread = new ListenFromTheClient(ms,this) ;
                tmpLftcTread.start() ;
                lftcThreads.add(tmpLftcTread);
            }
        } catch (Exception ex)
        {
            Tools.Debug.printError(ex);
        }
    }
    /**
     * Μέθοδος που στέλνει σε κάποιο νέο UserClient κάποια μηνύματα αρχικοποίησης
     * της συσκευής μας για να μπορέσει ο UserClient να ξέρει την κατάσταση του
     * Device μας και να ξεκινήσει την εποπτεία μας .
     * @param ms Ένα {@link ManageSocket} ενός νέου UserClient .
     * @return Επιστρέφει "OK" αν όλα πήγαν καλά και "X" αν κάτι δεν πήγε καλά .
     */
    private String sendUserControllersProccess(ManageSocket ms)
    {
        try {
            ArrayList<String> alNewControllers = device.getNewControllerMessages() ;
            Tools.send(alNewControllers.get(0),ms.out);
            msm.reload(ms.in.readLine()) ;
            
            if (Tools.isCorrectCommand(msm.getCommand(),"Answer",deviceManSocket.clientSystemMessage,msm.getMessage(),deviceManSocket.out))
            {
                if ("FAILED".equals(msm.getParameters().get(0).get(0)))
                {
                    Tools.send(Globals.messageFailed, ms.out);
                    return "X" ;
                }
                else
                    Tools.Debug.print("Device Client |"+deviceManSocket.clientSystemMessage+ "  send User Init Controllers OK!",Tools.MSGOKEMO);
            }
            else
                return "X" ;
            for (int i = 1 ; i < alNewControllers.size();i++)
                Tools.send(alNewControllers.get(i), ms.out);
            Tools.send("#S$InitializationFinished:1*(OK);", ms.out);
            
            msm.reload(ms.in.readLine()) ;
            Tools.Debug.print(msm.getMessage(),Tools.MSGOKEMO);
            return "OK" ;
        } catch (Exception ex)
        {
            Tools.Debug.printError(ex);
            return "X" ;
        }
    }
    /**
     * Μέθοδος που διαβάζει από ένα νέο DeviceClient κάποια μηνύματα αρχικοποίησης
     * για να αρχικοποιήσουμε το εκάστοτε νέο Device που πρωτοσυνδέεται στο Server μας .
     * @return Επιστρέφει "OK" αν όλα πήγαν καλά και "X" αν κάτι δεν πήγε καλά .
     */
    private String initControllersProccess()
    {
        try{
            ArrayList<String> alControllers = new ArrayList<>();
            msm.reload(deviceManSocket.in.readLine()) ;
            if (Tools.isCorrectCommand(msm.getCommand(),"InitControllers",deviceManSocket.clientSystemMessage,msm.getMessage(),deviceManSocket.out))
            {
                Tools.Debug.print("Device Client |"+deviceManSocket.clientSystemMessage+ " InitControllers OK!",Tools.MSGSYSTEMOKEMO);
                Tools.send(Globals.messageOK, deviceManSocket.out);
            }
            else
                return "X" ;
            int sum  = msm.getSumParameters() ;
            for (int i = 0 ; i < sum ;i++)
            {
                msm.reload(deviceManSocket.in.readLine()) ;
                if (Tools.isCorrectCommand(msm.getCommand(),"NewController",deviceManSocket.clientSystemMessage,msm.getMessage(),deviceManSocket.out))
                {
                    alControllers.add(msm.getMessage()) ;
                    Tools.Debug.print("Device Client |"+deviceManSocket.clientSystemMessage+ " NewController OK!",Tools.MSGSYSTEMOKEMO);
                    Tools.send(Globals.messageOK, deviceManSocket.out);
                }
                else
                    return "X" ;
            }
            Tools.Debug.print("Device Client |"+deviceManSocket.clientSystemMessage+ " put Initialization Finished!",Tools.MSGSYSTEMOKEMO);
            Tools.send("#S$InitializationFinished:1*(OK);", deviceManSocket.out);
            msm.reload(deviceManSocket.in.readLine()) ;
            if (Tools.isCorrectCommand(msm.getCommand(),"Answer",deviceManSocket.clientSystemMessage,msm.getMessage(),deviceManSocket.out))
            {
                if ("FAILED".equals(msm.getParameters().get(0).get(0)))
                {
                    Tools.Debug.print("Device Client |"+deviceManSocket.clientSystemMessage+ " get Initialization Failed!",Tools.MSGERROREMO);
                    return "X";
                }
                else
                {
                    device = new Device(null,null,deviceManSocket.DBID,deviceManSocket.SID) ;
                    device.prepareDevice(alControllers);
                    Tools.Debug.print("Device Client |"+deviceManSocket.clientSystemMessage+ " get Initialization Finished!",Tools.MSGOKEMO);
                }
            }
            else
                return "X" ;
            return "OK" ;
        } catch (Exception ex)
        {
            Tools.Debug.printError(ex);
            return "X" ;
        }
    }
}
/*
* * * * * * * * * * * * * * * * * * * * * * *
* + + + + + + + + + + + + + + + + + + + + + *
* +- - - - - - - - - - - - - - - - - - - -+ *
* +| P P P P    M M     M M    G G G G   |+ *
* +| P      P   M  M   M  M   G       G  |+ *
* +| P P P p    M   M M   M  G           |+ *
* +| P          M    M    M  G    G G G  |+ *
* +| P          M         M   G       G  |+ *
* +| P        ® M         M ®  G G G G   |+ *
* +- - - - - - - - - - - - - - - - - - - -+ *
* +           .----.   @   @             |+ * 
* +          / .-"-.`.  \v/              |+ * 
* +          | | '\ \ \_/ )              |+ * 
* +        ,-\ `-.' /.'  /               |+ * 
* +       '---`----'----'                |+ *         
* +- - - - - - - - - - - - - - - - - - - -+ *
* + + + + + + + + + + + + + + + + + + + + + *
* +- - - - - - - - - - - - - - - - - - - -+ *
* +|    Thesis Michael Galliakis 2016    |+ *
* +| Program m_g ; -) cs081001@teiath.gr |+ *
* +|     TEI Athens - IT department.     |+ *
* +|       michaelgalliakis@yahoo.gr     |+ *
* +| https://github.com/michaelgalliakis |+ *
* +|           (myThesis.git)            |+ *
* +- - - - - - - - - - - - - - - - - - - -+ *
* + + + + + + + + + + + + + + + + + + + + + *
* * * * * * * * * * * * * * * * * * * * * * *
*/