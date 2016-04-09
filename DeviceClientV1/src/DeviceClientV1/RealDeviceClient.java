package DeviceClientV1;

import gnu.io.CommPortIdentifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Κλάση από την οποία ξεκινάει ο Device Client
 * Ουσιαστικά ο Device Client είναι ο ενδιάμεσος μεταξύ του Server
 * και των Arduino . Δηλαδή κάνει την σύνδεση με το Server με βάση κάποια στοιχεία
 * που υπάρχουν σε ένα config αρχείο (DeviceName,Password,ip,address κ.α) και μεσολαβεί
 * για να τον ενημερώνει για τις καταστάσεις των μονάδων που  έχουν τα Arduino όπως
 * επίσης είναι διαρκώς έτοιμος να διαβάζει σε πραγματικό χρόνο τις "εντολές" για αλλαγή τιμών
 * για κάποιες μονάδες από τους συνδεδεμένους στο Server Χρήστες.
 * Και τα Arduino βέβαια επικοινωνούν και αυτά άμεσα σε πραγματικό χρόνο μέσα από
 * σειριακές εισόδους USB με το Device Client.
 * Χάρις λοιπόν τον Device Client μπορούμε να κάνουμε εποπτεία των μονάδων δικτυακά
 * δηλαδή πρακτικά από οποιοδήποτε υπολογιστή που μπορεί να επικοινωνήσει και αυτός
 * με τον Server (Που ουσιαστικά σημαίνει ακόμη και από οποιαδήποτε σημείο στον κόσμο
 * που έχει σύνδεση με το internet).
 * Ακόμη να πούμε ότι ο Device Client σε περίπτωση που χαθεί η σύνδεση με το Server
 * κάνει δοκιμές για να επανασυνδεθεί, κάθε συγκεκριμένα δευτερόλεπτα που έχουν ρυθμιστεί
 * και αυτά από το config αρχείο .
 * Και μαζί με το παραπάνω κάνει συνεχώς αναζητήσεις νέων Arduino ενώ τρέχει , δηλαδή
 * πρακτικά ενώ τρέχει το πρόγραμμα μπορούμε να προσθέσουμε κάποιο arduino στον
 * υπολογιστή και ο Device Client να το αναγνωρίσει και να το προετοιμάσει και
 * αυτό για εποπτεία . Ο χρόνος για το κάθε πότε θα κάνει αναζήτηση για
 * νέο-νέα arduino ρυθμίζεται και αυτό από το config αρχείο .
 * @author Michael Galliakis
 */
public final class RealDeviceClient {
    ListenThread myThread = null ;
    String deviceID ;
    public boolean connected = false ;
    ManageSocketMessage manSocMess = new ManageSocketMessage() ;
    private boolean firstTimePrepareArduino = true ;
    
    /**
     * Ο κατασκευαστής της κλάσης αναλαμβάνει να κάνει κάποια βήματα κυρίως για
     * να προετοιμάσει και να ξεκινήσει την σύνδεση με τον Server όπως ακόμη
     * και να συνδεθεί και να συγχρονιστεί με τα arduino για να πραγματοποιηθεί
     * η απομακρυσμένη εποπτεία όλων των μονάδων .
     */
    public RealDeviceClient()  {
        Globals.mainDeviceClient = this ;
        
        if (!Globals.loadConfig())
            System.exit(0);
        
        Globals.printConfigInfo() ;
        
        if (ConnectProcedure().equals("X"))
            System.exit(0);
        
        timer = new Timer();
        if (Globals.secondsForNextSearchArduino!=-1)
            timer.schedule(new supervisionNewArduinoTask(), 10000,Globals.secondsForNextSearchArduino*1000);
        if (Globals.secondsForNextTryConnect!=-1)
            timer.schedule(new supervisionForNextTryConnect(), 10000,Globals.secondsForNextTryConnect*1000);
    }
    
    private Timer timer ;
    
    /**
     * Μέθοδος που πρακτικά κάνει όλη τη διαδικασία για να προστεθεί ένα καινούριο Arduino
     * για πρώτη φόρα στην εποπτεία πραγματικού χρόνου .
     * @param arduinoPort Το CommPortIdentifier του Arduino που θα προστεθεί
     * @return True αν προστεθεί κανονικά ή False αν υπάρχει κάποιο σφάλμα .
     */
    public boolean addArduino(CommPortIdentifier arduinoPort)
    {
        return addArduino(arduinoPort,false) ;
    }
    /**
     * Μέθοδος που πρακτικά κάνει όλη τη διαδικασία για να προστεθεί ένα καινούριο Arduino
     * για πρώτη φόρα στην εποπτεία πραγματικού χρόνου .
     * @param arduinoPort Το CommPortIdentifier του Arduino που θα προστεθεί
     * @param isNewArduino Truε αν είναι ένα νέο arduino κατά την διάρκεια λειτουργίας
     * του προγράμματος ή False αν είναι arduino που θα προστεθεί στην αρχή που ξεκινάει το πρόγραμμα.
     * @return True αν προστεθεί κανονικά ή False αν υπάρχει κάποιο σφάλμα .
     */
    public boolean addArduino(CommPortIdentifier arduinoPort,boolean isNewArduino)
    {
        Tools.Debug.print(arduinoPort.getName()) ;
        ReadArduino main = new ReadArduino();
        if (main.initialize(arduinoPort))
        {
            String conName = main.prepare(arduinoPort) ;
            if (isNewArduino)
                main.startRead = true ;
            if (conName!=null)
            {
                if (isNewArduino && Globals.manSocket!=null)
                    Tools.send(Globals.device.getNewControllerMessage(conName), Globals.manSocket.out);
                //System.out.println(Globals.device.getNewControllerMessage(conName));
                return true ;
            }
            
        }
        return false ;
    }
    
    /**
     * Μέθοδος που βρίσκει όλα τα arduino που είναι συνδεδεμένα με τον υπολογιστή μας
     * και στην συνέχεια τα προσθέτει στην εποπτεία πραγματικού χρόνου .
     * @return True αν όλα πάνε καλά και False αν συμβεί κάποιο απρόβλεπτο σφάλμα .
     */
    public boolean prepareArduino()
    {
        try {
            Globals.hmAllArduino = ReadArduino.findAllArduinoHM() ;
            Globals.hmAllReadArduino.clear();
            Iterator<CommPortIdentifier> keyIterPort = Globals.hmAllArduino.keySet().iterator();
            while(keyIterPort.hasNext())
            {
                if (!addArduino(keyIterPort.next()))
                    return false ;
            }
        }
        catch (Exception ex)
        {
            Tools.Debug.print(ex.getMessage());
            return false ;
        }
        return true ;
    }
    
    /**
     * Μέθοδος που αναλαμβάνει να κάνει όλα τα απαραίτητα βήματα για την σύνδεση με
     * το Server και με τα arduino όπως επίσης αναλαμβάνει και το στήσιμο της εποπτείας
     * πραγματικού χρόνου όλων των μονάδων επικοινωνώντας κατάλληλα και με συγκεκριμένα
     * βήματα με το Server και τα arduino .
     * @return "OK" αν όλα πάνε καλά και False αν συμβεί κάποιο σφάλμα .
     */
    public String ConnectProcedure() {
        try
        {
            ManageSocket ms = Tools.getFullManageSocket(Globals.devicename, Globals.password, Globals.address, Globals.port);
            if (ms==null)
            { Tools.Debug.print("Connect UnsuccessFully!"); return "X" ; }
            
            Globals.manSocket = ms ;
            if (firstTimePrepareArduino)
            {
                Globals.device = new Device(Globals.devicename,Globals.password,ms.ID,ms.ID + "@D") ;
                firstTimePrepareArduino = false ;
                if (!prepareArduino())
                {
                    firstTimePrepareArduino = true ;
                    Tools.Debug.print("Connect UnsuccessFully!");
                    return "X" ;
                }
            }
            else
                Globals.device.initSettings(ms.ID,ms.ID + "@D") ;
            
            //device.prepareDevice(Tools.convertTableOfDataToNewControllersParammeters(tableModelOfUnits,listModelOfControllers,ms.ID));
            Tools.Debug.print("Prepare Local Device success!");
            
            ArrayList<String> alNewControllerMessages = Globals.device.getNewControllerMessages() ;
            Tools.Debug.print(alNewControllerMessages.get(0));
            Tools.send(alNewControllerMessages.get(0), ms.out);
            manSocMess.reload(ms.in.readLine()) ;
            
            if (Tools.isCorrectCommand(manSocMess.getCommand(),"Answer",Globals.messageFailed,manSocMess.getMessage(),ms.out))
            {
                if ("FAILED".equals(manSocMess.getParameters().get(0).get(0)))
                {
                    Tools.Debug.print(" InitControllers Failed!");
                    Tools.send(Globals.messageFailed,ms.out);
                    Tools.Debug.print("Connect UnsuccessFully!");
                    return "X";
                }
                else
                    Tools.Debug.print("InitControllers OK!");
            }
            else
            { Tools.Debug.print("Connect UnsuccessFully!"); return "X"; }
            
            for (int i = 1 ; i < alNewControllerMessages.size();i++)
            {
                Tools.send(alNewControllerMessages.get(i), ms.out);
                
                manSocMess.reload(ms.in.readLine()) ;
                
                if (Tools.isCorrectCommand(manSocMess.getCommand(),"Answer",Globals.messageFailed,manSocMess.getMessage(),ms.out))
                {
                    if ("FAILED".equals(manSocMess.getParameters().get(0).get(0)))
                    {
                        Tools.Debug.print("NewController Failed!");
                        Tools.send(Globals.messageFailed,ms.out);
                        Tools.Debug.print("Connect UnsuccessFully!");
                        return "X";
                    }
                    else
                        Tools.Debug.print("NewController OK!");
                }
                else
                { Tools.Debug.print("Connect UnsuccessFully!"); return "X"; }
            }
            
            manSocMess.reload(ms.in.readLine()) ;
            
            if (Tools.isCorrectCommand(manSocMess.getCommand(),"InitializationFinished",Globals.messageFailed,manSocMess.getMessage(),ms.out))
            {
                if ("FAILED".equals(manSocMess.getParameters().get(0).get(0)))
                {
                    Tools.Debug.print("NewController Failed!");
                    Tools.send(Globals.messageFailed,ms.out);
                    Tools.Debug.print("Connect UnsuccessFully!");
                    return "X";
                }
                else
                {
                    Tools.Debug.print("InitializationFinished OK!");
                    Tools.send("#"+ms.ID+"D@D"+"$Answer:1*(OK);",ms.out);
                }
            }
            else
            {Tools.Debug.print("Connect UnsuccessFully!");  return "X";}
            
            //Ξεκινάει το thread για να διαβάζει από το Server :
            myThread = new ListenThread(Globals.manSocket,this);
            myThread.start();
            
            connected = !connected ;
            
            //Ξεκινάει το πρόγραμμα μας να στέλνει τα μηνύματα που διαβάζει από τα Arduino
            Iterator<String> keyIterPort = Globals.hmAllReadArduino.keySet().iterator();
            while(keyIterPort.hasNext())
            {
                Globals.hmAllReadArduino.get(keyIterPort.next()).startRead= true ;
            }
            
        } catch (IOException e) {
            Tools.Debug.print("Couldn't get I/O for the connection to: " + Globals.address + "host");
            return "X" ;
        }
        catch (Exception e) {
            //e.printStackTrace();
            Tools.Debug.print("Couldn't get I/O for the connection to: " + Globals.address + "host - Port error");
            return "X" ;
        }
        return "OK" ;
    }
    
    /**
     * Μέθοδος που αναλαμβάνει να κλείνει ολοκληρωμένα την σύνδεση με το Server.
     * Δηλαδή το socket και το Listen thread .
     */
    public void correctClose()
    {
        try {
            Tools.Debug.print("Closing Connection..");
            if (Globals.manSocket!=null)
            {
                if (Globals.manSocket.out!=null){
                    Tools.send("#"+Globals.manSocket.ID+"@D$Quit:1*(Bey-bey!);", Globals.manSocket.out);
                }
                Globals.manSocket.close() ;
            }
            if (myThread!=null)
            {
                myThread.terminate();
                myThread.interrupt() ;
                myThread = null ;
            }
        } catch (IOException ex) {
            Logger.getLogger(RealDeviceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        connected = false ;
        
        Tools.Debug.print("Closed Connection!");
    }
    
    ManageSocketMessage reloadDeviceMsm = new ManageSocketMessage();
    
    /**
     * Κλασική static main μέθοδος από την οποία ξεκινάει η εκτέλεση του Device Client.
     * Πρακτικά δηλαδή δημιουργεί την κλάση {@link RealDeviceClient}.
     * @param args Default παράμετροι που δεν χρησιμοποιούνται και που μπορεί δυνητικά να πάρει
     * το πρόγραμμα μας στην περίπτωση που εκτελεστεί από περιβάλλον γραμμής εντολών .
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Globals.printMyInfo() ;
                RealDeviceClient rdc;
                rdc = new RealDeviceClient();
            }
        });
    }
    /**
     * Κλάση που έχει κληρονομήσει την TimerTask ·
     * Σκοπός της είναι να εκτελείτε παράλληλα με την ροή του προγράμματος και να
     * ελέγχει αν προστέθηκε κάποιο arduino στον υπολογιστή και στην περίπτωση που
     * συμβεί κάτι τέτοιο να κάνει όλες τις απαραίτητες διαδικασίες για να προστεθεί
     * και αυτό στην απομακρυσμένη εποπτεία πραγματικού χρόνου .
     * Βέβαια να πούμε ότι αυτό είναι ένα task και λειτουργεί όταν έχει ανατεθεί
     * σε κάποιον timer να το εκτελεί ανά κάποιο χρονικό διάστημα .
     */
    class supervisionNewArduinoTask extends TimerTask {
        @Override
        public void run() {
            Globals.checkForNewArduino(true) ;
        }
    }
    /**
     * Κλάση που έχει κληρονομήσει την TimerTask ·
     * Σκοπός της είναι να εκτελείτε παράλληλα με την ροή του προγράμματος και αν
     * για κάποιο λόγο δεν υπάρχει σύνδεση με το Server , να ξεκινάει την διαδικασία για
     * να συνδεθεί στο Server από την αρχή .
     * Βέβαια να πούμε ότι αυτό είναι ένα task και λειτουργεί όταν έχει ανατεθεί
     * σε κάποιον timer να το εκτελεί ανά κάποιο χρονικό διάστημα .
     */
    class supervisionForNextTryConnect extends TimerTask {
        @Override
        public void run() {
            //Globals.checkForNewArduino(true) ;
            //System.out.println("Mikeole!");
            if (!connected)
                ConnectProcedure();
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