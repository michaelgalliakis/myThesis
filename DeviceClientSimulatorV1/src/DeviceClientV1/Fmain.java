package DeviceClientV1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Ο προσομοιωτής δημιουργήθηκε επειδή στην αρχή της Πτυχιακής δεν είχα κάποιο
 * arduino για να δοκιμάσω τον Device Client σε πραγματικές συνθήκες.
 * Οπότε αναπτύχθηκε ο Simulator για να μπορούν να σταθούν, να υλοποιηθούν και να δοκιμαστούν
 * προσωρινά και τα υπόλοιπα κύρια κομμάτια του συστήματος (Server και UserClient).
 */


/**
 * Η βασική κλάση από την οποία ξεκινάει το project και που πρακτικά είναι το αρχικό και κύριο παράθυρο.
 * Μέσα από αυτή την φόρμα (κληρ. πλαίσιο-JFrame) μπορεί κάποιος χρήστης να προετοιμάσει τον προσομοιωτή με
 * τους ελεγκτές που θέλει όπως ακόμη και με τις μονάδες που θα έχει κάθε ελεγκτής.
 * Και βέβαια σε επόμενη φάση και αφού έχει καταχωρήσει τα κατάλληλα στοιχεία (IP,port,DeviceName,Password)
 * μπορεί να συνδεθεί στον Server παίζοντας τον ρόλο ενός εικονικού Device Client .
 * Επίσης μέσα από τη φόρμα υπάρχει η δυνατότητα να αποθηκεύσει κάποιος χρήστης τις
 * όποιες πληροφορίες έχει το εικονικό DeviceClient σε μορφή xml όπως επίσης και να
 * ανακτά ένα παλαιότερο xml με πληροφορίες για να αυτοσυμπληρώνονται αυτόματα όλο το
 * εικονικό Device Client .
 * Ακόμη ο χρήστης έχει την δυνατότητα ενώ είναι συνδεδεμένος ο εικονικός Device Client
 * στο server να αλλάζει την πιθανότητα που υπάρχει για να προκληθεί κάποια
 * εικονική αλλαγή στον φαινομενικό Device Client (Simulator-Προσωμοιοτής)
 * Βέβαια επειδή είναι ένα πρόγραμμα με γραφικό περιβάλλον έχει και ένα κουμπί
 * για να μπορεί ο χρήστης να δει κάποιες πληροφορίες που αφορούν την Πτυχιακή .
 * @author Michael Galliakis
 */
public class Fmain extends javax.swing.JFrame {
    DefaultListModel listModelOfControllers ;
    MyTableModel tableModelOfUnits ;
    
    ManageSocket manSocket ;
    ListenThread myThread = null ;
    
    String deviceID ;
    public boolean connected = false ;
    public Device device ;
    ManageSocketMessage manSocMess = new ManageSocketMessage() ;
    
    /**
     * Κατασκευαστής της κλάσης που κάνει όλα τις απαραίτητες διαδικασίες για
     * να προετοιμαστεί κατάλληλα το αρχικό και κύριο παράθυρο της φόρμας.
     */
    public Fmain()  {
        initComponents();
        Globals.prepareStaticVariables(this) ;
        
        setTitle(Globals.thesisTitle);
        this.setIconImage(Globals.biLogo);
        Globals.objMainFrame = this ;
        
        fillFrame();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        pMain = new javax.swing.JPanel();
        spMain = new javax.swing.JSplitPane();
        pView = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        pOptions = new javax.swing.JPanel();
        jbLoad = new javax.swing.JButton();
        jbSave = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tfAddress = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfPort = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfDeviceName = new javax.swing.JTextField();
        pfPassword = new javax.swing.JPasswordField();
        jSeparator2 = new javax.swing.JSeparator();
        jbConnect = new javax.swing.JButton();
        pDevice = new javax.swing.JPanel();
        jbEditUnit = new javax.swing.JButton();
        jbRemoveUnit = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtUnits = new javax.swing.JTable();
        jbAddUnit = new javax.swing.JButton();
        jbAddCon = new javax.swing.JButton();
        jbRemove = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlControllers = new javax.swing.JList();
        tfNewCon = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jbClear = new javax.swing.JButton();
        jbFillRandom = new javax.swing.JButton();
        jsTBChance = new javax.swing.JSlider();
        jlChance = new javax.swing.JLabel();
        pMenu = new javax.swing.JPanel();
        bSettings = new javax.swing.JButton();
        bThessis = new javax.swing.JButton();
        lLogo = new javax.swing.JLabel();
        lTEI = new javax.swing.JLabel();
        lCCRight = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        spMain.setDividerLocation(37);
        spMain.setDividerSize(0);
        spMain.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        pOptions.setBackground(new java.awt.Color(204, 204, 204));

        jbLoad.setText("Load");
        jbLoad.setToolTipText("");
        jbLoad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbLoadMouseClicked(evt);
            }
        });

        jbSave.setText("Save");
        jbSave.setToolTipText("");
        jbSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbSaveMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Address : ");

        tfAddress.setText("localhost");
        tfAddress.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Port :");
        jLabel2.setToolTipText("");

        tfPort.setText("50128");
        tfPort.setToolTipText("");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 0));
        jLabel4.setText("DeviceName :");
        jLabel4.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Password :");

        tfDeviceName.setText("korinthos");
        tfDeviceName.setToolTipText("");

        pfPassword.setText("korinthos");
        pfPassword.setToolTipText("");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jbConnect.setText("Connect!");
        jbConnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbConnectMouseClicked(evt);
            }
        });
        jbConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConnectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pOptionsLayout = new javax.swing.GroupLayout(pOptions);
        pOptions.setLayout(pOptionsLayout);
        pOptionsLayout.setHorizontalGroup(
            pOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbSave)
                    .addComponent(jbLoad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addGroup(pOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfDeviceName, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addComponent(jbConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pOptionsLayout.setVerticalGroup(
            pOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pOptionsLayout.createSequentialGroup()
                        .addGroup(pOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(tfAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(tfDeviceName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(pOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jbConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jSeparator2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pOptionsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbSave)
                .addGap(20, 20, 20)
                .addComponent(jbLoad)
                .addGap(20, 20, 20))
        );

        jbEditUnit.setText("View/Edit Unit");
        jbEditUnit.setToolTipText("");
        jbEditUnit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbEditUnitMouseClicked(evt);
            }
        });

        jbRemoveUnit.setText("Remove Unit");
        jbRemoveUnit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbRemoveUnitMouseClicked(evt);
            }
        });

        jtUnits.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtUnits.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jtUnits);

        jbAddUnit.setText("Add Unit");
        jbAddUnit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbAddUnitMouseClicked(evt);
            }
        });

        jbAddCon.setText("Add");
        jbAddCon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbAddConMouseClicked(evt);
            }
        });

        jbRemove.setText("Remove");
        jbRemove.setToolTipText("");
        jbRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbRemoveMouseClicked(evt);
            }
        });
        jbRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoveActionPerformed(evt);
            }
        });

        jlControllers.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jlControllers);

        tfNewCon.setToolTipText("");

        jLabel5.setText("New Controller :");
        jLabel5.setToolTipText("");

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 51, 0));
        jLabel6.setText("Units :");
        jLabel6.setToolTipText("");

        jbClear.setText("Clear All");
        jbClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbClearMouseClicked(evt);
            }
        });

        jbFillRandom.setBackground(new java.awt.Color(51, 255, 204));
        jbFillRandom.setText("Fill Random");
        jbFillRandom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbFillRandomMouseClicked(evt);
            }
        });

        jsTBChance.setMaximum(8);
        jsTBChance.setMinimum(1);
        jsTBChance.setToolTipText("");
        jsTBChance.setValue(3);
        jsTBChance.setMaximumSize(new java.awt.Dimension(32767, 20));
        jsTBChance.setMinimumSize(new java.awt.Dimension(36, 20));
        jsTBChance.setPreferredSize(new java.awt.Dimension(200, 20));
        jsTBChance.setRequestFocusEnabled(false);
        jsTBChance.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jsTBChanceStateChanged(evt);
            }
        });

        jlChance.setText("Chance : (1/3)");
        jlChance.setToolTipText("");

        javax.swing.GroupLayout pDeviceLayout = new javax.swing.GroupLayout(pDevice);
        pDevice.setLayout(pDeviceLayout);
        pDeviceLayout.setHorizontalGroup(
            pDeviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDeviceLayout.createSequentialGroup()
                .addGroup(pDeviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pDeviceLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pDeviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pDeviceLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jbRemove))
                            .addGroup(pDeviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pDeviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tfNewCon, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pDeviceLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jbAddCon)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pDeviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pDeviceLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addGroup(pDeviceLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbAddUnit)
                        .addGap(32, 32, 32)
                        .addComponent(jbEditUnit)
                        .addGap(18, 18, 18)
                        .addComponent(jbRemoveUnit)
                        .addGap(18, 18, 18)
                        .addComponent(jbClear)
                        .addGap(18, 18, 18)
                        .addComponent(jbFillRandom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlChance)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsTBChance, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pDeviceLayout.setVerticalGroup(
            pDeviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDeviceLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfNewCon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbAddCon)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbRemove)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pDeviceLayout.createSequentialGroup()
                .addGroup(pDeviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pDeviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbEditUnit)
                        .addComponent(jbRemoveUnit)
                        .addComponent(jbAddUnit)
                        .addComponent(jLabel6)
                        .addComponent(jbClear)
                        .addComponent(jbFillRandom)
                        .addComponent(jlChance))
                    .addComponent(jsTBChance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE))
            .addComponent(jSeparator3)
        );

        javax.swing.GroupLayout pViewLayout = new javax.swing.GroupLayout(pView);
        pView.setLayout(pViewLayout);
        pViewLayout.setHorizontalGroup(
            pViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pDevice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
        );
        pViewLayout.setVerticalGroup(
            pViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pViewLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pDevice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        spMain.setRightComponent(pView);

        bSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/DeviceClientV1/Images/settings24.png"))); // NOI18N
        bSettings.setText("Settings");
        bSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bSettingsMouseClicked(evt);
            }
        });

        bThessis.setBackground(new java.awt.Color(153, 51, 255));
        bThessis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/DeviceClientV1/Images/thesis24.png"))); // NOI18N
        bThessis.setText("Thesis");
        bThessis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bThessisMouseClicked(evt);
            }
        });

        lLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/DeviceClientV1/Images/logoPMGv2F48.png"))); // NOI18N
        lLogo.setToolTipText("");

        lTEI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/DeviceClientV1/Images/TEI36.png"))); // NOI18N
        lTEI.setName(""); // NOI18N

        lCCRight.setName(""); // NOI18N

        javax.swing.GroupLayout pMenuLayout = new javax.swing.GroupLayout(pMenu);
        pMenu.setLayout(pMenuLayout);
        pMenuLayout.setHorizontalGroup(
            pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bSettings)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bThessis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 691, Short.MAX_VALUE)
                .addComponent(lCCRight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lTEI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lLogo))
        );
        pMenuLayout.setVerticalGroup(
            pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMenuLayout.createSequentialGroup()
                .addGroup(pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bSettings)
                        .addComponent(bThessis))
                    .addGroup(pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lLogo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lTEI, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lCCRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        spMain.setLeftComponent(pMenu);

        javax.swing.GroupLayout pMainLayout = new javax.swing.GroupLayout(pMain);
        pMain.setLayout(pMainLayout);
        pMainLayout.setHorizontalGroup(
            pMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spMain)
        );
        pMainLayout.setVerticalGroup(
            pMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spMain)
        );

        getContentPane().add(pMain, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Μέθοδος που προετοιμάζει κατάλληλα την "λίστα" των ελεγκτών όπως ακόμη και
     * τον "πίνακα" των μονάδων που φαίνονται στη φόρμα μας ({@link Fmain}) .
     */
    private void fillFrame()
    {
        listModelOfControllers = new DefaultListModel();
        jlControllers.setModel(listModelOfControllers);
        
        tableModelOfUnits = new MyTableModel(null,Globals.ColumnNames) ;
        jtUnits.setModel(tableModelOfUnits);
    }
    /**
     * Μέθοδος που αναλαμβάνει να κάνει ομαδοποιημένα όλα τα απαραίτητα βήματα
     * που χρειάζονται για να δημιουργηθεί και να στηθεί η σύνδεση με το Server.
     * Δηλαδή στήνει αρχικά την σύνδεση με το server(απλό socket,certification,login) ,
     * γεμίζει-αρχικοποιεί κατάλληλα το εικονικό Device με βάση αυτά που έχει συμπληρώσει
     * ο χρήστης (Πίνακας Units) και στην συνέχεια σε συγχρονισμό με το Server τον ενημερώνει
     * κατάλληλα για όλα τα εικονικά Units που υπάρχουν στον πίνακα του παραθύρου μας .
     * Ακόμη εφόσον δεν έχει υπάρξει κάποιο σφάλμα δημιουργεί και ξεκινάει έναν
     * timer για να υπάρχει η δυνατότητα με βάση την πιθανότητα που έχει θέσει ο χρήστης
     * να γίνονται κάθε 2 δευτερόλεπτα εικονικές αλλαγές των μονάδων .
     * @return Επιστρέφει "OK" αν όλα πήγαν καλά ,δηλαδή όπως προβλέπονται ή "X" για οποιοδήποτε σφάλμα .
     * @throws IOException Μπορεί να προκληθεί κάποια εξαίρεση τέτοιου τύπου σε περίπτωση που συμβεί
     * κάποιο απρόβλεπτο σφάλμα στο διάβασμα από το socket που έχει στηθεί με το Server.
     */
    public String ConnectProcedure() throws IOException {
        
        ManageSocket ms = Tools.getFullManageSocket(this, tfDeviceName.getText(), pfPassword.getText(), tfAddress.getText(), tfPort.getText());
        if (ms==null)
            return "X" ;
        device = new Device(tfDeviceName.getText(),pfPassword.getText(),ms.ID,ms.ID + "@D") ;
        
        device.prepareDevice(Tools.convertTableOfDataToNewControllersParammeters(tableModelOfUnits,listModelOfControllers,ms.ID));
        Tools.Debug.print("Prepare Local Device success!");
        
        ArrayList<String> alNewControllerMessages = device.getNewControllerMessages() ;
        Tools.Debug.print(alNewControllerMessages.get(0));
        Tools.send(alNewControllerMessages.get(0), ms.out);
        manSocMess.reload(ms.in.readLine()) ;
        
        if (Tools.isCorrectCommand(manSocMess.getCommand(),"Answer",Globals.messageFailed,manSocMess.getMessage(),ms.out))
        {
            if ("FAILED".equals(manSocMess.getParameters().get(0).get(0)))
            {
                Tools.Debug.print(" InitControllers Failed!");
                Tools.send(Globals.messageFailed,ms.out);
                return "X";
            }
            else
                Tools.Debug.print("InitControllers OK!");
        }
        else
            return "X";
        
        for (int i = 1 ; i < alNewControllerMessages.size();i++)
        {
            Tools.send(alNewControllerMessages.get(i), ms.out);
            
            manSocMess.reload(ms.in.readLine()) ;
            
            if (Tools.isCorrectCommand(manSocMess.getCommand(),"Answer",Globals.messageFailed,manSocMess.getMessage(),ms.out))
            {
                if ("FAILED".equals(manSocMess.getParameters().get(0).get(0)))
                {
                    Tools.Debug.print("NewController Failed!");
                    Tools.send(Globals.messageFailed,ms.out);
                    return "X";
                }
                else
                    Tools.Debug.print("NewController OK!");
            }
            else
                return "X";
        }
        manSocMess.reload(ms.in.readLine()) ;
        
        if (Tools.isCorrectCommand(manSocMess.getCommand(),"InitializationFinished",Globals.messageFailed,manSocMess.getMessage(),ms.out))
        {
            if ("FAILED".equals(manSocMess.getParameters().get(0).get(0)))
            {
                Tools.Debug.print("InitializationFinished Failed!");
                Tools.send(Globals.messageFailed,ms.out);
                return "X";
            }
            else
            {
                Tools.Debug.print("InitializationFinished OK!");
                Tools.send("#"+ms.ID+"D@D"+"$Answer:1*(OK);",ms.out);
            }
        }
        else
            return "X";
        
        timer = new Timer();
        timer.schedule(new RemindTask(), 0,2000);
        
        manSocket = ms ;
        
        return "OK" ;
    }
    /**
     * Μέθοδος που εκτελείτε όταν πάει να κλείσει η φόρμα μας.
     * @param evt Το Default WindowEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        correctClose() ; 
    }//GEN-LAST:event_formWindowClosing
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Thesis" για να ανοίξει
     * η φόρμα με πληροφορίες που αφορούν την Πτυχιακή .
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
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
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Remove" για να
     * διαγράψει κάποιον ελεγκτή που έχει επιλέξει .
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jbRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRemoveMouseClicked
        if (!jbRemove.isEnabled())
            return ;
        int selInd = jlControllers.getSelectedIndex() ;
        if (selInd >= 0)
        {
            int dialogResult = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete Controller and all of the units ?",Globals.messDialTitle,JOptionPane.OK_CANCEL_OPTION);
            if(dialogResult == JOptionPane.OK_OPTION)
            {
                String name = (String)listModelOfControllers.remove(selInd);
                int counter = 0 ;
                while (counter<tableModelOfUnits.getRowCount())
                {
                    if (tableModelOfUnits.getValueAt(counter, 0).equals(name))
                        tableModelOfUnits.removeRow(counter);
                    else
                        counter++ ;
                }
            }
        }
        else
            JOptionPane.showMessageDialog(this,"No one selected controller!" +"                           ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jbRemoveMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Add" για να
     * προσθέσει κάποιον ελεγκτή που έχει γράψει στο πεδίο για "νέο ελεγκτή" .
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jbAddConMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbAddConMouseClicked
        if (!jbAddCon.isEnabled())
            return ;
        String newCon = tfNewCon.getText() ;
        
        if (newCon.trim().equals(""))
        {
            JOptionPane.showMessageDialog(this,"Controller name is empty" +"                           ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
            return ;
        }
        
        for (int i = 0 ;i<listModelOfControllers.getSize();i++)
        {
            if (newCon.equals(listModelOfControllers.getElementAt(i)))
            {
                JOptionPane.showMessageDialog(this,"There is another Controller of the same name!" +"                           ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
                return ;
            }
        }
        listModelOfControllers.addElement(newCon) ;
    }//GEN-LAST:event_jbAddConMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Add Unit" για να
     * προσθέσει μια νέα μονάδα.
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jbAddUnitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbAddUnitMouseClicked
        if (!jbAddUnit.isEnabled())
            return ;
        if (listModelOfControllers.getSize()>0)
        {
            DnewUnit newUnit = new DnewUnit(this,true) ;
            newUnit.setVisible(true);
        }
        else
            JOptionPane.showMessageDialog(this,"There is no Controller!" +"                           ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jbAddUnitMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "View/Edit Unit" για να
     * αλλάξει ή να δει(με καλύτερο τρόπο) μια μονάδα που ήδη υπάρχει στον πίνακα .
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jbEditUnitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEditUnitMouseClicked
        if (!jbEditUnit.isEnabled())
            return ;
        
        int selInd = jtUnits.getSelectedRow() ;
        if (selInd>=0)
        {
            DnewUnit newUnit = new DnewUnit(this,true,false) ;
            newUnit.setVisible(true);
            //tableModelOfUnits.removeRow(selInd) ;
        }
        else
            JOptionPane.showMessageDialog(this,"No one selected unit!" +"                           ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jbEditUnitMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Remove Unit" για να
     * διαγράψει από τον πίνακα μια μονάδα.
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jbRemoveUnitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRemoveUnitMouseClicked
        if (!jbRemoveUnit.isEnabled())
            return ;
        
        int selInd = jtUnits.getSelectedRow() ;
        if (selInd>=0)
        {
            int dialogResult = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete Unit?",Globals.messDialTitle,JOptionPane.OK_CANCEL_OPTION);
            if(dialogResult == JOptionPane.OK_OPTION)
                tableModelOfUnits.removeRow(selInd) ;
        }
        else
            JOptionPane.showMessageDialog(this,"No one selected unit!" +"                           ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jbRemoveUnitMouseClicked
    
    private void jbRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoveActionPerformed
        
    }//GEN-LAST:event_jbRemoveActionPerformed
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Clear All" για να
     * διαγράψει όλες τις μονάδες από τον πίνακα.
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jbClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbClearMouseClicked
        if (!jbClear.isEnabled())
            return ;
        
        if (tableModelOfUnits.getRowCount()>0)
        {
            int dialogResult = JOptionPane.showConfirmDialog(this,"Are you sure you want to clear all Units?",Globals.messDialTitle,JOptionPane.OK_CANCEL_OPTION);
            if(dialogResult == JOptionPane.OK_OPTION)
            {
                listModelOfControllers.clear();
                if (tableModelOfUnits.getRowCount() > 0) {
                    for (int i = tableModelOfUnits.getRowCount() - 1; i > -1; i--) {
                        tableModelOfUnits.removeRow(i);
                    }
                }
            }
        }
        else
            JOptionPane.showMessageDialog(this,"There are no Units!" +"                           ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jbClearMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Fill Random" για να
     * δημιουργηθούν τυχαία εικονικές μονάδες και να συμπληρωθεί αυτόματα ο πίνακας
     * των μονάδων όπως επίσης και η λίστα με τους ελεγκτές .
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jbFillRandomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbFillRandomMouseClicked
        if (!jbFillRandom.isEnabled())
            return ;
        
        int dialogResult = JOptionPane.showConfirmDialog(this,"Are you sure you want to fill all Units with random values?",Globals.messDialTitle,JOptionPane.OK_CANCEL_OPTION);
        if(dialogResult == JOptionPane.OK_OPTION)
        {
            String[] tmpControllers = Tools.getRandomControllers() ;
            String[][] tmpTable = Tools.getRandomData(tmpControllers);
            
            tableModelOfUnits.setDataVector(tmpTable, Globals.ColumnNames);
            
            listModelOfControllers.clear();
            for (String str : tmpControllers)
                listModelOfControllers.addElement(str);
        }
    }//GEN-LAST:event_jbFillRandomMouseClicked
    
    JFileChooser fcSave ;
    boolean firstTimeSaveDialog = true ;
    /**
     * Μέθοδος που προετοιμάζει το DialogSave και εκτελείτε μόνο την πρώτη φορά
     * που ο χρηστής θα πατήσει το κουμπί για να αποθηκεύσει όλα τα δεδομένα της
     * φόρμας σε ένα xml .
     */
    private void prepareDialogSave()
    {
        fcSave = new JFileChooser();
        fcSave.setSize(800, 600);
        fcSave.setSize(new Dimension(800,600));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "xml files (*.xml)", "xml");
        
        fcSave.setDialogTitle("Save schedule file | " +Globals.messDialTitle);
        fcSave.setFileFilter(filter);
        
        fcSave.setCurrentDirectory(new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Devices"+Globals.fSep));
    }
    
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Save" για να
     * αποθηκεύσει όλα τα δεδομένα της φόρμας σε ένα xml .
     * Αρχικά ανοίγει ένα Save Dialog για να καθορίσει ο χρήστης το όνομα και το μονοπάτι
     * του αρχείου xml που θα δημιουργηθεί και στην συνέχεια γίνονται οι κατάλληλες
     * διαδικασίες για να αποθηκευτούν στο αρχείο με σωστή μορφή και σύνταξη xml
     * όλες αυτές οι πληροφορίες της φόρμας .
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jbSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbSaveMouseClicked
        if (!jbSave.isEnabled())
            return ;
        if (firstTimeSaveDialog)
        {
            prepareDialogSave() ;
            firstTimeSaveDialog = false ;
        }
        File f ;
        f = new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Devices"+Globals.fSep +tfDeviceName.getText()+".xml") ;
        fcSave.setSelectedFile(f);
        
        int returnVal = fcSave.showSaveDialog(Globals.objMainFrame);
        if (returnVal==0)
        {
            
            if (fcSave.getSelectedFile().getName().endsWith(".xml"))
                f = new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Devices"+Globals.fSep +fcSave.getSelectedFile().getName()) ;
            else
                f = new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Devices"+Globals.fSep +fcSave.getSelectedFile().getName()+".xml") ;
            
            if (f.exists())
            {
                int dialogResult = JOptionPane.showConfirmDialog(this,"The file exists! Are you sure you want to overalp?" +"        ",Globals.messDialTitle,JOptionPane.OK_CANCEL_OPTION);
                if(dialogResult != JOptionPane.OK_OPTION)
                    return ;
            }
            
            if (saveToXML(f))
                JOptionPane.showMessageDialog(this,"Saved File Success!" +"                              ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(this,"Error : Failed save File!" +"                           ",Globals.messDialTitle,JOptionPane.WARNING_MESSAGE);
        }        
    }//GEN-LAST:event_jbSaveMouseClicked
    
    JFileChooser fcOpen ;
    boolean firstTimeOpenDialog = true ;
    /**
     * Μέθοδος που προετοιμάζει το DialogOpen και εκτελείτε μόνο την πρώτη φορά
     * που ο χρηστής θα πατήσει το κουμπί για να φορτώσει τα δεδομένα ενός αρχείου
     * xml με κατάλληλο περιεχόμενο (Θα το έχει φτιάξει ο ίδιος προσομοιωτής)
     * ώστε να συμπληρωθεί αυτόματα η φόρμα με όλες τις πληροφορίες του αρχείου.
     */
    private void prepareDialogOpen()
    {
        fcOpen = new JFileChooser();
        fcOpen.setSize(800, 600);
        fcOpen.setSize(new Dimension(800,600));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "xml files (*.xml)", "xml");
        
        fcOpen.setDialogTitle("Open schedule file |" + Globals.messDialTitle );
        fcOpen.setFileFilter(filter);
        fcOpen.setCurrentDirectory(new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Devices"+Globals.fSep));
    }
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Load" για να φορτώσει
     * τα δεδομένα ενός αρχείου xml με κατάλληλο περιεχόμενο (Θα το έχει φτιάξει
     * ο ίδιος προσομοιωτής) ώστε να συμπληρωθεί αυτόματα η φόρμα με όλες
     * τις πληροφορίες του αρχείου.
     * Αρχικά ανοίγει ένα Open Dialog για να καθορίσει ο χρήστης το όνομα και το μονοπάτι
     * του αρχείου xml που θα φορτωθεί και στην συνέχεια γίνονται οι κατάλληλες
     * διαδικασίες για να ανακτηθούν από το αρχείο xml όλες οι πληροφορίες
     * για να αυτοσυμπληρωθεί το περιεχόμενο της φόρμας κατάλληλα .
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jbLoadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbLoadMouseClicked
        if (!jbLoad.isEnabled())
            return ;
        if (firstTimeOpenDialog)
        {
            prepareDialogOpen() ;
            firstTimeOpenDialog = false ;
        }
        
        int returnVal = fcOpen.showOpenDialog(Globals.objMainFrame);
        if (returnVal==0)
        {
            File f ;
            if (fcOpen.getSelectedFile().getName().endsWith(".xml"))
                f = new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Devices"+Globals.fSep +fcOpen.getSelectedFile().getName()) ;
            else
                f = new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"Devices"+Globals.fSep +fcOpen.getSelectedFile().getName()+".xml") ;
            
            if (f.exists())
            {
                if (readXML(f))
                    JOptionPane.showMessageDialog(this,"Open File Success!" +"                              ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(this,"Error : Failed open File!" +"                           ",Globals.messDialTitle,JOptionPane.WARNING_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(this,"Failed : File not Exists!" +"                           ",Globals.messDialTitle,JOptionPane.WARNING_MESSAGE);
        }    
    }//GEN-LAST:event_jbLoadMouseClicked
    
    /**
     * Μέθοδος για να μπορούμε πολύ εύκολα από διάφορα σημεία του κώδικα του
     * προγράμματος να κάνουμε ενεργά ή ανενεργά κάποια στοιχεία της φόρμας.
     * Η χρήση του στην πράξη είναι όταν θέλουμε να "κλειδώσουμε" κάποιες
     * λειτουργίες που δεν θέλουμε να κάνει ο χρήστης όταν η εικονική συσκευή
     * μας είναι συνδεμένη με τον Server .
     * Και βέβαια αντίστοιχα να "ξεκλειδώνουμε" τις ίδιες λειτουργίες όταν o
     * Simulator μας δεν είναι συνδεδεμένος με τον Server ούτως ώστε να μπορεί
     * ο χρήστης να προσαρμόζει όπως θέλει την εικονική του συσκευή.
     * @param lock Παράμετρος που αν είναι False κλειδώνει τις λειτουργίες που
     * δεν πρέπει να πράττει ο χρήστης και όταν είναι True ξεκλειδώνει τις ίδιες λειτουργίες.
     */
    private void unlockedView(boolean lock)
    {
        tfDeviceName.setEnabled(lock);
        tfAddress.setEnabled(lock);
        tfPort.setEnabled(lock);
        pfPassword.setEnabled(lock);
        jbAddCon.setEnabled(lock);
        jbAddUnit.setEnabled(lock);
        jbClear.setEnabled(lock);
        jbEditUnit.setEnabled(lock);
        jbFillRandom.setEnabled(lock);
        jbLoad.setEnabled(lock);
        jbRemove.setEnabled(lock);
        jbRemoveUnit.setEnabled(lock);
        jbSave.setEnabled(lock);
        tfNewCon.setEnabled(lock);
        jlControllers.setEnabled(lock);
        //jsTBChance.setEnabled(lock);
        //jtUnits.setEnabled(lock);
    }
    
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί "Connect! - Connected"
     * για να γίνουν όλα αυτά που πρέπει ανάλογα αν είμαστε συνδεδεμένοι στον
     * Server ή όχι . Για παράδειγμα αν δεν είμαστε συνδεδεμένοι στο Server γίνονται
     * όλα τα απαραίτητα βήματα για να πραγματοποιηθεί η σύνδεση και αντίστοιχα αν είμαστε
     * συνδεδεμένοι στο Server γίνεται η διαδικασία για να κλείσει σωστά η σύνδεση.
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jbConnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbConnectMouseClicked
        if (!connected)
        {
            if (tableModelOfUnits.getRowCount()==0)
            {
                JOptionPane.showMessageDialog(this,"There is no Unit!" +"                           ",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
                return ;
            }
            try {
                if (ConnectProcedure().equals("OK"))
                {
                    myThread = new ListenThread(manSocket,this);
                    myThread.start();
                    jbConnect.setText("Connected");
                    jbConnect.setBackground(Color.green);
                    connected = !connected ;
                }
                else
                {
                    Tools.Debug.print("Connect UnsuccessFully!");
                }
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,"Couldn't get I/O for the connection to: " + tfAddress.getText() + "host","Demo Ptyxiaki!",JOptionPane.PLAIN_MESSAGE);
            }
            catch (Exception e) {
                //e.printStackTrace();
                JOptionPane.showMessageDialog(this,"Couldn't get I/O for the connection to: " + tfAddress.getText() + "host - Port error","Demo Ptyxiaki!",JOptionPane.PLAIN_MESSAGE);
            }
        }
        else
        {
            correctClose() ;
        }
        unlockedView(!connected) ;        
    }//GEN-LAST:event_jbConnectMouseClicked
    
    
    private void jbConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbConnectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbConnectActionPerformed
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης αλλάξει την κατάσταση-τιμή της μπάρας
     * που αφορά την πιθανότητα (chance) να προκληθούν εικονικές αλλαγές .
     * @param evt Το Default ChangeEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void jsTBChanceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jsTBChanceStateChanged
        jlChance.setText("Chance : (1:"+jsTBChance.getValue()+")");
    }//GEN-LAST:event_jsTBChanceStateChanged
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί ρυθμίσεων που είναι
     * στο μενού.Το κουμπί ουσιαστικά δεν κάνει τίποτα - είναι για πιθανόν μελλοντική χρήση.
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */    
    private void bSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSettingsMouseClicked
        JOptionPane.showMessageDialog(this,"             Δεν χρησιμοποιείται!\nΥπάρχει για πιθανόν μελλοντική χρήση.",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_bSettingsMouseClicked
    
    /**
     * Μέθοδος που αναλαμβάνει να αποθηκεύσει όλες τις απαραίτητες πληροφορίες
     * της φόρμας σε ένα αρχείο xml .
     * Σκοπός της είναι να μπορεί κάποια άλλη χρονική στιγμή ο Simulator να
     * ανακτήσει όλες αυτές τις πληροφορίες και να συμπληρωθεί αυτόματα
     * το περιεχόμενο της φόρμας με αυτές .
     * @param f Το αρχείο στο οποίο θα αποθηκευτούν με μορφή και σύνταξη xml
     * όλα τα στοιχεία της φόρμας .
     * @return True αν όλα πήγαν καλά και αποθηκεύτηκε το αρχείο και False
     * αν κάτι δεν πήγε καλά .
     */
    private boolean saveToXML(File f) {
        Document dom;
        Element Units,Controllers,unit,controller,attribute,settings;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            dom = db.newDocument();
            
            Element rootEle = dom.createElement(tfDeviceName.getText());
            
            settings = dom.createElement("Settings");
            
            unit = dom.createElement("Username");
            unit.appendChild(dom.createTextNode(tfDeviceName.getText()));
            settings.appendChild(unit);
            unit = dom.createElement("Password");
            unit.appendChild(dom.createTextNode(pfPassword.getText()));
            settings.appendChild(unit);
            unit = dom.createElement("Address");
            unit.appendChild(dom.createTextNode(tfAddress.getText()));
            settings.appendChild(unit);
            unit = dom.createElement("Port");
            unit.appendChild(dom.createTextNode(tfPort.getText()));
            settings.appendChild(unit);
            unit = dom.createElement("SumOfControllers");
            unit.appendChild(dom.createTextNode(String.valueOf(listModelOfControllers.getSize())));
            settings.appendChild(unit);
            unit = dom.createElement("SumOfUnits");
            unit.appendChild(dom.createTextNode(String.valueOf(tableModelOfUnits.getRowCount())));
            settings.appendChild(unit);
            
            rootEle.appendChild(settings);
            
            Controllers = dom.createElement("Controllers");
            
            for (int i = 0;i<listModelOfControllers.getSize();i++)
            {
                controller = dom.createElement("Controller");
                controller.appendChild(dom.createTextNode((String) listModelOfControllers.get(i)));
                Controllers.appendChild(controller);
            }
            
            rootEle.appendChild(Controllers);
            
            
            Units = dom.createElement("Units");
            
            for (int i = 0;i<tableModelOfUnits.getRowCount();i++)
            {
                unit = dom.createElement("Unit");
                attribute = dom.createElement("ConName");
                attribute.appendChild(dom.createTextNode((String) tableModelOfUnits.getValueAt(i, 0)));
                unit.appendChild(attribute);
                
                
                attribute = dom.createElement("UnitName");
                attribute.appendChild(dom.createTextNode((String) tableModelOfUnits.getValueAt(i, 1)));
                unit.appendChild(attribute);
                
                
                attribute = dom.createElement("Type");
                attribute.appendChild(dom.createTextNode((String) tableModelOfUnits.getValueAt(i, 2)));
                unit.appendChild(attribute);
                
                
                attribute = dom.createElement("Mode");
                attribute.appendChild(dom.createTextNode((String) tableModelOfUnits.getValueAt(i, 3)));
                unit.appendChild(attribute);
                
                
                attribute = dom.createElement("Tag");
                attribute.appendChild(dom.createTextNode((String) tableModelOfUnits.getValueAt(i, 4)));
                unit.appendChild(attribute);
                
                
                attribute = dom.createElement("Value");
                attribute.appendChild(dom.createTextNode((String) tableModelOfUnits.getValueAt(i, 5)));
                unit.appendChild(attribute);
                
                
                attribute = dom.createElement("Max");
                attribute.appendChild(dom.createTextNode((String) tableModelOfUnits.getValueAt(i, 6)));
                unit.appendChild(attribute);
                
                
                attribute = dom.createElement("Min");
                attribute.appendChild(dom.createTextNode((String) tableModelOfUnits.getValueAt(i, 7)));
                unit.appendChild(attribute);
                
                
                attribute = dom.createElement("Limit");
                attribute.appendChild(dom.createTextNode((String) tableModelOfUnits.getValueAt(i, 8)));
                unit.appendChild(attribute);
                
                Units.appendChild(unit);
            }
            
            rootEle.appendChild(Units);
            
            
            dom.appendChild(rootEle);
            
            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                
                tr.transform(new DOMSource(dom),new StreamResult(new FileOutputStream(f)));
                
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
     * Μέθοδος που αναλαμβάνει να διαβάσει όλες τις απαραίτητες πληροφορίες
     * από ένα αρχείο xml (Της κατάλληλης μορφής του Simulator μας βέβαια) .
     * Σκοπός της είναι να αξιοποιηθούν όλα αυτά τα δεδομένα , δηλαδή σε επόμενη
     * φάση να μπορεί να συμπληρωθεί αυτόματα το περιεχόμενο της φόρμας μας .
     * @param f Το αρχείο(με μορφή και σύνταξη xml) από το οποίο θα ανακτηθούν
     * όλα τα στοιχεία για να συμπληρωθεί κατάλληλα σε επόμενο χρόνο η φόρμα μας .
     * @return True αν όλα πήγαν καλά και ανακτήθηκε σωστά το αρχείο και False
     * αν κάτι δεν πήγε καλά .
     */
    private boolean readXML(File f) {
        Document dom;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            dom = db.parse(f);
            
            Element doc = dom.getDocumentElement();
            
            tfDeviceName.setText(getTextValue(doc, "Username", 0)) ;
            pfPassword.setText(getTextValue(doc, "Password", 0)) ;
            tfAddress.setText(getTextValue(doc, "Address", 0)) ;
            tfPort.setText(getTextValue(doc, "Port", 0)) ;
            
            
            int sumOfControllers = Integer.parseInt(getTextValue(doc, "SumOfControllers", 0)) ;
            int sumOfUnits = Integer.parseInt(getTextValue(doc, "SumOfUnits", 0)) ;
            
            listModelOfControllers.clear();
            for (int i = 0;i<sumOfControllers;i++)
                listModelOfControllers.addElement(getTextValue(doc, "Controller", i));
            
            
            Vector<String> vItems;
            if (tableModelOfUnits.getRowCount() > 0) {
                for (int i = tableModelOfUnits.getRowCount() - 1; i > -1; i--) {
                    tableModelOfUnits.removeRow(i);
                }
            }
            
            for (int i = 0 ;i<sumOfUnits;i++)
            {
                vItems = new Vector() ;
                vItems.add(getTextValue(doc, "ConName", i)) ;
                vItems.add(getTextValue(doc, "UnitName", i)) ;
                vItems.add(getTextValue(doc, "Type", i)) ;
                vItems.add(getTextValue(doc, "Mode", i)) ;
                vItems.add(getTextValue(doc, "Tag", i)) ;
                vItems.add(getTextValue(doc, "Value", i)) ;
                vItems.add(getTextValue(doc, "Max", i)) ;
                vItems.add(getTextValue(doc, "Min", i)) ;
                vItems.add(getTextValue(doc, "Limit", i)) ;
                tableModelOfUnits.addRow(vItems);
            }
            
            
        } catch (ParserConfigurationException | SAXException pce) {
            Tools.Debug.print(pce.getMessage());
            return false;
        } catch (IOException | NumberFormatException ioe) {
            Tools.Debug.print(ioe.getMessage());
            return false ;
        }
        return true;
    }
    /**
     * Μέθοδος που αναλαμβάνει να κλείσει σωστά την σύνδεση του Simulator μας
     * με τον Server .
     * Δηλαδή εκτός των άλλων κλείνει το socket , σταματάει το thread που διαβάζει συνεχώς από το Server ,
     * διακόπτει τον timer κ.α .
     */
    public void correctClose()
    {
        try {
            Tools.Debug.print("Closing Connection..");
            if (manSocket!=null)
            {
                if (manSocket.out!=null){
                    Tools.send("#"+manSocket.ID+"@D$Quit:1*(Bey-bey!);", manSocket.out);
                }
                manSocket.close() ;
            }
            if (myThread!=null)
            {
                myThread.terminate();
                myThread.interrupt() ;
                myThread = null ;
            }
            if (timer!=null)
            {
                timer.purge() ;
                timer.cancel();
                timer = null ;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Fmain.class.getName()).log(Level.SEVERE, null, ex);
        }
        jbConnect.setText("Connect!");
        jbConnect.setBackground( UIManager.getColor ( "Panel.background" ));
        
        connected = false ;
        Tools.Debug.print("Closed Connection!");
        unlockedView(true) ;
    }
    ManageSocketMessage reloadDeviceMsm = new ManageSocketMessage();
    
    /**
     * Μέθοδος που με βάση ένα μήνυμα πρωτοκόλλου (ChangeValues ή ChangeModes)
     * ενημερώνει κατάλληλα σε πραγματικό χρόνο και τον πίνακα των Units
     * που έχει η φόρμας μας ({@link Fmain})
     * @param message Συμβολοσειρά που έχει ένα μήνυμα πρωτοκόλλου για επικοινωνία μέσα από socket.
     * Συγκεκριμένα ένα μήνυμα "ChangeModes" ή "ChangeValues" .
     */
    public void reloadDevice(String message)
    {
        reloadDeviceMsm.reload(message);
        for (ArrayList<String> alParam : reloadDeviceMsm.getParameters())
        {
            String conName = alParam.get(0) ;
            String unitName = alParam.get(1) ;
            String value = alParam.get(2) ;
            
            for(int i = 0 ; i < tableModelOfUnits.getRowCount();i++)
            {
                if (tableModelOfUnits.getValueAt(i, 0).equals(conName) && tableModelOfUnits.getValueAt(i, 1).equals(unitName))
                    if (reloadDeviceMsm.getCommand().equals("ChangeModes"))
                        tableModelOfUnits.setValueAt(value, i, 3);
                    else
                        tableModelOfUnits.setValueAt(value, i, 5);
            }
        }
        //tableModelOfUnits.setDataVector(device.getRowData(), Globals.ColumnNames);
    }
    
    /**
     * Κλασική static main μέθοδος από την οποία ξεκινάει η εκτέλεση του Simulator Device Client.
     * Πρακτικά δηλαδή δημιουργεί και εμφανίζει την κλάση {@link Fmain}.
     * @param args Default παράμετροι που δεν χρησιμοποιούνται και που μπορεί δυνητικά να πάρει
     * το πρόγραμμα μας στην περίπτωση που εκτελεστεί από περιβάλλον γραμμής εντολών .
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Fmain frm;
                frm = new Fmain();
                //frm.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frm.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bSettings;
    public javax.swing.JButton bThessis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JButton jbAddCon;
    private javax.swing.JButton jbAddUnit;
    private javax.swing.JButton jbClear;
    public javax.swing.JButton jbConnect;
    private javax.swing.JButton jbEditUnit;
    private javax.swing.JButton jbFillRandom;
    private javax.swing.JButton jbLoad;
    private javax.swing.JButton jbRemove;
    private javax.swing.JButton jbRemoveUnit;
    private javax.swing.JButton jbSave;
    private javax.swing.JLabel jlChance;
    private javax.swing.JList jlControllers;
    private javax.swing.JSlider jsTBChance;
    public javax.swing.JTable jtUnits;
    private javax.swing.JLabel lCCRight;
    private javax.swing.JLabel lLogo;
    private javax.swing.JLabel lTEI;
    private javax.swing.JPanel pDevice;
    private javax.swing.JPanel pMain;
    private javax.swing.JPanel pMenu;
    private javax.swing.JPanel pOptions;
    private javax.swing.JPanel pView;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JSplitPane spMain;
    private javax.swing.JTextField tfAddress;
    private javax.swing.JTextField tfDeviceName;
    private javax.swing.JTextField tfNewCon;
    private javax.swing.JTextField tfPort;
    // End of variables declaration//GEN-END:variables
    /**
     * Δικό μας custom TableModel που έχει κληρονομήσει το DefaultTableModel.
     * Ουσιαστικά φτιάχτηκε για να μην μπορεί ο χρήστης να επεξεργαστεί-αλλάξει
     * τα περιεχόμενα των κελιών του πίνακα .
     */
    public class MyTableModel extends DefaultTableModel {
        MyTableModel(Object[][] data,Object[] col)
        {
            super(data,col) ;
        }
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
        
    }
    static Timer timer ;
    /**
     * Κλάση που έχει κληρονομήσει την TimerTask ·
     * Σκοπός της είναι να εκτελείτε παράλληλα με την ροή του προγράμματος και να
     * δημιουργεί τυχαίες εικονικές αλλαγές των μονάδων της εικονικής συσκευής μας
     * εφόσον βέβαια έχει ανατεθεί η συγκεκριμένη "εργασία" σε έναν timer .
     * Πιο πρακτικά με βάση ένα πιθανόν τυχαίο μήνυμα που δημιουργείτε για αλλαγή τιμών
     * σε κάποιες μονάδες , ενημερώνεται κατάλληλα το τοπικό εικονικό Device μας όπως
     * και ο πίνακας των μονάδων της φόρμας μας και στην συνέχεια στέλνετε το παραπάνω
     * μήνυμα πρωτοκόλλου στον Server για να ενημερωθούν όλοι οι Online Users που
     * εποπτεύουν "ζωντανά" την εικονική συσκευή .
     */
    class RemindTask extends TimerTask {
        @Override
        public void run() {
            if (connected)
            {
                if (manSocket.clientSocket.isConnected())
                {
                    String message  = Tools.getNewRandomDataForDevice(device,jsTBChance.getValue());
                    if (!message.equals("X"))
                    {
                        device.setValuesOfDevice(message);
                        reloadDevice(message);
                        Tools.send(message, manSocket.out);
                        Tools.Debug.print(message);
                    }
                    
                }
                
            }
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