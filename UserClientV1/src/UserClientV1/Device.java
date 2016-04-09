package UserClientV1;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import static UserClientV1.ManageUserClient.expandAll;
/**
 * Κλάση που μας βοηθάει να κάνουμε καλύτερη διαχείριση του περιεχομένου μιας συσκευής .
 * Είναι όλες οι πληροφορίες που αφορούν μια συσκευή μαζί με μεθόδους που μας δίνουν
 * την δυνατότητα να κάνουμε ομαδοποιημένα πράγματα πολύ εύκολα και στοχευμένα
 * να επηρεάσουμε το περιεχόμενο της συσκευής μας .
 * @author Michael Galliakis
 */
public class Device {
    private static final int sumOfDifferentsTypes  = 12;
    private String deviceName ;
    
    private String deviceID ;
    private String senderFullID ;
    private ArrayList<UnitControl> allControls ;
    public BackgroundPanel backPanel ;
    private HashMap<String,HashMap<String,Unit>> allUnits ;
    private ManageSocketMessage msm ;
    public  ManageSocket ms ;
    public String devAccess ;
    public boolean saveSettings = true;
    public ListenThread ltForRead ;
    private String pathFile ="" ;
    public Ptab myPTab ;
    JTree tree ;
    DefaultTreeModel  treeModel ;
    public HashMap<CustomTreeNode ,Unit> hmUnitNodes = new HashMap<>() ;
    int sumOfControllers ;
    
    public Device(String id,String sfID,String dn,ManageSocket _ms,String devAcc) {
        deviceName = dn ;
        deviceID = id ;
        senderFullID = sfID ;
        allUnits = new HashMap<>() ;
        msm = new ManageSocketMessage() ;
        sumOfControllers = 0 ;
        allControls = new ArrayList<>() ;
        ms = _ms ;
        devAccess = devAcc ;
        saveSettings = true ;
    }
    /**
     * Get μέθοδος που απλά επιστρέφει το μονοπάτι την εικόνας του background
     * σε συμβολοσειρά .
     * @return Το μονοπάτι της εικόνας για το background της συσκευής .
     */
    public String getPathFile() {
        return pathFile;
    }
    /**
     * Set μέθοδος που με βάση ένα μονοπάτι εικόνας μέσα στο δίσκο (παράμετρος)
     * την φορτώνει και την εμφανίζει σαν background στην καρτέλα της συσκευής .
     * @param pathFile Συμβολοσειρά με ολόκληρο το μονοπάτι της εικόνας .
     */
    public void setPathFile(String pathFile) {
        if (pathFile==null)
            pathFile = "";
        
        this.pathFile = pathFile;
        
        if (!pathFile.equals(""))
            backPanel.setImage(new File(pathFile));
    }
    /**
     * Απλή set μέθοδος που ενημερώνει και συσχετίσει την εκάστοτε συσκευή με το
     * αντίστοιχο ListenThread που την αφορά .
     * @param lt  Κάποιο {@link ListenThread} με το οποίο θα συσχετίσθει το {@link Device} .
     */
    public void setLTForRead(ListenThread lt)
    {
        ltForRead = lt ;
    }
    /**
     * Απλή set μέθοδος που ενημερώνει και συσχετίσει την εκάστοτε συσκευή με το
     * αντίστοιχο Ptab που την αφορά .
     * @param tab  Κάποιο {@link Ptab} με το οποίο θα συσχετηθεί το {@link Device} .
     */
    public void setPTab(Ptab tab)
    {
        myPTab = tab ;
    }
    /**
     * Μέθοδος που αναλαμβάνει να στείλει το ανάλογο μήνυμα αλλαγής τιμής στον
     * Server μέσω του αντίστοιχου socket .
     * @param unitStrValue Μια συμβολοσειρά με το όνομα του ελεγκτή,το όνομα της
     * μονάδας και την νέα τιμή .
     * Μεταξύ τους έχουν μια "|" για διαχωριστικό όπως επίσης να αναφέρουμε ότι
     * και όλη η συμβολοσειρά με την παραπάνω πληροφορία πρέπει να είναι μέσα
     * σε παρένθεση . Πχ ένα unitStrValue θα μπορούσε να είναι : "(con1|unit1|21)" .
     */
    public void changeRemoteValue(String unitStrValue)
    {
        String message ;
        message = "#"+senderFullID+"$ChangeValues:1*"+ unitStrValue +";" ;
        Tools.send(message,ms.out) ;
        Tools.Debug.print(message);
        
    }
    /**
     * Μέθοδος που αναλαμβάνει να στείλει το ανάλογο μήνυμα αλλαγής mode(τρόπος λειτουργίας)
     * στον Server μέσω του αντίστοιχου socket .
     * @param unitStrMode Μια συμβολοσειρά με το όνομα του ελεγκτή,το όνομα της
     * μονάδας και το νέο mode .
     * Μεταξύ τους έχουν μια "|" για διαχωριστικό όπως επίσης να αναφέρουμε ότι
     * και όλη η συμβολοσειρά με την παραπάνω πληροφορία πρέπει να είναι μέσα
     * σε παρένθεση . Πχ ένα unitStrMode θα μπορούσε να είναι : "(con1|unit1|3)" .
     */
    public void changeRemoteMode(String unitStrMode)
    {
        String message ;
        message = "#"+senderFullID+"$ChangeModes:1*"+ unitStrMode +";" ;
        Tools.send(message,ms.out) ;
        Tools.Debug.print(message);
        
    }
    /**
     * Μέθοδος που "καθαρίζει" όλες τις επιλεγμένες μονάδες από δέξια που είναι
     * "εικονίδια" (δηλαδή παύουν να είναι πορτοκαλί αν ήταν) όπως επίσης σταματάει
     * να είναι επιλεγμένο κάποιο "κλαδί" από το "δέντρο" της εκάστοτε συσκευής .
     */
    public void clearSelection()
    {
        UnitControl.clearSelection(allControls);
        tree.clearSelection();
    }
    //Απλοί Get μέθοδοι - Αρχή :
    public String getDeviceID() {
        return deviceID;
    }
    
    public String getDeviceName() {
        return deviceName;
    }
    //Απλοί Get μέθοδοι - Τέλος :
    
    /**
     * Μέθοδος που αναλαμβάνει να κτίσει ένας πλήρες δέντρο με όλες τις απαραίτητες
     * πληροφορίες που γνωρίζει από το ίδιο το {@link Device} και στην συνέχεια να
     * επιστρέφει ένα JTree .
     * Σκοπός της μεθόδου είναι να μπορούμε πολύ απλά και ομαδοποιημένα να πάρουμε
     * ένας πλήρες "δέντρο" και απλά να το εμφανίσουμε στην δεξιά πλευρά (όπως κοιτάμε εμείς)
     * της εκάστοτε καρτέλας της συγκεκριμένης συσκευής .
     * @param createTree Αν true τότε δημιουργείται εκ νέου το "δέντρο" αλλιώς απλά επιστρέφετε
     * το "δέντρο" που είχε φτιαχτεί την τελευταία φορά .
     * @return Ένα πλήρες JTree με κορυφή το όνομα της συσκευής και μέσα του όλους
     * τους ελεγκτές με όλες τις μονάδες τους κ.α .
     */
    public JTree getTree(boolean createTree)
    {
        if (createTree)
        {
            CustomTreeNode top = new CustomTreeNode(deviceName);
            
            createNodes(top);
            
            tree = new JTree(top);
            treeModel =  (DefaultTreeModel)tree.getModel();
            tree.setName(deviceName);
            /**
             * Άμεση δημιουργία προγραμματιστικά ενός MouseListener
             */
            tree.addMouseListener(new MouseListener() {
                /**
                 * Μέθοδος που εκτελείτε όταν πατήσουμε κάποιο "κλικ" μέσα στο
                 * δέντρο μας .
                 * Πρακτικά το χρειαζόμαστε στην περίπτωση που είναι το "φύλλο" είναι
                 * ελεγκτής ή μονάδα για να ανοίγει ένα μενού και να έχει ο χρήστης
                 * την δυνατότητα να κάνει αλλαγή κάποιου description.
                 * @param e Ένα Default MouseEvent αντικείμενο που το χρειαζόμαστε για να
                 * βρούμε αν ο χρήστης πάτησε δεξί κλικ όπως επίσης για να βρούμε
                 * με βάση τη θέση που έγινε το κλικ , σε ποιο "φύλλο" του δέντρου
                 * απευθύνεται .
                 */
                @Override
                public void mouseClicked( MouseEvent e) {
                    if ( SwingUtilities.isRightMouseButton ( e ) )
                    {
                        Point myPointOfScreen = e.getLocationOnScreen() ;
                        TreePath path = tree.getPathForLocation ( e.getX (), e.getY () );
                        
                        tree.setSelectionPath(path);
                        Rectangle pathBounds = tree.getUI ().getPathBounds ( tree, path );
                        if ( pathBounds != null && pathBounds.contains ( e.getX (), e.getY () ) )
                        {
                            CustomTreeNode ctn = (CustomTreeNode)path.getLastPathComponent();
                            if (ctn.getLevel()==1 || ctn.getLevel()==3)
                            {
                                JPopupMenu menu = new JPopupMenu ();
                                JMenuItem jmi = new JMenuItem ( "Change Description" ) ;
                                /**
                                 * Άμεση δημιουργία προγραμματιστικά ενός MouseListener
                                 */
                                jmi.addMouseListener(new MouseListener() {
                                    /**
                                     * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
                                     * @param me Ένα Default MouseEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
                                     */
                                    @Override
                                    public void mouseClicked(MouseEvent me) {
                                        //new DfChangeText(tree,ctn,myPointOfScreen) ;
                                        // System.out.println("Mike12");
                                    }
                                    /**
                                     * Μέθοδος που εκτελείτε όταν έχει πατηθεί κάποιο "κλικ" πάνω στο
                                     * menuitem 'Change Description" .
                                     * Και ουσιαστικά ανοίγει ένα {@link DfChangeText} για να δοθεί η δυνατότητα
                                     * στον χρήστη να αλλάξει το description μιας μονάδας ή ενός ελεγκτή.
                                     * @param me Ένα Default MouseEvent αντικείμενο που δεν το χρησιμοποιούμε .
                                     */
                                    @Override
                                    public void mousePressed(MouseEvent me) {
                                        new DfChangeText(tree,(CustomTreeNode)path.getLastPathComponent(),myPointOfScreen).setVisible(true);
                                    }
                                    /**
                                     * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
                                     * @param me Ένα Default MouseEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
                                     */
                                    @Override
                                    public void mouseReleased(MouseEvent me) {
                                    }
                                    /**
                                     * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
                                     * @param me Ένα Default MouseEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
                                     */
                                    @Override
                                    public void mouseEntered(MouseEvent me) {
                                    }
                                    /**
                                     * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
                                     * @param me Ένα Default MouseEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
                                     */
                                    @Override
                                    public void mouseExited(MouseEvent me) {
                                    }
                                });
                                menu.add ( jmi );
                                menu.show ( tree, pathBounds.x+50, pathBounds.y + pathBounds.height-20 );
                            }
                        }
                    }
                }
                /**
                 * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
                 * @param e Ένα Default MouseEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
                 */
                @Override
                public void mousePressed ( MouseEvent e )
                {
                    
                }
                /**
                 * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
                 * @param me Ένα Default MouseEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
                 */
                @Override
                public void mouseReleased(MouseEvent me) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                /**
                 * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
                 * @param me Ένα Default MouseEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
                 */
                @Override
                public void mouseEntered(MouseEvent me) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                /**
                 * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
                 * @param me Ένα Default MouseEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
                 */
                @Override
                public void mouseExited(MouseEvent me) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            tree.setCellRenderer(new MyRenderer());
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            /**
             * Άμεση δημιουργία προγραμματιστικά ενός TreeSelectionListener
             */
            tree.addTreeSelectionListener(new TreeSelectionListener() {
                /**
                 * Μέθοδος που εκτελείτε όταν αλλάξει το επιλεγμένο "φύλλο"
                 * του δέντρο μας .
                 * Πρακτικά το χρειαζόμαστε για να απο-επιλέγει τα επιλεγμένα εικονίδια
                 * των μονάδων (δηλαδή να παύουν να είναι πορτοκαλί) και ανάλογα
                 * το "φύλλο" που έχει επιλέξει να επιλέγονται (Να γίνονται πορτοκαλί)
                 * τα αντίστοιχα εικονίδια μονάδων(Αν επιλέξει ο χρήστης "φύλλο" μας
                 * μονάδας επιλέγεται[γίνεται πορτοκαλί] μόνο ένα εικονίδιο μονάδας στα δεξιά)
                 * @param tse Ένα Default TreeSelectionEvent αντικείμενο το οποίο
                 * δεν χρησιμοποιείται.
                 */
                @Override
                public void valueChanged(TreeSelectionEvent tse) {
                    CustomTreeNode node = (CustomTreeNode)
                            tree.getLastSelectedPathComponent();
                    
                    if (node != null)
                    {
                        switch(node.getLevel())
                        {
                            case 0 :
                                UnitControl.clearSelection(allControls);
                                break ;
                            case 1 :
                                UnitControl.clearSelection(allControls);
                                for (int j = 0 ; j< node.getChildCount();j++)
                                    for (int i = 0 ; i< node.getChildAt(j).getChildCount();i++)
                                        hmUnitNodes.get(node.getChildAt(j).getChildAt(i)).uc.setSelected(true);
                                break ;
                            case 2 :
                                if (node.getChildCount()>0)
                                {
                                    UnitControl.clearSelection(allControls);
                                    for (int i = 0 ; i< node.getChildCount();i++)
                                        hmUnitNodes.get(node.getChildAt(i)).uc.setSelected(true);
                                }
                                
                                break ;
                            case 3 :
                                UnitControl.clearSelection(allControls);
                                hmUnitNodes.get(node).uc.setSelected(true);
                                break ;
                            case 4 :
                                UnitControl.clearSelection(allControls);
                                hmUnitNodes.get(node.getParent()).uc.setSelected(true);
                                break ;
                        }
                    }
                }
            });
        }
        return tree ;
    }
    /**
     * Custom Renderer για το δέντρο το οποίο έχει κληρονομήσει το DefaultTreeCellRenderer.
     * Σκοπός του είναι πρακτικά να εμφανίζονται τα διάφορα εικονίδια στα "φύλλα" του
     * δέντρου και όχι αυτά της προεπιλογής .
     * Δηλαδή στην κορυφή το εικονίδιο του επεξεργαστή , στους ελεγκτές το εικονίδιο
     * του arduino , στις μονάδες εικονίδια ανάλογα με το τύπο τους ,
     * στις κατηγορίες 2(ανοικτό-κλειστό) εικονίδια πιο ξεχωριστά όπως επίσης και
     * ένα πράσινο φύλλο στις πληροφορίες της κάθε μονάδας .
     */
    private class MyRenderer extends DefaultTreeCellRenderer {
        /**
         * Κατασκευαστής που αρχικοποιεί τα εικονίδια των "φύλλων" του δέντρου.
         */
        public MyRenderer() {
            //backgroundSelectionColor = Globals.selectedColor ;
            textSelectionColor =Globals.selectedColor ;
            setClosedIcon(Globals.imTreeOpen);
            setOpenIcon(Globals.imTreeClose);
            setLeafIcon(Globals.imTreeLeaf);
        }
        /**
         * Μέθοδος που εκτελείτε αυτόματα κάθε φορά που "επαναζωγραφίζεται" το δέντρο.
         * @param tree Default παράμετρος που δεν επεμβαίνουμε προγραμματιστικά.
         * @param value Default παράμετρος που πρακτικά είναι ένα {@link CustomTreeNode}
         * και που σε επόμενη φάση θα ερευνηθεί και θα πάρει την κατάλληλη εικόνα με βάση
         * το είδος του "φύλλου".
         * @param sel Default παράμετρος που δεν επεμβαίνουμε προγραμματιστικά.
         * @param expanded Default παράμετρος που δεν επεμβαίνουμε προγραμματιστικά.
         * @param leaf Default παράμετρος που δεν επεμβαίνουμε προγραμματιστικά.
         * @param row Default παράμετρος που δεν επεμβαίνουμε προγραμματιστικά.
         * @param hasFocus Default παράμετρος που δεν επεμβαίνουμε προγραμματιστικά.
         * @return Default Component αντικείμενο που επιστρέφεται αυτόματα κάθε
         * κάποια χρονική στιγμή για να "επαναζωγραφιστεί" σε επόμενη φάση το δέντρο.
         */
        @Override
        public Component getTreeCellRendererComponent(
                JTree tree,
                Object value,
                boolean sel,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {
            
            super.getTreeCellRendererComponent(
                    tree, value, sel,
                    expanded, leaf, row,
                    hasFocus);
            ImageIcon imIc = getImageIconFromTreeNode(value) ;
            if (imIc!= null)
                setIcon(imIc);
            
            //CustomTreeNode node = (CustomTreeNode)value;
            
            return this;
        }}
    /**
     * Μέθοδος που επιστρέφει το κατάλληλο εικονίδιο με βάση το είδος του "φύλλου" .
     * @param value Ένα Default Object αντικείμενο που πρακτικά είναι ένα {@link CustomTreeNode}
     * το οποίο θα ερευνηθεί και θα επιστραφεί η κατάλληλη εικόνα με βάση αυτού .
     * @return Ένα ImageIcon - εικόνα το οποίο θα έχει το αντίστοιχο "φύλλο" του δέντρου.
     */
    protected ImageIcon getImageIconFromTreeNode(Object value) {
        CustomTreeNode node = (CustomTreeNode)value;
        ImageIcon tmpImIc  ;
        if (node.getLevel()==0)
            tmpImIc = Globals.imProcessor ;
        else if (node.getLevel()==1)
            tmpImIc = Globals.imController ;
        else
            tmpImIc = (hmUnitNodes.get(node)!=null) ? hmUnitNodes.get(node).getImIc(): null ;
        
        return tmpImIc ;
    }
    /**
     * Μέθοδος που με βάση όλες τις απαραίτητες πληροφορίες του {@link Device}
     * κτίζεται και όλο το δέντρο πάνω στο αρχικό "φύλλο" κορυφής που παίρνει
     * σαν παράμετρο . Δηλαδή πρακτικά κρεμιούνται όλοι οι ελεγκτές μαζί με τις
     * μονάδες τους και τα υπόλοιπα βασικά στοιχεία της κάθε μονάδας(Value,Mode).
     * @param top Το αρχικό "φύλλο" κορυφής στο οποίο θα κρεμαστούν όλες οι
     * υπόλοιπες βασικές πληροφορίες της συσκευής με μορφή δέντρου .
     */
    private void createNodes(CustomTreeNode top) {
        CustomTreeNode nodController,typeNode ;
        String keyCon ;
        Iterator<String> keyIterUnit ;
        Iterator<String> keyIterCon = allUnits.keySet().iterator();
        int conCount = 0;
        int unitCount  ;
        while(keyIterCon.hasNext())
        {
            keyCon = keyIterCon.next() ;
            nodController = new CustomTreeNode("["+conCount+"]"+keyCon) ;
            nodController.setName(keyCon);
            nodController.setID("["+conCount+"]");
            top.add(nodController);
            unitCount = 0 ;
            for (Integer i = 0 ;i<sumOfDifferentsTypes;i++)
            {
                keyIterUnit = allUnits.get(keyCon).keySet().iterator() ;
                typeNode = createSomeCateroryNodes(i.toString(),keyCon,keyIterUnit,conCount,unitCount) ;
                if (typeNode!=null)
                {
                    unitCount+=typeNode.getChildCount() ;
                    nodController.add(typeNode);
                }
            }
            conCount++ ;
        }
    }
    /**
     * Μέθοδος που μέσω αυτής μπορούμε να έχουμε τις μονάδες που έχουν ίδιο τύπο
     * σε ένα ελεγκτή όλες μαζί σαν ομάδα κάτω από ένα "φύλλο" "τύπου μονάδας" .
     * Επίσης μέσα από αυτή τη μέθοδο παίρνουν οι μονάδες το αναγνωριστικό ID
     * συστήματος που τους δίνει ο User Client .
     * @param type Λεκτικό για τον τύπο (Πρακτικά είναι ένας αριθμός)
     * @param keyCon Λεκτικό του ελεγκτή
     * @param keyIterUnit Ένας iterator για το
     * @param conCount Αριθμός σειράς του ελεγκτή μέσα στη συσκευή.
     * @param unitCount Αριθμός σειράς της μονάδας μέσα στον ελεγκτή
     * @return Ένα {@link CustomTreeNode} "τύπου μονάδας" στο οποίο από "κάτω"
     * θα βρίσκονται ομαδοποιημένα όλες οι μονάδες του ίδιου τύπου στον ίδιο ελεγκτή.
     */
    private CustomTreeNode createSomeCateroryNodes(String type,String keyCon,Iterator<String> keyIterUnit,int conCount,int unitCount)
    {
        CustomTreeNode typeNode = new CustomTreeNode() ;
        CustomTreeNode nodUnit ;
        String sysID ;
        String keyUnit  ;
        int count = 0 ;
        while(keyIterUnit.hasNext())
        {
            keyUnit = keyIterUnit.next() ;
            if (allUnits.get(keyCon).get(keyUnit).getType().equals(type))
            {
                sysID = conCount+"/"+unitCount ;
                nodUnit = new  CustomTreeNode("["+sysID+"]"+keyUnit) ;
                nodUnit.setName(keyUnit);
                unitCount++ ;
                allUnits.get(keyCon).get(keyUnit).setFullID(sysID);
                nodUnit.add(allUnits.get(keyCon).get(keyUnit).getNodValue());
                nodUnit.add(allUnits.get(keyCon).get(keyUnit).getNodMode());
                
                typeNode.add(nodUnit) ;
                String unType = allUnits.get(keyCon).get(keyUnit).getType() ;
                //System.out.println(unType);
                ImageIcon imIc = Globals.hmUnitImages.get("1"+unType.toString()) ;
                if (imIc==null)
                    imIc = Globals.hmUnitImages.get("0"+unType.toString()) ;
                allUnits.get(keyCon).get(keyUnit).setImIc(imIc) ;
                hmUnitNodes.put(nodUnit, allUnits.get(keyCon).get(keyUnit)) ;
                count++ ;
            }
        }
        typeNode.setUserObject(Globals.namesOfTypes[Integer.parseInt(type)] + "("+count+")" );
        if (typeNode.getChildCount()>0)
            return typeNode ;
        else
            return null ;
    }
    /**
     * Μέθοδος που όταν εκτελεστεί φέρνει όλα τα {@link UnitControl} μέσα στο
     * οπτικό πεδίο του χρήστη .
     * Μπορεί λόγου χάρη μετά από κάποιο resize του παραθύρου του UserClient
     * να μην "βλέπει" ο χρήστης κάποια εικονίδια των μονάδων , οπότε με
     * αυτή την μέθοδο εισέρχονται όλες οι μονάδες μέσα στο χώρο που βλέπει
     * ο χρήστης .
     */
    public void fixUnitControlToResize()
    {
        for (UnitControl uc : allControls)
            uc.fixToResize() ;
    }
    public void fillUnitControls(BackgroundPanel bp, Insets insets)
    {
        //saveSettings = true ;
        backPanel = bp ;
        for (UnitControl uc : allControls)
        {
            Dimension size = uc.getPreferredSize();
            uc.setBounds(insets.left,insets.top, size.width, size.height);
            uc.setBackPanel(bp);
        }
        this.readXML()  ;
    }
    /**
     * Απλοί μέθοδος που επιστρέφει μια λίστα με όλα τα {@link UnitControl}
     * της εκάστοτε συσκευής .
     * @return Επιστρέφει μια λίστα με όλα τα {@link UnitControl} αυτής της συσκευής
     */
    public ArrayList<UnitControl> getAllControls() {
        return allControls;
    }
    
    /**
     * Μέθοδος που με βάση μια λίστα από μηνύματα πρωτοκόλλου γίνεται η προετοιμασία
     * της συσκευής μας και ουσιαστικά γεμίζει με περιεχόμενο το Device μας .
     * @param newControllers Μια λίστα με συμβολοσειρές με μηνύματα πρωτοκόλλου
     * που ουσιαστικά προσθέτουν στο Device μας ελεγκτές μαζί με τις μονάδες τους .
     */
    public void prepareDevice(ArrayList<String> newControllers)
    {
        sumOfControllers = newControllers.size() ;
        allUnits.clear();
        
        String conName,unitName,unitType,unitValue ;
        String unitMode;
        String unitLimit;
        String unitMax ;
        String unitMin;
        String unitTag ;
        String sysID ;
        int conCount = 0 ;
        HashMap<String, Unit> hmUnits ;
        for (String mess : newControllers)
        {
            msm.reload(mess);
            conName = msm.getParameters().get(0).get(0) ;
            hmUnits = new HashMap<>() ;
            Tools.Debug.print(msm.getMessage()) ;
            for (int i = 1 ; i<msm.getParameters().size();i++)
            {
                unitName = msm.getParameters().get(i).get(0) ;
                unitType = msm.getParameters().get(i).get(1) ;
                unitMode = msm.getParameters().get(i).get(2) ;
                
                unitTag = msm.getParameters().get(i).get(3) ;
                if (msm.getParameters().get(i).size()>2)
                    unitValue = msm.getParameters().get(i).get(4) ;
                else
                    unitValue = "0" ;
                unitMax = msm.getParameters().get(i).get(5) ;
                unitMin = msm.getParameters().get(i).get(6) ;
                unitLimit = msm.getParameters().get(i).get(7) ;
                
                sysID =  conCount +"/" +(i-1) ;
                
                Unit newUnit = new Unit(this,conName,unitName,unitType,unitValue,unitMode,unitLimit,unitMax,unitMin,unitTag,sysID) ;
                
                hmUnits.put(unitName, newUnit) ;
            }
            allUnits.put(conName,hmUnits) ;
            conCount++ ;
        }
    }
    
    /**
     * Μέθοδος που απλά προσθέτει έναν νέο ελεγκτή στη συσκευή μας .
     * @param newController Συμβολοσειρά που έχει μορφή μηνύματος πρωτοκόλλου .
     * @return Μια συμβολοσειρά με το όνομα του Ελεγκτή .
     */
    public String addController(String newController)
    {
        //sumOfControllers = newControllers.size() ;
        //allUnits.clear();
        
        String conName,unitName,unitType,unitValue ;
        String unitMode;
        String unitLimit;
        String unitMax ;
        String unitMin;
        String unitTag ;
        String sysID ;
        int conCount = 0 ;
        HashMap<String, Unit> hmUnits ;
        
        msm.reload(newController);
        conName = msm.getParameters().get(0).get(0) ;
        hmUnits = new HashMap<>() ;
        Tools.Debug.print(msm.getMessage()) ;
        for (int i = 1 ; i<msm.getParameters().size();i++)
        {
            unitName = msm.getParameters().get(i).get(0) ;
            unitType = msm.getParameters().get(i).get(1) ;
            unitMode = msm.getParameters().get(i).get(2) ;
            
            unitTag = msm.getParameters().get(i).get(3) ;
            if (msm.getParameters().get(i).size()>2)
                unitValue = msm.getParameters().get(i).get(4) ;
            else
                unitValue = "0" ;
            unitMax = msm.getParameters().get(i).get(5) ;
            unitMin = msm.getParameters().get(i).get(6) ;
            unitLimit = msm.getParameters().get(i).get(7) ;
            
            sysID =  conCount +"/" +(i-1) ;
            
            Unit newUnit = new Unit(this,conName,unitName,unitType,unitValue,unitMode,unitLimit,unitMax,unitMin,unitTag,sysID) ;
            
            hmUnits.put(unitName, newUnit) ;
        }
        allUnits.put(conName,hmUnits) ;
        conCount++ ;
        
        
        JTree tree = this.getTree(true) ;
        myPTab.scpDevTree.getViewport().removeAll();
        myPTab.scpDevTree.getViewport().add(tree) ;
        expandAll(tree) ;
        Tools.Debug.print("Connect SuccessFully!");
        BackgroundPanel bp = new BackgroundPanel(this) ;
        bp.setLayout(null);
        myPTab.scpScreen.getViewport().removeAll();
        myPTab.scpScreen.getViewport().add(bp) ;
        bp.setVisible(true);
        
        Insets insets = bp.getInsets();
        
        for (UnitControl uc : this.getAllControls())
        {
            uc.setVisible(true);
            bp.add(uc) ;
        }
        this.fillUnitControls(bp,insets) ;
        this.setPTab(myPTab) ;
        this.clearSelection();
        //  myPTab.stopMonitoring(true);
        // myPTab.startMonitoring();
        return conName ;
    }
    
    /**
     * Μέθοδος που απλά διαγράφει έναν ελεγκτή από τη συσκευή μας .
     * @param msm Συμβολοσειρά που έχει μορφή μηνύματος πρωτοκόλλου .
     */
    public void deleteController(ManageSocketMessage msm)
    {
        if (msm!=null)
            if (msm.getCommand().equals("DeleteController"))
            {
                if (msm.getParameters().size()>0)
                {
                    String conName = msm.getParameters().get(0).get(0) ;
                    
                    Iterator<String> keyIterUnit = allUnits.get(conName).keySet().iterator();
                    String keyUnit;
                    while(keyIterUnit.hasNext())
                    {
                        keyUnit = keyIterUnit.next() ;
                        allControls.remove(allUnits.get(conName).get(keyUnit).getUc()) ;
                    }
                    allUnits.get(conName).clear();
                    allUnits.remove(conName) ;
                }
                
                JTree tree = this.getTree(true) ;
                myPTab.scpDevTree.getViewport().removeAll();
                myPTab.scpDevTree.getViewport().add(tree) ;
                expandAll(tree) ;
                Tools.Debug.print("Connect SuccessFully!");
                BackgroundPanel bp = new BackgroundPanel(this) ;
                bp.setLayout(null);
                myPTab.scpScreen.getViewport().removeAll();
                myPTab.scpScreen.getViewport().add(bp) ;
                bp.setVisible(true);
                
                Insets insets = bp.getInsets();
                
                for (UnitControl uc : this.getAllControls())
                {
                    uc.setVisible(true);
                    bp.add(uc) ;
                }
                this.fillUnitControls(bp,insets) ;
                this.setPTab(myPTab) ;
                this.clearSelection();
            }
        
    }
    /**
     * Μέθοδος που με βάση ένα μήνυμα πρωτοκόλλου (που μεταφέρετε μέσα από socket δηλαδή)
     * αλλάζει στοχευμένα το value σε ένα ή περισσότερα Unit της συσκευής .
     * @param valuesOfMessage Συμβολοσειρά που έχει μορφή μηνύματος πρωτοκόλλου .
     */
    public void setValuesOfDevice(String valuesOfMessage)
    {
        String conName,unitName,unitValue ;
        
        msm.reload(valuesOfMessage);
        for (int i = 0 ; i<msm.getParameters().size();i++)
        {
            conName = msm.getParameters().get(i).get(0) ;
            unitName = msm.getParameters().get(i).get(1) ;
            unitValue = msm.getParameters().get(i).get(2) ;
            
            allUnits.get(conName).get(unitName).setValue(unitValue);
        }
    }
    /**
     * Μέθοδος που με βάση ένα μήνυμα  πρωτοκόλλου (που μεταφέρετε μέσα από socket δηλαδή)
     * αλλάζει στοχευμένα το Mode σε ένα ή περισσότερα Unit της συσκευής .
     * @param modesOfMessage Συμβολοσειρά που έχει μορφή μηνύματος  πρωτοκόλλου .
     */
    public void setModesOfDevice(String modesOfMessage)
    {
        String conName,unitName,modeValue ;
        
        msm.reload(modesOfMessage);
        for (int i = 0 ; i<msm.getParameters().size();i++)
        {
            conName = msm.getParameters().get(i).get(0) ;
            unitName = msm.getParameters().get(i).get(1) ;
            modeValue = msm.getParameters().get(i).get(2) ;
            
            allUnits.get(conName).get(unitName).setMode(modeValue);
        }
    }
    
    /**
     * Μέθοδος που αναλαμβάνει να αποθηκεύσει όλες τις απαραίτητες πληροφορίες
     * ρυθμίσεων για την εκάστοτε συσκευή .
     * Σκοπός της είναι να μπορεί κάποια άλλη χρονική στιγμή ο User Client να
     * ανακτήσει όλες αυτές τις πληροφορίες και πιο συγκεκριμένα οι μονάδες
     * και οι ελεγκτές να πάρουν τα description που έχει καταχωρήσει ο χρήστης.
     * Επίσης να μπορούν να ανακτηθούν οι θέσεις πάνω στην οθόνη που βρίσκονται τώρα
     * οι μονάδες όπως επίσης και το να είναι "κλειδωμένες" ή όχι ανάλογα με την
     * τώρα κατάσταση ..
     * Ακόμη θα μπορεί να ανακτηθεί το path της εικόνας και κατά επέκταση η ίδια η εικόνα για το
     * background της συσκευής , αν έχει αλλάξει ο χρήστης την default .
     * @throws java.io.FileNotFoundException Εξαίρεση που μπορεί να υπάρψει αν για κάποιο
     * λόγο δεν μπορέσει να εγγραφεί το αρχείο στον δίσκο .
     */
    public void saveToXML() throws FileNotFoundException {
        if (!saveSettings)
        {
            File f = new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Settings"+Globals.fSep+deviceName + "_" + Globals.username+".xml") ;
            f.delete();
            //System.out.println(f.toPath().toAbsolutePath());
            return ;
        }
        
        Document dom;
        Element controllers,unit,attribute,BackImPa,descriptionCon,descriptionsOfControllers,ConSystemID;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            dom = db.newDocument();
            
            Element rootEle = dom.createElement(deviceName);
            
            BackImPa = dom.createElement("Settings");
            unit = dom.createElement("BackgroundImagePath");
            unit.appendChild(dom.createTextNode(getPathFile()));
            BackImPa.appendChild(unit);
            //System.out.println(getPathFile());
            rootEle.appendChild(BackImPa);
            ///////////////////////////////////
            descriptionsOfControllers = dom.createElement("descriptionsOfControllers");
            for (int i = 0 ; i<tree.getRowCount();i++)
            {
                TreePath tpath = tree.getPathForRow(i) ;
                CustomTreeNode ctn = (CustomTreeNode) tpath.getLastPathComponent() ;
                if (ctn.getLevel() == 1)
                {
                    if (!ctn.getUserObject().toString().equals(ctn.getName()))
                    {
                        ConSystemID  = dom.createElement("ConSystemID");
                        ConSystemID.appendChild(dom.createTextNode(ctn.getName()));
                        descriptionsOfControllers.appendChild(ConSystemID);
                        descriptionCon  = dom.createElement("ConDescription");
                        String desc = Tools.getOnlyDescriptionWithoutSysID(ctn.getUserObject().toString());
                        descriptionCon.appendChild(dom.createTextNode((desc.equals(ctn.getName())?"":desc)));
                        descriptionsOfControllers.appendChild(descriptionCon);
                    }
                }
            }
            rootEle.appendChild(descriptionsOfControllers);
            
            controllers = dom.createElement("Controllers");
            
            for (UnitControl uc : allControls)
            {
                unit = dom.createElement("Unit");
                attribute = dom.createElement("ID");
                attribute.appendChild(dom.createTextNode(uc.getUnit().fullID));
                unit.appendChild(attribute);
                
                attribute = dom.createElement("Description");
                attribute.appendChild(dom.createTextNode((uc.getUnit().getDescription().equals(uc.getUnit().getName())?"":uc.getUnit().getDescription())));
                unit.appendChild(attribute);
                
                attribute = dom.createElement("Locked");
                attribute.appendChild(dom.createTextNode(String.valueOf(uc.locked)));
                unit.appendChild(attribute);
                
                attribute = dom.createElement("LocationX");
                attribute.appendChild(dom.createTextNode(String.valueOf(uc.getLocation().x)));
                unit.appendChild(attribute);
                
                attribute = dom.createElement("LocationY");
                attribute.appendChild(dom.createTextNode(String.valueOf(uc.getLocation().y)));
                unit.appendChild(attribute);
                
                controllers.appendChild(unit);
            }
            rootEle.appendChild(controllers);
            
            dom.appendChild(rootEle);
            
            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Settings"+ Globals.fSep +deviceName + "_" + Globals.username+".xml")));
            } catch (TransformerException te) {
                Tools.Debug.print(te.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            Tools.Debug.print("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }
    
    /**
     * Μέθοδος που αναλαμβάνει να διαβάσει όλες τις απαραίτητες πληροφορίες
     * από ένα αρχείο xml (Της κατάλληλης μορφής βέβαια) για την συγκεκριμένη συσκευή
     * εφόσον βέβαια υπάρχει κάποιο αρχείο "ρυθμίσεων" στον δίσκο από παλαιότερη χρήση
     * του User Client και της ίδιας συσκευής.
     * Σκοπός της είναι να αξιοποιηθούν όλα αυτά τα δεδομένα , δηλαδή οι μονάδες
     * και οι ελεγκτές να πάρουν τα description που είχε καταχωρήσει ο χρήστης όταν
     * έκλεισε για τελευταία φορά το User Client .
     * Επίσης ανακτούνται οι θέσεις πάνω στην οθόνη που βρισκόντουσαν οι μονάδες
     * όπως επίσης και το αν ήταν "κλειδωμένες" ή όχι .
     * Ακόμη ανακτάται το path της εικόνας και κατά επέκταση η ίδια η εικόνα για το
     * background της συσκευής , αν είχε αλλάξει ο χρήστης την default .
     * @return Επιστρέφει True αν όλα πήγαν καλά και False αν κάτι δεν πήγα .
     */
    public boolean readXML()  {
        String lock ,x,y ,desc;
        Document dom;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            dom = db.parse(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Settings"+Globals.fSep+deviceName + "_" + Globals.username+".xml");
            
            Element doc = dom.getDocumentElement();
            setPathFile(Globals.getTextValue(doc, "BackgroundImagePath", 0)) ;
            
            int conCounter = 0 ;
            for (int i = 0 ; i<tree.getRowCount();i++)
            {
                TreePath tpath = tree.getPathForRow(i) ;
                CustomTreeNode ctn = (CustomTreeNode) tpath.getLastPathComponent() ;
                if (ctn.getLevel() == 1)
                {
                    if (ctn.getName() == null ? Globals.getTextValue(doc, "ConSystemID", conCounter) == null : ctn.getName().equals(Globals.getTextValue(doc, "ConSystemID", conCounter)))
                    {
                        desc = Globals.getTextValue(doc, "ConDescription", conCounter);
                        if (desc!=null && !desc.equals("") && !desc.equals(ctn.getName()))
                            ctn.setUserObject(ctn.getID() + desc);
                        ((DefaultTreeModel)tree.getModel()).reload(ctn);
                        ManageUserClient.expandAll(tree,Tools.getPath(ctn));
                        conCounter ++ ;
                    }
                }
            }
            
            int i = 0 ;
            for (UnitControl uc : allControls)
            {
                desc = Globals.getTextValue( doc, "Description",i);
                x = Globals.getTextValue( doc, "LocationX",i);
                y = Globals.getTextValue( doc, "LocationY",i);
                lock = Globals.getTextValue( doc, "Locked",i++);
                
                if (desc!=null && !desc.equals("") && !desc.equals(uc.getUnit().getName()))
                    uc.getUnit().setDescription(desc,true);
                if (lock!=null)
                    uc.setLocked(Boolean.parseBoolean(lock));
                if ((x!=null)&& (y!=null))
                    uc.setLocation(Integer.parseInt(x),Integer.parseInt(y));
                
                
            }
            return true;
            
        } catch (ParserConfigurationException | SAXException pce) {
            Tools.Debug.print(pce.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        
        return false;
    }
    
    /**
     * Κλάση που έχει όλη την πληροφορία για μια μονάδα μας .
     */
    public class Unit
    {
        private CustomTreeNode nodValue,nodMode ;
        private String name ;
        private String myType ;
        private String value ;
        private String fullID ;
        private String mode ;
        public String limit ;
        public String tag ;
        public double max,min ;
        private String controller ;
        private UnitControl uc ;
        public ImageIcon imIc ;
        public Device device ;
        private String description  = "";
        
        /**
         * Κατασκευαστής που αρχικοποιεί πλήρως την μονάδα .
         * @param d Device στο οποίο ανήκει η μονάδα .
         * @param c Ελεγκτής στον οποίο ανήκει η μονάδα
         * @param n Όνομα της μονάδας
         * @param t Τύπος της μονάδας
         * @param v Τιμή της μονάδας
         * @param mo "Τρόπος λειτουργίας" της μονάδας
         * @param l Το όριο που έχει η μονάδα
         * @param ma Την μέγιστη τιμή που μπορεί να πάρει η μονάδα
         * @param mi Την μικρότερη τιμή που μπορεί να πάρει η μονάδα
         * @param ta Μια ετικέτα που μπορεί να έχει η μονάδα (Για μελλοντική χρήση)
         * @param fID  Το πλήρες ID που έχει η μονάδα .
         */
        public Unit(Device d,String c,String n,String t,String v,String mo,String l,String ma,String mi,String ta,String fID) {
            device = d ;
            name = n ;
            myType = t ;
            value = v ;
            controller = c ;
            nodValue = new CustomTreeNode("Value : " + v) ;
            mode =mo;
            switch(mode)
            {
                case "2":
                    nodMode = new CustomTreeNode("Mode : Both") ;
                    break ;
                case "0" :
                case "3" :
                    nodMode = new CustomTreeNode("Mode : Auto") ;
                    break ;
                case "1" :
                case "4" :
                    nodMode = new CustomTreeNode("Mode : Remote") ;
                    break ;
                default :
                    nodMode = new CustomTreeNode("Mode : Both") ;
            }
            
            fullID = fID ;//ELT.getOnlyDBID(c) +"/" +Tools.getOnlyDBID(n) ;
            
            limit = l ;
            tag =ta;
            max= (Tools.isNumeric(ma))?Double.parseDouble(ma):255 ;
            min =(Tools.isNumeric(mi))?Double.parseDouble(mi):0 ;
            uc = new UnitControl(this) ;
            setDescription(name) ;
            allControls.add(uc) ;
        }
        
        /**
         * Μέθοδος που αλλάζει το description της κλάσης (Μονάδας) όπως επίσης
         * και το description του αντίστοιχου {@link UnitControl} της ίδιας μονάδας.
         * @param description Το λεκτικό για το νέο description.
         */
        public void setDescription(String description) {
            setDescription(description,false);
        }
        public void setDescription(String description,boolean andNode) {
            this.description = description;
            uc.setDescription(description);
            if (andNode)
            {
                ((CustomTreeNode)nodValue.getParent()).setUserObject("["+fullID+"]"+description);
                treeModel.reload((CustomTreeNode)nodValue.getParent());
            }
        }
        /**
         * Ενημέρωση κάποιων βασικών στοιχείων της κλάσης , δηλαδή του name,type και
         * value της μονάδας .
         * @param n Το λεκτικό του ονόματος
         * @param t Το λεκτικό του τύπου (πρακτικά αν και συμβολοσειρά πρέπει να είναι αριθμός)
         * @param v Το λεκτικό της τιμής (πρακτικά αν και συμβολοσειρά πρέπει να είναι αριθμός)
         */
        public void setUnit(String n,String t,String v)
        {
            if (name!=null)
            {
                name = n ;
                uc.setName(value) ;
            }
            if (myType!=null)
            {
                myType = t ;
                uc.setType(value) ;
            }
            
            if (value!=null)
            {
                value = v.trim().toString() ;
                if (nodValue!=null)
                {
                    nodValue.setUserObject("Value : " + value);
                    uc.setValue(value) ;
                    treeModel.reload(nodValue);
                }
            }
        }
        
        /**
         * Καταχωρείτε το νέο mode τόσο στο αντικείμενο της κλάσης {@link Unit} όσο
         * και στο αντίστοιχο "φύλλο" του δέντρου της συσκευής
         * αλλά και στο ανάλογο {@link UnitControl} της ίδιας μονάδας .
         * @param mode Το νέο mode που θα καταχωρηθεί .
         */
        public void setMode(String mode) {
            this.mode = mode.trim();
            
            uc.setMode((Tools.isNumeric(this.mode)?Integer.parseInt(this.mode):0)) ;
            switch(mode)
            {
                case "2":
                    nodMode.setUserObject("Mode : Both");
                    break ;
                case "0" :
                case "3" :
                    nodMode.setUserObject("Mode : Auto");
                    break ;
                case "1" :
                case "4" :
                    nodMode.setUserObject("Mode : Remote");
                    break ;
                default :
                    nodMode.setUserObject("Mode : Both");
            }
            treeModel.reload(nodMode);
        }
        /**
         * Καταχωρείτε μια νέα τιμή τόσο στο αντικείμενο της κλάσης {@link Unit} όσο
         * και στο αντίστοιχο "φύλλο" του δέντρου της συσκευής
         * αλλά και στο ανάλογο {@link UnitControl} της ίδιας μονάδας .
         * @param value Η νέα τιμή που θα καταχωρηθεί .
         */
        public void setValue(String value) {
            this.value = value.trim();
            if (nodValue!=null)
            {
                nodValue.setUserObject("Value : " + value.trim());
                uc.setValue(value.trim()) ;
                treeModel.reload(nodValue);
            }
        }
        /**
         * Επιλέγετε (Selected) το αντίστοιχο "φύλλο" της μονάδας που βρίσκεται στο δέντρο .
         */
        public void setSelectedNode()
        {
            TreePath tp = Tools.getPath(nodValue.getParent()) ;
            tree.setSelectionPath(tp) ;
            tree.scrollPathToVisible(tp);
        }
        /**
         * Καταχωρείτε το πλήρες ID μιας μονάδας στην μεταβλητή του αντικειμένου
         * αλλά και στο αντίστοιχο αντικείμενο του {@link UnitControl} της ίδιας μονάδας.
         * @param fullID Το πλήρες Id μιας μονάδας .
         */
        public void setFullID(String fullID) {
            this.fullID = fullID;
            uc.setID(fullID) ;
        }
        
        //Απλοί set μεθόδοι : - Αρχή:
        public void setImIc(ImageIcon imIc) {
            this.imIc = imIc;
        }
        //Απλοί set μεθόδοι : - Τέλος:
        
        //Απλοί get μεθόδοι : - Αρχή:
        
        public UnitControl getUc() {
            return uc;
        }
        
        public String getDescription() {
            return description;
        }
        
        public ImageIcon getImIc() {
            return imIc;
        }
        
        public CustomTreeNode getNodMode() {
            return nodMode;
        }
        
        public CustomTreeNode getNodValue() {
            return nodValue;
        }
        
        public String getFullID() {
            return fullID;
        }
        public String getMode() {
            return mode;
        }
        public String getType() {
            return myType;
        }
        
        public String getName() {
            return name;
        }
        public String getFullValue()
        {
            return "("+controller+"|"+name+"|"+value+")" ;
        }
        public String getFullMode()
        {
            return "("+controller+"|"+name+"|"+mode+")" ;
        }
        public String getValue() {
            return value;
        }
        public String getUnit()
        {
            return "("+name+"|"+myType+")" ;
        }
        
        //Απλοί get μεθόδοι : - Τέλος:
        
        @Override
        public String toString() {
            return "("+name+"|"+myType+"|"+value+")" ;
        }
        
    }
    
    /**
     * Δικό μας custom TreeNode που έχει κληρονομήσει το DefaultMutableTreeNode.
     * Ουσιαστικά φτιάχτηκε για να μπορούμε μέσα σε ένα "φύλλο" του δέντρου να
     * έχουμε εκτός από το λεκτικό του , που έχει σαν τίτλο, και κάποιο Name.
     * Και όλο αυτό για να μπορούμε σε επόμενη φάση , να αλλάζει ο εκάστοτε χρήστης
     * το λεκτικό - description του "φύλλου" αλλά όμως να συνεχίζουμε να ξέρουμε το αρχικό Name που είχε .
     */
    class CustomTreeNode extends DefaultMutableTreeNode
    {
        String name;
        String ID ;
        /**
         * Κατασκευαστής που αρχικοποιεί κατάλληλα το "φύλλο" μας όπως επίσης
         * δίνει αρχική τιμή στο name μας με βάση αυτό που έχει στην παράμετρο.
         * @param name Το λεκτικό που θα έχει το "φύλλο" αλλά και η μεταβλητή name μας.
         */
        public CustomTreeNode(String name) {
            super(name) ;
            this.name = name ;
        }
        /**
         * Default κατασκευαστής
         */
        public CustomTreeNode() {
            super() ;
        }
        
        //Απλοί set μεθόδοι : - Αρχή:
        public void setID(String ID) {
            this.ID = ID;
        }
        public void setName(String name) {
            this.name = name;
        }
        //Απλοί set μεθόδοι : - Τέλος:
        
        //Απλοί get μεθόδοι : - Αρχή:
        public String getID() {
            return ID;
        }
        public String getName() {
            return name;
        }
        //Απλοί get μεθόδοι : - Τέλος:
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