package UserClientV1;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
/**
 * Κλάση η οποία έχει κληρονομήσει ένα JDialog
 * Και ουσιαστικά είναι το παραθυράκι που μέσω αυτού μπορεί να δει και να ανοίξει
 * για εποπτεία κάποιος χρήστης τις συσκευές στις οποίες έχει δικαιώματα .
 * @author Michael Galliakis
 */
public class Ddevices extends JDialog {
    private static final String[] ColumnNames = {"ID","Devices","Access","Enable"} ;
    private JScrollPane sp ;
    private JFrame frame ;
    ArrayList<ArrayList<String>> lastAlWithDevices ;
    String[] lastArrayWithDevEnable;
    
    /**
     * Κατασκευαστής της κλάσης που την αρχικοποιεί .
     * @param frame Η φόρμα που "ανήκει" το νέο panel της κλάσης μας
     * @param str Ένα λεκτικό που θα έχει για τίτλο το panel μας .
     */
    public Ddevices(JFrame frame, String str)  {
        super(frame, str);
        this.frame = frame ;
        /**
         * Προσθέτουμε προγραμματιστικά άμεσα κάποιο Listener για το παράθυρο
         */
        addWindowListener(new WindowAdapter() {
            /**
             * Η μέθοδος εκτελείτε σε περίπτωση που κλείσει το panel μας .
             * @param evt Το Default WindowEvent που έχει το συγκεκριμένο event το οποίο στη
             * περίπτωση μας δεν χρησιμοποιείται.
             */
            @Override
            public void windowClosing(WindowEvent evt) {
                //System.exit(0);
                Globals.objDevices.setVisible(false);
                Globals.objMainFrame.bDevices.setBackground(Color.GREEN);
            }
        });
        
        this.setIconImage(Globals.biLogo);
        sp = new JScrollPane();
        sp.setName("scpDevices");
        this.add(sp) ;
        this.setMinimumSize(new Dimension(200,200));
    }
    
    /**
     * Μέθοδος που ουσιαστικά κάνει όλα τα απαραίτητα βήματα για να γεμίσει
     * ο πίνακας του dialog μας με όλα τα devices που μπορεί να εποπτεύσει
     * ο χρήστης .
     * @param alWithDevices Είναι μια λίστα από λίστες που έχουν λεκτικά και που πρακτικά εμπεριέχουν
     * όλες τις συσκευές που μπορεί να εποπτεύσει ο χρήστης μαζί με τα δικαιώματα προσπέλασης,
     * αν είναι online ή όχι και το ID της βάσης για την κάθε συσκευή .
     */
    public void setDevicesInfo(ArrayList<ArrayList<String>> alWithDevices)
    {
        lastAlWithDevices = (ArrayList<ArrayList<String>>) alWithDevices.clone() ;
        
        for (int i = 0 ; i < alWithDevices.size();i++)
        {
            if (alWithDevices.get(i).get(3).equals("U"))
            {
                int index = alWithDevices.indexOf(alWithDevices.get(i))+1;
                alWithDevices.add(0, alWithDevices.get(i));
                alWithDevices.remove(index);
            }
        }
        String[] arrayWithDevEnable = new String[alWithDevices.size()] ;
        if (lastArrayWithDevEnable==null)
            lastArrayWithDevEnable = new String[alWithDevices.size()] ;
        String[][] data = new String[alWithDevices.size()][4] ;
        
        for (int i = 0 ; i < alWithDevices.size();i++)
        {
            data[i][0] = alWithDevices.get(i).get(0) ;
            data[i][1] = alWithDevices.get(i).get(1) ;
            data[i][2] = alWithDevices.get(i).get(2) ;
            
            for (int j = 0; j <lastArrayWithDevEnable.length;j++ )
            {
                if (lastArrayWithDevEnable.length > i && lastArrayWithDevEnable[j]!=null && lastArrayWithDevEnable[j].equals("O|"+data[i][1]))
                {
                    arrayWithDevEnable[i] = "O|" + data[i][1]  ;
                    data[i][3] ="O" ;
                    break ;
                }
                else if (j == lastArrayWithDevEnable.length-1)
                {
                    //arrayWithDevEnable[i] = new String() ;
                    arrayWithDevEnable[i] = alWithDevices.get(i).get(3) + "|" + data[i][1] ;
                    data[i][3] = alWithDevices.get(i).get(3) ;
                }
            }
        }
        
        lastArrayWithDevEnable = arrayWithDevEnable.clone() ;
        MyTable table = new MyTable(data, ColumnNames) ;
        
        sp.getViewport().removeAll();
        sp.getViewport().add(table) ;
    }
    
    /**
     * Δικό μας custom TableModel που έχει κληρονομήσει το DefaultTableModel.
     * Ουσιαστικά φτιάχτηκε για να μην μπορεί ο χρήστης να επεξεργαστεί-αλλάξει
     * τα περιεχόμενα των κελιών του πίνακα .
     */
    private class MyDefaultTableModel extends DefaultTableModel
    {
        private MyDefaultTableModel(String[][] data, String[] ColumnNames) {
            super(data,ColumnNames) ;
        }
        public boolean isCellEditable(int row, int column)
        {
            return false;//This causes all cells to be not editable
        }
    }
    /**
     * Δικό μας custom Table που έχει κληρονομήσει το JTable αλλά και που περιέχει
     * όλες τις μεθόδους του interface MouseListener.
     * Πρακτικά έχει δημιουργηθεί για να είναι παραμετροποιημένος κατάλληλα
     * ούτως ώστε να μην εμφανίζονται όλες οι στήλες του πίνακα , αλλά και ακόμη
     * να έχει κάθε πεδίο του πίνακα χρώμα,γραμματοσειρά και "ύφος" ανάλογα την
     * κάθε περίπτωση .
     * Επίσης χρειάζεται για να του δώσουμε την δυνατότητα αν ο χρήστης πατήσει
     * διπλό κλικ σε κάποιο πεδίο μιας συσκευής να ανοίγει μια καρτέλα και να
     * ξεκινάει η εποπτεία για την συγκεκριμένη επιλογή του (Εφόσον η συσκευή βέβαια είναι online-Up).
     */
    private class MyTable extends JTable implements MouseListener
    {
        TableModel model ;
        String[][] data ;
        String[] columns ;
        /**
         * Κατασκευαστής που κάνει κάποιες ενέργειες προετοιμασίας της κλάσης .
         * @param data Όλα τα περιεχόμενα των συσκευών του χρήστη.
         * @param columns Όλα τα λεκτικά των στηλών του πίνακα .
         */
        MyTable(String[][] data,String[] columns)
        {
            super(data,columns) ;
            this.addMouseListener(this) ;
            this.data =data ;
            this.columns = columns ;
            
            model = new MyDefaultTableModel(data, columns) ;
            this.setModel(model);
            /**
             * Πρακτικά οι παρακάτω 6 γραμμές "κρύβουν" για να μην φαίνονται
             * τις 3 στήλες με το ID,το αν είναι Up ή Down και τα δικαιώματα προσπέλασης.
             */
            this.getColumnModel().getColumn(0).setMinWidth(0);
            this.getColumnModel().getColumn(0).setMaxWidth(0);
            this.getColumnModel().getColumn(2).setMinWidth(0);
            this.getColumnModel().getColumn(2).setMaxWidth(0);
            this.getColumnModel().getColumn(3).setMinWidth(0);
            this.getColumnModel().getColumn(3).setMaxWidth(0);
            
            DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)this.getDefaultRenderer(Object.class);
            renderer.setHorizontalAlignment( JLabel.CENTER );
        }
        /**
         * Default Event που εκτελείτε πολλές φορές κατά την διάρκεια ροής του
         * προγράμματος και το οποίο διαμορφώνει κατάλληλα τα πεδία του πίνακα περνώντας
         * υπόψιν αν είναι η καρτέλα της κάθε συσκευής ανοικτή, τα δικαιώματα πάνω στην
         * συσκευή που έχει ο χρήστης και αν είναι up ή down η συσκευή στο Server.
         * @param renderer Ο default renderer που παίρνει κάθε φορά η μέθοδος
         * και που εμείς απλά τον χρησιμοποιούμε στην κλήση της prepareRenderer του Πατέρα(JTable)
         * @param row η σειρά του πίνακα κάθε φορά
         * @param col η στήλη του πίνακα κάθε φορά
         * @return Επιστρέφει ένα Component κατάλληλα διαμορφωμένο όπως ακριβώς το
         * θέλουμε για να είναι πιο ευδιάκριτες οι πληροφορίες του πίνακα στον χρήστη.
         */
        public java.awt.Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
            java.awt.Component comp = super.prepareRenderer(renderer, row, col);
            Object value = getModel().getValueAt(row, 3);
            if (value.equals("U")) {
                comp.setBackground(Color.green);
            }else if (value.equals("O"))
            {
                comp.setBackground(Color.ORANGE);
            }
            else
            {
                comp.setBackground(Color.white);
            }
            Font myFont = new Font("Courier", Font.BOLD ,12);
            Font myFont2 = new Font("Courier", Font.BOLD | Font.ITALIC ,12);
            comp.setForeground(Color.black);
            Map attributes = myFont.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            Object value2 = getModel().getValueAt(row, 2);
            if (!value2.equals("R")) {
                comp.setFont(myFont.deriveFont(attributes));
            }  else {
                comp.setFont(myFont2);
            }
            
            return comp;
        }
        /**
         * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει κάποιο πλήκτρο του ποντικιού
         * πάνω σε ένα κελί του πίνακα δύο συνεχόμενες φορές (διπλό κλικ) .
         * Ουσιαστικά αναλαμβάνει να κάνει όλα τα απαραίτητα βήματα για να ανοίξει
         * (με βάση το κελί) την κατάλληλη συσκευή για εποπτεία σε μια νέα καρτέλα.
         * Αν η συσκευή δεν είναι online (up) δεν κάνει τίποτα , όπως επίσης δεν
         * κάνει τίποτα αν η συσκευή είναι ήδη ανοικτή σε μια καρτέλα .
         * @param evt Ένα default MouseEvent αντικείμενο που το χρησιμοποιούμε
         * για να εξετάσουμε ότι έχουν πατηθεί 2 συνεχόμενα "κλικ" από το χρήστη
         * πάνω σε κάποιο κελί του πίνακα όπως επίσης και για να πάρουμε με βάση το
         * σημείο που βρίσκεται ο δείκτης του ποντικιού τη θέση της γραμμής και της στήλης του
         * πίνακα για να βρούμε σε επόμενη φάση σε ποια συσκευή απευθύνεται.
         */
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2 && !evt.isConsumed()) {
                evt.consume();
                int row = this.rowAtPoint(evt.getPoint());
                int col = this.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    TableModel model = this.getModel() ;
                    String enable = model.getValueAt(row, 3).toString() ;
                    if (enable.equals("U"))
                    {
                        model.getValueAt(row, 3) ;
                        
                        String devID = model.getValueAt(row, 0).toString() ;
                        String devName = model.getValueAt(row, 1).toString() ;
                        String devAccess = model.getValueAt(row, 2).toString() ;
                        //System.out.println("To access einai :" + devAccess);
                        
                        //System.out.println(devID);
                        Globals.objMainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        Globals.objMainFrame.newDeviceProcess(devID+"D",devName,devAccess) ;
                        model.setValueAt("O", row, 3);
                        lastArrayWithDevEnable[row] = "O|"+devName ;
                        this.clearSelection();
                        Globals.objMainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        }
        
        //Υπόλοιπα Events τα οποία πρέπει να γίνουν override , αλλά ουσιαστικά δεν κάνουν τίποτα.
        @Override
        public void mousePressed(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            ///throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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