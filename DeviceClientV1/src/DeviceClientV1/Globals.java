package DeviceClientV1;

import gnu.io.CommPortIdentifier;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    public final static String CERTIFICATIONUSERDESKTOP = "2qa5wlJ4dZa4E9y" ;
    
    public static String messageFailed = "#UD$Answer:1*(FAILED);" ;
    public static String messageOK = "#UD$Answer:1*(OK);" ;
    
    //public static final String[] namesOfModes = {"Only Auto","Only Remote","Both","Auto by User","Remote by User"} ;
    //public static final Object[] ColumnNames = {"Controller","Unit","Type","mode","tag","Value","max","min","limit"} ;
    
    public static String messDialTitle ="Thesis Galliakis Michael ΤΕΙ Αθήνας!";
    
    public static String devicename,password,address ;
    public static int port,secondsForNextTryConnect,secondsForNextSearchArduino ;
    public static boolean viewMessage = true;
    
    public static HashMap<CommPortIdentifier,String> hmAllArduino  = new HashMap() ;
    public static HashMap<String,ReadArduino> hmAllReadArduino = new HashMap() ;
    public static RealDeviceClient mainDeviceClient ;
    public static ManageSocket manSocket ;
    public static Device device ;
    
    /**
     * Συνάρτηση που τυπώνει πληροφορίες σχετικά με το project .
     */
    public final static void printMyInfo()
    {
       Tools.Debug.print("###########################################################") ;
        Tools.Debug.print("Thesis Michael Galliakis 2016.") ;
        Tools.Debug.print("TEI Athens - IT department.") ;
        Tools.Debug.print("Email : cs081001@teiath.gr & michaelgalliakis@yahoo.gr .") ;
        Tools.Debug.print("All files can be found :") ;        
        Tools.Debug.print("\"https://github.com/michaelgalliakis/myThesis.git\"") ;        
        Tools.Debug.print("###########################################################") ;        
    }
    /**
     * Συνάρτηση η οποία ελέγχει αν κάποιο όνομα ελεγκτή υπάρχει ήδη σε κάποιον προηγούμενο
     * ελεγκτή που έχει συνδεθεί , και επιστρέφει το όνομα ακριβώς το ίδιο αν
     * δεν υπάρχει συνωνυμία ή ελαφρώς αλλαγμένο (για να είναι μοναδικό) βάζοντας
     * στο τέλος του ονόματος κάποιον αριθμό .
     * @param conName Το όνομα του ελεγκτή για να γίνει ο έλεγχος αν υπάρχει συνονιμία .
     * @return Ένα μοναδικό όνομα ελεγκτή .
     */
    public static String checkConNameIfthereIS(String conName)
    {
        
        Iterator<CommPortIdentifier> keyIterPort = Globals.hmAllArduino.keySet().iterator();
        while(keyIterPort.hasNext())
        {
            CommPortIdentifier item = keyIterPort.next() ;
            
            if (Globals.hmAllArduino.get(item).equals(conName))
            {
                int counter = 0 ;
                String tmpConName = conName +"0" ;
                while (Globals.hmAllArduino.get(item).equals(tmpConName))
                {
                    tmpConName = tmpConName.substring(0,tmpConName.length()-2) ;
                    tmpConName+=String.valueOf(++counter) ;
                }
                //return checkConName(tmpConName) ;
                conName = tmpConName ;
                keyIterPort = Globals.hmAllArduino.keySet().iterator();
            }
        }
        return conName ;
        
    }
    /**
     * Συνάρτηση που ελέγχει αν υπάρχουν νέα arduino συνδεδεμένα πάνω στον υπολογιστή
     * και αν υπάρχουν τα προσθέτει για να μπορεί να γίνει εποπτεία πάνω τους .
     * @param isNewArduino True αν είναι νέο arduino ή False αν είναι arduino που
     * θα προστεθεί στην αρχή που ξεκινάει το πρόγραμμα . Πρακτικά περνάει σαν παράμετρος
     * για να χρησιμοποιηθεί από την addArduino() .
     */
    public static void checkForNewArduino(boolean isNewArduino)
    {
        ArrayList<CommPortIdentifier> ar = ReadArduino.findAllArduino() ;
        
        for(CommPortIdentifier port : ar)
        {
            String tmp = Globals.hmAllArduino.get(ar);
            if (tmp==null)
            {
                mainDeviceClient.addArduino(port,isNewArduino) ;
            }
        }
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
     * Συνάρτηση που απλά τυπώνει τις πληροφορίες που έχουν διαβαστεί από το
     * config αρχείο ή απλά τις default πληροφορίες που χρειάζονται για να
     * ρυθμιστούν κάποιες απαραίτητοι παράμετροι του προγράμματος.
     */
    public static void printConfigInfo()
    {
        Tools.Debug.print(devicename);
        Tools.Debug.print(password);
        Tools.Debug.print(address);
        Tools.Debug.print(String.valueOf(port));
        Tools.Debug.print(String.valueOf(secondsForNextTryConnect));
        Tools.Debug.print(String.valueOf(secondsForNextSearchArduino));
        Tools.Debug.print(String.valueOf(viewMessage));
        
    }
    
    /**
     * Συνάρτηση που αναλαμβάνει να διαβάσει το config αρχείο ούτως ώστε να
     * ρυθμιστούν κάποιες απαραίτητοι παράμετροι του προγράμματος.
     * Αν για κάποιο λόγο δεν είναι δυνατή η ανάκτηση του προγράμματος
     * τότε φορτώνονται οι default παράμετροι .
     * @return True αν φορτώθηκε κανονικά το αρχείο ή False αν δεν κατάφερε να φορτωθεί .
     */
    public static boolean loadConfig()
    {
        try {
            //File PMGTCRC = new File(Globals.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()+Globals.fSep+"realcontrollersclientv1" + Globals.fSep+"PMGTCRC.config");
            File PMGTCRC = new File(Globals.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            PMGTCRC = new File(PMGTCRC.getParentFile().getPath() +Globals.fSep+"PMGTCDC.config");
            Tools.Debug.print(PMGTCRC.getAbsolutePath());
            if (PMGTCRC.exists())
            {
                if (readXML(PMGTCRC))
                    Tools.Debug.print("Open 'PMGTCDC.config' File Success!");
                
                else
                {
                    Tools.Debug.print("Error : Failed open 'PMGTCDC.config' File!");
                    return false ;
                }
            }
            else
            {
                Tools.Debug.print("Failed : File 'PMGTCDC.config' not Exists!");
                devicename = "perama" ;
                password = "perama" ;
                address = "localhost" ;
                port = 50128 ;
                secondsForNextSearchArduino = 10 ;
                secondsForNextTryConnect = -1 ;
                return true ;
            }
        } catch (URISyntaxException ex) {
            return false ;
        }
        return true ;
    }
    /**
     * Μέθοδος που αναλαμβάνει να διαβάσει όλες τις απαραίτητες πληροφορίες
     * από ένα αρχείο xml (Μιας κατάλληλης μορφής βέβαια) .
     * Σκοπός της είναι να αξιοποιηθούν όλα αυτά τα δεδομένα , δηλαδή να ρυθμιστούν
     * κάποιες απαραίτητοι παράμετροι του προγράμματος (όπως devicename,password κ.α).
     * @param f Το αρχείο(με μορφή και σύνταξη xml) από το οποίο θα ανακτηθούν
     * όλα τα στοιχεία για να ρυθμιστούν κάποιοι απαραίτητοι παράμετροι του προγράμματος.
     * @return True αν όλα πήγαν καλά και ανακτήθηκε σωστά το αρχείο και False
     * αν κάτι δεν πήγε καλά .
     */
    private static boolean readXML(File f) {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            dom = db.parse(f);
            
            Element doc = dom.getDocumentElement();
            
            devicename = getTextValue(doc, "Devicename", 0) ;
            
            password = getTextValue(doc, "Password", 0) ;
            
            address = getTextValue(doc, "Address", 0) ;
            
            String strPort = getTextValue(doc, "Port", 0) ;
            if (!Tools.isNumeric(strPort))
            {
                Tools.Debug.print("Error : Port is not number (read xml!)");
                return false ;
            }
            port = Integer.parseInt(strPort) ;
            
            String strSecondsForNextTryConnect = getTextValue(doc, "secondsForNextTryConnect", 0) ;
            if (!Tools.isNumeric(strSecondsForNextTryConnect))
            {
                // Tools.Debug.print("Error : secondsForNextTryConnect is not number (read xml!)");
                // return false ;
                secondsForNextTryConnect = 60 ;
            }
            else
                secondsForNextTryConnect = Integer.parseInt(strSecondsForNextTryConnect) ;
            
            String strSecondsForNextSearchArduino = getTextValue(doc, "secondsForNextSearchArduino", 0) ;
            if (!Tools.isNumeric(strSecondsForNextSearchArduino))
            {
                //Tools.Debug.print("Error : secondsForNextSearchArduino is not number (read xml!)");
                //return false ;
                secondsForNextSearchArduino = 20 ;
            }
            else
                secondsForNextSearchArduino = Integer.parseInt(strSecondsForNextSearchArduino) ;
            
            String strViewMessage = getTextValue(doc, "viewMessage", 0) ;
            if (strViewMessage.toUpperCase().equals("TRUE"))
                viewMessage = true ;
            else
                viewMessage = false ;
            
        } catch (ParserConfigurationException | SAXException pce) {
            Tools.Debug.print(pce.getMessage());
            Tools.Debug.print("Error : Exception(read xml!)");
            return false;
        } catch (IOException | NumberFormatException ioe) {
            Tools.Debug.print(ioe.getMessage());
            Tools.Debug.print("Error : Exception(read xml!)");
            return false ;
        }catch (Exception e) {
            Tools.Debug.print(e.getMessage());
            Tools.Debug.print("Error : Exception(read xml!)");
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