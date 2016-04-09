package UserClientV1;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JSpinner;
/**
 * Κλάση η οποία ουσιαστικά είναι το παραθυράκι (JDialog) που εμφανίζεται
 * για να δοθεί η δυνατότητα στο χρήστη να αλλάξει την τιμή κάποιας μονάδας
 * με την βοήθεια μπάρας ή και ακόμη με το παραδοσιακό τρόπο της απλής
 * εισαγωγής με γραφή .
 * Εμφανίζεται όταν κάνει ο χρήστης "διπλό κλικ" πάνω σε κάποιο εικονίδιο μονάδας.
 * Βέβαια για να εμφανιστεί πρέπει ο χρήστης να έχει δικαίωμα να αλλάζει την τιμή
 * όπως επίσης και η μονάδα να είναι σε mode που να της επιτρέπετε να αλλάξει η
 * τιμή της και μάλιστα να είναι η μονάδα τύπου διακριτής τιμής με TR-trackbar(και όχι SW-Switch).
 * @author Michael Galliakis
 */
public class DfChangeValue extends javax.swing.JDialog {
    private UnitControl parentUC ;
    /**
     * Κατασκευαστής που κάνει όλα τα απαραίτητα βήματα για να αρχικοποιηθεί
     * κατάλληλα το dialog μας .
     * @param modal True αν θέλουμε να είναι συνέχεια onTop μέχρι να κλείσουμε το dialog μας
     * αλλιώς False αν θέλουμε ο χρήστης να μπορεί να κάνει τρέχον παράθυρο την φόρμα που ανήκει το dialog.
     * Πρακτικά δεν χρησιμοποιείται γιατί θέλουμε την προεπιλογή (default).
     * @param v Ένας Double αριθμός που παριστάνει την τρέχον τιμή της μονάδας .
     * @param uc Το {@link UnitControl} το οποίο αντιστοιχεί στην μονάδα .
     */
    public DfChangeValue(boolean modal,Double v,UnitControl uc) {
        //super(modal);
        initComponents();
        spValue.setValue((int) Math.round(v));
        sValue.setValue((int) Math.round(v));
        
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spValue.getEditor();
        
        parentUC = uc ;
        
        Point paOnScreen = parentUC.getLocationOnScreen() ;
        Dimension mainFSize = Globals.objMainFrame.getSize() ;
        Point mainFOnSc = Globals.objMainFrame.getLocationOnScreen();
        mainFOnSc.x += mainFSize.width ;
        if (paOnScreen.x + this.size().width>mainFOnSc.x)
            paOnScreen.x = mainFOnSc.x - this.size().width  ;
        else
            paOnScreen.x += parentUC.size().width-8 ;
        //System.out.println(paOnScreen.x);
        //System.out.println(mainFOnSc.x);
        
        setLocation(paOnScreen);
        /**
         * Προστέτουμε προγραμματιστικά άμεσα κάποιο Listener για τον spinner
         */
        editor.getTextField().addKeyListener(new KeyListener() {
            //Event το οποίο πρέπει να γίνει override , αλλά ουσιαστικά δεν κάνει τίποτα.
            @Override
            public void keyTyped(KeyEvent ke) {
                
            }
            /**
             * Μέθοδος που εκτελείτε όταν πατήσει ο χρήστης κάποιο κουμπί μέσα στον spinner(editor) του value.
             * @param ke Το Default KeyEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
             * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην keyPressedEvent.
             */
            @Override
            public void keyPressed(KeyEvent ke) {
                keyPressedEvent(ke);
                
            }
            //Event το οποίο πρέπει να γίνει override , αλλά ουσιαστικά δεν κάνει τίποτα.
            @Override
            public void keyReleased(KeyEvent ke) {
                
            }
        });
    }
    /**
     * Μέθοδος που κάνει όλους του απαραίτητους ελέγχους και ενέργειες ούτως
     * ώστε να αλλάξει επιτυχημένα την τιμή της ανάλογης μονάδας τόσο τοπικά
     * στο πρόγραμμα μας όσο και απομακρυσμένα στην συσκευή που ανήκει η μονάδα
     * και βέβαια και σε όλους τους άλλους UserClient που έχουν "ζωντανή" εποπτεία
     * στην ίδια συσκευή (μέσω του server) .
     */
    private void setValue()
    {
        
        if (Tools.isNumeric(spValue.getValue().toString()))
        {
            parentUC.setValue(spValue.getValue().toString());
            parentUC.getUnit().setValue(spValue.getValue().toString());
            if (parentUC.getMode() == 3)
            {
                parentUC.setMode(4) ;
                parentUC.unit.setMode(String.valueOf(parentUC.mode));
                parentUC.unit.device.changeRemoteMode(parentUC.unit.getFullMode());
            }
            
            parentUC.getUnit().device.changeRemoteValue(parentUC.getUnit().getFullValue());
            this.dispose();
        }
        else
            this.dispose();
    }
    /**
     * Μέθοδος που αναλαμβάνει να κλείνει το dialog αν ο χρήστης
     * πατήσει το πλήκτρο "Esqape" .
     * Δημιουργήθηκε (όπως και άλλοι παρόμοιοι τέτοιοι μέθοδοι σε όλα τα project
     * του συστήματος) για να μην επαναλαμβάνεται συνέχεια το ίδιο κομμάτι
     * κώδικα και για να έχουμε καλύτερη διαχείριση .
     * @param evt Ένα KeyEvent αντικείμενο που το αξιοποιούμε με το να βρούμε
     * μέσω αυτού πιο ακριβώς πλήκτρο πάτησε ο χρήστης .
     */
    private void keyPressedEvent(java.awt.event.KeyEvent evt)
    {
        /*if (evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
        setValue();
        }
        else */if (evt.getKeyCode() == KeyEvent.VK_ESCAPE)
            this.dispose();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sValue = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        spValue = new javax.swing.JSpinner();
        bOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setType(java.awt.Window.Type.POPUP);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        sValue.setMaximum(255);
        sValue.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sValueStateChanged(evt);
            }
        });
        sValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sValueKeyPressed(evt);
            }
        });

        jLabel1.setText("Value : ");
        jLabel1.setToolTipText("");
        jLabel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel1KeyPressed(evt);
            }
        });

        spValue.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));
        spValue.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spValueStateChanged(evt);
            }
        });
        spValue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                spValueMouseClicked(evt);
            }
        });
        spValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                spValueKeyPressed(evt);
            }
        });

        bOK.setText("OK");
        bOK.setToolTipText("");
        bOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bOK.setMaximumSize(new java.awt.Dimension(40, 22));
        bOK.setMinimumSize(new java.awt.Dimension(40, 22));
        bOK.setPreferredSize(new java.awt.Dimension(40, 22));
        bOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bOKMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(sValue, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spValue, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(spValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Μέθοδος που εκτελείτε όταν το παραθυράκι μας γίνει ανενεργό και πρακτικά
     * όταν πατήσει ο χρήστης κάπου έξω από το dialog .
     * Ο σκοπός του είναι να κλείνει αυτόματα το dialog χωρίς να γίνεται κάποια αλλαγή τιμής.
     * @param evt Το Default WindowEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        this.dispose();         
    }//GEN-LAST:event_formWindowDeactivated
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης αλλάξει την τιμή του trackBar (JSlider) .
     * @param evt Το Default ChangeEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void sValueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sValueStateChanged
        spValue.setValue(sValue.getValue());
    }//GEN-LAST:event_sValueStateChanged
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης αλλάξει την τιμή του spinner .
     * @param evt Το Default ChangeEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void spValueStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spValueStateChanged
        sValue.setValue((int) spValue.getValue());
    }//GEN-LAST:event_spValueStateChanged
    /**
     * Μέθοδος που εκτελείτε όταν πατήσει ο χρήστης κάποιο κουμπί μέσα στο spinner του value.
     * @param evt Το Default KeyEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην keyPressedEvent.
     */
    private void spValueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spValueKeyPressed
        keyPressedEvent(evt) ;
        
    }//GEN-LAST:event_spValueKeyPressed
    /**
     * Μέθοδος που εκτελείτε όταν πατήσει ο χρήστης κάποιο πλήκτρο ενώ βρίσκεται στην trackBar (JSlider).
     * @param evt Το Default KeyEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην keyPressedEvent.
     */
    private void sValueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sValueKeyPressed
        keyPressedEvent(evt) ;
        
    }//GEN-LAST:event_sValueKeyPressed
    /**
     * Μέθοδος που εκτελείτε όταν πατήσει ο χρήστης κάποιο κουμπί ενώ βρίσκεται στην ετικέτα του "Value :".
     * @param evt Το Default KeyEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην keyPressedEvent.
     */
    private void jLabel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel1KeyPressed
        keyPressedEvent(evt) ;
        
    }//GEN-LAST:event_jLabel1KeyPressed
    /**
     * Μέθοδος που εκτελείτε όταν πατήσει ο χρήστης κάποιο κουμπί μέσα στο dialog.
     * @param evt Το Default KeyEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην keyPressedEvent.
     */
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        keyPressedEvent(evt) ;        
    }//GEN-LAST:event_formKeyPressed
    
    private void spValueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_spValueMouseClicked
        
    }//GEN-LAST:event_spValueMouseClicked
    
    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        
    }//GEN-LAST:event_formWindowDeiconified
    
    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        
    }//GEN-LAST:event_formWindowGainedFocus
    
    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
        
    }//GEN-LAST:event_formWindowLostFocus
    /**
     * Μέθοδος που εκτελείτε όταν πατήσει ο χρήστης το κουμπί "OK" .
     * Ο σκοπός του είναι να αλλάζει να εκτελεί την setValue και κατά επέκταση
     * να αλλάζει την τιμή της μονάδας και να κλείνει το dialog.
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void bOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bOKMouseClicked
        setValue();
    }//GEN-LAST:event_bOKMouseClicked
    
    /**
     * Κλασική static main μέθοδος που δίνει την δυνατότητα να ξεκινήσει η εκτέλεση
     * του project μας από εδώ και πρακτικά χρησιμοποιείται για να μπορούμε να κάνουμε
     * αλλαγές και ελέγχους όσο αναφορά κυρίως στην εμφάνιση του dialog μας.
     * Δεν έχει καμία χρήση κατά την λειτουργία του User Client στην κανονική ροή εκτέλεσης.
     * @param args Default παράμετροι που δεν χρησιμοποιούνται και που μπορεί δυνητικά να πάρει
     * το πρόγραμμα μας στην περίπτωση που εκτελεστεί από περιβάλλον γραμμής εντολών και όταν
     * συγχρόνως το project μας έχει σαν κύριο-αρχικό αρχείο εκτέλεσης το DfChangeValue.java.
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DfChangeValue dialog = new DfChangeValue(true,0.0,null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSlider sValue;
    private javax.swing.JSpinner spValue;
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