package DeviceClientV1;

import static DeviceClientV1.Tools.MSGFROMSERVER;
import java.util.ArrayList;
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
    
    ManageSocket manSocket ;
    /**
     * Αρχικοποιεί το thread με το {@link ManageSocket} που έχουμε με το server ,
     * όπως επίσης και με το {link fMain} μας για να μπορεί να χρησιμοποιήσει
     * κάποιες μεθόδους και αντικείμενα του .
     * @param ms {@link ManageSocket} που έχουμε με το server
     * @param _myParent To {link fMain}, δηλαδή το main παράθυρο που ξεκινάει το πρόγραμμα.
     */
    ListenThread(ManageSocket ms,RealDeviceClient _myParent)  {
        manSocket = ms ;
    }
    boolean running = true ;
    /**
     * Με το που ξεκινήσει το thread απλά περιμένει να διαβάσει μηνύματα από την άλλη άκρη
     * του socket που έχουμε , δηλαδή από το Server .
     * Για κάθε είδους μήνυμα κάνει και τις ανάλογες ενέργειες .
     * Για παράδειγμα όταν πάρει ένα μήνυμα αλλαγής τιμής ή mode ενός Unit αναλαμβάνει να
     * στείλει το κατάλληλο μήνυμα στο εκάστοτε arduino για να πραγματοποιηθεί η αλλαγή.
     */
    public void run() {
        ManageSocketMessage manSocMess = new ManageSocketMessage() ;
        try {
            while (running) {
                
                manSocMess.reload(manSocket.in.readLine());
                
                if (manSocMess.getCommand().equals("ERROR"))
                {
                    Tools.Debug.print("Stopping Listen thread - ERROR (by read)! ");
                    Globals.mainDeviceClient.correctClose() ;
                    running = false ;
                    break ;
                }
                else if (manSocMess.getCommand().equals("Quit"))
                {
                    Tools.Debug.print("Quit From Server!");
                    Globals.mainDeviceClient.correctClose() ;
                    break ;
                }
                else if (manSocMess.getCommand().equals("ChangeValues"))
                {
                    ///////////   Tools.Debug.print("Read from server : " + manSocMess.getMessage());
                    
                    Tools.Debug.print("Read from server : " + manSocMess.getMessage(),MSGFROMSERVER);
                    for (ArrayList<String> alParam : manSocMess.getParameters())
                    {
                        String conName = alParam.get(0) ;
                        String unitName = alParam.get(1) ;
                        String value = alParam.get(2) ;
                        
                        String post = "c" + unitName +":" + value ;
                        
                        
                        Globals.hmAllReadArduino.get(conName).output.write(post.getBytes());
                        Globals.hmAllReadArduino.get(conName).output.flush();
                        try {
                            Thread.sleep(500);                 //1000 milliseconds is one second.
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        //System.out.println("Apo Cha Value:" + post) ;
                        // System.out.println(post.getBytes().toString()) ;
                    }
                    Globals.device.setValuesOfDevice(manSocMess.getMessage());
                    // System.out.println(manSocMess.getParameters().get(0).toString());
                    
//myParent.st.output.write('i');
                    //myParent.st.output.flush();
                    
                }
                else if (manSocMess.getCommand().equals("ChangeModes"))
                {
                    Tools.Debug.print("Read from server : " + manSocMess.getMessage(),MSGFROMSERVER);
                    int intMode = -1 ;
                    for (ArrayList<String> alParam : manSocMess.getParameters())
                    {
                        String conName = alParam.get(0) ;
                        String unitName = alParam.get(1) ;
                        String mode = alParam.get(2) ;
                        intMode = -1 ;
                        try
                        {
                            intMode = Integer.parseInt(mode);
                            if (intMode<0 || intMode >4)
                                break ;
                        }
                        catch(Exception ex)
                        {
                            //Nothing
                            break ;
                        }
                        
                        String post = "m" + unitName +":" + intMode ;
                        Globals.hmAllReadArduino.get(conName).output.write(post.getBytes());
                        Globals.hmAllReadArduino.get(conName).output.flush();
                        try {
                            Thread.sleep(1500);                 //1000 milliseconds is one second.
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        // Globals.hmAllSerialTest.get(conName).output.flush();
                        
                        //System.out.println("Apo Cha Mode:" + post) ;
                    }
                    if (intMode!=-1)
                        Globals.device.setModesOfDevice(manSocMess.getMessage());
                    
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