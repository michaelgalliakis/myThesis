package UserClientV1;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
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
import static UserClientV1.ManageUserClient.initDevicesFrame;
import java.awt.event.KeyEvent;

/**
 * Κλάση(έχει κληρονομήσει την JFrame) από την οποία ξεκινάει ο UserClient .
 * Πρακτικά είναι ένα μικρό παραθυράκι που δίνει την δυνατότητα στον
 * χρήστη να δώσει ip και πόρτα του server όπως επίσης και username
 * και password για να επιτευχθεί η σύνδεση με το server.
 * Στην συνέχεια αν επιτευχθεί η σύνδεση κανονικά χωρίς κάποιο πρόβλημα
 * αναλαμβάνει να ανοίξει το κύριο παράθυρο της εφαρμογής ({@link Fmain}) για να ξεκινήσει
 * τις πιθανές εποπτείες του πάνω στις συσκευές που βλέπει ο εκάστοτε χρήστης.
 * Ακόμη μέσω του παραθύρου της κλάσης ο χρήστης έχει την δυνατότητα να
 * ανοίξει το παράθυρο με κάποιες πληροφορίες της πτυχιακής ({@link Dthesis})
 * @author Michael Galliakis
 */
public class FuserLogin extends javax.swing.JFrame {
    private boolean connectProcIsBusy = false ;
    /**
     * Κατασκευαστής της κλάσης που κάνει όλα τα απαραίτητα βήματα για να είναι
     * έτοιμη η εφαρμογή μας να ξεκινήσει .
     */
    public FuserLogin() {
        initComponents();
        
        setTitle(Globals.messDialTitle);
        Globals.prepareStaticVariables(this) ;
        this.setIconImage(Globals.biLogo);
        Globals.objUserLogin = this ;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        loadConfig() ;
    }
    /**
     * Μέθοδος που απλά κάνει ομαδοποιημένα όλη την διαδικασία για να συνδεθεί
     * ο χρήστης στον Server και να ανοίξει ουσιαστικά το κύριο παράθυρο
     * του User Client για να ξεκινήσει ο χρήστης τις πιθανόν εποπτείες του .
     * Πρακτικά εκτελείτε όταν ο χρήστης πατήσει το πλήκτρο για να συνδεθεί .
     */
    private void connectProccessButton()
    {
        if (!connectProcIsBusy)
        {
            connectProcIsBusy = true ;
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (!Tools.isCorrectPort(tfPort.getText()))
            {
                Tools.Debug.print("Connect UnsuccessFully - Port error!");
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                connectProcIsBusy = false ;
                JOptionPane.showMessageDialog(this,"Port error!","Info",JOptionPane.PLAIN_MESSAGE);
                return ;
            }
            ManageSocket ms = ManageUserClient.connectProcess(tfAddress.getText().toString(),Integer.parseInt(tfPort.getText().toString())) ;
            if (ms==null)
            {
                Tools.Debug.print("Connect UnsuccessFully - Connect Failed!");
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                connectProcIsBusy = false ;
                JOptionPane.showMessageDialog(this,"Connect Failed","Info",JOptionPane.PLAIN_MESSAGE);
                return ;
            }
            if (ManageUserClient.certificationProcess(ms))
            {
                String DBID = ManageUserClient.loginProcess(ms, tfUsername.getText().toString(), pfPassword.getText().toString());
                
                if (!DBID.equals("X"))
                {
                    String typeUser = ManageUserClient.getTypeUser(ms,DBID) ;
                    if (!typeUser.equals("X"))
                    {
                        Globals.typeUser = typeUser ;
                        Globals.username = tfUsername.getText().toString() ;
                        Globals.password = pfPassword.getText().toString() ;
                        Globals.address = tfAddress.getText().toString() ;
                        Globals.port = Integer.parseInt(tfPort.getText().toString()) ;
                        Globals.DBUserID = DBID ;
                        Globals.username = tfUsername.getText().toString() ;
                        Fmain frm = new Fmain(false) ;
                        Globals.objMainFrame = frm ;
                        if (Globals.objDevices == null)
                        {
                            initDevicesFrame(this);
                            
                        }
                        
                        this.setVisible(false);
                        frm.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        frm.setVisible(true);
                        Tools.Debug.print("Connect SuccessFully!");
                    }
                    else
                    {
                        Tools.Debug.print("Connect UnsuccessFully - Login Failed(Type User)!");
                        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        connectProcIsBusy = false ;
                        JOptionPane.showMessageDialog(this,"Login Failed","Info",JOptionPane.PLAIN_MESSAGE);
                    }
                }
                else
                {
                    Tools.Debug.print("Connect UnsuccessFully - Login Failed!");
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    connectProcIsBusy = false ;
                    JOptionPane.showMessageDialog(this,"Login Failed","Info",JOptionPane.PLAIN_MESSAGE);
                }
            }
            else
            {
                Tools.Debug.print("Connect UnsuccessFully - Certification Failed!");
                this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                connectProcIsBusy = false ;
                JOptionPane.showMessageDialog(this,"Certification Failed","Info",JOptionPane.PLAIN_MESSAGE);
            }
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            connectProcIsBusy = false ;
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfUsername = new javax.swing.JTextField();
        pfPassword = new javax.swing.JPasswordField();
        tfAddress = new javax.swing.JTextField();
        tfPort = new javax.swing.JTextField();
        bConnect = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lTEI = new javax.swing.JLabel();
        lLogo = new javax.swing.JLabel();
        bThessis = new javax.swing.JButton();
        lPostIt = new javax.swing.JLabel();
        checkbox1 = new java.awt.Checkbox();
        cbRem = new javax.swing.JCheckBox();
        pRem = new javax.swing.JPanel();
        cbRemPass = new javax.swing.JCheckBox();
        cbRemUser = new javax.swing.JCheckBox();
        lCCRight = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tfUsername.setText("maria");
        tfUsername.setToolTipText("");
        tfUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfUsernameKeyPressed(evt);
            }
        });

        pfPassword.setText("maria");
        pfPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pfPasswordKeyPressed(evt);
            }
        });

        tfAddress.setText("localhost");
        tfAddress.setToolTipText("");
        tfAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfAddressKeyPressed(evt);
            }
        });

        tfPort.setText("50128");
        tfPort.setToolTipText("");
        tfPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfPortKeyPressed(evt);
            }
        });

        bConnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/connect.png"))); // NOI18N
        bConnect.setToolTipText("");
        bConnect.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bConnect.setMaximumSize(new java.awt.Dimension(80, 80));
        bConnect.setMinimumSize(new java.awt.Dimension(80, 80));
        bConnect.setPreferredSize(new java.awt.Dimension(80, 80));
        bConnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bConnectMouseClicked(evt);
            }
        });

        bCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/closeLog.png"))); // NOI18N
        bCancel.setToolTipText("");
        bCancel.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bCancel.setMaximumSize(new java.awt.Dimension(80, 80));
        bCancel.setMinimumSize(new java.awt.Dimension(80, 80));
        bCancel.setName(""); // NOI18N
        bCancel.setPreferredSize(new java.awt.Dimension(80, 80));
        bCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bCancelMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Address : ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Port :");
        jLabel2.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Password :");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Username :");
        jLabel4.setToolTipText("");

        lTEI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/TEI36.png"))); // NOI18N
        lTEI.setName(""); // NOI18N

        lLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/logoPMGv2F48.png"))); // NOI18N
        lLogo.setToolTipText("");

        bThessis.setBackground(new java.awt.Color(153, 51, 255));
        bThessis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/thesis24.png"))); // NOI18N
        bThessis.setText("Thesis");
        bThessis.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bThessis.setMaximumSize(new java.awt.Dimension(80, 80));
        bThessis.setMinimumSize(new java.awt.Dimension(80, 80));
        bThessis.setName(""); // NOI18N
        bThessis.setPreferredSize(new java.awt.Dimension(80, 80));
        bThessis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bThessisMouseClicked(evt);
            }
        });

        lPostIt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/postit.png"))); // NOI18N
        lPostIt.setToolTipText("");

        checkbox1.setLabel("checkbox1");

        cbRem.setSelected(true);
        cbRem.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cbRemStateChanged(evt);
            }
        });
        cbRem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbRemMouseClicked(evt);
            }
        });

        pRem.setBorder(javax.swing.BorderFactory.createTitledBorder("Remember"));

        cbRemPass.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        cbRemPass.setText("Password");
        cbRemPass.setToolTipText("");
        cbRemPass.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cbRemPassStateChanged(evt);
            }
        });

        cbRemUser.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        cbRemUser.setSelected(true);
        cbRemUser.setText("Username");
        cbRemUser.setToolTipText("");
        cbRemUser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cbRemUserStateChanged(evt);
            }
        });
        cbRemUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbRemUserMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pRemLayout = new javax.swing.GroupLayout(pRem);
        pRem.setLayout(pRemLayout);
        pRemLayout.setHorizontalGroup(
            pRemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pRemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbRemPass)
                    .addComponent(cbRemUser))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pRemLayout.setVerticalGroup(
            pRemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pRemLayout.createSequentialGroup()
                .addComponent(cbRemUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbRemPass))
        );

        lCCRight.setName(""); // NOI18N

        jLabel5.setFont(new java.awt.Font("Dialog", 2, 14)); // NOI18N
        jLabel5.setText("cs081001");
        jLabel5.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(20, 20, 20)
                                .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pRem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(cbRem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lPostIt))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bThessis, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lTEI)
                                .addGap(4, 4, 4)
                                .addComponent(lLogo))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lCCRight)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pRem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(cbRem))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lPostIt)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bThessis, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bConnect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lTEI))
                        .addGap(7, 7, 7)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lCCRight)))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης "κλικάρει" το κουμπί για να κλείσει
     * το παράθυρο της κλάσης μας (με την εικόνα του μεγάλου "Χ") .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */    
    private void bCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bCancelMouseClicked
        saveToXMLLoginSettings() ;
        this.dispose();
        Runtime.getRuntime().exit(0) ;
    }//GEN-LAST:event_bCancelMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης "κλικάρει" το κουμπί για να γίνει
     * σύνδεση με το server και να εκτελεστεί η connectProccessButton.
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */    
    private void bConnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bConnectMouseClicked
        connectProccessButton() ;
    }//GEN-LAST:event_bConnectMouseClicked
    boolean lastRemState = false ;
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης αλλάξει την κατάσταση του checkbox
     * για το αν θα αποθηκεύονται τα στοιχεία σύνδεσης. Δηλαδή αν το "τσεκάρει" ή το "ξε-τσεκάρει" .
     * @param evt Το Default ChangeEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */    
    private void cbRemStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cbRemStateChanged
        pRem.setEnabled(cbRem.isSelected());
        cbRemUser.setEnabled(cbRem.isSelected());
        cbRemUser.setSelected(cbRem.isSelected());
        cbRemPass.setEnabled(cbRem.isSelected());
        if (!cbRem.isSelected())
            cbRemPass.setSelected(false);
        lastRemState = pRem.isEnabled();
    }//GEN-LAST:event_cbRemStateChanged
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης αλλάξει την κατάσταση του checkbox
     * για το Username . Δηλαδή αν το "τσεκάρει" ή το "ξε-τσεκάρει" .
     * @param evt Το Default ChangeEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */    
    private void cbRemUserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cbRemUserStateChanged
        if (cbRemUser.isSelected())
        {
            cbRemPass.setEnabled(true);
        }
        else
        {
            cbRemPass.setEnabled(false);
            cbRemPass.setSelected(false);
        }
    }//GEN-LAST:event_cbRemUserStateChanged
    
    private void cbRemPassStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cbRemPassStateChanged
        
    }//GEN-LAST:event_cbRemPassStateChanged
    
    private void cbRemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbRemMouseClicked
    }//GEN-LAST:event_cbRemMouseClicked
    
    private void cbRemUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbRemUserMouseClicked
    }//GEN-LAST:event_cbRemUserMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί για να ανοίξει το παράθυρο
     * με τις πληροφορίες της Πτυχιακής ({@link Dthesis}).
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */    
    private void bThessisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bThessisMouseClicked
        
        if (Globals.objThesis==null)
        {
            Dthesis the = new Dthesis(new Frame(), false) ;
            the.setVisible(true);
            Globals.objThesis = the ;
        }
        else
            Globals.objThesis.setVisible(true);
        bThessis.setBackground(UIManager.getColor ( "Panel.background" ));  
    }//GEN-LAST:event_bThessisMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν πάει να κλείσει η φόρμα μας.
     * @param evt Το Default WindowEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        saveToXMLLoginSettings() ;        
    }//GEN-LAST:event_formWindowClosing
 /**
     * Μέθοδος που εκτελείτε όποτε πατήσει οποιοδήποτε κουμπί μέσα στο JPasswordField
     * του password ο χρήστης .     
     * @param evt Το Default KeyEvent αντικείμενο που το αξιοποιούμε με το να βρούμε
     * μέσω αυτού πιο ακριβώς πλήκτρο πάτησε ο χρήστης .
     */
    private void pfPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pfPasswordKeyPressed
         if (evt.getKeyCode() ==  KeyEvent.VK_ENTER)
            connectProccessButton() ;
    }//GEN-LAST:event_pfPasswordKeyPressed
    /**
     * Μέθοδος που εκτελείτε όποτε πατήσει οποιοδήποτε κουμπί μέσα στο JTextField
     * του username ο χρήστης .     
     * @param evt Το Default KeyEvent αντικείμενο που το αξιοποιούμε με το να βρούμε
     * μέσω αυτού πιο ακριβώς πλήκτρο πάτησε ο χρήστης .
     */
    private void tfUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfUsernameKeyPressed
        if (evt.getKeyCode() ==  KeyEvent.VK_ENTER)
            connectProccessButton() ;
    }//GEN-LAST:event_tfUsernameKeyPressed
    /**
     * Μέθοδος που εκτελείτε όποτε πατήσει οποιοδήποτε κουμπί μέσα στο JTextField
     * της IP διεύθυνσης ο χρήστης .     
     * @param evt Το Default KeyEvent αντικείμενο που το αξιοποιούμε με το να βρούμε
     * μέσω αυτού πιο ακριβώς πλήκτρο πάτησε ο χρήστης .
     */
    private void tfAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfAddressKeyPressed
        if (evt.getKeyCode() ==  KeyEvent.VK_ENTER)
            connectProccessButton() ;
    }//GEN-LAST:event_tfAddressKeyPressed
    /**
     * Μέθοδος που εκτελείτε όποτε πατήσει οποιοδήποτε κουμπί μέσα στο JTextField
     * της πόρτας ο χρήστης .     
     * @param evt Το Default KeyEvent αντικείμενο που το αξιοποιούμε με το να βρούμε
     * μέσω αυτού πιο ακριβώς πλήκτρο πάτησε ο χρήστης .
     */
    private void tfPortKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPortKeyPressed
        if (evt.getKeyCode() ==  KeyEvent.VK_ENTER)
            connectProccessButton() ;
    }//GEN-LAST:event_tfPortKeyPressed
     
    /**
     * Συνάρτηση που φορτώνει από αρχείο τις επιλογές σύνδεσης του χρήστη που είχε 
     * συμπληρώσει την τελευταία φορά εκτέλεσης του User Client .
     * @return True αν φορτώθηκαν και False αν έγινε κάποιο σφάλμα .
     */
    public boolean loadConfig()
    {
        File PMGLoginSettings = new File(Globals.runFolder.getAbsolutePath() +Globals.fSep+"PMGLoginSettings.config");
        
        Tools.Debug.print(PMGLoginSettings.getAbsolutePath());
        if (PMGLoginSettings.exists())
        {
            if (readXML(PMGLoginSettings))
                Tools.Debug.print("Open 'PMGLoginSettings.config' File Success!");
            
            else
            {
                Tools.Debug.print("Error : Failed open 'PMGLoginSettings.config' File!");
                return false ;
            }
        }
        else
        {
            Tools.Debug.print("Failed : File 'PMGLoginSettings.config' not Exists!");
            return false ;
        }
        return true ;
    }    
    /**
     * Μέθοδος που αναλαμβάνει να ανακτήσει (από αρχείο xml) και να συμπληρώσει τα στοιχεία
     * σύνδεσης του χρήστη στην φόρμα της κλάσης μας({@link FuserLogin}) με βάση βέβαια
     * αυτά που είχε χρησιμοποιήσει ο χρήστης την τελευταία φορά εκτέλεσης του User Client και
     * που κατά το κλείσιμο της εφαρμογής είχαν αποθηκευτεί στο εν λόγω αρχείο αρχίο xml .
     * @return True αν όλα πήγαν καλά και False αν υπήρξε κάποιο σφάλμα .
     */
    private boolean readXML(File f) {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {            
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            dom = db.parse(f);
            
            Element doc = dom.getDocumentElement();
            
            tfUsername.setText(Globals.getTextValue(doc, "Username", 0)) ;
            pfPassword.setText(Globals.getTextValue(doc, "Password", 0)) ;
            tfAddress.setText(Globals.getTextValue(doc, "Address", 0)) ;
            tfPort.setText(Globals.getTextValue(doc, "Port", 0)) ;
            
            if (tfUsername.getText().equals("") && pfPassword.getText().equals("") && tfAddress.getText().equals("") && tfPort.getText().equals(""))
            {
                cbRem.setSelected(false);               
                pRem.setEnabled(cbRem.isSelected());
                cbRemUser.setEnabled(cbRem.isSelected());
                cbRemUser.setSelected(cbRem.isSelected());
                cbRemPass.setEnabled(cbRem.isSelected());
                cbRemPass.setSelected(false);
            }
            else  if (!tfUsername.getText().equals("") && !pfPassword.getText().equals(""))
            {
                cbRem.setSelected(true);
                cbRemUser.setSelected(true);
                cbRemPass.setSelected(true);                
            }
            else if (!pfPassword.getText().equals("") && tfUsername.getText().equals(""))
            {
                cbRem.setSelected(true);
                cbRemUser.setSelected(true);
                cbRemPass.setSelected(true);                
            }
            else if (pfPassword.getText().equals("") && !tfUsername.getText().equals(""))
            {
                cbRem.setSelected(true);                
                cbRemUser.setSelected(true);
                cbRemPass.setSelected(false);
            }
            else if (pfPassword.getText().equals("") && tfUsername.getText().equals(""))
            {
                cbRem.setSelected(true);
                cbRemPass.setSelected(false);
                cbRemUser.setSelected(false);
            }

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
    /**
     * Μέθοδος που αναλαμβάνει να αποθηκεύσει (αρχείο xml) τα στοιχεία σύνδεσης του χρήστη που 
     * έχει συμπληρώσει στην φόρμα της κλάσης μας({@link FuserLogin}) , με βάση 
     * βέβαια τις επιλογές του χρήστη ως προς το τι ακριβώς θα αποθηκευτεί (checkboxs).
     * @return True αν όλα πήγαν καλά και False αν υπήρξε κάποιο σφάλμα .
     */
    public boolean saveToXMLLoginSettings() {
        
        File PMGLoginSettings = new File(Globals.runFolder.getAbsolutePath() +Globals.fSep+"PMGLoginSettings.config");
        Document dom;
        Element unit,settings;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            dom = db.newDocument();
            
            Element rootEle = dom.createElement("PMGLoginSettings.config");
            
            settings = dom.createElement("Settings");
            
            unit = dom.createElement("Username");
            if (cbRemUser.isSelected() && cbRem.isSelected())
                unit.appendChild(dom.createTextNode(tfUsername.getText()));
            else
                unit.appendChild(dom.createTextNode(""));
            settings.appendChild(unit);
            unit = dom.createElement("Password");
            if (cbRemPass.isSelected()  && cbRem.isSelected())
                unit.appendChild(dom.createTextNode(pfPassword.getText()));
            else
                unit.appendChild(dom.createTextNode(""));
            
            settings.appendChild(unit);
            
            unit = dom.createElement("Address");
            
            if (cbRem.isSelected())
                unit.appendChild(dom.createTextNode(tfAddress.getText()));
            else
                unit.appendChild(dom.createTextNode(""));
            settings.appendChild(unit);
            unit = dom.createElement("Port");
            if (cbRem.isSelected())
                unit.appendChild(dom.createTextNode(tfPort.getText()));
            else
                unit.appendChild(dom.createTextNode(""));
            settings.appendChild(unit);
            
            
            rootEle.appendChild(settings);
            dom.appendChild(rootEle);
            
            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                
                tr.transform(new DOMSource(dom),new StreamResult(new FileOutputStream(PMGLoginSettings)));
                
            } catch (TransformerException te) {
                Tools.Debug.print(te.getMessage());
                return false ;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Fmain.class.getName()).log(Level.SEVERE, null, ex);
                return false ;
            }
        } catch (ParserConfigurationException pce) {
            Tools.Debug.print("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
            return false ;
        }
        return true ;
    }
     /**
     * Κλασική static main μέθοδος από την οποία ξεκινάει η εκτέλεση του User Client.
     * Πρακτικά δηλαδή δημιουργεί και εμφανίζει την κλάση {@link FuserLogin}.
     * @param args Default παράμετροι που δεν χρησιμοποιούνται και που μπορεί δυνητικά να πάρει
     * το πρόγραμμα μας στην περίπτωση που εκτελεστεί από περιβάλλον γραμμής εντολών .
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FuserLogin().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bConnect;
    public javax.swing.JButton bThessis;
    private javax.swing.JCheckBox cbRem;
    private javax.swing.JCheckBox cbRemPass;
    private javax.swing.JCheckBox cbRemUser;
    private java.awt.Checkbox checkbox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lCCRight;
    private javax.swing.JLabel lLogo;
    private javax.swing.JLabel lPostIt;
    private javax.swing.JLabel lTEI;
    private javax.swing.JPanel pRem;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JTextField tfAddress;
    private javax.swing.JTextField tfPort;
    private javax.swing.JTextField tfUsername;
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