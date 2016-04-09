package DeviceClientV1;

import java.util.Objects;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
/**
 * Κλάση που μέσω αυτής μπορεί κάποιος χρήστης με γραφικό περιβάλλον
 * να διαχειριστεί(νέο ή αλλαγή) ένα Unit του Simulator.
 * Η κλάση έχει κληρονομήσει ένα JDialog .
 * Επίσης η κλάση είναι πλήρως εξαρτημένη και παίζει σε συνδυασμό με τη {@link Fmain} .
 * @author Michael Galliakis
 */
public class DnewUnit extends javax.swing.JDialog {
    Fmain myParent ;
    boolean newOrEdit ;
    String firstConName = "",firstUnitName = "";
    int selectedRowIndex ;
    DefaultComboBoxModel cbModelOfControllers ;
    /**
     * Κατασκευαστής της κλάσης που την αρχικοποιεί .
     * @param parent Η φόρμα που "ανήκει" το νέο panel της κλάσης μας
     * @param modal True αν θέλουμε να είναι συνέχεια onTop μέχρι να κλείσουμε το panel μας
     * αλλιώς False αν θέλουμε ο χρήστης να μπορεί να κάνει τρέχον παράθυρο την φόρμα που ανήκει το panel.
     */
    public DnewUnit(java.awt.Frame parent, boolean modal) {
        this(parent,modal,true) ;
    }
    /**
     * Κατασκευαστής της κλάσης που την αρχικοποιεί .
     * @param parent Η φόρμα που ανήκει το νέο panel της κλάσης μας
     * @param modal True αν θέλουμε να είναι συνέχεια onTop μέχρι να κλείσουμε το panel μας
     * αλλιώς False αν θέλουμε ο χρήστης να μπορεί να κάνει τρέχον παράθυρο την φόρμα που ανήκει το panel.
     * @param _newOrEdit True αν θέλουμε να διαχειριστούμε νέο Unit και false αν θέλουμε να κάνουμε
     * edit σε ένα ήδη υπάρχων Unit .
     */
    public DnewUnit(java.awt.Frame parent, boolean modal,boolean _newOrEdit) { //true : "New" And false : "Edit"
        super(parent, modal);
        initComponents();
        newOrEdit = _newOrEdit ;
        this.setIconImage(Globals.biLogo);
        this.setLocationRelativeTo(parent);
        try
        {
            myParent = (Fmain)parent ;
            if (myParent!=null)
            {
                fillData() ;
                
                if (!newOrEdit)
                {
                    setTitle("View/Edit Unit");
                    fillValuesFromSelectedRow() ;
                }
            }
        }
        catch (Exception ex)
        {
            //Nothing
        }
    }
    /**
     * Μέθοδος που αναλαμβάνει με βάση τη γραμμή που είναι επιλεγμένοι στον πίνακα
     * των Units της {@link Fmain} να φορτώσει ανάλογα τα δεδομένα στο Panel μας
     * ούτως ώστε να μπορεί ο χρήστης να τα διαχειριστεί.
     */
    private void fillValuesFromSelectedRow()
    {
        selectedRowIndex = myParent.jtUnits.getSelectedRow() ;
        
        cbControllers.setSelectedItem(myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 0)) ; // Selected Cont
        
        tfUnitName.setText(String.valueOf(myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 1)));
        cbTypes.setSelectedIndex(Integer.parseInt((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 2)));
        
        cbModes.setSelectedIndex(Integer.parseInt((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 3)));
        tfTag.setText(String.valueOf(myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 4))) ;
        
        Double inVa = Double.parseDouble((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 5)) ;
        if ((inVa == Math.floor(inVa)) && !Double.isInfinite(inVa))
            spInitialValue.setValue(Integer.parseInt((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 5)));
        else
            spInitialValue.setValue(Double.parseDouble((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 5)));
        
        
        Double maxVa = Double.parseDouble((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 6)) ;
        if ((maxVa == Math.floor(maxVa)) && !Double.isInfinite(maxVa))
            spMaxValue.setValue(Integer.parseInt((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 6)));
        else
            spMaxValue.setValue(Double.parseDouble((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 6)));
        
        Double minVa = Double.parseDouble((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 7));
        if ((minVa == Math.floor(minVa)) && !Double.isInfinite(minVa))
            spMinValue.setValue(Integer.parseInt((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 7)));
        else
            spMinValue.setValue(Double.parseDouble((String) myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 7)));
        
        
        String limitValue = String.valueOf(myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 8)) ;
        if (limitValue.contains("NL"))
            rbNoLimit.setSelected(true);
        else if (limitValue.contains("SW"))
        {
            rbLimitSW.setSelected(true);
            
            Double liVa = Double.parseDouble(limitValue);
            if ((liVa == Math.floor(liVa)) && !Double.isInfinite(liVa))
                spLimitValue.setValue(Integer.parseInt(limitValue.replace("SW","")));
            else
                spLimitValue.setValue(Double.parseDouble(limitValue.replace("SW","")));
        }
        else if (limitValue.contains("TB"))
        {
            rbLimitTB.setSelected(true);
            
            Double liVa = Double.parseDouble(limitValue);
            if ((liVa == Math.floor(liVa)) && !Double.isInfinite(liVa))
                spLimitValue.setValue(Integer.parseInt(limitValue.replace("TB","")));
            else
                spLimitValue.setValue(Double.parseDouble(limitValue.replace("TB","")));
        }
        else
        {
            rbNoLimit.setSelected(true);
            
            Double liVa = Double.parseDouble(limitValue);
            if ((liVa == Math.floor(liVa)) && !Double.isInfinite(liVa))
                spLimitValue.setValue(Double.parseDouble(limitValue));
            else
                spLimitValue.setValue(Double.parseDouble(limitValue));
        }
        
        firstConName = String.valueOf(myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 0)) ;
        firstUnitName = String.valueOf(myParent.tableModelOfUnits.getValueAt(selectedRowIndex, 1));
    }
    
    /**
     * Μέθοδος που αναλαμβάνει να φορτώσει τις προεπιλεγμένες λίστες στα combobox των Units και Modes.
     */
    private void fillData()
    {
        cbModelOfControllers  = new DefaultComboBoxModel();
        
        for (int i = 0 ; i<myParent.listModelOfControllers.getSize();i++)
            cbModelOfControllers.addElement(myParent.listModelOfControllers.getElementAt(i));
        
        cbControllers.setModel(cbModelOfControllers);
        
        DefaultComboBoxModel cbModelOfTypes ;
        cbModelOfTypes = new DefaultComboBoxModel(Globals.namesOfTypes) ;
        cbTypes.setModel(cbModelOfTypes);
        
        DefaultComboBoxModel cbModelOfModes ;
        cbModelOfModes = new DefaultComboBoxModel(Globals.namesOfModes) ;
        cbModes.setModel(cbModelOfModes);
    }
    /**
     * Μέθοδος που εξετάζει αν όλα τα στοιχεία του panel είναι σωστά συμπληρωμένα ούτως ώστε να μπορούν
     * να ενσωματωθούν στο πίνακα των Units της {@link Fmain} .
     * @return Επιστρέφει "OK" αν όλα είναι σωστά ή ένα μήνυμα υπόδειξης της λάθος συμπλήρωσης .
     */
    private String checkForm()
    {
        if (tfUnitName.getText().trim().equals(""))
            return "Unit name is empty" ;
        
        Vector<Vector<String>> vItems = myParent.tableModelOfUnits.getDataVector() ;
        
        for (int i = 0 ; i<vItems.size();i++)
        {
            String conName =String.valueOf(vItems.elementAt(i).elementAt(0)) ;
            String unitName =String.valueOf(vItems.elementAt(i).elementAt(1)) ;
            if (!newOrEdit && selectedRowIndex==i)
                break ;
            
            
            if (conName.equals(String.valueOf(cbControllers.getSelectedItem())) && unitName.equals(tfUnitName.getText()))
                return "There is another Unit of the same name " ;
        }
        
        Double max = Double.parseDouble(spMaxValue.getValue().toString()) ;
        Double min = Double.parseDouble(spMinValue.getValue().toString()) ;
        
        if (Objects.equals(min, max))
            return "The min is equals to Max " ;
        else if (min>max)
            return "The min more than Max " ;
        
        
        Double initialValue = Double.parseDouble(spInitialValue.getValue().toString()) ;
        
        if (initialValue>max || initialValue <min)
            return "The initial value is not between Max and Min" ;
        if (spLimitValue.isEnabled())
        {
            Double limit = Double.parseDouble(spLimitValue.getValue().toString()) ;
            if (limit>max || limit <min)
                return "The limit is not between Max and Min" ;
            
        }
        return "OK" ;
    }
    /**
     * Μέθοδος που απλά προσθέτει ή αλλάζει (ανάλογα τον τύπο που έχει πάρει από το Constractor)
     * μια γραμμή στο πίνακα των Units της {@link Fmain} με τα στοιχεία που έχει συμπληρώσει
     * ο χρήστης για το συγκεκριμένο Unit .
     */
    private void addRow()
    {
        Vector<String> vItems = new Vector();
        
        vItems.add((String) cbControllers.getSelectedItem()) ;
        vItems.add(tfUnitName.getText()) ;
        vItems.add(String.valueOf(cbTypes.getSelectedIndex())) ;
        vItems.add(String.valueOf(cbModes.getSelectedIndex())) ;
        vItems.add(tfTag.getText()) ;
        vItems.add(String.valueOf(spInitialValue.getValue())) ;
        vItems.add(String.valueOf(spMaxValue.getValue())) ;
        vItems.add(String.valueOf(spMinValue.getValue())) ;
        if (rbNoLimit.isSelected())
            vItems.add("NL") ;
        else if (rbLimitSW.isSelected())
            vItems.add(String.valueOf(spLimitValue.getValue()) + "SW") ;
        else
            vItems.add(String.valueOf(spLimitValue.getValue()) + "TB") ;
        
        if (newOrEdit)
            myParent.tableModelOfUnits.addRow(vItems);
        else
        {
            myParent.tableModelOfUnits.removeRow(selectedRowIndex);
            myParent.tableModelOfUnits.insertRow(selectedRowIndex,vItems);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgLimitGroup = new javax.swing.ButtonGroup();
        jLabel5 = new javax.swing.JLabel();
        spLimitValue = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbTypes = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        cbModes = new javax.swing.JComboBox();
        cbControllers = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        rbNoLimit = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        tfUnitName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        rbLimitSW = new javax.swing.JRadioButton();
        spInitialValue = new javax.swing.JSpinner();
        tfTag = new javax.swing.JTextField();
        rbLimitTB = new javax.swing.JRadioButton();
        spMinValue = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        bOK = new javax.swing.JButton();
        spMaxValue = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        bCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Unit");
        setResizable(false);

        jLabel5.setText("Limit Value :");
        jLabel5.setToolTipText("");

        spLimitValue.setEditor(new javax.swing.JSpinner.NumberEditor(spLimitValue, ""));
        spLimitValue.setEnabled(false);
        spLimitValue.setValue(128);

        jLabel6.setText("Initial Value :");
        jLabel6.setToolTipText("");

        jLabel7.setText("Min Value :");
        jLabel7.setToolTipText("");

        cbTypes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Max Value :");

        cbModes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbControllers.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("Controller :");
        jLabel9.setToolTipText("");

        bgLimitGroup.add(rbNoLimit);
        rbNoLimit.setSelected(true);
        rbNoLimit.setText("No Limit");
        rbNoLimit.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbNoLimitStateChanged(evt);
            }
        });

        jLabel1.setText("Unit name :");

        jLabel2.setText("Unit Type :");
        jLabel2.setToolTipText("");

        bgLimitGroup.add(rbLimitSW);
        rbLimitSW.setText("Limit (SW)");
        rbLimitSW.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbLimitSWStateChanged(evt);
            }
        });

        spInitialValue.setEditor(new javax.swing.JSpinner.NumberEditor(spInitialValue, ""));
        spInitialValue.setValue(200);

        bgLimitGroup.add(rbLimitTB);
        rbLimitTB.setText("Limit (TB)");
        rbLimitTB.setToolTipText("");
        rbLimitTB.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                rbLimitTBrbNoLimitStateChanged(evt);
            }
        });

        spMinValue.setEditor(new javax.swing.JSpinner.NumberEditor(spMinValue, ""));

        jLabel3.setText("Mode :");

        bOK.setText("OK");
        bOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bOKMouseClicked(evt);
            }
        });
        bOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOKActionPerformed(evt);
            }
        });

        spMaxValue.setEditor(new javax.swing.JSpinner.NumberEditor(spMaxValue, ""));
        spMaxValue.setValue(255);

        jLabel4.setText("Tag :");

        bCancel.setText("Cancel");
        bCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bCancelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(bOK)
                .addGap(35, 35, 35)
                .addComponent(bCancel)
                .addGap(73, 73, 73))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1)
                                                .addComponent(jLabel4)
                                                .addComponent(jLabel6))
                                            .addGap(36, 36, 36))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel3))
                                            .addGap(53, 53, 53)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(51, 51, 51)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbControllers, 0, 173, Short.MAX_VALUE)
                                    .addComponent(tfUnitName)
                                    .addComponent(cbTypes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbModes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tfTag)
                                    .addComponent(spInitialValue)
                                    .addComponent(spMaxValue)
                                    .addComponent(spMinValue)
                                    .addComponent(spLimitValue)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(rbNoLimit))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbLimitSW)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbLimitTB)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cbControllers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfUnitName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbTypes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbModes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(spInitialValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(spMaxValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(spMinValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbNoLimit)
                    .addComponent(rbLimitSW)
                    .addComponent(rbLimitTB))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(spLimitValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bOK)
                    .addComponent(bCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης αλλάξει την κατάσταση του NoLimit radio Button.
     * @param evt Το Default ChangeEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void rbNoLimitStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbNoLimitStateChanged
        if (rbNoLimit.isSelected())
            spLimitValue.setEnabled(false);
        else
            spLimitValue.setEnabled(true);
    }//GEN-LAST:event_rbNoLimitStateChanged
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης αλλάξει την κατάσταση του LimitΤΒ radio Button.
     * @param evt Το Default ChangeEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void rbLimitTBrbNoLimitStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbLimitTBrbNoLimitStateChanged
        if (rbNoLimit.isSelected())
            spLimitValue.setEnabled(false);
        else
            spLimitValue.setEnabled(true);
    }//GEN-LAST:event_rbLimitTBrbNoLimitStateChanged
    
    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed
        
    }//GEN-LAST:event_bOKActionPerformed
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης αλλάξει την κατάσταση του LimitSW radio Button.
     * @param evt Το Default ChangeEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void rbLimitSWStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_rbLimitSWStateChanged
        if (rbNoLimit.isSelected())
            spLimitValue.setEnabled(false);
        else
            spLimitValue.setEnabled(true);
    }//GEN-LAST:event_rbLimitSWStateChanged
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "ΟΚ" .
     * Ουσιαστικά κάνει τον απαραίτητο έλεγχο σωστής συμπλήρωσης και αν όλα είναι "OK"
     * προσθέτει ή αλλάζει (ανάλογα το τύπο που έχει πάρει από το Constractor)την κατάλληλη γραμμή
     * των Units από τη {@link Fmain} και στην συνέχεια κλείνει το panel .
     * Αν δεν είναι όλα σωστά συμπληρωμένα εμφανίζετε στον χρήστη το ανάλογο μήνυμα χωρίς να
     * κάνει κάτι άλλο .
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void bOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bOKMouseClicked
        String result = checkForm() ;
        if (result.equals("OK"))
        {
            addRow() ;
            this.dispose();
        }
        else
            JOptionPane.showMessageDialog(this,"Error! :" + result + "                        ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
        
    }//GEN-LAST:event_bOKMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Cancel" και πρακτικά κλείνει το panel .
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void bCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bCancelMouseClicked
        this.dispose();
    }//GEN-LAST:event_bCancelMouseClicked
    /**
     * Κλασική static main μέθοδος που δίνει την δυνατότητα να ξεκινήσει η εκτέλεση
     * του project μας από εδώ και πρακτικά χρησιμοποιείται για να μπορούμε να κάνουμε
     * αλλαγές και ελέγχους όσο αναφορά κυρίως στην εμφάνιση του panel μας.
     * Δεν έχει καμία χρήση κατά την λειτουργία του Simulator στην κανονική ροή εκτέλεσης.
     * @param args Default παράμετροι που δεν χρησιμοποιούνται και που μπορεί δυνητικά να πάρει
     * το πρόγραμμα μας στην περίπτωση που εκτελεστεί από περιβάλλον γραμμής εντολών και όταν
     * συγχρόνως το project μας έχει σαν κύριο-αρχικό αρχείο εκτέλεσης το DnewUnit.java.
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DnewUnit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DnewUnit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DnewUnit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DnewUnit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DnewUnit dialog = new DnewUnit(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bOK;
    private javax.swing.ButtonGroup bgLimitGroup;
    private javax.swing.JComboBox cbControllers;
    private javax.swing.JComboBox cbModes;
    private javax.swing.JComboBox cbTypes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton rbLimitSW;
    private javax.swing.JRadioButton rbLimitTB;
    private javax.swing.JRadioButton rbNoLimit;
    private javax.swing.JSpinner spInitialValue;
    private javax.swing.JSpinner spLimitValue;
    private javax.swing.JSpinner spMaxValue;
    private javax.swing.JSpinner spMinValue;
    private javax.swing.JTextField tfTag;
    private javax.swing.JTextField tfUnitName;
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