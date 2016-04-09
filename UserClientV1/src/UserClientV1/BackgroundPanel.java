package UserClientV1;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * Κλάση η οποία έχει κληρονομήσει ένα JPanel
 * Πρακτικά είναι το panel που βρίσκεται πίσω από τις εκάστοτε μονάδες μιας συσκευής,
 * δηλαδή στο δεξί μέρος μιας καρτέλας({@link Ptab}) που έχει μέσα τα Units με πολύγωνη μορφή.
 * Έχει δημιουργηθεί κυρίως για να μπορούμε να έχουμε κάποια εικόνα πίσω από τις μονάδες
 * και φυσικά η εικόνα αυτή να μπορεί να αλλάζει .
 * @author Michael Galliakis
 */
public class BackgroundPanel extends javax.swing.JPanel{
    Image image ;
    public Device device ;
    /**
     * Default κατασκευαστής ο οποίος σχηματίζει το πάνελ με την εικόνα προεπιλογής που έχουμε.
     */
    public BackgroundPanel() {
        initComponents();
        image = Globals.imageBackground  ;
    }
    /**
     * Κατασκευαστής της κλάσης ο οποίος σχηματίζει το πάνελ με την εικόνα προεπιλογής
     * που έχουμε όπως επίσης συσχετίζει την κλάση μας με κάποιο συγκεκριμένο {@link Device}
     * @param dev Το {@link Device} που είναι συσχετισμένο το συγκεκριμένο panel .
     */
    public BackgroundPanel(Device dev) {
        initComponents();
        device = dev ;
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // device.fixUnitControlToResize();
                //System.out.println("New width: " + getWidth() + " New height: " + getHeight());
            }
        });
        image = Globals.imageBackground  ;
    }
    /**
     * Μέθοδος που μας επιτρέπει να αλλάζουμε την εικόνα του panel.
     * @param imagePathFile Ένα αρχείο εικόνας για να φορτωθεί στο panel .
     */
    public void setImage(File imagePathFile) {
        try {
            this.image = ImageIO.read(imagePathFile);
        } catch (IOException ex) {
            device.setPathFile("");
            image = Globals.imageBackground  ;
            //Logger.getLogger(BackgroundPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.updateUI();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Μέθοδος που εκτελείτε όταν πατήσει κάποιος χρήστης κάποιο κουμπί του ποντικιού
     * μέσα στο πάνελ.
     * @param evt Το Default MouseEvent που έχει το συγκεκριμένο event το οποίο στη
     * περίπτωση μας δεν χρησιμοποιείται.
     */
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        device.clearSelection();
    }//GEN-LAST:event_formMouseClicked
    /**
     * Μέθοδος που εκτελείτε κάθε φορά που επαναζωγραφίζεται το panel.
     * @param g Το Default Graphics αντικείμενο που έχει το συγκεκριμένο event το οποίο
     * χρησιμοποιούμε για να κάνουμε την εικόνα του panel να ίση με όλο το μέγεθος του (stretch).
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // see javadoc for more info on the parameters
        //device.fixUnitControlToResize() ;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
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