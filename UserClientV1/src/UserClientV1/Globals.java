package UserClientV1;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Κλάση που έχει μέσα Static μεταβλητές και συναρτήσεις με σκοπό να χρησιμοποιούνται
 * από παντού μέσα στο project .
 * @author Michael Galliakis
 */
public class Globals {
    public static String username ;
    public static String password ;
    public static String DBUserID ;
    public static String address ;
    public static String typeUser ;
    public static int port ;
    
    public static StyledDocument debugDoc ;
    public static JTextPane debugTextPane ;
    public static HashMap<String,Device> hmDevices = new HashMap<>();
    
    public static ArrayList<ArrayList<String>> alWithDevices ;
    
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
    public final static String CERTIFICATIONUSERDESKTOP = "2qa5wlJ4dZa4E9y" ;
    public static String thesisTitle = "Thesis - Michael Galliakis TEI Αθήνας ΑΜ:081001 (4/2016)" ;
    public static String messDialTitle ="Thesis Galliakis Michael ΤΕΙ Αθήνας!";
    
    /**
     * Ανάλογα το λειτουργικό σύστημα που τρέχει το πρόγραμμα παίρνει το ανάλογο seperator
     * πχ "/" για Linux και "\" για Windows .
     */
    public static String fSep = System.getProperty("file.separator") ;
    public static Fmain objMainFrame ;
    public static Ddevices objDevices ;
    public static FuserLogin objUserLogin ;
    public static Dthesis objThesis ;
    public static File runFolder ;
    
    
    public static BufferedImage biLogo ;
    
    public static ImageIcon imUser ;
    public static ImageIcon imVip ;
    public static ImageIcon imSystem ;
    
    public static ImageIcon imController ;
    public static ImageIcon imProcessor ;
    public static ImageIcon imTreeClose ;
    public static ImageIcon imTreeOpen ;
    public static ImageIcon imTreeLeaf ;
    
    public static ImageIcon imHand ;
    public static ImageIcon imSwitch ;
    public static ImageIcon imNoHand ;
    public static Image imageBackground ;
    //Keys images:
    public static ImageIcon imReadOnly ;
    public static ImageIcon imOwner ;
    public static ImageIcon imAdmin ;
    public static ImageIcon imFull ;
    //Status :
    public static ImageIcon imOK ;
    public static ImageIcon imOops ;
    public static ImageIcon imDisconnect ;
    
    public static ImageIcon imStart ;
    public static ImageIcon imStop ;
    
    public static ImageIcon imPostIt ;
    
    public static final Color selectedColor = new Color(255,153,51) ;
    public static final Color lockColor = new Color(224,224,224) ;
    public static final Color unLockColor = new Color(51,255,51) ;
    public static final Color thesisColor = new Color(153,51,255) ;
    
    public static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static Date today ;
    
    public static HashMap<String,ImageIcon> hmUnitImages ;
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
            ,"Fire"
            ,"Temperature"
            ,"Humidity"
            ,"switch"
    } ;
    
    /**
     * Static μέθοδος που αυτά που κάνει κυρίως είναι ότι βρίσκει το run folder και φτιάχνει το ευρετήριο
     * Settings και backgroundImages μέσα σε αυτό όπως επίσης φορτώνει τις εικόνες που χρειάζεται
     * το project και προετοιμάζει το TextPane του "Report Area".
     * @param myClass Η κλάση που καλέι αυτή τη μέθοδο(τύπου JFrame) . Χρειάζεται για την εμφάνιση messageDialog .
     * @return True αν όλα πήγαν καλά και False αν έγινε κάποιο σφάλμα .
     */
    public static boolean prepareStaticVariables(Object myClass)
    {
        try {
            File f = new File(Globals.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            runFolder = new File(f.getParent()+fSep+"UserClientV1"+ fSep);
            runFolder.mkdir() ;
            
            debugTextPane = new JTextPane();
            debugDoc = debugTextPane.getStyledDocument();
            debugTextPane.setText( "Thesis - Michael Galliakis TEI Αθήνας ΑΜ:081001 (4/2016)" );
            debugTextPane.setEditable(false);
        } catch (URISyntaxException ex) {
            return false ;
        }
        
        if (!fillStaticImages(myClass))
            return false ;
        
        File f = new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Settings") ;
        f.mkdir() ;
        f = new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"backgroundImages") ;
        f.mkdir() ;
        return true ;
    }
    
    /**
     * Μέθοδος που είναι υπεύθυνη να φορτώσει τις εικόνες που χρειάζεται το project μας.
     * @param myClass Μια κλάση τύπου JFrame  . Χρειάζεται για την εμφάνιση messageDialog .
     * @return True αν όλα πήγαν καλά και False αν έγινε κάποιο σφάλμα .
     */
    public static boolean fillStaticImages(Object myClass)
    {
        BufferedImage img;
        try {
            biLogo = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"logoPMGv2F48.png"));
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"controller.png"));
            imController = new ImageIcon(img);
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"devices24.png"));
            imProcessor = new ImageIcon(img); //DefaultOff
            
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"collapse.gif"));
            imTreeClose = new ImageIcon(img); //DefaultOn
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"expand.gif"));
            imTreeOpen = new ImageIcon(img);
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"leaf.png"));
            imTreeLeaf = new ImageIcon(img);
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"hand.png"));
            imHand = new ImageIcon(img);
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"switch.png"));
            imSwitch = new ImageIcon(img);
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"noHand.png"));
            imNoHand = new ImageIcon(img);
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"start36.png"));
            imStart = new ImageIcon(img);
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"stop36.png"));
            imStop = new ImageIcon(img);
            //Keys
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"typeDevices"+"/"+"readonly.png"));
            imReadOnly = new ImageIcon(img);
            
            imageBackground = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"background.jpg"));
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"typeDevices"+"/"+"full.png"));
            imFull = new ImageIcon(img);
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"typeDevices"+"/"+"admin.png"));
            imAdmin = new ImageIcon(img);
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"typeDevices"+"/"+"owner.png"));
            imOwner = new ImageIcon(img);
            //Status :
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"OK.png"));
            imOK = new ImageIcon(img);
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"Oops.png"));
            imOops = new ImageIcon(img);
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"disconnect.png"));
            imDisconnect = new ImageIcon(img);
            
            //Users :
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"typeUsers"+"/"+"user36.png"));
            imUser = new ImageIcon(img);
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"typeUsers"+"/"+"system36.png"));
            imSystem = new ImageIcon(img);
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"typeUsers"+"/"+"vip36.png"));
            imVip = new ImageIcon(img);
            
            //img = ImageIO.read(myClass.getClass().getResource(fSep+"Images"+fSep+"postit.png"));
            //imPostIt = new ImageIcon(img);
            
        } catch (IOException ex) {
            //Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog((Component) myClass,"Δεν φορτώθηκαν όλες οι εικόνες! Ξανα εγκαταστήστε την εφαρμογή",Globals.messDialTitle,JOptionPane.PLAIN_MESSAGE);
            return false ; //Failed
        }
        if (initUnitImages(myClass))
            return true ; //OK
        else
            return false ; //Failed
    }
    /**
     * Μέθοδος που είναι υπεύθυνη να γεμίσει κατάλληλα τη hmUnitImages (HashMap "String,ImageIcon")
     * με όλες τις εικόνες των τύπων που έχουν οι μονάδες ούτως ώστε κάθε εικόνα να έχει ένα μοναδικό key .
     * Ο πρώτος χαρακτήρας είναι το 0 αν είναι η πρώτη κατάσταση της μονάδας ή 1
     * αν είναι η δεύτερη κατάσταση της . Αν η φύση της μονάδας έχει μια μόνο κατάσταση
     * τότε το πρώτο γράμμα είναι πάντα το 0 . Οι υπόλοιποι χαρακτήρες είναι ο αριθμός που
     * έχει ο κάθε τύπος με βάση τη σειρά που βρίσκεται και στον πίνακα namesOfTypes.
     * Για παράδειγμα σε μια λάμπα που έχει 2 καταστάσεις , η σβηστή εικόνα θα έχει
     * key το "02" και η ανοικτή το "12" (2 είναι ο αριθμός σειράς στον πίνακα των τύπων).
     * @param myClass Μια κλάση τύπου JFrame . Χρειάζεται για την εμφάνιση messageDialog .
     * @return True αν όλα πήγαν καλά και False αν έγινε κάποιο σφάλμα .
     */
    public static boolean initUnitImages(Object myClass)
    {
        hmUnitImages = new HashMap<>() ;
        
        BufferedImage img;
        try {
            ImageIcon imIc ;
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"default.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("00", imIc) ; //Default
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"defaultOff.png"));
            imIc = new ImageIcon(img); //DefaultOff
            hmUnitImages.put("01", imIc) ;
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"defaultOn.png"));
            imIc = new ImageIcon(img); //DefaultOn
            hmUnitImages.put("11", imIc) ;
            /////////////////
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"lampOff.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("02", imIc) ; //Lamp off
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"lampOn.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("12", imIc) ; //lamp on
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"brightness.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("03", imIc) ; //brightness
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"motionOff.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("04", imIc) ; //motion off
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"motionOn.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("14", imIc) ;// motion on
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"distance.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("05", imIc) ; //distance
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"sound.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("06", imIc) ; //sound
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"vibration.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("07", imIc) ; //vibration
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"smoke.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("08", imIc) ; // Smoke
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"temperature.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("09", imIc) ; //temperature
            ///^^
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"humidity.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("010", imIc) ; //humidity
            
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"switchOff.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("011", imIc) ; // Switch off
            img = ImageIO.read(myClass.getClass().getResource("Images"+"/"+"UnitImages"+"/"+"switchOn.png"));
            imIc = new ImageIcon(img);
            hmUnitImages.put("111", imIc) ; // Switch on
            
        } catch (IOException ex) {
            //Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
            return false ;
        }
        
        return true ;
    }
    
    /**
     * Συνάρτηση που χρησιμοποιείται για να μπορούμε να διαβάσουμε δεδομένα
     * με μορφή και σύνταξη xml .
     * @param doc Ένα Element που έχει το περιεχόμενο του αρχείου xml μας .
     * @param tag Ετικέτα της xml από την οποία θέλουμε να ανακτήσουμε το περιεχόμενο της.
     * @param index Επειδή υπάρχουν πολλά στοιχεία με την ίδια ετικέτα, στο index
     * γράφουμε τη θέση της ετικέτας μέσα στην "σειρά" που βρίσκεται στα δεδομένα μας (της μορφής xml)
     * για να ανακτήσουμε το περιεχόμενο της . Πχ αν θέλουμε το περιεχόμενο του τρίτου στοιχείου της ίδιας
     * ετικέτας δίνουμε σαν index το 2 (ξεκινάει η αρίθμηση από το 0).
     * @return Το περιεχόμενο της εκάστοτε ετικέτας του κειμένου με βάση βέβαια και τη σειρά εύρεσης του μέσα σε αυτό.
     */
    public static String getTextValue( Element doc, String tag,int index) {
        String value = null ;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.getLength()>index) {
            if ( nl.item(index).hasChildNodes())
                value = nl.item(index).getFirstChild().getNodeValue();
        }
        return value;
    }
    
    /*
    private static String OS = System.getProperty("os.name").toLowerCase();
    public static boolean isWindows() {
    return (OS.indexOf("win") >= 0);
    }
    
    public static boolean isMac() {
    return (OS.indexOf("mac") >= 0);
    }
    
    public static boolean isUnix() {
    return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }
    
    public static boolean isSolaris() {
    return (OS.indexOf("sunos") >= 0);
    }
    */
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