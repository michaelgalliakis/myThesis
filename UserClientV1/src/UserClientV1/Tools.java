package UserClientV1;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
/**
 * Κλάση που έχει μέσα Static συναρτήσεις - εργαλεία με σκοπό να χρησιμοποιούνται
 * από παντού μέσα στο project .
 * @author Michael Galliakis
 */
public class Tools {
    /**
     * Συνάρτηση που παίρνει ένα Dercription και επιστρέφει το λεκτικό χωρίς
     * το ID συστήματος .
     * @param Description Ολόκληρη η περιγραφή μιας μονάδας ή ενός ελεγκτή .
     * @return Το λεκτικό της περιγραφής χωρίς το ID συστήματος που έχει.
     */
    public static String getOnlyDescriptionWithoutSysID(String Description)
    {
        return Description.replaceFirst("^\\[[\\d/]+\\]", "");
    }
    
    /**
     * Συνάρτηση που αν της δώσεις ένα ID συστήματος σου επιστρέφει μόνο το ID της βάσης.
     * Ουσιαστικά παίρνει μόνο το αριθμητικό αρχικό κομμάτι του ID συστήματος.
     * @param ID Συμβολοσειρά με το ID συστήματος.
     * @return Συμβολοσειρά με το ID της βάσης .
     */
    public static String getOnlyDBID(String ID)
    {
        String result  = "" ;
        char[] arrayOfChar = ID.toCharArray() ;
        for (int i = 0 ; i<arrayOfChar.length;i++)
            if (Character.isDigit(arrayOfChar[i]))
                result += arrayOfChar[i] ;
        
        return result ;
    }
    
    /**
     * Συνάρτηση που δημιουργήθηκε για να στέλνει ένα μήνυμα σε ένα socket με μια εντολή .
     * @param message Το μήνυμα που είναι να σταλεί μέσα από socket
     * @param pwReceiver Ένας {@link PrintWriter} για να στείλουμε μέσω αυτού το μήνυμα
     * μας στην "άλλη πλευρά" του socket .
     */
    public static void send(String message,PrintWriter pwReceiver)
    {
        pwReceiver.println(message);
        pwReceiver.flush();
    }
    
    /**
     * Συνάρτηση που ελέγχει αν είναι σωστό ένα command , δηλαδή αν το command που
     * λάβαμε από ένα message ενός socket είναι αυτό που περιμέναμε .
     * Επίσης απαντάει αυτοματοποιημένα στον αποστολέα σε περίπτωση λάθους .
     * @param command Η εντολή που λάβαμε .
     * @param awaitCommand Η εντολή που περιμένουμε να λάβουμε .
     * @param systemMessage Ένα μήνυμα για αναγνωριστικό ως προς το σύστημα .
     * @param messageReceived Το μήνυμα ολόκληρο που λάβαμε .
     * @param pwReceiver Ένας {@link PrintWriter} του αποστολέα του μηνύματος που θα παραλάβει τυχών απάντηση σε σφάλμα.
     * @return True αν είναι ίδια η εντολή που παραλάβαμε με αυτή που περιμέναμε , αλλιώς False .
     */
    public static boolean isCorrectCommand(String command,String awaitCommand,String systemMessage,String messageReceived,PrintWriter pwReceiver)
    {
        return isCorrectCommand(command,awaitCommand,systemMessage,messageReceived,pwReceiver,true) ;
    }
    
    /**
     * Συνάρτηση που ελέγχει αν είναι σωστό ένα command , δηλαδή αν το command που
     * λάβαμε από ένα message ενός socket είναι αυτό που περιμέναμε .
     * Επίσης απαντάει αυτοματοποιημένα στον αποστολέα σε περίπτωση λάθους .
     * @param command Η εντολή που λάβαμε .
     * @param awaitCommand Η εντολή που περιμένουμε να λάβουμε .
     * @param systemMessage Ένα μήνυμα για αναγνωριστικό ως προς το σύστημα .
     * @param messageReceived Το μήνυμα ολόκληρο που λάβαμε .
     * @param pwReceiver Ένας {@link PrintWriter} του αποστολέα του μηνύματος που θα παραλάβει τυχών απάντηση σε σφάλμα.
     * @param writeMessage Ένας είναι True στέλνει μήνυμα σφάλματος στον αποστολέα του μηνύματος.
     * @return True αν είναι ίδια η εντολή που παραλάβαμε με αυτή που περιμέναμε , αλλιώς False .
     */
    public static boolean isCorrectCommand(String command,String awaitCommand,String systemMessage,String messageReceived,PrintWriter pwReceiver,boolean writeMessage)
    {
        if (command.equals(awaitCommand))
        {
            return true ;
        }
        else if (command.equals("ERROR"))
        {
            if (writeMessage)
                printSMError(awaitCommand,systemMessage,messageReceived,pwReceiver) ;
            return false ;
        }
        else
        {
            if (writeMessage)
                printSMACError(awaitCommand,systemMessage,messageReceived,pwReceiver);
            return false ;
        }
        
    }
    
    /**
     * Συνάρτηση που εμφανίζει με πιο ωραίο τρόπο μηνύματα σφάλματος στο Server.
     * @param awaitCommand Η εντολή που περιμένουμε να λάβουμε .
     * @param messageFailed Ένα μήνυμα για αναγνωριστικό ως προς το σύστημα .
     * @param messageReceived Το μήνυμα ολόκληρο που λάβαμε .
     */
    public static void printSMError(String awaitCommand,String messageFailed,String messageReceived)
    {
        printSMError(awaitCommand,messageFailed,messageReceived,null);
    }
    
    /**
     * Συνάρτηση που εμφανίζει με πιο ωραίο τρόπο μηνύματα σφάλματος στο Server.
     * @param awaitCommand Η εντολή που περιμένουμε να λάβουμε .
     * @param messageFailed Ένα μήνυμα για αναγνωριστικό ως προς το σύστημα .
     * @param messageReceived Το μήνυμα ολόκληρο που λάβαμε .
     * @param pwReceiver Ένας {@link PrintWriter} του αποστολέα του μηνύματος που θα παραλάβει τυχών απάντηση σε σφάλμα.
     */
    public static void printSMError(String awaitCommand,String messageFailed,String messageReceived,PrintWriter pwReceiver)
    {
        Debug.print(awaitCommand +" ERROR(1)!!!") ;
        Debug.print(" Receiver  the message :("+messageReceived+ ") - ERROR(2)!!!") ;
        if (pwReceiver!=null)
            send(messageFailed,pwReceiver) ;
    }
    
    /**
     * Συνάρτηση που εμφανίζει με πιο ωραίο τρόπο μηνύματα σφάλματος στο Server.
     * @param awaitCommand Η εντολή που περιμένουμε να λάβουμε .
     * @param messageFailed Ένα μήνυμα για αναγνωριστικό ως προς το σύστημα .
     * @param messageReceived Το μήνυμα ολόκληρο που λάβαμε .
     */
    public static void printSMACError(String awaitCommand,String messageFailed,String messageReceived)
    {
        printSMACError(awaitCommand,messageFailed,messageReceived,null);
    }
    
    /**
     * Συνάρτηση που εμφανίζει με πιο ωραίο τρόπο μηνύματα σφάλματος στο Server.
     * @param awaitCommand Η εντολή που περιμένουμε να λάβουμε .
     * @param messageFailed Ένα μήνυμα για αναγνωριστικό ως προς το σύστημα .
     * @param messageReceived Το μήνυμα ολόκληρο που λάβαμε .
     * @param pwReceiver Ένας {@link PrintWriter} του αποστολέα του μηνύματος που θα παραλάβει τυχών απάντηση σε σφάλμα.
     */
    public static void printSMACError(String awaitCommand,String messageFailed,String messageReceived,PrintWriter pwReceiver)
    {
        Debug.print(" Await : " + awaitCommand +" and receiver another command - ERROR(1)!!!") ;
        Debug.print(" Receiver  the message :("+messageReceived+ ") - ERROR(2)!!!") ;
        if (pwReceiver!=null)
            send(messageFailed,pwReceiver) ;
    }
    
    
    /**
     * Είναι μια συνάρτηση που ελέγχει αν μια συμβολοσειρά είναι αριθμός .
     * @param sNum Μια συμβολοσειρά για έλεγχο .
     * @return True αν είναι αριθμός η συμβολοσειρά ή False αν δεν είναι .
     */
    public static boolean isNumeric(String sNum)
    {
        double res ;
        try {
            res = Double.parseDouble(sNum);
        }
        catch (Exception ex)
        {
            //Nothing
            return false ;
        }
        return true ;
    }
    
    /**
     * Είναι μια συνάρτηση που ελέγχει αν μια συμβολοσειρά είναι αποδεκτή πόρτα .
     * @param sPort Μια συμβολοσειρά για έλεγχο .
     * @return True αν είναι αποδεκτή πόρτα ή False αν όχι.
     */
    public static boolean isCorrectPort(String sPort)
    {
        int res  ;
        try {
            res = Integer.parseInt(sPort);
        }
        catch (Exception ex)
        {
            return false ;
        }
        if (res<0 || res>65536)
            return false ;
        return true ;
    }
    
    /**
     * Συνάρτιση που βρίσκει και επιστρέφει το TreePath ενός TreeNode .
     * @param treeNode Το TreeNode που θέλουμε να του βρούμε το TreePath.
     * @return Το TreePath που έχει το treeNode που έχουμε δώσει στην παράμετρο
     * και αν δεν έχει κάποιο TreePath επιστρέφει null.
     */
    public static TreePath getPath(TreeNode treeNode) {
        List<Object> nodes = new ArrayList<>();
        if (treeNode != null) {
            nodes.add(treeNode);
            treeNode = treeNode.getParent();
            while (treeNode != null) {
                nodes.add(0, treeNode);
                treeNode = treeNode.getParent();
            }
        }
        
        return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
    }
    
    /**
     * Κλάση που βοηθάει στο Debug .
     * Κυρίως εμφανίζει μηνύματα.
     */
    public static class Debug
    {
        Debug()
        {
            mikeSpentCounter = 0;
        }
        
        /**
         * Τυπώνει ένα μήνυμα στην οθόνη .
         * @param message Το μήνυμα
         */
        public static void print(String message)
        {
            try
            {
                //Globals.debugDoc.insertString(Globals.debugDoc.getLength(), "\n" + " | " + message + " | ", null);
                System.out.println(" | " + message + " | ");
            }
            catch(Exception e) { System.out.println(e); }
        }
        
        /**
         * Εμφανίζει στην οθόνη ένα Exception αναλυτικά.
         * @param ex Ένα {@link Exception} για να εμφανιστεί αναλυτικά στην οθόνη .
         */
        public static void printError(Exception ex)
        {
            //System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        private static int mikeSpentCounter ;
        
        /**
         * Μια μέθοδος για βοήθεια στο debugging .
         */
        public static void Spent()
        {
            System.out.println("Mike Spent here! :" + mikeSpentCounter++);
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