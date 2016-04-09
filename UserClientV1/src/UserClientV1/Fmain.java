package UserClientV1;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import static UserClientV1.ManageUserClient.initDevicesFrame;

/**
 * Κλάση που εχει κληρονομήσει ένα JFrame και που ουσιαστικά είναι το κύριο παράθυρο
 * του User Client .
 * Μέσα από αυτό μπορεί ο χρήστης να κάνει όλες τις δυνατές λειτουργίες του User Client .
 * Κυρίως δηλαδή μπορεί ο χρήστης να ανήξει το παραθυράκι με τις συσκευές του ({@link Ddevices})
 * και να ξεκινήσει να κάνει εποπτεία .
 * Ακόμη μέσω του μενού που έχει η κλάση μας μπορεί ο χρήστης να ανοίξει το παράθυρο
 * με τις πληροφορίες της Πτυχιακής ({@link Dthesis}), να εμφανίσει ή να κρύψει την
 * ενημερωτική "κονσόλα" από τις καρτέλες({@link Ptab}) της εποπτείας όπως ακόμη και να κάνει logout .
 * @author Michael Galliakis
 */
public class Fmain extends javax.swing.JFrame {
    public static boolean isConsoleReportEnable = false ;
    /**
     * Κατασκευαστής που προετοιμάζει κατάλληλα το αντικείμενο της κλάσης μας
     * στην δημιουργία του . Δηλαδή πρακτικά κυρίως διαμορφώνει το εικονίδιο
     * του χρήστη με βάση το τύπο του , βάζει σαν tooltip στο εικονίδιο το όνομα
     * του χρήστη και τέλος ανοίγει μια καρτέλα που έχει το "Report Area"
     * που φαίνεται και στις υπόλοιπες καρτέλες αν ο χρήστης έχει επιλέξει
     * να φαίνεται η "Console" σε όλες τις καρτέλες .
     * @param debug Αν True σημαίνει ότι θα φορτώσει κάποιες default ρυθμίσεις
     * και θα κάνει όλες τις ενέργειες για να συνδεθεί με το Server . Δηλαδή
     * πρακτικά θα παρακάμψει ένα βήμα ήτοι τη φόρμα της σύνδεσης του χρήστη ({@link FuserLogin}).
     * Χρησιμοποιείται στην περίπτωση ανάπτυξης της εφαρμογής για να μην δίνουμε συνέχεια
     * όνομα χρήστη , κωδικό , πόρτα και ip και να ξεκινάει η εφαρμογή μας πιο γρήγορα.
     * Στην κανονική διάρκεια της εφαρμογής όταν θα την τρέχει ο "τελικός" χρήστης
     * το debug πρέπει να είναι πάντα false .
     */
    public Fmain(boolean debug)  {
        initComponents();
        setTitle(Globals.thesisTitle);
        if (debug)
        {
            Globals.objMainFrame = this ;
            Globals.address = "localhost";
            Globals.port = 50128 ;
            Globals.username = "maria" ;
            Globals.password = "maria" ;
            //Globals.DBUserID = "2" ;
            Globals.prepareStaticVariables(this) ;
            initDevicesFrame(this);
        }
        this.setIconImage(Globals.biLogo);
        switch ( Globals.typeUser)
        {
            case "S" :
                lTypeUser.setIcon(Globals.imSystem);
                break;
            case "V" :
                lTypeUser.setIcon(Globals.imVip);
                break;
                //case "U" :
            default :
                lTypeUser.setIcon(Globals.imUser);
        }
        lTypeUser.setToolTipText(Globals.username);
        
        tpAllDevices.add("Report Area", Globals.debugTextPane);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        pMain = new javax.swing.JPanel();
        spMain = new javax.swing.JSplitPane();
        pTabs = new javax.swing.JPanel();
        tpAllDevices = new javax.swing.JTabbedPane();
        pMenu = new javax.swing.JPanel();
        bDevices = new javax.swing.JButton();
        bSettings = new javax.swing.JButton();
        bConsole = new javax.swing.JButton();
        bThessis = new javax.swing.JButton();
        bLogout = new javax.swing.JButton();
        lLogo = new javax.swing.JLabel();
        lTEI = new javax.swing.JLabel();
        lTypeUser = new javax.swing.JLabel();
        lCCRight = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        spMain.setDividerLocation(37);
        spMain.setDividerSize(0);
        spMain.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        javax.swing.GroupLayout pTabsLayout = new javax.swing.GroupLayout(pTabs);
        pTabs.setLayout(pTabsLayout);
        pTabsLayout.setHorizontalGroup(
            pTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpAllDevices, javax.swing.GroupLayout.DEFAULT_SIZE, 1084, Short.MAX_VALUE)
        );
        pTabsLayout.setVerticalGroup(
            pTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpAllDevices, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
        );

        spMain.setRightComponent(pTabs);

        bDevices.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/devices24.png"))); // NOI18N
        bDevices.setText("Devices");
        bDevices.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bDevicesMouseClicked(evt);
            }
        });

        bSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/settings24.png"))); // NOI18N
        bSettings.setText("Settings");
        bSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bSettingsMouseClicked(evt);
            }
        });

        bConsole.setBackground(java.awt.Color.orange);
        bConsole.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/console24.png"))); // NOI18N
        bConsole.setText("Console");
        bConsole.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bConsoleMouseClicked(evt);
            }
        });

        bThessis.setBackground(new java.awt.Color(153, 51, 255));
        bThessis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/thesis24.png"))); // NOI18N
        bThessis.setText("Thesis");
        bThessis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bThessisMouseClicked(evt);
            }
        });

        bLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/logout24.png"))); // NOI18N
        bLogout.setText("Logout");
        bLogout.setToolTipText("");
        bLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bLogoutMouseClicked(evt);
            }
        });

        lLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/logoPMGv2F48.png"))); // NOI18N
        lLogo.setToolTipText("");

        lTEI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/TEI36.png"))); // NOI18N
        lTEI.setName(""); // NOI18N

        lTypeUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UserClientV1/Images/typeUsers/system36.png"))); // NOI18N
        lTypeUser.setText("jLabel1");
        lTypeUser.setMaximumSize(new java.awt.Dimension(36, 36));
        lTypeUser.setMinimumSize(new java.awt.Dimension(36, 36));
        lTypeUser.setName(""); // NOI18N
        lTypeUser.setPreferredSize(new java.awt.Dimension(36, 36));

        lCCRight.setName(""); // NOI18N

        javax.swing.GroupLayout pMenuLayout = new javax.swing.GroupLayout(pMenu);
        pMenu.setLayout(pMenuLayout);
        pMenuLayout.setHorizontalGroup(
            pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMenuLayout.createSequentialGroup()
                .addComponent(lTypeUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bDevices)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bSettings)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bConsole)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bThessis)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bLogout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 251, Short.MAX_VALUE)
                .addComponent(lCCRight)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lTEI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lLogo))
        );
        pMenuLayout.setVerticalGroup(
            pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMenuLayout.createSequentialGroup()
                .addGroup(pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lLogo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lTEI, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bDevices)
                        .addComponent(bSettings)
                        .addComponent(bConsole)
                        .addComponent(bThessis)
                        .addComponent(bLogout)
                        .addComponent(lTypeUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lCCRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί για να ανοίξει το παραθυράκι
     * των συσκευών ({@link Ddevices}).
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */    
    private void bDevicesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bDevicesMouseClicked
        Globals.objDevices.setVisible(true);
        bDevices.setBackground(UIManager.getColor ( "Panel.background" ));        
    }//GEN-LAST:event_bDevicesMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί για να εμφανίζεται
     * ή όχι η "κονσόλα" σε όλες τις καρτέλες των εποπτειών ({@link Ptab}) .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */    
    private void bConsoleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bConsoleMouseClicked
        if (isConsoleReportEnable)
        {
            isConsoleReportEnable = false ;
            bConsole.setBackground(Color.orange);
        }
        else
        {
            isConsoleReportEnable = true ;
            bConsole.setBackground(UIManager.getColor ( "Panel.background" ));
        }
        voidViewReportArea(isConsoleReportEnable) ;        
    }//GEN-LAST:event_bConsoleMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν πάει να κλείσει η φόρμα μας.
     * @param evt Το Default WindowEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        ManageUserClient.saveAllDevices();
        Globals.objUserLogin.saveToXMLLoginSettings() ;
    }//GEN-LAST:event_formWindowClosing
    /**
     * Μέθοδος που αναλαμβάνει να κάνει αποσύνδεση του χρήστη και να εμφανίσει
     * το αρχικό παράθυρο σύνδεσης του χρήστη με το Server ({@link FuserLogin})
     */
    public void logoutProccess()
    {
        logoutProccess(true);
    }
    /**
     * Μέθοδος που αναλαμβάνει να κάνει αποσύνδεση του χρήστη και να εμφανίσει
     * το αρχικό παράθυρο σύνδεσης του χρήστη με το Server ({@link FuserLogin})
     * @param byUser Αν true σημαίνει ότι ο χρήστης επιχείρησε να κάνει την
     * αποσύνδεση του αλλιώς αν false τότε γίνεται απροσδόκητα η αποσύνδεση .
     */
    public void logoutProccess(boolean byUser)
    {
        ManageUserClient.saveAllDevices();
        if (Globals.objUserLogin!=null)
            Globals.objUserLogin.setVisible(true);
        else
            System.exit(0) ;
        
        Globals.objDevices.dispose();
        Globals.objDevices = null ;
        Globals.objMainFrame = null ;
        
        this.dispose();
        if (!byUser)
            JOptionPane.showMessageDialog(Globals.objUserLogin,"        ERROR : Lost connection with the Server!!!          ",Globals.messDialTitle,JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί για να κάνει αποσύνδεση.
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */    
    private void bLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bLogoutMouseClicked
        logoutProccess() ;
    }//GEN-LAST:event_bLogoutMouseClicked
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
     * Μέθοδος που εκτελείτε όταν ο χρήστης πατήσει το κουμπί ρυθμίσεων που είναι
     * στο μενού.Το κουμπί ουσιαστικά δεν κάνει τίποτα - είναι για πιθανόν μελλοντική χρήση.
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event
     * το οποίο στη περίπτωση μας δεν χρησιμοποιείται.
     */    
    private void bSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSettingsMouseClicked
        JOptionPane.showMessageDialog(this,"             Δεν χρησιμοποιείται!\nΥπάρχει για πιθανόν μελλοντική χρήση.",Globals.messDialTitle,JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_bSettingsMouseClicked
    /**
     * Μέθοδος που κάνει όλα τα απαραίτητα βήματα ομαδοποιημένα για να φορτωθεί
     * σε μια καρτέλα και να ξεκινήσει η εποπτεία μιας συσκευής .
     * @param devID Λεκτικό με το ID της συσκευής
     * @param devName Λεκτικό με το όνομα της συσκευής.
     * @param devAccess Λεκτικό με τα δικαιώματα που έχει ο χρήστης πάνω στην συσκευή.
     */
    public void newDeviceProcess(String devID,String devName,String devAccess)
    {
        Ptab devTab  = new Ptab(devID, devName, devAccess) ;
        devTab.startMonitoring();
        tpAllDevices.add(devName, devTab);
        tpAllDevices.setSelectedComponent(devTab);
    }
    /**
     * Μέθοδος που αναλαμβάνει ανάλογα την παράμετρο να εμφανίσει ή να κρύψει
     * την κάτω περιοχή με πληροφορίες που υπάρχει σε κάθε καρτέλα εποπτείας .
     * Δηλαδή με λίγα λόγια να εμφανίσει ή να κρύψει την "Console" .
     * @param enable Αν true εμφανίζει την "console" αλλιώς αν false την κρύβει .
     */
    private void voidViewReportArea(boolean enable)
    {
        if (enable)
        {
            for (Component comp : tpAllDevices.getComponents())
            {
                try
                {
                    Ptab tmpCom = null ;
                    tmpCom = (Ptab)comp ;
                    if (tmpCom!=null)
                    {
                        tmpCom.scpConsole.setVisible(true);
                        tmpCom.spView.setDividerLocation(tmpCom.spView.size().height - 200);
                        tmpCom.spView.setDividerSize(3);
                    }
                } catch(Exception ex)
                {
                    //Nothing
                }
            }
        }
        else
        {
            for (Component comp : tpAllDevices.getComponents())
            {
                try
                {
                    Ptab tmpCom = null ;
                    tmpCom = (Ptab)comp ;
                    if (tmpCom!=null)
                    {
                        tmpCom.spView.setDividerSize(0);
                        tmpCom.spView.setDividerLocation(tmpCom.spView.size().height);
                        tmpCom.scpConsole.setVisible(false);
                    }
                } catch(Exception ex)
                {
                    //Nothing
                }
            }
        }
    }
    /**
     * Κλασική static main μέθοδος που δίνει την δυνατότητα να ξεκινήσει η εκτέλεση
     * του project μας από εδώ .
     * Πρακτικά χρησιμοποιείται μόνο στην περίπτωση που κάνουμε ανάπτυξη της εφαρμογής
     * και θέλουμε να παρακάμψουμε το πρώτο βήμα της σύνδεσης του χρήστη({@link FuserLogin})
     * φορτώνοντας κάποιες default ρυθμίσεις που υπάρχουν στον κατασκευαστή.
     * Δεν έχει καμία χρήση κατά την λειτουργία του User Client στην κανονική ροή εκτέλεσης.
     * @param args Default παράμετροι που δεν χρησιμοποιούνται και που μπορεί δυνητικά να πάρει
     * το πρόγραμμα μας στην περίπτωση που εκτελεστεί από περιβάλλον γραμμής εντολών και όταν
     * συγχρόνως το project μας έχει σαν κύριο-αρχικό αρχείο εκτέλεσης το Fmain.java.
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Fmain frm;
                frm = new Fmain(true);
                frm.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frm.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bConsole;
    public javax.swing.JButton bDevices;
    private javax.swing.JButton bLogout;
    private javax.swing.JButton bSettings;
    public javax.swing.JButton bThessis;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JLabel lCCRight;
    private javax.swing.JLabel lLogo;
    private javax.swing.JLabel lTEI;
    private javax.swing.JLabel lTypeUser;
    private javax.swing.JPanel pMain;
    private javax.swing.JPanel pMenu;
    private javax.swing.JPanel pTabs;
    private javax.swing.JSplitPane spMain;
    public javax.swing.JTabbedPane tpAllDevices;
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