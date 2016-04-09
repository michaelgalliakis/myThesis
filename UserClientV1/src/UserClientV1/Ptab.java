package UserClientV1;

import java.awt.Dimension;
import java.awt.Insets;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import java.io.File;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import static UserClientV1.ManageUserClient.expandAll;

/**
 * Κλάση που έχει κληρονομήσει ένα JPanel και που ουσιαστικά είναι μια καρτέλα εποπτείας
 * κάποιας συσκευής μέσα στο κύριο παράθυρο του User Client ({@link Fmain}).
 * Μέσα από αυτό το panel μπορεί ο χρήστης να βλέπει τις καταστάσεις των μονάδων είτε
 * μέσα από το δέντρο(JTree) που είναι αριστερά είτε με τα εικονίδια({@link UnitControl}) που
 * βρίσκονται δεξιά .
 * Επίσης με τη κλάση αυτή (καρτέλα) μπορεί ο χρήστης να κάνει και άλλες 
 * λειτουργίες όπως μα σταματάει ή να ξεκινάει την εποπτεία , να αλλάζει εικόνα στο
 * background , να επαναφέρει τα εικονίδια των μονάδων στη περίπτωση που μετά από ένα 
 * resize δεν φαίνονται , να επιλέγει αν θα αποθηκεύονται οι επιλογές του χρήστη 
 * στον υπολογιστή για να μπορούν να ανακτηθούν όταν κλείσει και ξανά ανοίξει το 
 * πρόγραμμα και τέλος ο χρήστης μπορεί να κλείσει την καρτέλα.
 * Ακόμη όμως κάποιος χρήστης μέσω της κλάσης αυτής μπορεί να βλέπει διάφορα μηνύματα ροής
 * μέσα από τη κονσόλα όπως επίσης και να βλέπει ένα διακριτικό κλειδί ανάλογα με τα δικαιώματα
 * που έχει πάνω στη συγκεκριμένη συσκευή που κάνει εκείνη την ώρα εποπτεία , καθώς επιπλέον
 * μπορεί να ενημερώνεται με κάποιο μήνυμα και ένα εικονίδιο κατάστασης στο κάτω μέρος αριστερά.
 * @author Michael Galliakis
 */
public class Ptab extends javax.swing.JPanel {
    public Device device ;
    public String devID ;
    public String devName ;
    public String devAccess ;
    
    public boolean isConnected ;
    JFileChooser fc ;
    boolean firstTimeChangeImage = true ;
    /**
     * Κατασκευαστής της κλάσης μας που προετοιμάζει κατάλληλα την καρτέλα μας.
     * @param devID Το ID της συσκευής για την οποία θα γίνεται εποπτεία .
     * @param devName Το όνομα της συσκευής για την οποία θα γίνεται εποπτεία .
     * @param devAccess Τα δικαιώματα που έχει ο χρήστης πάνω στη συσκευή η οποία θα εποπτεύεται.
     */
    public Ptab(String devID,String devName,String devAccess) {
        initComponents();
        this.devID = devID;
        this.devName  = devName;
        this.devAccess  = devAccess;
        bSaveSettings.setBackground(Globals.selectedColor);
        //System.out.println(spTreeAndStatus.getSize().height - 45);
        //spTreeAndStatus.setDividerLocation(spTreeAndStatus.getSize().height - 45) ;
    }
    /**
     * Μέθοδος που αναλαμβάνει να κάνει όλα τα απαραίτητα βήματα που χρειάζονται
     * για να ξεκινήσει η εποπτεία μιας συσκευής .
     */
    public void startMonitoring()
    {
        Device locDevice ;
        try {
            ManageSocket ms = ManageUserClient.connectProcess(Globals.address, Globals.port);
            
            if (ms==null)
            {
                Tools.Debug.print("Connect UnsuccessFully (Connect Failed)!");
                JOptionPane.showMessageDialog(this,"Connect Failed","Failed",JOptionPane.PLAIN_MESSAGE);
                //Globals.objDevices.setEnabled(false);
                return ;
            }
            //Globals.objDevices.setEnabled(false);
            if (!ManageUserClient.certificationProcess(ms))
            {
                Tools.Debug.print("Connect UnsuccessFully (Certification Failed)!");
                JOptionPane.showMessageDialog(this,"Certification Failed","Failed",JOptionPane.PLAIN_MESSAGE);
                
                return ;
            }
            
            String DBUserID = ManageUserClient.loginProcess(ms, Globals.username, Globals.password);
            
            if (DBUserID.equals("X"))
            {
                Tools.Debug.print("Connect UnsuccessFully (Login Failed)!");
                JOptionPane.showMessageDialog(this,"Login Failed","Failed",JOptionPane.PLAIN_MESSAGE);
                
                return ;
            }
            
            locDevice = ManageUserClient.bringMeDeviceProcess(ms, DBUserID,devID,devName,devAccess) ;
            
            if (locDevice==null)
            {
                Tools.Debug.print("InitControllers Fail!");
                Tools.send("#"+DBUserID+"UD"+"@UD$Quit:1*(Bey-bey!);",ms.out);
                Tools.Debug.print("bringMeDeviceProcess UnsuccessFully!");
                JOptionPane.showMessageDialog(this,"bring Me Device Process Failed","Failed",JOptionPane.PLAIN_MESSAGE);
                return ;
            }
            device = locDevice ;
            
            loadaccessIcon(device.devAccess) ;
            
            Globals.hmDevices.put(devName,device)  ;
            
            JTree tree = device.getTree(true) ;
            this.scpDevTree.getViewport().removeAll();
            this.scpDevTree.getViewport().add(tree) ;
            expandAll(tree) ;
            Tools.Debug.print("Connect SuccessFully!");
            BackgroundPanel bp = new BackgroundPanel(device) ;
            bp.setLayout(null);
            this.scpScreen.getViewport().removeAll();
            this.scpScreen.getViewport().add(bp) ;
            bp.setVisible(true);
            
            Insets insets = bp.getInsets();
            
            for (UnitControl uc : device.getAllControls())
            {
                uc.setVisible(true);
                bp.add(uc) ;
            }
            device.fillUnitControls(bp,insets) ;
            device.setPTab(this) ;
            device.clearSelection();
            device.setLTForRead(new ListenThread(device,ms.clientSocket,ms.in,this)); //Logika tha fygei i teleytaia parametros!
            device.ltForRead.start() ;
            
            JTextPane tmpTextPane = new JTextPane() ;
            tmpTextPane.setStyledDocument(Globals.debugDoc);
            tmpTextPane.setEditable(false);
            
            
            tmpTextPane.setMinimumSize(new Dimension (100,100));
            tmpTextPane.setMaximumSize(new Dimension (100,100));
            tmpTextPane.setSize(new Dimension (100,100));
            
            this.scpConsole.getViewport().removeAll();
            this.scpConsole.getViewport().add(tmpTextPane) ;
            this.scpConsole.setVisible(Globals.objMainFrame.isConsoleReportEnable);
            setIsConnected(true) ;
            setStatus("Start success!",0) ;
            
        } catch (Exception ex) {
            // Logger.getLogger(Ptab.class.getName()).log(Level.SEVERE, null, ex);
            setStatus("Start failed!",2) ;
            JOptionPane.showMessageDialog(this,"Δεν μπόρεσε να ξεκινήσει το Monitoring",Globals.messDialTitle,JOptionPane.PLAIN_MESSAGE);
        }
    }
    /**
     * Μέθοδος που απλά εμφανίζει ένα μήνυμα κατάστασης στην κάτω μεριά της καρτέλας
     * όπως επίσης και καθορίζει και το εικονίδιο κατάστασης .
     * @param message Ένα μήυνμα κατάστασης που εμφανίζεται .
     * @param optIcon Επιλογή για το ποια εικόνα θα εμφανιστεί σαν εικονίδιο κατάστασης.
     * Είναι Ο αν θέλουμε την εικόνα με το "ΟΚ" , 1 αν θέλουμε την εικόνα με το "Disconnect"
     * και 2 αν θέλουμε την εικόνα με το "Oops".
     */
    public void setStatus(String message,int optIcon)
    {
        Globals.today = Calendar.getInstance().getTime();
        String reportDate = Globals.dateFormat.format(Globals.today);
        lStatusText.setText("<html>" + message +":<br>" + reportDate +"</html>");
        switch (optIcon){
            case 0 : //OK
                lStatus.setIcon(Globals.imOK) ;
                break ;
            case 1 : //Stop
                lStatus.setIcon(Globals.imDisconnect) ;
                break ;
            case 2 : //Oops
                lStatus.setIcon(Globals.imOops) ;
                break ;
            default :
                //lStatus.setIcon(Globals.imOops) ;
        }
    }
    /**
     * Μέθοδος που απλά αλλάζει την εικόνα του κουμπιού bStartStop
     * ανάλογα με το αν υπάρχει ενεργή εποπτεία ή όχι όπως επίσης και
     * ενημερώνει τη μεταβλητή για το αν υπάρχει σύνδεση η όχι .
     * Αν είναι ενεργή η εποπτεία εμφανίζεται το κουμπί "stop" αλλιώς
     * αν είναι κλειστή η εποπτεία  εμφανίζεται το κουμπί "start" .
     * Αλλάζει το χρώμα , για να είναι πιο εμφανές αν είναι το κουμπί "πατημένο"(πορτοκαλί)
     * ή όχι (default).
     * @param isConnected True αν υπάρχει σύνδεση και False αν δεν υπάρχει .
     */
    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
        if (isConnected)
        {
            bStartStop.setIcon(Globals.imStop);
        }
        else
        {
            bStartStop.setIcon(Globals.imStart);
        }
    }
    /**
     * Μέθοδος που απλά σταματάει ολοκληρωμένα την εποπτεία (χωρίς να κλείνει η
     * καρτέλα της συσκευής).
     */
    public void stopMonitoring()
    {
        stopMonitoring(true);
    }
    /**
     * Μέθοδος που απλά σταματάει ολοκληρωμένα την εποπτεία (χωρίς να κλείνει η
     * καρτέλα της συσκευής).
     * @param byUser Παράμετρος που καθορίζει αν η εποπτεία κλείνει επειδή το
     * επέλεξε ο χρήστης(true) ή αν κλείνει από απροσδόκητη αιτία(false) (πχ έπεσε ο Server).
     */
    public void stopMonitoring(boolean byUser)
    {
        if (byUser)
            setStatus("Stopped success!",1) ;
        else
            setStatus("  Oops !!! :<br>" + "stopped monitoring!",2) ;
        
        try {
            if (device!=null)
            {
                device.ms.close();
                device.saveToXML();
            }
        } catch (IOException ex) {
            Logger.getLogger(Ptab.class.getName()).log(Level.SEVERE, null, ex);
        }
        setIsConnected(false);
        if (device.ltForRead!=null)
            device.ltForRead.stop();
    }
    /**
     * Μέθοδος που απλά αλλάζει το χρώμα στο background του κουμπιού bSaveSettings
     * ανάλογα με το ποια επιλογή είναι ήδη επιλεγμένη από το χρήστη .
     * Αλλάζει το χρώμα , για να είναι πιο εμφανές αν είναι το κουμπί "πατημένο"(πορτοκαλί)
     * ή όχι (default).
     */
    public void setSettingsButton ()
    {
        if (device.saveSettings)
            bSaveSettings.setBackground(Globals.selectedColor);
        else
            bSaveSettings.setBackground(UIManager.getColor ( "Panel.background" ));
    }
    /**
     * Μέθοδος που αναλαμβάνει με βάση τα δικαιώματα που έχει ο χρήστης πάνω σε μια
     * συγκεκριμένη συσκευή να εμφανίσει την κατάλληλη εικόνα κλειδιού στο κάτω
     * μέρος της καρτέλας που έχει την εποπτεία της εν λόγο συσκευής .
     * @param access Ουσιαστικά είναι ένα γράμμα(O,A,F,R) για τα δικαιώματα προσπέλασης
     */
    private void loadaccessIcon(String access)
    {
        switch(access)
        {
            case "O" :
                lAccess.setIcon(Globals.imOwner);
                break ;
            case "A" :
                lAccess.setIcon(Globals.imAdmin);
                break ;
            case "F" :
                lAccess.setIcon(Globals.imFull);
                break ;
            case "R" :
                lAccess.setIcon(Globals.imReadOnly);
                break ;
            default :
                lAccess.setIcon(Globals.imReadOnly);
        }
    }
    /**
     * Μέθοδος που κλείνει ολοκληρωμένα μια καρτέλα μιας εποπτείας συσκευής .
     */
    private void closeTab ()
    {
        stopMonitoring() ;
        for (int i = 0 ; i< Globals.objDevices.lastArrayWithDevEnable.length;i++)
            if (Globals.objDevices.lastArrayWithDevEnable[i].equals("O|"+device.getDeviceName()))
            {
                //Tools.Debug.print( Globals.objDevices.lastArrayWithDevEnable[i]);
                Globals.objDevices.lastArrayWithDevEnable[i] = Globals.objDevices.lastArrayWithDevEnable[i].replace("O|","|") ;
                //Tools.Debug.print( Globals.objDevices.lastArrayWithDevEnable[i]);
            }
        Globals.hmDevices.remove(devName,device)  ;
        Globals.objDevices.setDevicesInfo(Globals.objDevices.lastAlWithDevices) ;
        device = null ;
        Globals.objMainFrame.tpAllDevices.remove(this);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pTabMain = new javax.swing.JPanel();
        spTabMain = new javax.swing.JSplitPane();
        pLeft = new javax.swing.JPanel();
        spDevMenuAndTree = new javax.swing.JSplitPane();
        pDevMenu = new javax.swing.JPanel();
        bStartStop = new javax.swing.JButton();
        bChangeMap = new javax.swing.JButton();
        bFix = new javax.swing.JButton();
        bClose = new javax.swing.JButton();
        bSaveSettings = new javax.swing.JButton();
        pTreeAndStatus = new javax.swing.JPanel();
        pStatus = new javax.swing.JPanel();
        lAccess = new javax.swing.JLabel();
        lStatus = new javax.swing.JLabel();
        lStatusText = new javax.swing.JLabel();
        scpDevTree = new javax.swing.JScrollPane();
        pRight = new javax.swing.JPanel();
        spView = new javax.swing.JSplitPane();
        scpConsole = new javax.swing.JScrollPane();
        scpScreen = new javax.swing.JScrollPane();

        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(250, 716));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        spTabMain.setDividerLocation(240);
        spTabMain.setDividerSize(2);

        pLeft.setPreferredSize(new java.awt.Dimension(216, 714));

        spDevMenuAndTree.setDividerLocation(42);
        spDevMenuAndTree.setDividerSize(0);
        spDevMenuAndTree.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        pDevMenu.setPreferredSize(new java.awt.Dimension(210, 40));

        bStartStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/stop36.png"))); // NOI18N
        bStartStop.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bStartStop.setMaximumSize(new java.awt.Dimension(40, 40));
        bStartStop.setMinimumSize(new java.awt.Dimension(40, 40));
        bStartStop.setName(""); // NOI18N
        bStartStop.setPreferredSize(new java.awt.Dimension(40, 40));
        bStartStop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bStartStopMouseClicked(evt);
            }
        });

        bChangeMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/map36.png"))); // NOI18N
        bChangeMap.setToolTipText("");
        bChangeMap.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bChangeMap.setMaximumSize(new java.awt.Dimension(40, 40));
        bChangeMap.setMinimumSize(new java.awt.Dimension(40, 40));
        bChangeMap.setName(""); // NOI18N
        bChangeMap.setPreferredSize(new java.awt.Dimension(40, 40));
        bChangeMap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bChangeMapMouseClicked(evt);
            }
        });

        bFix.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/fix36.png"))); // NOI18N
        bFix.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bFix.setMaximumSize(new java.awt.Dimension(40, 40));
        bFix.setMinimumSize(new java.awt.Dimension(40, 40));
        bFix.setName(""); // NOI18N
        bFix.setPreferredSize(new java.awt.Dimension(40, 40));
        bFix.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bFixMouseClicked(evt);
            }
        });

        bClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/close36.png"))); // NOI18N
        bClose.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bClose.setMaximumSize(new java.awt.Dimension(40, 40));
        bClose.setMinimumSize(new java.awt.Dimension(40, 40));
        bClose.setName(""); // NOI18N
        bClose.setPreferredSize(new java.awt.Dimension(40, 40));
        bClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bCloseMouseClicked(evt);
            }
        });

        bSaveSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/save36.png"))); // NOI18N
        bSaveSettings.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bSaveSettings.setMaximumSize(new java.awt.Dimension(40, 40));
        bSaveSettings.setMinimumSize(new java.awt.Dimension(40, 40));
        bSaveSettings.setPreferredSize(new java.awt.Dimension(40, 40));
        bSaveSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bSaveSettingsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pDevMenuLayout = new javax.swing.GroupLayout(pDevMenu);
        pDevMenu.setLayout(pDevMenuLayout);
        pDevMenuLayout.setHorizontalGroup(
            pDevMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDevMenuLayout.createSequentialGroup()
                .addComponent(bStartStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bChangeMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bFix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bSaveSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bClose, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pDevMenuLayout.setVerticalGroup(
            pDevMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDevMenuLayout.createSequentialGroup()
                .addGroup(pDevMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(bClose, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bFix, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bChangeMap, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bSaveSettings, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bStartStop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        spDevMenuAndTree.setTopComponent(pDevMenu);

        pStatus.setMaximumSize(new java.awt.Dimension(0, 42));
        pStatus.setMinimumSize(new java.awt.Dimension(0, 42));
        pStatus.setName(""); // NOI18N
        pStatus.setPreferredSize(new java.awt.Dimension(0, 42));

        lAccess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/typeDevices/full.png"))); // NOI18N
        lAccess.setToolTipText("");
        lAccess.setBorder(new javax.swing.border.LineBorder(java.awt.Color.blue, 1, true));
        lAccess.setMaximumSize(new java.awt.Dimension(60, 30));
        lAccess.setMinimumSize(new java.awt.Dimension(60, 30));
        lAccess.setName(""); // NOI18N
        lAccess.setPreferredSize(new java.awt.Dimension(60, 30));
        lAccess.setRequestFocusEnabled(false);

        lStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/OK.png"))); // NOI18N
        lStatus.setToolTipText("");
        lStatus.setBorder(new javax.swing.border.LineBorder(java.awt.Color.blue, 1, true));
        lStatus.setMaximumSize(new java.awt.Dimension(40, 40));
        lStatus.setMinimumSize(new java.awt.Dimension(40, 40));
        lStatus.setName(""); // NOI18N
        lStatus.setPreferredSize(new java.awt.Dimension(40, 40));

        lStatusText.setFont(new java.awt.Font("Dialog", 2, 10)); // NOI18N
        lStatusText.setText(" 04/11/2015  & 12:12");
        lStatusText.setToolTipText("");
        lStatusText.setBorder(new javax.swing.border.LineBorder(java.awt.Color.blue, 1, true));
        lStatusText.setMaximumSize(new java.awt.Dimension(60, 30));
        lStatusText.setMinimumSize(new java.awt.Dimension(60, 30));
        lStatusText.setName(""); // NOI18N
        lStatusText.setPreferredSize(new java.awt.Dimension(60, 30));
        lStatusText.setRequestFocusEnabled(false);

        javax.swing.GroupLayout pStatusLayout = new javax.swing.GroupLayout(pStatus);
        pStatus.setLayout(pStatusLayout);
        pStatusLayout.setHorizontalGroup(
            pStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pStatusLayout.createSequentialGroup()
                .addComponent(lAccess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lStatusText, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pStatusLayout.setVerticalGroup(
            pStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pStatusLayout.createSequentialGroup()
                .addGroup(pStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lAccess, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lStatusText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pTreeAndStatusLayout = new javax.swing.GroupLayout(pTreeAndStatus);
        pTreeAndStatus.setLayout(pTreeAndStatusLayout);
        pTreeAndStatusLayout.setHorizontalGroup(
            pTreeAndStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTreeAndStatusLayout.createSequentialGroup()
                .addComponent(pStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
            .addComponent(scpDevTree)
        );
        pTreeAndStatusLayout.setVerticalGroup(
            pTreeAndStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTreeAndStatusLayout.createSequentialGroup()
                .addComponent(scpDevTree, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        spDevMenuAndTree.setRightComponent(pTreeAndStatus);

        javax.swing.GroupLayout pLeftLayout = new javax.swing.GroupLayout(pLeft);
        pLeft.setLayout(pLeftLayout);
        pLeftLayout.setHorizontalGroup(
            pLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spDevMenuAndTree)
        );
        pLeftLayout.setVerticalGroup(
            pLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spDevMenuAndTree, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
        );

        spTabMain.setLeftComponent(pLeft);

        spView.setDividerLocation(650);
        spView.setDividerSize(2);
        spView.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        spView.setRightComponent(scpConsole);
        spView.setLeftComponent(scpScreen);

        javax.swing.GroupLayout pRightLayout = new javax.swing.GroupLayout(pRight);
        pRight.setLayout(pRightLayout);
        pRightLayout.setHorizontalGroup(
            pRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spView)
        );
        pRightLayout.setVerticalGroup(
            pRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spView)
        );

        spTabMain.setRightComponent(pRight);

        javax.swing.GroupLayout pTabMainLayout = new javax.swing.GroupLayout(pTabMain);
        pTabMain.setLayout(pTabMainLayout);
        pTabMainLayout.setHorizontalGroup(
            pTabMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spTabMain, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
        );
        pTabMainLayout.setVerticalGroup(
            pTabMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spTabMain)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pTabMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pTabMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Μέθοδος που εκτελείτε όταν "κλικάρει" ο χρήστης το κουμπί (bFix) για να fixάρει τα {@link ControlUnit}s.
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται .
     */
    private void bFixMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bFixMouseClicked
        device.fixUnitControlToResize();
    }//GEN-LAST:event_bFixMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν γίνει resize της καρτέλας .
     * Σκοπός είναι η καλύτερη δυνατή εμφάνηση της κονσόλας μηνυμάτων εφόσων
     * έχει επιλέξει ο χρήστης να εμφανίζεται .
     * @param evt
     */
    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if (scpConsole.getSize().height > 200)
            spView.setDividerLocation(spView.getSize().height-200) ;
    }//GEN-LAST:event_formComponentResized
    /**
     * Μέθοδος που εκτελείτε όταν "κλικάρει" ο χρήστης το κουμπί (bSaveSettings) για να
     * αποθηκεύονται ή όχι κάποιες πληροφορίες της συσκευής της καρτέλας στον υπολογιστή του χρήστη.
     * Πληροφορίες εννοούνται πχ θέσεις των {@link UnitControl} στην οθόνη , τα Description τους κα .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται .
     */
    private void bSaveSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSaveSettingsMouseClicked
        device.saveSettings = !device.saveSettings ;
        setSettingsButton()  ;
    }//GEN-LAST:event_bSaveSettingsMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν "κλικάρει" ο χρήστης το κουμπί (bClose) για να
     * κλείσει την καρτέλα της εποπτείας .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται .
     */
    private void bCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bCloseMouseClicked
        closeTab() ;
    }//GEN-LAST:event_bCloseMouseClicked
    /**
     * Μέθοδος που προετοιμάζει το DialogOpen και εκτελείτε μόνο την πρώτη φορά
     * που ο χρηστής θα πατήσει το κουμπί για να αλλάξει την εικόνα του background
     * της συγκεκριμένης συσκευής .
     */
    private void prepareChangeBackgroundImage()
    {
        fc = new JFileChooser();
        fc.setSize(800, 600);
        fc.setSize(new Dimension(800,600));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & GIF Images", "jpg", "gif");
        fc.setFileFilter(filter);
        
        fc.setCurrentDirectory(new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"backgroundImages"+Globals.fSep));
    }
    /**
     * Μέθοδος που εκτελείτε όταν "κλικάρει" ο χρήστης το κουμπί (bChangeMap) για να
     * αλλάξει την εικόνα του background της εποπτείας.
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται .
     */
    private void bChangeMapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bChangeMapMouseClicked
        if (firstTimeChangeImage)
        {
            prepareChangeBackgroundImage() ;
            firstTimeChangeImage = false ;
        }
        int returnVal = fc.showDialog(Globals.objMainFrame, "Change Background Icon");
        if (returnVal==0)
        {
            File f = new File(Globals.runFolder.getAbsolutePath() + Globals.fSep+"backgroundImages"+Globals.fSep +fc.getSelectedFile().getName()) ;
            try {
                if (!fc.getSelectedFile().toPath().toString().equals(f.toPath().toString()))
                    Files.copy(fc.getSelectedFile().toPath(),f.toPath(), REPLACE_EXISTING);
                device.setPathFile(f.toPath().toString());
            } catch (IOException ex) {
                Tools.Debug.print("Dont copy Image");
            }
        }
        
    }//GEN-LAST:event_bChangeMapMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν "κλικάρει" ο χρήστης το κουμπί (bStartStop) για να
     * σταματήσει ή να ξεκινήσει την εποπτεία σε μια συσκευή .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται .
     */
    private void bStartStopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bStartStopMouseClicked
        if (isConnected)
            stopMonitoring() ;
        else
            startMonitoring() ;        
    }//GEN-LAST:event_bStartStopMouseClicked
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bChangeMap;
    private javax.swing.JButton bClose;
    private javax.swing.JButton bFix;
    private javax.swing.JButton bSaveSettings;
    private javax.swing.JButton bStartStop;
    private javax.swing.JLabel lAccess;
    public javax.swing.JLabel lStatus;
    public javax.swing.JLabel lStatusText;
    private javax.swing.JPanel pDevMenu;
    private javax.swing.JPanel pLeft;
    private javax.swing.JPanel pRight;
    public javax.swing.JPanel pStatus;
    private javax.swing.JPanel pTabMain;
    private javax.swing.JPanel pTreeAndStatus;
    public javax.swing.JScrollPane scpConsole;
    public javax.swing.JScrollPane scpDevTree;
    public javax.swing.JScrollPane scpScreen;
    private javax.swing.JSplitPane spDevMenuAndTree;
    private javax.swing.JSplitPane spTabMain;
    public javax.swing.JSplitPane spView;
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