package UserClientV1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import UserClientV1.Device.Unit;

/**
 * Κλάση η οποία έχει κληρονομήσει ένα JPanel και που πρακτικά είναι υπεύθυνη
 * να αναπαραστήσει με γραφικό τρόπο μια μονάδα .
 * Δηλαδή με ένα μικρό σχετικά panel μπορεί ο χρήστης να δει όλες τις βασικές
 * πληροφορίες μιας μονάδας όπως επίσης μπορεί να αλλάζει(ανάλογα βέβαια την κάθε
 * περίπτωση) την τιμή και το mode της με απλό και γραφικό τρόπο με τη χρήση του ποντικιού.
 * Επίσης μας επιτρέπει να υπάρχουν ομαδοποιημένα όλα αυτά που πρέπει για την
 * απεικόνηση της κάθε μονάδας και έτσι να έχουμε καλύτερη διαχείρηση της .
 * @author Michael Galliakis
 */
public class UnitControl extends javax.swing.JPanel {
    private Double value ;
    public boolean locked = false;
    private boolean selected =false ;
    public int mode ;
    Point initPoint ;
    boolean isSwitch ;
    private Double dbLimit ;
    private boolean isReadOnly ;
    ImageIcon imIcOn ;
    ImageIcon imIcOff ;
    
    public Unit unit ;
    /**
     * Κατασκευαστής που προετοιμάζει κατάλληλα την κλάση μας ούτως ώστε να είναι
     * έτοιμη να διαχειριστεί πλήρως τη γραφική αναπαράσταση της εκάστοτε μονάδας .
     * @param u Ένα αντικείμενο τύπου {@link Unit} με το οποίο θα συσχετιστεί η κλάση μας.
     * Δηλαδή η κλάση μας θα αφορά την ίδια μονάδα που αφορά και το αντικείμενο u .
     */
    public UnitControl(Unit u) {
        initComponents();
        
        unit = u ;
        if (Tools.isNumeric(u.getMode()))
            setMode((int)Math.round(correctNum(u.getMode()))) ;
        else
            setMode(2) ;
        
        if (unit.device.devAccess.equals("R"))
        {
            isReadOnly= true ;
            bMode.setEnabled(false);
            lAccess.setIcon(Globals.imNoHand);
        }
        else
        {
            if (this.mode ==0)
            {
                isReadOnly = true ;
                lAccess.setIcon(Globals.imNoHand);
            }
            else
            {
                isReadOnly = false ;
                lAccess.setIcon(Globals.imHand);
            }
        }
        
        if (u.limit.contains("SW"))
        {
            isSwitch = true ;
            lValue.setIcon(Globals.imSwitch);
            lValue.setText("");
            dbLimit = (Tools.isNumeric(u.limit.replace("SW", "")))?Double.parseDouble(u.limit.replace("SW", "")):null ;
        }
        else if (u.limit.contains("TB"))
        {
            isSwitch = false ;
            dbLimit = (Tools.isNumeric(u.limit.replace("TB", "")))?Double.parseDouble(u.limit.replace("TB", "")):null ;
        }
        else
        {
            isSwitch = false ;
            dbLimit = null ;
        }
        
        setDescription(unit.getFullID()) ;
        setID(u.getFullID()) ;
        setType(u.getType()) ; // Έχει σημασία να είναι πρώτο!
        setValue(u.getValue()) ;
        
        lAccess.setText("");
        
        initPoint = new Point(this.getLocation());
        PopClickListener pcl = new PopClickListener() ;
        this.addMouseListener(pcl);
        lID.addMouseListener(pcl);
        lValue.addMouseListener(pcl);
        lImage.addMouseListener(pcl);
        
        setOpaque(false);
    }
    /**
     * Μέθοδος που αλλάζει το description της μονάδας τόσο στην μεταβλητή της κλάσης
     * όσο και στα toolTip που εμφανίζονται αν είναι λίγη ώρα το ποντίκι
     * του χρήστη πάνω στο {@link UnitControl} μας .
     * @param description Το νέο Description .
     */
    public void setDescription(String description) {
        lValue.setToolTipText(description);
        lAccess.setToolTipText(description);
        lID.setToolTipText(description);
        lImage.setToolTipText(description);
    }
    /**
     * Απλή get μέθοδος που επιστρέφει το mode της μονάδας .
     * @return Το mode που έχει η μονάδα .
     */
    public int getMode() {
        return mode;
    }
    /**
     * Μέθοδος που απλά αλλάζει το τύπο του {@link UnitControl} μας .
     * Όπως βέβαια συγχρόνως και την εικόνα τύπου που έχει το panel μας.
     * @param type Ένα λεκτικό που πρακτικά είναι ένας αριθμός που αναπαριστά το τύπο της μονάδας.
     */
    public void setType(String type) {
        fillTypeImage(type);
    }
    /**
     * Static μέθοδος που απλά απο-επιλέγει όλα τα εικονίδια των μονάδων (παύουν
     * δηλαδή να είναι πορτοκαλί)
     * @param allPanels Μια λίστα με όλα τα {@link UnitControl} που θα απο-επιλεχθούν.
     */
    public static void clearSelection(ArrayList<UnitControl> allPanels)
    {
        for (UnitControl p : allPanels)
        {
            p.setSelected(false);
            p.setLocked(p.locked);
        }
    }
    /**
     * Ανάλογα την παράμετρο κάνει το εκάστοτε εικονίδιο μονάδας να είναι
     * κλειδωμένο(γκρι) ή όχι(πράσινο) .
     * @param locked Αν είναι True τότε "κλειδώνεται" αλλιώς αν False "ξεκλειδώνεται" .
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
        if (locked)
        {
            //this.setBorder(BorderFactory.createLoweredBevelBorder());
            if (!selected)
                this.setBackground(Globals.lockColor);
        }
        else
        {
            //this.setBorder(BorderFactory.createLineBorder(Color.black));
            if (!selected)
                this.setBackground(Globals.unLockColor);
        }
    }
    
    /**
     * Μέθοδος που αναλαμβάνει να φορτώνει την ανάλογη εικόνα-εικόνες(αν έχει 2 καταστάσεις
     * ο τύπος) με βάση το τύπο .
     * @param t Ένα λεκτικό που πρακτικά είναι ο αριθμός του τύπου της μονάδας .
     */
    private void fillTypeImage(String t)
    {
        if (dbLimit!=null)
        {
            imIcOff = Globals.hmUnitImages.get("0" + t) ;
            imIcOn = Globals.hmUnitImages.get("1" + t) ;
        }
        else
        {
            imIcOff = Globals.hmUnitImages.get("0" + t) ;
            imIcOn = null ;
        }
        
        if (imIcOff!=null && imIcOn==null)
            imIcOn = imIcOff ;
        else if (imIcOff==null)
        {
            imIcOff = Globals.hmUnitImages.get("00") ;
            dbLimit =null;
            //imIcOn = Globals.hmUnitImages.get("00") ;
        }
    }
    /**
     * Μέθοδος που αναλαμβάνει να αλλάζει κατάλληλα το Mode με βάση τη τιμή της παράμετρου.
     * Δηλαδή πέρα από την τιμή του mode που αλλάζει στην κλάση , αλλάζει και
     * το κουμπί του mode πάνω στο {@link UnitControl} όπως επίσης και το λεκτικό
     * του αντίστοιχου "φύλλου" που υπάρχει στο "δέντρο" αριστερά της καρτέλας της συσκευής .
     * @param mode ένας αριθμός (από 0-4) για το νέο mode της μονάδας .
     */
    public void setMode(int mode) {
        this.mode = mode;
        switch (mode)
        {
            case 0 :
                bMode.setText("A"); //only Auto
                bMode.setEnabled(false);
                break ;
            case 1 :
                bMode.setText("R"); //only Remote
                bMode.setEnabled(false);
                break ;
            case 2 :
                bMode.setText("B"); //Both
                bMode.setEnabled(false);
                break ;
            case 3 :
                bMode.setText("A"); //Auto by User
                bMode.setEnabled(!isReadOnly);
                break ;
            case 4 :
                bMode.setText("R"); //Remote by User
                bMode.setEnabled(!isReadOnly);
                break ;
            default :
                bMode.setText("B"); //Both
                bMode.setEnabled(false);
        }
    }
    
    /**
     * Μέθοδος που αναλαμβάνει να αλλάζει κατάλληλα το value με βάση τη τιμή της παραμέτρου.
     * Δηλαδή πέρα από την τιμή του value που αλλάζει στην κλάση , αλλάζει και
     * τη τιμή που φαίνεται πάνω στο {@link UnitControl} ή αν δεν είναι είδος switch .
     * Επίσης αλλάζει και το λεκτικό του αντίστοιχου "φύλλου" που υπάρχει στο "δέντρο"
     * αριστερά της καρτέλας της συσκευής .
     * Ακόμη αν η μονάδα έχει limit και ο τύπος της είναι 2 καταστάσεων,
     * αλλάζει και το εικονίδιο του τύπου (Πχ από ανοιχτή σε κλειστή λάμπα)
     * @param val Ένα λεκτικό που θα μετατραπεί σε αριθμό για να αποδοθεί στο νέο Value
     */
    public void setValue(String val) {
        this.value = correctNum(val) ;
        if (!isSwitch)
            lValue.setText(String.valueOf(Math.round(value)));
        
        if (dbLimit==null)
            lImage.setIcon(imIcOff);
        else
        {
            if (value>dbLimit)
                lImage.setIcon(imIcOn);
            else
                lImage.setIcon(imIcOff);
        }
    }
    
    /**
     * Μέθοδος που απλά βρίσκει αν ένα λεκτικό είναι αριθμός ή όχι .
     * @param sNum Ένα λεκτικό που θα ελεγχθεί αν είναι αριθμός.
     * @return Τον ίδιο αριθμό αν το λεκτικό είναι έγκυρος αριθμός
     * ή 0 αν δεν είναι έγκυρος αριθμός  .
     */
    private static double correctNum(String sNum)
    {
        double res ;
        try {
            res = Double.parseDouble(sNum);
        }
        catch (Exception ex)
        {
            //Nothing
            return 0 ;
        }
        return res ;
    }
    /**
     * Απλή set μέθοδος που εκτός από το να καταχωρεί το ID στην κλάση αλλάζει
     * ανάλογα και το κείμενο στο label του ID .
     * @param ID Συμβολοσειρά του νέου ID .
     */
    public void setID(String ID) {
        lID.setText(ID);
    }
    /**
     * Μέθοδος που κάνει να είναι επιλεγμένο ή όχι το εκάστοτε {@link UnitControl}
     * ανάλογα με τη τιμή στην παράμετρο .
     * @param selected Αν True γίνεται το {@link UnitControl} μας επιλεγμένο αλλιώς
     * αν False γίνεται το ανάποδο .
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        
        if (selected)
        {
            this.setBackground(Globals.selectedColor);
        }
        else
            this.setBackground((locked)?Globals.lockColor:Globals.unLockColor);
    }
    
    /**
     * Απλά επιστρέφει το {@link Unit} με το οποίο είναι συσχετισμένο .
     * @return Το {@link Unit} με το οποίο είναι συσχετισμένο .
     */
    public Unit getUnit() {
        return unit;
    }
    /**
     * Συσχετίζεται το συγκεκριμένο {@link UnitControl} με το {@link BackgroundPanel} στο οποίο βρίσκεται .
     * @param backPanel Το {@link BackgroundPanel} στο οποίο βρίσκεται το
     * συγκεκριμένο {@link UnitControl} .
     */
    public void setBackPanel(BackgroundPanel backPanel) {
        unit.device.backPanel = backPanel;
    }
    
    /**
     * Μέθοδος που απλά γίνεται override για να επιστρέφει διαφορετικό
     * Insets από το Default .
     * @return Insets με 10 απόσταση προς όλες τις πλευρές .
     */
    @Override
    public Insets getInsets() {
        return new Insets(10, 10, 10,10);
    }
    /**
     * Μέθοδος που εκτελείτε κάθε φορά που επαναζωγραφίζεται το panel.
     * Όλος ο κώδικας που υπάρχει μέσα στο σώμα της μεθόδου απλά δίνει αυτό το
     * ιδιαίτερο σχήμα στο {@link UnitControl}. Δηλαδή αυτή την πολύγωνη μορφή
     * αντί της κλασικής τετράγωνης .
     * @param g Το Default Graphics αντικείμενο που έχει το συγκεκριμένο event το οποίο
     * χρησιμοποιούμε για να αλλάξουμε το πίσω σχήμα του panel μας .
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int width = getWidth() - 1;
        int height = getHeight() - 1;
        
        int radius = Math.min(width, height) / 10;
        
        Path2D p = new Path2D.Float();
        p.moveTo(0, radius / 2);
        p.curveTo(0, 0, 0, 0, radius / 2, 0);
        p.curveTo(width / 4, radius, width / 4, radius, (width / 2) - radius, radius / 2);
        p.curveTo(width / 2, 0, width / 2, 0, (width / 2) + radius, radius / 2);
        p.curveTo(width - (width / 4), radius, width - (width / 4), radius, width - (radius / 2), 0);
        p.curveTo(width, 0, width, 0, width, radius / 2);
        
        p.curveTo(width - radius, height / 4, width - radius, height / 4, width - (radius / 2), (height / 2) - radius);
        p.curveTo(width, height / 2, width, height / 2, width - (radius / 2), (height / 2) + radius);
        p.curveTo(width - radius, height - (height / 4), width - radius, height - (height / 4), width, height - (radius / 2));
        p.curveTo(width, height, width, height, width - (radius / 2), height);
        
        p.curveTo(width - (width / 4), height - radius, width - (width / 4), height - radius, (width / 2) + radius, height - (radius / 2));
        p.curveTo(width / 2, height, width / 2, height, (width / 2) - radius, height - (radius / 2));
        p.curveTo((width / 4), height - radius, (width / 4), height - radius, (radius / 2), height);
        p.curveTo(0, height, 0, height, 0, height - (radius / 2));
        
        p.curveTo(radius, height - (height / 4), radius, height - (height / 4), (radius / 2), (height / 2) + radius);
        p.curveTo(0, height / 2, 0, height / 2, (radius / 2), (height / 2) - radius);
        p.curveTo(radius, (height / 4), radius, (height / 4), 0, (radius / 2));
        
        p.closePath();
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setColor(getBackground());
        g2d.fill(p);
        g2d.dispose();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lID = new javax.swing.JLabel();
        lImage = new javax.swing.JLabel();
        lValue = new javax.swing.JLabel();
        lAccess = new javax.swing.JLabel();
        bMode = new javax.swing.JButton();

        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(74, 88));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        lID.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lID.setText("1/2/0");
        lID.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lIDMouseDragged(evt);
            }
        });
        lID.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lIDMouseClicked(evt);
            }
        });

        lImage.setText("                     ");
        lImage.setToolTipText("");
        lImage.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lImageMouseDragged(evt);
            }
        });
        lImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lImageMouseClicked(evt);
            }
        });

        lValue.setText("52%");
        lValue.setToolTipText("");
        lValue.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                lValueMouseDragged(evt);
            }
        });
        lValue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lValueMouseClicked(evt);
            }
        });

        lAccess.setText("A");
        lAccess.setToolTipText("");

        bMode.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bMode.setText("R");
        bMode.setFocusPainted(false);
        bMode.setFocusable(false);
        bMode.setMargin(new java.awt.Insets(0, 0, 0, 0));
        bMode.setMaximumSize(new java.awt.Dimension(18, 18));
        bMode.setMinimumSize(new java.awt.Dimension(18, 18));
        bMode.setPreferredSize(new java.awt.Dimension(18, 18));
        bMode.setVerifyInputWhenFocusTarget(false);
        bMode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bModeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lAccess)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lValue)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lImage, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lID)
                    .addComponent(bMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lImage, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lValue, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lAccess))
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Μέθοδος που αναλαμβάνει να κάνει όλη την απαραίτητη διαδικασία
     * όταν ο χρήστης "κλικάρει" ένα "εικονίδιο" μονάδας .
     * Στο ένα μόνο συνεχόμενο "κλικ" επιλέγεται(γίνεται πορτοκαλί) το εικονίδιο
     * μονάδας .
     * Αν κάνει ο χρήστης 2 συνεχόμενα "κλικ" τότε αν ο χρήστης έχει δικαίωμα
     * να αλλάξει την τιμή της μονάδας (Και βέβαια είναι ενεργή η εποπτεία στην συσκευή)
     * αλλάζει την τιμή της μονάδας .
     * Η αλλαγή μπορεί να γίνει είτε άμεσα αν η μονάδα είναι ρυθμισμένη να είναι
     * τύπου "switch" ή αλλιώς ανοίγει το {@link DfChangeValue} .
     * @param evt Default MouseEvent αντικείμενο το οποίο το χρησιμοποιούμε
     * για να βρούμε πόσα "κλικ" έχει πατήσει ο χρήστης .
     */
    private void clickedProcess(java.awt.event.MouseEvent evt)
    {
        if (evt.getClickCount()==1)
        {
            clearSelection(unit.device.getAllControls()) ;
            unit.setSelectedNode();
            if (!selected)
                setSelected(true) ;
        }
        else
        {
            if (!isReadOnly && unit.device.myPTab.isConnected)
            {
                if (dbLimit==null)
                {
                    new DfChangeValue( true, value, this).setVisible(true); ;
                }
                else
                {
                    if (isSwitch)
                    {
                        if (value>dbLimit)
                            setValue(String.valueOf(unit.min));
                        else
                            setValue(String.valueOf(unit.max));
                        if (mode == 3)
                        {
                            setMode(4) ;
                            unit.setMode(String.valueOf(mode));
                            unit.device.changeRemoteMode(unit.getFullMode());
                        }
                        unit.setValue(String.valueOf(value));
                        unit.device.changeRemoteValue(unit.getFullValue());
                    }
                    else
                        new DfChangeValue( true, value, this).setVisible(true);
                }
            }
        }
    }
    
    /**
     * Μέθοδος που αναλαμβάνει να κάνει όλη την απαραίτητη διαδικασία
     * όταν ο χρήστης αρπάξει ένα "εικονίδιο" μονάδας και αρχίσει να το
     * μεταφέρει .
     * Αρχικά κάνει επιλεγμένο-πορτοκαλί το εικονίδιο μονάδας και στην συνέχεια
     * αν δεν είναι "κλειδωμένο" το {@link UnitControl} το μεταφέρει μέσα στο
     * χώρο ανάλογα με την κίνηση του ποντικιού .
     * @param evt Default MouseEvent αντικείμενο το οποίο το χρησιμοποιούμε
     * για να βρούμε αν ο χρήστης έχει πατήσει το αριστερό κλικ .
     */
    private void draggedProcess(java.awt.event.MouseEvent evt)
    {
        if (!selected)
        {
            clearSelection(unit.device.getAllControls()) ;
            unit.setSelectedNode();
            setSelected(true) ;
        }
        if (!locked)
        {
            if (SwingUtilities.isLeftMouseButton(evt))
            {
                Point p = MouseInfo.getPointerInfo().getLocation() ;
                
                p.x -=  unit.device.backPanel.getLocationOnScreen().x + (this.size().getWidth()/2);
                p.y -= unit.device.backPanel.getLocationOnScreen().y+ (this.size().getHeight()/2);
                
                
                if (p.x<0) p.x = 0 ;
                if (p.y<0) p.y = 0 ;
                if (p.y> unit.device.backPanel.size().height-this.size().height) p.y =  unit.device.backPanel.size().height-this.size().height ;
                if (p.x> unit.device.backPanel.size().width-this.size().width) p.x =  unit.device.backPanel.size().width-this.size().width ;
                
                this.setLocation(p);
            }
        }
    }
    /**
     * Μέθοδος που μεταφέρει ένα {@link UnitControl} μέσα στο οπτικό πεδίο του
     * χρήστη αν για κάποιο λόγο δεν του εμφανίζεται το εικονίδιο μονάδας , πχ μετά
     * από κάποιο resize του κεντρικού παραθύρου του User Client .
     */
    public void fixToResize()
    {
        Point p = this.getLocation() ;
        
        if (p.x<0) p.x = 0 ;
        if (p.y<0) p.y = 0 ;
        if (p.y> unit.device.backPanel.size().height-this.size().height) p.y =  unit.device.backPanel.size().height-this.size().height ;
        if (p.x> unit.device.backPanel.size().width-this.size().width) p.x =  unit.device.backPanel.size().width-this.size().width ;
        
        this.setLocation(p);
    }
    
    /**
     * Μέθοδος που εκτελείτε όταν "κλικάρει" ο χρήστης το panel μας
     * Ο σκοπός του είναι να εκτελεί την μέθοδο clickedProcess .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην clickedProcess.
     */
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        clickedProcess(evt) ;
    }//GEN-LAST:event_formMouseClicked
    
    /**
     * Μέθοδος που εκτελείτε όταν τραβήξει-αρπάξει ο χρήστης το panel μας
     * Ο σκοπός του είναι να εκτελεί την μέθοδο draggedProcess .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην draggedProcess.
     */
    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        draggedProcess(evt) ;
    }//GEN-LAST:event_formMouseDragged
    
    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        
    }//GEN-LAST:event_formMouseMoved
    /**
     * Μέθοδος που εκτελείτε όταν τραβήξει-αρπάξει ο χρήστης το label της εικόνας μας
     * Ο σκοπός του είναι να εκτελεί την μέθοδο draggedProcess .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην draggedProcess.
     */
    private void lImageMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lImageMouseDragged
        draggedProcess(evt) ;
    }//GEN-LAST:event_lImageMouseDragged
    /**
     * Μέθοδος που εκτελείτε όταν τραβήξει-αρπάξει ο χρήστης το label του value μας
     * Ο σκοπός του είναι να εκτελεί την μέθοδο draggedProcess .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην draggedProcess.
     */
    private void lValueMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lValueMouseDragged
        draggedProcess(evt) ;
    }//GEN-LAST:event_lValueMouseDragged
    /**
     * Μέθοδος που εκτελείτε όταν τραβήξει-αρπάξει ο χρήστης το label του ID μας
     * Ο σκοπός του είναι να εκτελεί την μέθοδο draggedProcess .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην draggedProcess.
     */
    private void lIDMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lIDMouseDragged
        draggedProcess(evt) ;
    }//GEN-LAST:event_lIDMouseDragged
    /**
     * Μέθοδος που εκτελείτε όταν "κλικάρει" ο χρήστης το label του ID μας
     * Ο σκοπός του είναι να εκτελεί την μέθοδο clickedProcess .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην clickedProcess.
     */
    private void lIDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lIDMouseClicked
        clickedProcess(evt) ;
    }//GEN-LAST:event_lIDMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν "κλικάρει" ο χρήστης το label της εικόνας μας
     * Ο σκοπός του είναι να εκτελεί την μέθοδο clickedProcess .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην clickedProcess.
     */
    private void lImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lImageMouseClicked
        clickedProcess(evt) ;
    }//GEN-LAST:event_lImageMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν "κλικάρει" ο χρήστης το label του Value μας
     * Ο σκοπός του είναι να εκτελεί την μέθοδο clickedProcess .
     * @param evt Το Default MouseEvent αντικείμενο που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας χρησιμοποιείται με το να δίνεται σαν παράμετρος στην clickedProcess.
     */
    private void lValueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lValueMouseClicked
        clickedProcess(evt) ;
    }//GEN-LAST:event_lValueMouseClicked
    /**
     * Μέθοδος που εκτελείτε όταν "κλικάρει" ο χρήστης το κουμπί του Mode μας
     * Ο σκοπός του είναι να αλλάζει το mode με το αντίθετο του , δηλαδή από Remote
     * σε Auto και το αντίστροφο , αν βέβαια το επιτρέπει η αρχική ρύθμιση της μονάδας .
     * Αλλάζει το mode τόσο στο πρόγραμμα μας όσο και στην ίδια την συσκευή όπως επίσης
     * και σε όλους τους απομακρυσμένους UserClints που μπορεί να έχουν και αυτοί την ίδια
     * στιγμή εποπτεία πραγματικού χρόνου (μέσω του Server).
     * @param evt Το Default MouseEvent αντικείμενο το οποίο στην περίπτωση μας
     * δεν χρησιμοποιείται .
     */
    private void bModeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bModeMouseClicked
        if (unit.device.myPTab.isConnected)
        {
            if (bMode.isEnabled())
            {
                if (mode==3)
                    setMode(4);
                else if (mode ==4)
                    setMode(3);
                unit.setMode(String.valueOf(mode));
                unit.device.changeRemoteMode(unit.getFullMode());
            }
        }      
    }//GEN-LAST:event_bModeMouseClicked
    
    /**
     * Κλάση η οποία έχει κληρονομήσει την MouseAdapter και που πρακτικά
     * την φτιάξαμε με απώτερο σκοπό να έχουμε στο panel (και σε όλα τα αντικείμενα
     * που είναι πάνω του) μενού αν πατήσουμε δεξί "κλικ" .
     * Και πιο συγκεκριμένα για να μπορούμε να κλειδώνουμε σε κάποια θέση το
     * εκάστοτε {@link UnitControl} ή αντίστοιχα να το ξεκλειδώνουμε .
     */
    class PopClickListener extends MouseAdapter {
        /**
         * Μέθοδος που εκτελείται αν πατήσει ο χρήστης κάποιο πλήκτρο του ποντικιού.
         * @param e Default MouseEvent αντικείμενο που το χρησιμοποιούμε
         * για να δούμε αν είναι Popup ο trigger .
         */
        @Override
        public void mousePressed(MouseEvent e){
            if (e.isPopupTrigger())
                doPop(e);
        }
        /**
         * Μέθοδος που εκτελείται αν σταματήσει να πατάει ο χρήστης κάποιο πλήκτρο του ποντικιού.
         * @param e Default MouseEvent αντικείμενο που το χρησιμοποιούμε
         * για να δούμε αν είναι Popup ο trigger .
         */
        @Override
        public void mouseReleased(MouseEvent e){
            if (e.isPopupTrigger())
                doPop(e);
        }
        /**
         * Μέθοδος που φτιάχνει ένα custom JPopupMenu ({@link MyPopUpMenu}) και το εμφανίζει.
         * @param e Default MouseEvent αντικείμενο που το χρησιμοποιούμε
         * με σκοπό να αξιοποιήσουμε το Component του όπως και τη Χ και Y θέση του.
         */
        private void doPop(MouseEvent e){
            MyPopUpMenu menu = new MyPopUpMenu();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    /**
     * Κλάση η οποία έχει κληρονομήσει την JPopupMenu και σκοπό έχει
     * να φτιάξουμε κατάλληλα ένα δικό μας popup menu που θα εμφανίζεται όταν
     * ο χρήστης κάνει δεξί "κλικ" πάνω στο {@link UnitControl} και ουσιαστικά
     * για να μπορεί ο χρήστης να "κλειδώνει" ή να "ξεκλειδώνει" το "εικονίδιο" μονάδας.
     */
    class MyPopUpMenu extends JPopupMenu {
        JMenuItem anItem,anItem2;
        /**
         * Κατασκευαστής που απλά κάνει όλες τις απαραίτητες ενέργειες για να
         * αρχικοποιήσει το αντικείμενο στην ώρα εκτέλεσης .
         */
        public MyPopUpMenu(){
            anItem = new JMenuItem("Unlocked!");
            anItem2 = new JMenuItem("Locked!");
            add(anItem);
            add(anItem2);
            anItem.addMouseListener(new customMousePopUpListener());
            anItem2.addMouseListener(new customMousePopUpListener());
            /**
             * Άμεση δημιουργία προγραμματιστικά ενός PopupMenuListener
             */
            this.addPopupMenuListener(new PopupMenuListener() {
                /**
                 * Μέθοδος που εκτελείτε λίγο πριν πάει να εμφανιστεί το μενού.
                 * Πρακτικά κρύβει την επιλογή να ξεκλειδωθεί το {@link UnitControl}
                 * αν είναι ξεκλείδωτο και το ανάποδο .
                 * @param e Ένα Default PopupMenuEvent αντικείμενο το οποίο δεν
                 * το χρησιμοποιούμε .
                 */
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
                    if (locked)
                    {
                        anItem.setVisible(true);
                        anItem2.setVisible(false);
                    }
                    else
                    {
                        anItem.setVisible(false);
                        anItem2.setVisible(true);
                    }
                }
                /**
                 * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
                 * @param pme Ένα Default PopupMenuEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
                 */
                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                /**
                 * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
                 * @param pme Ένα Default PopupMenuEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
                 */
                @Override
                public void popupMenuCanceled(PopupMenuEvent pme) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
        }
    }
    class customMousePopUpListener implements MouseListener
    {
        /**
         * Μέθοδος που εκτελείτε όταν ο χρήστης αφήσει κάποιο κουμπί του
         * ποντικιού που είχε πατημένο .
         * Ουσιαστικά αλλάζει την κατάσταση του "κλειδώματος" με το ανάποδο
         * δηλαδή αν είναι κλειδωμένο το {@link UnitControl} το ξεκλειδώνει και το ανάποδο .
         * @param me Ένα Default MouseEvent αντικείμενο το οποίο δεν
         * το χρησιμοποιούμε .
         */
        @Override
        public void mouseReleased(MouseEvent me) {
            setLocked(!locked);
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
        /**
         * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
         * @param me Ένα Default MouseEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
         */
        @Override
        public void mousePressed(MouseEvent me) {
        }
        /**
         * Μέθοδος που απλά πρέπει να γίνει override και που πρακτικά δεν κάνει τίποτα.
         * @param me Ένα Default MouseEvent αντικείμενο το οποίο δεν το χρησιμοποιούμε.
         */
        @Override
        public void mouseClicked(MouseEvent me) {
            
        }
    }
    
    /*
    @Override
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
    }
    */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bMode;
    private javax.swing.JLabel lAccess;
    private javax.swing.JLabel lID;
    private javax.swing.JLabel lImage;
    private javax.swing.JLabel lValue;
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