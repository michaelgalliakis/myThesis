package UserClientV1;

import java.awt.Point;
import java.awt.event.KeyEvent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

/**
 * Κλάση η οποία ουσιαστικά είναι το παραθυράκι (JDialog) που εμφανίζεται
 * για να δοθεί η δυνατότητα στο χρήστη να αλλάξει το Description(περιγραφή)
 * κάποιας μονάδας ή ελεγκτή .
 * Εμφανίζεται όταν κάνει ο χρήστης "δεξί κλικ" πάνω σε κάποια μονάδα ή ελεγκτή
 * του δέντρου(JTable) της συσκευής.
 * @author Michael Galliakis
 */
public class DfChangeText extends javax.swing.JDialog {
    public Device.CustomTreeNode treeNode ;
    public JTree tree ;
    public String deviceName  ;
    String sysID ;
    /**
     * Κατασκευαστής που κάνει όλα τα απαραίτητα βήματα για να αρχικοποιηθεί
     * κατάλληλα το dialog μας .
     * @param tree Το δέντρο της συγκεκριμένης συσκευής από το οποίο έχει ανοίξει το dialog μας .
     * @param treeNode Το συγκεκριμένο "φύλλο" για το οποίο αφορά η αλλαγή του description.
     * @param pPoint Το σημείο πάνω στην οθόνη που βρίσκεται το συγκεκριμένο "φύλλο" .
     */
    public DfChangeText(JTree tree ,Device.CustomTreeNode treeNode,Point pPoint) {
        super();
        initComponents();
        this.treeNode = treeNode ;
        
        this.tree = tree ;
        lSystemID.setText(treeNode.getName());
        String desc = (String)treeNode.getUserObject() ;
        String onlyDescWithoutSysID = Tools.getOnlyDescriptionWithoutSysID(desc) ;
        tfDescription.setText(onlyDescWithoutSysID) ;
        
        sysID = desc.replace(onlyDescWithoutSysID, "") ;
        
        setLocation(pPoint);
        
        deviceName = tree.getName() ;
        tfDescription.setFocusable(true);
        tfDescription.requestFocusInWindow() ;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfDescription = new javax.swing.JTextField();
        lSystemID = new javax.swing.JLabel();
        bOK = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 255));
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 0, 51));
        jLabel1.setText("System ID : ");

        jLabel2.setForeground(new java.awt.Color(0, 153, 102));
        jLabel2.setText("Description :");
        jLabel2.setToolTipText("");

        tfDescription.setToolTipText("");
        tfDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfDescriptionKeyPressed(evt);
            }
        });

        lSystemID.setText("Con 1");
        lSystemID.setToolTipText("");

        bOK.setText("OK");
        bOK.setToolTipText("");
        bOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bOK.setMaximumSize(new java.awt.Dimension(50, 30));
        bOK.setMinimumSize(new java.awt.Dimension(50, 30));
        bOK.setPreferredSize(new java.awt.Dimension(50, 30));
        bOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bOKMouseClicked(evt);
            }
        });

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator2)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(lSystemID)))
                        .addGap(135, 135, 135)
                        .addComponent(bOK, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                    .addComponent(tfDescription)
                    .addComponent(jSeparator1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(366, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(lSystemID))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2))
                            .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(21, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Μέθοδος που κάνει όλους του απαραίτητους ελέγχους και ενέργειες ούτως
     * ώστε να αλλάξει επιτυχημένα το description κάποια μονάδας ή ελεγκτή από το
     * δέντρο της ανάλογης καρτέλας συσκευής ,όπως ακόμη και αν είναι μονάδα ,από το
     * εικονίδιο της αντίστοιχης μονάδας .
     */
    private void changeDescription()
    {
        Device dev = Globals.hmDevices.get(deviceName) ;
        //System.out.println(dev.toString()) ;
        
        if (treeNode.getLevel() == 3)
        {
            Device.Unit uc = dev.hmUnitNodes.get(treeNode) ;
            
            if (!tfDescription.getText().equals(""))
            {
                uc.setDescription(tfDescription.getText());
                treeNode.setUserObject(sysID+tfDescription.getText());
            }
            else
            {
                uc.setDescription(treeNode.getName());
                treeNode.setUserObject(sysID+treeNode.getName());
            }
            ((DefaultTreeModel)tree.getModel()).reload(treeNode);
        }
        else if (treeNode.getLevel() == 1)
        {
            if (!tfDescription.getText().equals(""))
                treeNode.setUserObject(sysID+tfDescription.getText());
            else
                treeNode.setUserObject(sysID+treeNode.getName());
            
            ((DefaultTreeModel)tree.getModel()).reload(treeNode);
            ManageUserClient.expandAll(tree,Tools.getPath(treeNode));
        }
    }
    /**
     * Μέθοδος που εκτελείτε όταν πατήσει ο χρήστης το κουμπί "OK" .
     * Ο σκοπός του είναι να αλλάζει το description και να κλείνει στην συνέχεια το dialog.
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void bOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bOKMouseClicked
        changeDescription();
        this.dispose();  
    }//GEN-LAST:event_bOKMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν το παραθυράκι μας γίνει ανενεργό και πρακτικά
     * όταν πατήσει ο χρήστης κάπου έξω από το dialog .
     * Ο σκοπός του είναι να κλείνει αυτόματα το dialog χωρίς να γίνεται κάποια αλλαγή.
     * @param evt Το Default WindowEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        this.dispose();  
    }//GEN-LAST:event_formWindowDeactivated
    
    /**
     * Μέθοδος που εκτελείτε όποτε πατήσει οποιοδήποτε κουμπί μέσα στο JTextField
     * του description ο χρήστης .
     * Ο σκοπός του είναι να αλλάζει το description και να κλείνει στην συνέχεια το dialog
     * αν ο χρήστης πατήσει το "enter" ή να κλείσει το dialog αν ο χρήστης πατήσει το "esqape" .
     * @param evt Το Default KeyEvent αντικείμενο που το αξιοποιούμε με το να βρούμε
     * μέσω αυτού πιο ακριβώς πλήκτρο πάτησε ο χρήστης .
     */
    private void tfDescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfDescriptionKeyPressed
        if (evt.getKeyCode() ==  KeyEvent.VK_ENTER)
        {
            changeDescription();
            this.dispose();
        }
        else if (evt.getKeyCode() ==  KeyEvent.VK_ESCAPE)
            this.dispose();
    }//GEN-LAST:event_tfDescriptionKeyPressed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lSystemID;
    private javax.swing.JTextField tfDescription;
    // End of variables declaration//GEN-END:variables
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