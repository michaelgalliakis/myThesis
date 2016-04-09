package ServerV1;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Κλάση που έχει μέσα Static μεταβλητές και συναρτήσεις με σκοπό να χρησιμοποιούνται
 * από παντού μέσα στο project .
 * @author Michael Galliakis
 */
public class Globals {
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
    public final static String messageOK = "#S$Answer:1*(OK);" ;
    public final static String messageFailed = "#S$Answer:1*(FAILED);" ;
    
    public static String fSep = System.getProperty("file.separator") ;
    
    public static String db_url ;
    public static String db_dbName ;
    public final static String db_driver = "com.mysql.jdbc.Driver";
    public static String db_username ;
    public static String db_password ;
    public static int serverPort ;
    public static int viewMessages = 2;
    
    /**
     * Συνάρτηση που τυπώνει πληροφορίες σχετικά με το project .
     */
    public final static void printMyInfo()
    {
        Tools.Debug.print("###########################################################",Tools.MSGSYSTEMOKEMO) ;
        Tools.Debug.print("Thesis Michael Galliakis 2016.",Tools.MSGSYSTEMOKEMO) ;
        Tools.Debug.print("TEI Athens - IT department.",Tools.MSGSYSTEMOKEMO) ;
        Tools.Debug.print("Email : cs081001@teiath.gr & michaelgalliakis@yahoo.gr .",Tools.MSGSYSTEMOKEMO) ;
        Tools.Debug.print("All files can be found :",Tools.MSGSYSTEMOKEMO) ;        
        Tools.Debug.print("\"https://github.com/michaelgalliakis/myThesis.git\"",Tools.MSGSYSTEMOKEMO) ;        
        Tools.Debug.print("###########################################################",Tools.MSGSYSTEMOKEMO) ;
        
    }
    /**
     * Συνάρτηση που φορτώνει ρυθμίσεις του Server από αρχείο .
     * @return True αν φορτώθηκαν και False αν έγινε κάποιο σφάλμα .
     */
    public static boolean loadConfig()
    {
        try {
            File PMGConfig = new File(Globals.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            PMGConfig = new File(PMGConfig.getParentFile().getPath() +Globals.fSep+"PMGTCS.config");
            Tools.Debug.print("Search file : " + PMGConfig.getAbsolutePath(),Tools.MSGOKEMO);
            if (PMGConfig.exists())
            {
                if (readXML(PMGConfig))
                    Tools.Debug.print("Open 'PMGTCS.config' File Success!",Tools.MSGOKEMO);
                
                else
                {
                    Tools.Debug.print("Error : Failed open 'PMGTCS.config' File!",Tools.MSGERROREMO);
                    return false ;
                }
            }
            else
            {
                Tools.Debug.print("Failed : File 'PMGTCS.config' not Exists!",Tools.MSGNOTHINGEMO);
                db_url = "jdbc:mysql://localhost:3306/";
                db_dbName = "thesisv1";
                db_username = "root";
                db_password = "panagia";
                serverPort = 50128 ;
                Tools.Debug.print("Loaded default config!",Tools.MSGOKEMO);
                return true ;
            }
        } catch (URISyntaxException ex) {
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
    private static String getTextValue( Element doc, String tag,int index) {
        String value = null ;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.getLength()>index) {
            if ( nl.item(index).hasChildNodes())
                value = nl.item(index).getFirstChild().getNodeValue();
        }
        return (value==null)?"":value;
    }
    /**
     * Συνάρτηση που διαβάζει κάποιες ρυθμίσεις από ένα xml αρχείο .
     * @param f Το αρχείο μέσα στο δίσκο από όπου θέλουμε να διαβάσουμε τις ρυθμίσεις.
     * @return True αν όλα πήγαν καλά και False αν κάτι δεν πήγε.
     */
    private static boolean readXML(File f) {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(f);
            Element doc = dom.getDocumentElement();
            db_username = getTextValue(doc, "DB_Username", 0) ;
            db_password = getTextValue(doc, "DB_Password", 0) ;
            db_dbName = getTextValue(doc, "DB_Name", 0) ;
            db_url = getTextValue(doc, "DB_URL", 0) ;
            String strPort = getTextValue(doc, "Port", 0) ;
            if (!Tools.isNumeric(strPort))
            {
                Tools.Debug.print("Error : Port is not number (read xml!)",Tools.MSGERROREMO);
                return false ;
            }
            serverPort = Integer.parseInt(strPort) ;
            
            String strViewMessages = getTextValue(doc, "ViewMessages", 0) ;
            if (Tools.isNumeric(strViewMessages))
                viewMessages = Integer.parseInt(strPort) ;
            
        } catch (ParserConfigurationException | SAXException pce) {
            Tools.Debug.print(pce.getMessage(),Tools.MSGERROREMO);
            Tools.Debug.print("Error : Exception(read xml!)",Tools.MSGERROREMO);
            return false;
        } catch (IOException | NumberFormatException ioe) {
            Tools.Debug.print(ioe.getMessage(),Tools.MSGERROREMO);
            Tools.Debug.print("Error : Exception(read xml!)",Tools.MSGERROREMO);
            return false ;
        }catch (Exception e) {
            Tools.Debug.print(e.getMessage(),Tools.MSGERROREMO);
            Tools.Debug.print("Error : Exception(read xml!)",Tools.MSGERROREMO);
            return false ;
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