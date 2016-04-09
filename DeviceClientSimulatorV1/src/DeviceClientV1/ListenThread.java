package DeviceClientV1;
/**
 * Κλάση που αναλαμβάνει να διαβάζει συνεχώς ότι του στέλνει ο Server
 * μέσα από το ανοικτό socket .
 * Ανάλογα τα μηνύματα που λαμβάνει κάνει και τις κατάλληλες ενέργειες ,
 * όπως για παράδειγμα αν πάρει κάποιο μήνυμα (που αρχικά έχει στείλει κάποιος χρήστης)
 * για να αλλάξει την κατάσταση-τιμή κάποιου Unit , αναλαμβάνει να το κάνει .
 * Η κλάση αυτή δουλεύει παράλληλα με την main ροή του προγράμματος γιαυτό και
 * έχει κληρονομήσει την κλάση thread .
 * Υπάρχει αυτή η ανάγκη (του thread) για να μπορεί συγχρόνως να λειτουργεί το πρόγραμμα μας κανονικά
 * και να διαβάζει συνεχώς μηνύματα ασύγχρονα από το Server με παράλληλο τρόπο.
 * @author Michael Galliakis
 */
public class ListenThread extends Thread {
    Fmain myParent ;
    ManageSocket manSocket ;
    /**
     * Αρχικοποιεί το thread με το {@link ManageSocket} που έχουμε με το server ,
     * όπως επίσης και με το {link Fmain} μας για να μπορεί να χρησιμοποιήσει
     * κάποιες μεθόδους και αντικείμενα του .
     * @param ms {@link ManageSocket} που έχουμε με το server
     * @param _myParent To {link Fmain}, δηλαδή το main παράθυρο που ξεκινάει το πρόγραμμα.
     */
    ListenThread(ManageSocket ms,Fmain _myParent)  {
        manSocket = ms ;
        myParent = _myParent ;
    }
    boolean running = true ;
    /**
     * Με το που ξεκινήσει το thread απλά περιμένει να διαβάσει μηνύματα από την άλλη άκρη
     * του socket που έχουμε , δηλαδή από το Server .
     * Για κάθε είδους μήνυμα κάνει και τις ανάλογες ενέργειες .
     */
    public void run() {
        ManageSocketMessage manSocMess = new ManageSocketMessage() ;
        try {
            while (running) {
                
                manSocMess.reload(manSocket.in.readLine());
                
                if (manSocMess.getCommand().equals("ERROR"))
                {
                    Tools.Debug.print("Stopping Listen thread - ERROR (by read)! ");
                    myParent.correctClose() ;
                    break ;
                }
                else if (manSocMess.getCommand().equals("Quit"))
                {
                    Tools.Debug.print("Quit From Server!");
                    myParent.correctClose() ;
                    break ;
                }
                else if (manSocMess.getCommand().equals("ChangeValues"))
                {
                    Tools.Debug.print("Read from server : " + manSocMess.getMessage());
                    myParent.device.setValuesOfDevice(manSocMess.getMessage());
                    myParent.reloadDevice(manSocMess.getMessage()) ;
                }
                else if (manSocMess.getCommand().equals("ChangeModes"))
                {
                    Tools.Debug.print("Read from server : " + manSocMess.getMessage());
                    myParent.device.setModesOfDevice(manSocMess.getMessage());
                    myParent.reloadDevice(manSocMess.getMessage()) ;
                }
                else
                    Tools.Debug.print("Read unknown command from Server : " + manSocMess.getMessage());
                
            }
        } catch (Exception e) {
            Tools.Debug.print("Stopped Listen thread!!");
            //myParent.correctClose() ;
        }
    }
    
    /**
     * Public μέθοδος που βοηθάει στο τερματισμό του thread μας .
     * Με αυτό το τρόπο δεν τερματίζεται ακαριαία το thread αλλά λίγο πριν
     * το επόμενο μήνυμα που θα χρειαστεί να διαβάσει από το Server.
     */
    public void terminate()
    {
        running = false;
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