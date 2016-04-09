package DeviceClientV1;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;

import javax.swing.JOptionPane;
/**
 * Κλάση που έχει μέσα Static μεταβλητές και συναρτήσεις με σκοπό να χρησιμοποιούνται
 * από παντού μέσα στο project .
 * @author Michael Galliakis
 */
public class Globals {
    /**
     * Ανάλογα το λειτουργικό σύστημα που τρέχει το πρόγραμμα παίρνει το ανάλογο seperator
     * πχ "/" για Linux και "\" για Windows .
     */
    public static String fSep = System.getProperty("file.separator") ;
    public static BufferedImage biLogo ;
    /**
     * Τα λεκτικά των Types των Units όπως και την συγκεκριμένη θέση που έχουν μέσα στο πίνακα .
     */
    public static final String[] namesOfTypes = { "No Category Dimming"
            ,"No  Category Switch"
            ,"Lamp"
            ,"Brightness"
            ,"Motion"
            ,"Distance"
            ,"Sound"
            ,"Vibration"
            ,"Smoke"
            ,"Temperature"
            ,"Humidity"
            ,"switch"
    } ;
    /**
     * Certification strings για μεγαλύτερη ασφάλεια .
     * Επειδή το project είναι ανοικτού λογισμικού και μπορεί πολλοί χρήστες
     * να έχουν τον δικό τους server ,με το συγκεκριμένο τρόπο κάθε server
     * μπορεί να ξέρει τους δικούς του clients .
     * Για παράδειγμα μελλοντικά μπορεί να εξελιχθούν οι clients από κάποιους προγραμματιστές
     * και να μην ταιριάζουν όλοι με όλους τους servers . Με αυτό το τρόπο
     * μπορούμε να εξασφαλίσουμε ότι δεν θα έχουν πρόσβαση όλοι οι clients(που μπορεί να
     * είναι διαφορετικοί) σε όλους τους servers ...
     *
     * Ακόμη είναι δυσκολότερο να προσποιηθεί κάποιος κακόβουλος client
     * (Που λόγω ότι είναι ελεύθερο το λογισμικό μπορεί να πειράξει το κώδικα κατάλληλα κάποιος)
     * ότι είναι σωστός σε κάποιον server γιατί απλά δεν είναι γνωστό στους χρήστες το
     * κάθε certifification string που δέχεται ο εκάστοτε Server.
     * Βέβαια δεν έχουμε πλήρη ασφάλεια έτσι απλά είναι ένα επιπλέον εμπόδιο .
     *
     * Το ένα παρακάτω είναι για Device Client .
     */
    public final static String CERTIFICATIONDEVICE =      "8a2D5528df24E91" ;
    /**
     * Certification strings για μεγαλύτερη ασφάλεια .
     * Επειδή το project είναι ανοικτού λογισμικού και μπορεί πολλοί χρήστες
     * να έχουν τον δικό τους server ,με το συγκεκριμένο τρόπο κάθε server
     * μπορεί να ξέρει τους δικούς του clients .
     * Για παράδειγμα μελλοντικά μπορεί να εξελιχθούν οι clients από κάποιους προγραμματιστές
     * και να μην ταιριάζουν όλοι με όλους τους servers . Με αυτό το τρόπο
     * μπορούμε να εξασφαλίσουμε ότι δεν θα έχουν πρόσβαση όλοι οι clients(που μπορεί να
     * είναι διαφορετικοί) σε όλους τους servers ...
     *
     * Ακόμη είναι δυσκολότερο να προσποιηθεί κάποιος κακόβουλος client
     * (Που λόγω ότι είναι ελεύθερο το λογισμικό μπορεί να πειράξει το κώδικα κατάλληλα κάποιος)
     * ότι είναι σωστός σε κάποιον server γιατί απλά δεν είναι γνωστό στους χρήστες το
     * κάθε certifification string που δέχεται ο εκάστοτε Server.
     * Βέβαια δεν έχουμε πλήρη ασφάλεια έτσι απλά είναι ένα επιπλέον εμπόδιο .
     *
     * Το ένα παρακάτω είναι για User Client .
     */
    
    public static String messageFailed = "#UD$Answer:1*(FAILED);" ;
    public static String messageOK = "#UD$Answer:1*(OK);" ;
    
    public static final String[] namesOfModes = {"Only Auto","Only Remote","Both","Auto by User","Remote by User"} ;
    public static final Object[] ColumnNames = {"Controller","Unit","Type","mode","tag","Value","max","min","limit"} ;
    
    public static String thesisTitle = "Device Client (Simulator) * Thesis - Michael Galliakis TEI Αθήνας ΑΜ:081001 (4/2016)" ;
    public static String messDialTitle ="Thesis Galliakis Michael ΤΕΙ Αθήνας!";
    
    
    public static File runFolder ;
    public static Fmain objMainFrame ;
    public static Dthesis objThesis ;
    public static final Color thesisColor = new Color(153,51,255) ;
    
    /**
     * Static μέθοδος που βρίσκει το run folder και φτιάχνει το ευρετήριο Devices
     * όπως επίσης φορτώνει τις εικόνες που χρειάζεται το project .
     * @param myClass Η κλάση που καλέι αυτή τη μέθοδο(τύπου JFrame) . Χρειάζεται για την εμφάνιση messageDialog .
     * @return True αν όλα πήγαν καλά και False αν έγινε κάποιο σφάλμα .
     */
    public static boolean prepareStaticVariables(Object myClass)
    {
        try {
            File f = new File(Globals.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            runFolder = new File(f.getParent()+fSep+"DeviceClientV1"+ fSep);
            runFolder.mkdir() ;
        } catch (URISyntaxException ex) {
            return false ;
        }
        
        if (!fillStaticImages(myClass))
            return false ;
        
        File f = new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Devices") ;
        f.mkdir() ;
        
        return true ;
    }
    /**
     * Μέθοδος που είναι υπέυθυνη να φορτώσει τις εικόνες που χρειάζεται το project μας
     * @param myClass  Μια κλάση τύπου JFrame . Χρειάζεται για την εμφάνιση messageDialog .
     * @return True αν όλα πήγαν καλά και False αν έγινε κάποιο σφάλμα .
     */
    public static boolean fillStaticImages(Object myClass)
    {
        try {
            biLogo = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"logoPMGv2F48.png"));
        } catch (IOException ex) {
            //Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog((Component) myClass,"Δεν φορτώθηκαν όλες οι εικόνες! Ξανα εγκαταστήστε την εφαρμογή",Globals.messDialTitle,JOptionPane.PLAIN_MESSAGE);
            return false ; //Failed
        }
        return true;
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