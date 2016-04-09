package ServerV1;

import java.io.IOException;
/**
 * Κλάση που διαβάζει συνεχώς μηνύματα από τους clients είτε αυτοί είναι User είτε Device.
 * Ξεκινάει αυτή η κλάση όταν έχουν γίνει τα απαραίτητα βήματα στα socket και όταν
 * όλα έχουν δρομολογηθεί . Ουσιαστικά κάθε client-socket έχει ένα τέτοιο thread
 * για να μπορεί να πραγματοποιηθεί ταυτόχρονα αμφίδρομη επικοινωνία από και προς κάθε socket.
 * Το thread αυτό πάντα είναι σε κατάσταση διαβάσματος και έτσι αν προκληθεί μια αλλαγή από
 * κάποιο client (πχ αλλαγή τιμής μιας μονάδας κλπ) είναι σε θέση άμεσα να διαβάσει την
 * αλλαγή , να ενημερώσει το Device του Server(που αφορά την αλλαγή) και στην συνέχεια να στείλει
 * την αλλαγή που προκλήθηκε προς όλους τους client που είναι στο ίδιο "κανάλι" , δηλαδή συγχρονίζονται
 * απόλυτα το DeviceClient με κάθε άλλο UserClient που είναι συνδεδεμένο στο Server
 * και κάνει πραγματικού χρόνου εποπτεία πάνω στη συγκεκριμένη συσκευή .
 * @author Michael Galliakis
 */
public class ListenFromTheClient extends Thread {
    ManageSocketMessage manSocMess = new ManageSocketMessage() ;
    boolean running ;
    ManageSocket clientManSocket ;
    ManageDevice manDeviceThread ;
    /**
     * Αρχικοποιεί το thread με το {@link ManageSocket} του Client , αλλά και με το {@link ManageDevice}
     * της συσκευής που αφορά τον συγκεκριμένο Client .
     * @param s {@link ManageSocket} του client , είτε User είτε Device
     * @param mdt {@link ManageDevice} της συσκευής που αφορά τον συγκεκριμένο Client .
     */
    ListenFromTheClient(ManageSocket s ,ManageDevice mdt) {
        clientManSocket = s ;
        manDeviceThread = mdt ;
    }
    int UserSIDkeyLocation ;
    /**
     * Με το που ξεκινήσει το thread απλά περιμένει να διαβάσει μηνύματα από την άλλη άκρη
     * του εκάστοτε socket .
     * Για κάθε είδους μήνυμα κάνει και τις ανάλογες εργασίες .
     */
    public void run() {
        try {
            String message ;
            Tools.Debug.print("Entered the Device |"+clientManSocket.clientSystemMessage+" , Address - " + clientManSocket.clientSocket.getInetAddress().getHostName(),Tools.MSGOKEMO);
            running = true ;
            while (running) {
                try
                {
                    message = clientManSocket.in.readLine() ;
                }
                catch (Exception ex)
                {
                    if (clientManSocket.SID.equals(manDeviceThread.deviceManSocket.SID))
                        closeAllClients("ERROR!") ;
                    else
                    {
                        UserSIDkeyLocation = ClientServiceThread.alAllUsers.indexOf(clientManSocket.SID);
                        if (UserSIDkeyLocation!=-1)
                            ClientServiceThread.alAllUsers.remove(UserSIDkeyLocation) ;
                    }
                    running = false ;
                    break ;
                }
                manSocMess.reload(message);
                switch (manSocMess.getCommand())
                {
                    case ("ERROR") :
                        Tools.printSMError("Left the Device : " + manDeviceThread.deviceManSocket.SID,"ID : " + clientManSocket.SID ,manSocMess.getMessage());
                        if (clientManSocket.SID.equals(manDeviceThread.deviceManSocket.SID))
                            closeAllClients("ERROR!") ;
                        else
                        {
                            UserSIDkeyLocation = ClientServiceThread.alAllUsers.indexOf(clientManSocket.SID);
                            if (UserSIDkeyLocation!=-1)
                                ClientServiceThread.alAllUsers.remove(UserSIDkeyLocation) ;
                        }
                        running = false ;
                        break ;
                    case ("Quit") :
                        Tools.printSMError("!!!Left the Device : " + manDeviceThread.deviceManSocket.SID,"ID : " + clientManSocket.SID ,manSocMess.getMessage());
                        if (clientManSocket.SID.equals(manDeviceThread.deviceManSocket.SID))
                            closeAllClients("Closed Device!") ;
                        else
                        {
                            UserSIDkeyLocation = ClientServiceThread.alAllUsers.indexOf(clientManSocket.SID);
                            if (UserSIDkeyLocation!=-1)
                                ClientServiceThread.alAllUsers.remove(UserSIDkeyLocation) ;
                        }
                        running = false ;
                        break ;
                    case ("ChangeValues") :
                        Tools.Debug.print(clientManSocket.clientSystemMessage + " Says : " + manSocMess.getMessage(),Tools.MSGOKEMO);
                        manDeviceThread.device.setValuesOfDevice(manSocMess.getMessage());
                        manDeviceThread.deviceManSocket.out.println(manSocMess.getMessage());
                        manDeviceThread.deviceManSocket.out.flush();
                        for (ManageSocket item : manDeviceThread.listWithConnectedUserClientInDevice )
                        {
                            if( item != clientManSocket )
                            {
                                item.out.println(manSocMess.getMessage());
                                item.out.flush();
                            }
                        }
                        break ;
                    case ("NewValues") :
                        Tools.Debug.print(clientManSocket.clientSystemMessage + " Says : " + manSocMess.getMessage(),Tools.MSGOKEMO);
                        manDeviceThread.device.setValuesOfDevice(manSocMess.getMessage());
                        for (ManageSocket item : manDeviceThread.listWithConnectedUserClientInDevice )
                        {
                            item.out.println(manSocMess.getMessage());
                            item.out.flush();
                        }
                        break ;
                    case ("NewController") :
                        Tools.Debug.print(clientManSocket.clientSystemMessage + " Says : " + manSocMess.getMessage(),Tools.MSGOKEMO);
                        manDeviceThread.device.addController(manSocMess.getMessage());
                        for (ManageSocket item : manDeviceThread.listWithConnectedUserClientInDevice )
                        {
                            item.out.println(manSocMess.getMessage());
                            item.out.flush();
                        }
                        break ;
                    case ("DeleteController") :
                        Tools.Debug.print(clientManSocket.clientSystemMessage + " Says : " + manSocMess.getMessage(),Tools.MSGOKEMO);
                        manDeviceThread.device.deleteController(manSocMess);
                        for (ManageSocket item : manDeviceThread.listWithConnectedUserClientInDevice )
                        {
                            item.out.println(manSocMess.getMessage());
                            item.out.flush();
                        }
                        break ;
                    case ("ChangeModes") :
                        Tools.Debug.print(clientManSocket.clientSystemMessage + " Says : " + manSocMess.getMessage(),Tools.MSGOKEMO);
                        manDeviceThread.device.setModesOfDevice(manSocMess.getMessage());
                        manDeviceThread.deviceManSocket.out.println(manSocMess.getMessage());
                        manDeviceThread.deviceManSocket.out.flush();
                        for (ManageSocket item : manDeviceThread.listWithConnectedUserClientInDevice )
                        {
                            if( item != clientManSocket )
                            {
                                item.out.println(manSocMess.getMessage());
                                item.out.flush();
                            }
                        }
                        break ;
                    default :
                        Tools.Debug.print(clientManSocket.clientSystemMessage + " Says : " + manSocMess.getMessage(),Tools.MSGERROREMO);
                }
            }
            if (clientManSocket.out!=null)
                clientManSocket.out.close();
            if (clientManSocket.in!=null)
                clientManSocket.in.close();
            UserSIDkeyLocation = ClientServiceThread.alAllUsers.indexOf(clientManSocket.SID);
            if (UserSIDkeyLocation!=-1)
                ClientServiceThread.alAllUsers.remove(UserSIDkeyLocation) ;
            manDeviceThread.listWithConnectedUserClientIDsInDevice.remove(clientManSocket.SID) ;
            manDeviceThread.listWithConnectedUserClientInDevice.remove(clientManSocket) ;
            clientManSocket.close() ;
            Tools.Debug.print("Left the Device |"+clientManSocket.clientSystemMessage+" , Address - " + clientManSocket.clientSocket.getInetAddress().getHostName(),Tools.MSGOKEMO);
        } catch (Exception e) {
            Tools.Debug.printError(e);
        }
    }
    /**
     * Κλείνει όσους τους client που βρίσκονται στο ίδιο "κανάλι" .
     * Αυτό γίνεται συνήθως όταν χαθεί η σύνδεση με το DeviceClient του
     * συγκεκριμένου καναλιού και πρέπει αναγκαστικά να κλείσουν και όλοι
     * οι UserClients που κάνουν εκείνη τη στιγμή εποπτεία στη συσκευή .
     * @param mess Ένα μήνυμα που θα σταλεί στους clients που θα κλείσουν
     * @throws IOException
     */
    private void closeAllClients(String mess) throws IOException
    {
        for (ListenFromTheClient thread : manDeviceThread.lftcThreads)
        {
            if (thread != this)
            {
                thread.stop() ;
                thread = null ;
            }
        }
        //Υπάρχει bug!
        for (ManageSocket item : manDeviceThread.listWithConnectedUserClientInDevice )
        {
            if (item.out!=null)
            {
                item.out.close();
                item.out.println("#S$Quit:1*("+mess+")");
                item.out.flush();
            }
            if (item.in!=null)
                item.in.close();
            UserSIDkeyLocation = ClientServiceThread.alAllUsers.indexOf(item.SID);
            if (UserSIDkeyLocation!=-1)
                ClientServiceThread.alAllUsers.remove(UserSIDkeyLocation) ;
            item.close() ;
        }
        ManageDevice.listWithConnectedCDeviceIDs.remove(clientManSocket.SID) ;
        manDeviceThread.listWithConnectedUserClientIDsInDevice.clear();
        manDeviceThread.listWithConnectedUserClientInDevice.clear();
        manDeviceThread.lftcThreads.clear();
        ManageDevice.listWithConnectedDevices.remove(manDeviceThread) ;
        ClientServiceThread.hmUserSockets.remove(ClientServiceThread.hmUserSockets.get(clientManSocket.DBID)) ;
        ClientServiceThread.sendAllUserNewDevice(manDeviceThread.deviceManSocket,clientManSocket.DBID) ;
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