package UserClientV1;

import java.io.BufferedReader;
import java.net.Socket;
/**
 * Κλάση που αναλαμβάνει να διαβάζει συνεχώς ότι του στέλνει ο Server
 * μέσα από το ανοικτό socket .
 * Ανάλογα τα μηνύματα που λαμβάνει κάνει και τις κατάλληλες ενέργειες ,
 * όπως για παράδειγμα αν πάρει κάποιο μήνυμα (που αρχικά έχει στείλει κάποιος άλλος
 * χρήστης ή και κάποια συσκευή) για να αλλάξει την κατάσταση-τιμή κάποιου Unit
 * , αναλαμβάνει να το κάνει εμφανίζοντας την αλλαγή στο χρήστη με γραφικό τρόπο.
 * Η κλάση αυτή δουλεύει παράλληλα με την main ροή του προγράμματος γιαυτό και
 * έχει κληρονομήσει την κλάση thread .
 * Επειδή μπορούμε να έχουμε πολλές εποπτείες ταυτόχρονα δημιουργούνται τόσα αντικείμενα
 * αυτής της κλάσης όσες είναι και οι εποπτείες συν μία που υπάρχει όσο λειτουργεί
 * το πρόγραμμα για να "κρατιέται" η σύνδεση με το server και να λαμβάνει πληροφορίες για
 * το αν προστέθηκε ή αφαιρέθηκε κάποια συσκευή που έχει δικαιώματα ο χρήστης
 * , ουτώς ώστε σε επόμενη φάση να ενημερωθούν τα devices στο παράθυρο που βρίσκονται ({@link Ddevices}) .
 * Υπάρχει αυτή η ανάγκη (του thread) για να μπορεί συγχρόνως να λειτουργεί το πρόγραμμα μας κανονικά
 * και να διαβάζει συνεχώς μηνύματα ασύγχρονα από το Server με παράλληλο τρόπο.
 * @author Michael Galliakis
 */
public class ListenThread extends Thread {
    BufferedReader in ;
    Socket socket ;
    
    Ptab devTab ;
    Device device ;
    /**
     * Αρχικοποιεί το thread με ένα ανοικτό socket που έχουμε με το server ,
     * όπως επίσης και με το BufferedReader του socket για να μπορούμε σε επόμενη
     * φάση να διαβάζουμε από το κανάλι.
     * Η χρήση αυτού του κατασκευαστή είναι για τη περίπτωση που θέλουμε να έχουμε ένα
     * κανάλι , όχι για εποπτεία κάποιας συσκευής, αλλά για ενημερωνόμαστε για
     * τις καταστάσεις των συσκευών (Αν είναι up ή Down κυρίως) από το server σε real time .
     * @param s Το Socket που έχουμε με το Server .
     * @param i Το BufferedReader του socket .
     */
    ListenThread( Socket s,BufferedReader i) {
        socket = s ;
        in = i ;
        devTab = null ;
        device = null ;
    }
    /**
     * Αρχικοποιεί το thread με ένα ανοικτό socket που έχουμε με το server ,
     * όπως επίσης και με το BufferedReader του socket για να μπορούμε σε επόμενη
     * φάση να διαβάζουμε από το κανάλι.
     * Η χρήση αυτού του κατασκευαστή είναι για τη περίπτωση που θέλουμε να έχουμε ένα
     * κανάλι με το server για εποπτεία κάποιας συγκεκριμένης συσκευής του χρήστη.
     * Οι άλλοι παράμετροι είναι για να ξέρουμε σε ποια συσκευή({@link device}) και σε ποια
     * καρτέλα συσκευής({@link Ptab}) αφορούν τα μελλοντικά μηνύματα που θα αβουμε από το server .
     * @param dev Το {@link device} στο οποίο κάνει εποπτεία ο χρήστης .
     * @param s Το Socket που έχουμε με το Server .
     * @param i Το BufferedReader του socket .
     * @param _myParent Η καρτέλα ({@link Ptab}) που δείχνει την εποπτεία για
     * τη συγκεκριμένη συσκευή του χρήστη .
     */
    ListenThread(Device dev, Socket s,BufferedReader i,Ptab _myParent) {
        socket = s ;
        in = i ;
        device = dev ;
        devTab = _myParent ;
    }
    String clientCommand;
    ManageSocketMessage manSocMess = new ManageSocketMessage() ;
    /**
     * Με το που ξεκινήσει το thread απλά περιμένει να διαβάσει μηνύματα από την άλλη άκρη
     * του socket που έχουμε , δηλαδή από το Server .
     * Για κάθε είδους μήνυμα κάνει και τις ανάλογες ενέργειες .
     * Για παράδειγμα όταν πάρει ένα μήνυμα αλλαγής τιμής ή mode ενός Unit αναλαμβάνει να
     * ενημερώσει το αντίστοιχο αντικείμενο {@link Device} της εκάστοτε εποπτείας
     * με βάση την αντιστοίχιση που υπάρχει μεταξύ του συγκεκριμένου socket και μίας
     * συγκεκριμένης συσκευής που είχε καθοριστεί αρχικά στον κατασκευαστή μας .
     */
    public void run() {
        try {
            String message ;
            while (true) {
                try {
                    message = in.readLine() ;
                }
                catch (Exception ex)
                {
                    Tools.Debug.print("Stopping Listen thread - ERROR read from socket");
                    if (devTab!=null)
                        devTab.stopMonitoring(true);
                    else
                    {
                        Globals.objMainFrame.logoutProccess(false);
                    }
                    
                    break ;
                }
                manSocMess.reload(message);
                if (manSocMess.getCommand().equals("ERROR"))
                {
                    Tools.Debug.print("Stopping Listen thread - ERROR");
                    ///if (Globals.objDevices)
                    Tools.Debug.print(manSocMess.getCommand()) ;
                    if (devTab!=null)
                        devTab.stopMonitoring(false);
                    else
                    {
                        
                        Globals.objMainFrame.logoutProccess(false);
                    }
                    
                    break ;
                }
                else if (manSocMess.getCommand().equals("Quit"))
                {
                    Tools.Debug.print("Quit From Server!");
                    if (devTab!=null)
                    {
                        Tools.Debug.print("Stop Monitoring");
                        devTab.stopMonitoring(false);
                    }
                    else
                    {
                        
                        Globals.objMainFrame.logoutProccess(false);
                    }
                    
                    break ;
                }
                else if (manSocMess.getCommand().equals("NewValues") || manSocMess.getCommand().equals("ChangeValues"))
                {
                    device.setValuesOfDevice(manSocMess.getMessage());
                    //System.out.println("Read : " + manSocMess.getMessage());
                }
                else if (manSocMess.getCommand().equals("ChangeModes"))
                {
                    device.setModesOfDevice(manSocMess.getMessage());
                    //System.out.println("Read : " + manSocMess.getMessage());
                }
                else if (manSocMess.getCommand().equals("NewController"))
                {
                    device.addController(manSocMess.getMessage());
                    //System.out.println("Read : " + manSocMess.getMessage());
                }
                else if (manSocMess.getCommand().equals("DeleteController"))
                {
                    device.deleteController(manSocMess);
                    Tools.Debug.print("Read : " + manSocMess.getMessage());
                }
                else if (manSocMess.getCommand().equals("PutDevicesInfo") && device==null)
                {
                    if ("FAILED".equals(manSocMess.getParameters().get(0).get(0)))
                    {
                        Tools.Debug.print("Device refresh info Failed");
                    }
                    else
                    {
                        Tools.Debug.print(manSocMess.getMessage());
                        
                        Globals.objDevices.setDevicesInfo(manSocMess.alParameters);
                        Tools.Debug.print("Device info OK!");
                    }
                }
                else
                {
                    //System.out.println("Read : " + manSocMess.getMessage());
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Tools.Debug.print("Read Thread Stop!");
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

