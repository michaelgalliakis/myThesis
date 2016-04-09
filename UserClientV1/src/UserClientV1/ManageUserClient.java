package UserClientV1;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Κλάση που δημιουργήθηκε για να λειτουργεί υποστηρικτικά προς διάφορες άλλες κλάσεις
 * και πιο συγκεκριμένα για να μπορούν οι κλάσεις {@link Ptab},{@link FuserLogin},{@link Fmain}
 * {@link Device} και {@link DfChangeText} να εκτελούν διάφορες static συναρτήσεις της .
 * Απώτερος σκοπός είναι να μειωθεί η πολυπλοκότητα και να υπάρχει καλύτερη διαχείριση .
 * @author Michael Galliakis
 */
public class ManageUserClient {
    
    private static String messageFailed = "#UD$Answer:1*(FAILED);" ;
    private static String messageOK = "#UD$Answer:1*(OK);" ;
    /**
     * Συνάρτηση που αναλαμβάνει να σταματήσει όλες τις εποπτείες που έχει
     * ανοίξει ο χρήστης σε καρτέλες και βέβαια συγχρόνως να αποθηκευτούν και όλες
     * οι πληροφορίες που χρειάζονται σε αντίστοιχα αρχεία xml .
     * Χρησιμοποιείται πρακτικά όταν χρήστης επιλέξει μέσα από την εφαρμογή να κλείσει
     * τον User client ή να κάνει logout .
     */
    public static void saveAllDevices()
    {
        Iterator<String> keyIterDev = Globals.hmDevices.keySet().iterator();
        String devKey ;
        while (keyIterDev.hasNext())
        {
            
            devKey = keyIterDev.next() ;
            
            for (Component com :Globals.objMainFrame.tpAllDevices.getComponents())
            {
                try
                {
                    Ptab devTab = (Ptab) com ;
                    if (devTab!=null)
                        devTab.stopMonitoring();
                }
                catch(Exception ex)
                {
                    //Nothing
                }
            }
            //Globals.hmDevices.get(devKey).saveToXML();
        }
    }
    /**
     * Static μέθοδος που αναλαμβάνει να κάνει όλα τα απαραίτητα βήματα που χρειάζονται για
     * να συνδεθεί,να πιστοποιηθεί στο Server και να πάρει όλες τις πληροφορίες για τις συσκευές
     * του συγκεκριμένου χρήστη που έχει συνδεθεί και σε επόμενη φάση να δημιουργήσει
     * το παραθυράκι των συσκευών({@link Ddevices}) και ένα {@link ListenThread} για
     * να ενημερώνεται ο User Client συνεχώς για πιθανόν αλλαγές στις καταστάσεις
     * (Up ή Down) των διαθέσιμων συσκευών του χρήστη .
     * @param mainFrame Είναι ένα JFrame που χρειάζεται για την εμφάνιση τυχών
     * μηνυμάτων βοήθειας προς τον χρήστη .
     * @return True Αν όλα πήγαν καλά και False αν κάτι δεν πήγε .
     */
    public static boolean initDevicesFrame(Component mainFrame)
    {
        
        ManageSocket ms = ManageUserClient.connectProcess(Globals.address, Globals.port);
        
        if (ms==null)
        {
            Tools.Debug.print("Connect UnsuccessFully!");
            JOptionPane.showMessageDialog(mainFrame,"Connect Failed","ThesisV1",JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        
        if (!ManageUserClient.certificationProcess(ms))
        {
            Tools.Debug.print("CertificationUnsuccessFully!");
            JOptionPane.showMessageDialog(mainFrame,"Certification Failed","ThesisV1",JOptionPane.PLAIN_MESSAGE);
            return false ;
        }
        
        String DBUserID = ManageUserClient.loginProcess(ms, Globals.username, Globals.password);
        if (DBUserID.equals("X"))
        {
            Tools.Debug.print("Login UnsuccessFully!");
            JOptionPane.showMessageDialog(mainFrame,"Login Failed","ThesisV1",JOptionPane.PLAIN_MESSAGE);
            return false ;
        }
        Globals.DBUserID = DBUserID ;
        Globals.alWithDevices = ManageUserClient.getDeviceInfoProcess(ms,DBUserID) ;
        
        
        Ddevices frame = new Ddevices(new JFrame(), "Devices (" + Globals.username+")");
        frame.setSize(250, 300);
        frame.setLocationByPlatform( true );
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.setDevicesInfo(Globals.alWithDevices);
        
        Globals.objDevices = frame ;
        ListenThread myThread = new ListenThread(ms.clientSocket, ms.in) ;
        myThread.start();
        
        return true ;
    }
    
    
    /**
     * Static μέθοδος που αναλαμβάνει να πιστοποιήσει στον Server ότι ο συγκεκριμένος
     * User Client είναι κατάλληλος και σωστός για να συνεχιστεί η επικοινωνία .
     * @param ms Ένα {@link ManageSocket} με τις απαραίτητες πληροφορίες
     * για επικοινωνία με το Server .
     * @return True αν επιτεύχθηκε η πιστοποίηση ή False για οποιαδήποτε άλλο λόγο .
     */
    public static boolean certificationProcess(ManageSocket ms)
    {
        try
        {
            String message ;
            ManageSocketMessage msm  = new ManageSocketMessage();
            String tmp[][] = {{Globals.CERTIFICATIONUSERDESKTOP}} ;
            message = ManageSocketMessage.newMessage("UD", ManageSocketMessage.CommandType.certification, tmp);
            Tools.send(message,ms.out) ;
            
            msm.reload(ms.in.readLine()) ;
            if (Tools.isCorrectCommand(msm.getCommand(),"Answer",messageFailed,msm.getMessage(),ms.out))
            {
                //System.out.println(msm.getMessage());
                //System.out.println(msm.getParameters().get(0).get(0));
                
                if ("FAILED".equals(msm.getParameters().get(0).get(0)))
                {
                    Tools.Debug.print("CertificationFailed!");
                    Tools.send(messageFailed,ms.out);
                    return false;
                }
                else
                    Tools.Debug.print("Certification OK!");
            }
            else
                return false;
        } catch (Exception ex) {
            Tools.Debug.print("Certification exception!");
            return false ;
        }
        return true ;
    }
    /**
     * Static μέθοδος που αναλαμβάνει να πιστοποιήσει στον Server ότι το συγκεκριμένο
     * username και password που έδωσε ο χρήστης είναι σωστά .
     * @param ms Ένα {@link ManageSocket} με τις απαραίτητες πληροφορίες
     * για επικοινωνία με το Server .
     * @param userName Το username που έδωσε ο χρήστης .
     * @param passWord Το password που έδωσε ο χρήστης .
     * @return Επιστρέφεται το ID του χρήστη αν επιτεύθηκε η πιστοποίηση ή "X"
     * για οποιαδήποτε άλλη περίπτωση .
     */
    public static String loginProcess(ManageSocket ms,String userName,String passWord)
    {
        try
        {
            String message ;
            ManageSocketMessage msm  = new ManageSocketMessage();
            
            String[][] tmp = {{userName},{passWord}};
            message = ManageSocketMessage.newMessage("UD", ManageSocketMessage.CommandType.Login, tmp);
            Tools.send(message,ms.out) ;
            
            msm.reload(ms.in.readLine()) ;
            if (Tools.isCorrectCommand(msm.getCommand(),"LoginReply",messageFailed,msm.getMessage(),ms.out))
            {
                if ("FAILED".equals(msm.getParameters().get(0).get(0)))
                {
                    Tools.Debug.print("Username or/and Password incorrect - Login Failed");
                    Tools.send(messageFailed,ms.out);
                    return "X";
                }
                else
                    Tools.Debug.print("Username and Password Correct - Login OK!");
            }
            else
                return "X";
            
            if (msm.getParameters().get(1).size()>0)
                return msm.getParameters().get(1).get(0) ;
            else
                return "X" ;
        } catch (IOException ex) {
            //Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            Tools.Debug.print("Certification exception!");
            return "X" ;
        }
    }
    /**
     * Static μέθοδος που αναλαμβάνει να "μάθει" από το Server τις απαραίτητες
     *  πληροφορίες από όλες τις συσκευές του χρήστη .
     * Πρακτικά δηλαδή για όλες τις συσκευές του χρήστη "μαθαίνει" το όνομα τους ,
     * το ID τους , αν είναι up ή down και τα δικαιώματα προσπέλασης που έχει ο χρήστης σε αυτές.
     * @param ms Ένα {@link ManageSocket} με τις απαραίτητες πληροφορίες
     * για επικοινωνία με το Server .
     * @param ID Το ID που έχει πάρει από το Server το συγκεκριμένο socket του UserClient.
     * @return Μια λίστα μέσα σε λίστα από συμβολοσειρές με όλες τις απαραίτητες
     * αρχικές πληροφορίες όλων των συσκευών του χρήστη που είναι συνδεδεμένος
     * στο User Client .
     */
    public static ArrayList<ArrayList<String>> getDeviceInfoProcess(ManageSocket ms,String ID)
    {
        try {
            String messageVarFailed = "#"+ID + "@UD"+"$Answer:1*(FAILED);" ;
            String messageVarOK = "#"+ ID + "@UD"+"$Quit:1*(Bey-Bey);" ;
            String message ;
            ManageSocketMessage msm  = new ManageSocketMessage();
            
            String[][] tmp = {{}};
            message = ManageSocketMessage.newMessage(ID + "@UD", ManageSocketMessage.CommandType.GetDevicesInfo, tmp);
            Tools.send(message,ms.out) ;
            
            
            msm.reload(ms.in.readLine()) ;
            
            if (Tools.isCorrectCommand(msm.getCommand(),"PutDevicesInfo",messageVarFailed,msm.getMessage(),ms.out))
            {
                if ("FAILED".equals(msm.getParameters().get(0).get(0)))
                {
                    Tools.Debug.print("Device info Failed");
                    Tools.send(messageVarFailed,ms.out);
                    return null ;
                }
                else
                    Tools.Debug.print("Device info OK!");
            }
            else
                return null;
            
            Tools.send(messageVarOK,ms.out);
            return msm.getParameters() ;
        } catch (IOException ex) {
            Tools.Debug.print("ERROR - Device info Failed");
            return null ;
        }
    }
    /**
     * Static μέθοδος που "μαθαίνει" από το Server τι τύπου είναι ο χρήστης.
     * Δηλαδή αν είναι απλός χρήστης , VIP ή συστήματος .
     * Πρακτικά το μόνο που αλλάζει πάνω στην συσκευή είναι το πάνω αριστερά
     * (όπως κοιτάμε εμείς την οθόνη) εικονίδιο χρήστη .
     * @param ms Ένα {@link ManageSocket} με τις απαραίτητες πληροφορίες
     * για επικοινωνία με το Server .
     * @param ID Το ID που έχει πάρει από το Server το συγκεκριμένο socket του UserClient.
     * @return To αρχικό γράμμα του τύπου χρήστη(S,V,U) ή "X" σε περίπτωση σφάλματος ή
     * άλλης απρόβλεπτης περίπτωσης .
     */
    public static String getTypeUser(ManageSocket ms,String ID)
    {
        String messageVarFailed = "#"+ID + "@UD"+"$Answer:1*(FAILED);" ;
        String messageVarOK = "#"+ ID + "@UD"+"$Quit:1*(Bey-Bey);" ;
        String message ;
        ManageSocketMessage msm  = new ManageSocketMessage();
        
        String[][] tmp = {{}};
        message = ManageSocketMessage.newMessage(ID + "@UD", ManageSocketMessage.CommandType.GetTypeUser, tmp);
        Tools.send(message,ms.out) ;
        try {
            msm.reload(ms.in.readLine()) ;
        } catch (IOException ex) {
            return "X" ;
        }
        if (Tools.isCorrectCommand(msm.getCommand(),"PutTypeUser",messageVarFailed,msm.getMessage(),ms.out))
        {
            if ("FAILED".equals(msm.getParameters().get(0).get(0)))
            {
                Tools.Debug.print("Type User Failed");
                Tools.send(messageVarFailed,ms.out);
                return "X" ;
            }
            else
                Tools.Debug.print("Type User OK!");
        }
        else
            return "X";
        
        Tools.send(messageVarOK,ms.out);
        return msm.getParameters().get(0).get(0) ;
    }
    /**
     * Μέθοδος που αναλαμβάνει να συνδεθεί για πρώτη φορά στο Server με βάση την
     * ip και την πόρτα που έχει βάλει ο χρήστης στη φόρμα σύνδεσης του ({@link FuserLogin}).
     * Πρακτικά δημιουργείται το socket και άρα στήνεται το ανοικτό κανάλι με το server
     * και στην συνέχεια δημιουργούνται 2 αντικείμενα που χρειαζόμαστε για να διαβάζουμε
     * και να στέλνουμε μηνύματα από και προς τον Server . Όλες οι προηγούμενες πληροφορίες
     * βέβαια επιστρέφονται από τη μέθοδο ομαδοποιημένες στη μορφή ενός {@link ManageSocket}.
     * @param address Διεύθυνση ip στην οποία έχει ο Server μας.
     * @param port Πόρτα στην οποία ακούει ο Server μας .
     * @return Ένα {@link ManageSocket} με ομαδοποιημένες όλες τις πληροφορίες που
     * αφορούν το νέο socket μας .
     */
    public static ManageSocket connectProcess(String address,int port)
    {
        try
        {
            Socket userClientSocket = new Socket(address, port);
            PrintWriter out = new PrintWriter(userClientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(userClientSocket.getInputStream()));
            
            ManageSocket ms = new ManageSocket(userClientSocket, out, in, null, null) ;
            return ms ;
        }
        catch (Exception ex)
        {
            return null ;
        }
    }
    
    /**
     * Static μέθοδος που αναλαμβάνει να "μάθει" από το Server όλες τις πληροφορίες
     * μιας συγκεκριμένης συσκευής όσο αφορά τους ελεγκτές και τις μονάδες της.
     * Πρακτικά αντλεί τα μηνύματα αρχικοποίησης μιας συγκεκριμένης συσκευής από
     * το Server με σκοπό να προετοιμαστεί κατάλληλα με όλες τις απαραίτητες πληροφορίες
     * το αντικείμενο {@link Device} που αντιστοιχεί στην συγκεκριμένη συσκευή.
     * @param ms Ένα {@link ManageSocket} με τις απαραίτητες πληροφορίες
     * για επικοινωνία με το Server .
     * @param ID Το ID που έχει πάρει από το Server το συγκεκριμένο socket του UserClient.
     * @param devID Το ID της συγκεκριμένης συσκευής .
     * @param devName Το όνομα της συγκεκριμένης συσκευής .
     * @param devAccess Τα δικαιώματα προσπέλασης που έχει ο χρήστης πάνω στη συσκευή .
     * @return Ένα {@link Device} πλήρως συμπληρωμένο .
     */
    public static Device bringMeDeviceProcess(ManageSocket ms,String ID,String devID,String devName,String devAccess)
    {
        try {
            String messageVarFailed = "#"+ID + "@UD"+"$Answer:1*(FAILED);" ;
            String messageVarOK = "#"+ ID + "@UD"+"$Answer:1*(OK);" ;
            
            ManageSocketMessage msm  = new ManageSocketMessage();
            
            String message = ManageSocketMessage.newMessage(ID + "@UD", ManageSocketMessage.CommandType.BringMeDevice, 1, "("+devID+")") ;
            Tools.send(message,ms.out) ;
            Device device = new Device(ID,ID + "@UD",devName,ms,devAccess) ;
            
            
            msm.reload(ms.in.readLine()) ;
            
            if (Tools.isCorrectCommand(msm.getCommand(),"InitControllers",messageVarFailed,msm.getMessage(),ms.out))
            {
                //initControllers = msm.getMessage() ;
                Tools.Debug.print("InitControllers OK!");
                Tools.send(messageVarOK,ms.out);
            }
            else
                return null;
            
            
            int sumOfControllers  = msm.getSumParameters() ;
            
            ArrayList<String> newControllers  = new ArrayList<>();
            for (int i = 0 ; i < sumOfControllers ;i++)
            {
                msm.reload(ms.in.readLine()) ;
                if (Tools.isCorrectCommand(msm.getCommand(),"NewController",messageVarFailed,msm.getMessage(),ms.out))
                {
                    //initControllers = msm.getMessage() ;
                    newControllers.add(msm.getMessage()) ;
                    Tools.Debug.print("New Controller : "+msm.getParameters().get(0).get(0)+" OK!");
                    //ELT.send(messageOK,out);
                }
                else
                    return null;
            }
            
            msm.reload(ms.in.readLine()) ;
            if (Tools.isCorrectCommand(msm.getCommand(),"InitializationFinished",messageVarFailed,msm.getMessage(),ms.out))
            {
                Tools.Debug.print("Initialization Finished!");
                Tools.send(messageVarOK,ms.out);
            }
            else
                return null;
            
            device.prepareDevice(newControllers);
            //reloadDeviceTable() ;
            return device ;
        } catch (IOException ex) {
            //Logger.getLogger(ManageUserClient.class.getName()).log(Level.SEVERE, null, ex);
            Tools.Debug.print("ERROR - Initialization Finished!");
            return null;
        }
    }
    /**
     * Static μέθοδος που ανοίγει όλα τα φύλλα ενός δέντρου (JTree) εκτός
     * από τα φύλλα των τελευταίων "κλαδιών" .
     * @param tree Το δέντρο στο οποίο θα ανοιχτούν τα φύλλα .
     */
    public static void expandAll(JTree tree) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(tree, new TreePath(root));
        
    }
    /**
     * Static μέθοδος που ανοίγει όλα τα φύλλα ενός δέντρου (JTree) εκτός
     * από τα φύλλα των τελευταίων "κλαδιών" .
     * @param tree Το δέντρο στο οποίο θα ανοιχτούν τα φύλλα .
     * @param parent Η κορυφή του path από το οποίο θα ξεκινήσει να ανοίγουν όλα
     * τα φύλλα του από εκεί και κάτω .
     */
    public static void expandAll(JTree tree, TreePath parent) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                if (!(n.getChildCount()>0 && n.getChildAt(0).getChildCount() ==0))
                {
                    TreePath path = parent.pathByAddingChild(n);
                    expandAll(tree, path);
                }
            }
        }
        tree.expandPath(parent);
        // tree.collapsePath(parent);
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