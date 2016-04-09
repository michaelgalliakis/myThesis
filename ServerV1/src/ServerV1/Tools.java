package ServerV1;

import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Κλάση που έχει μέσα Static συναρτήσεις - εργαλεία με σκοπό να χρησιμοποιούνται
 * από παντού μέσα στο project .
 * @author Michael Galliakis
 */
public class Tools {
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
     * @param systemMessage Ένα μήνυμα για αναγνωριστικό ως προς το σύστημα .
     * @param messageReceived Το μήνυμα ολόκληρο που λάβαμε .
     */
    public static void printSMError(String awaitCommand,String systemMessage,String messageReceived)
    {
        printSMError(awaitCommand,systemMessage,messageReceived,null);
    }
    /**
     * Συνάρτηση που εμφανίζει με πιο ωραίο τρόπο μηνύματα σφάλματος στο Server.
     * @param awaitCommand Η εντολή που περιμένουμε να λάβουμε .
     * @param systemMessage Ένα μήνυμα για αναγνωριστικό ως προς το σύστημα .
     * @param messageReceived Το μήνυμα ολόκληρο που λάβαμε .
     * @param pwReceiver Ένας {@link PrintWriter} του αποστολέα του μηνύματος που θα παραλάβει τυχών απάντηση σε σφάλμα.
     */
    public static void printSMError(String awaitCommand,String systemMessage,String messageReceived,PrintWriter pwReceiver)
    {
        Debug.print(systemMessage + " | " + awaitCommand +" ERROR(1)!!!",MSGERROREMO) ;
        Debug.print(systemMessage + " | Receiver  the message :("+messageReceived+ ") - ERROR(2)!!!",MSGERROREMO) ;
        if (pwReceiver!=null)
            send(Globals.messageFailed,pwReceiver) ;
    }
    /**
     * Συνάρτηση που εμφανίζει με πιο ωραίο τρόπο μηνύματα σφάλματος στο Server.
     * @param awaitCommand Η εντολή που περιμένουμε να λάβουμε .
     * @param systemMessage Ένα μήνυμα για αναγνωριστικό ως προς το σύστημα .
     * @param messageReceived Το μήνυμα ολόκληρο που λάβαμε .
     */
    public static void printSMACError(String awaitCommand,String systemMessage,String messageReceived)
    {
        printSMACError(awaitCommand,systemMessage,messageReceived,null);
    }
    
    /**
     * Συνάρτηση που εμφανίζει με πιο ωραίο τρόπο μηνύματα σφάλματος στο Server.
     * @param awaitCommand Η εντολή που περιμένουμε να λάβουμε .
     * @param systemMessage Ένα μήνυμα για αναγνωριστικό ως προς το σύστημα .
     * @param messageReceived Το μήνυμα ολόκληρο που λάβαμε .
     * @param pwReceiver Ένας {@link PrintWriter} του αποστολέα του μηνύματος που θα παραλάβει τυχών απάντηση σε σφάλμα.
     */
    public static void printSMACError(String awaitCommand,String systemMessage,String messageReceived,PrintWriter pwReceiver)
    {
        Debug.print(systemMessage + " | Await : " + awaitCommand +" and receiver another command - ERROR(1)!!!",MSGERROREMO) ;
        Debug.print(systemMessage + " | Receiver  the message :("+messageReceived+ ") - ERROR(2)!!!",MSGERROREMO) ;
        if (pwReceiver!=null)
            send(Globals.messageFailed,pwReceiver) ;
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
     * Συνάρτηση που επιστρέφει το μήνυμα που του δίνουμε στην παράμετρο
     * με κρυπτογραφημένη μορφή σύμφωνα με τον αλγόριθμο MD5 .
     * Στην περίπτωση μας χρησιμοποιείται γιατί θέλουμε να αποθηκεύονται οι
     * κωδικοί των λογαριασμών και χρηστών κρυπτογραφημένοι στην βάση .
     * Και έτσι αν για κάποιο λόγο διαρρεύσουν να μην μπορούν κάποιοι κακόβουλοι χρήστες
     * να αποκτήσουν πρόσβαση.
     * @param mess Το αρχικό μας μήνυμα .
     * @return Το κρυπτογραφημένο μήνυμα .
     */
    public static String getHashCode_MD5_Algorithm(String mess)
    {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(mess.getBytes());
            byte[] messageDigestMD5 = messageDigest.digest();
            StringBuilder stringBuffer = new StringBuilder();
            for (byte bytes : messageDigestMD5)
                stringBuffer.append(String.format("%02x", bytes & 0xff));
            return stringBuffer.toString() ;
        } catch (NoSuchAlgorithmException exception) {
            return "" ;
        }
    }
    
    public static final int MSGNOTHING = 0 ; //Είναι ένας συμβολικός αριθμός για να μην δείχνει κανένα face .
    public static final int MSGNOTHINGEMO = 1 ; //Είναι ένας συμβολικός αριθμός για ένα "κανονικό" face .
    public static final int MSGOKEMO = 2 ; //Είναι ένας συμβολικός αριθμός για ένα "χαμογελαστό" face .
    public static final int MSGERROREMO = 3 ;//Είναι ένας συμβολικός αριθμός για ένα "στεναχωρημένο" face .
    public static final int MSGSYSTEMOKEMO = 4 ;//Είναι ένας συμβολικός αριθμός για ένα "χαμογελαστό"
    //face που θα εμφανίζεται πάντα.
    
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
            print(message,MSGNOTHING);
        }
        /**
         * Τυπώνει ένα μήνυμα στην οθόνη (μαζί με ένα "πρόσωπο" κατάστασης) .
         * @param message Το μήνυμα
         * @param msgoption Χαρακτηριστικό για να εμφανιστεί μαζί με το μήνυμα και ένα 'face' κατάστασης.
         */
        public static void print(String message,int msgoption)
        {
            String extramsg = "";
            switch(msgoption)
            {
                case MSGNOTHING :
                    //extramsg = "" ;
                    break ;
                case MSGNOTHINGEMO :
                    extramsg = "[ :| ] " ;
                    if (Globals.viewMessages>1)
                        System.out.println(extramsg + message);
                    break ;
                case MSGOKEMO :
                    extramsg = "[ :) ] " ;
                    if (Globals.viewMessages>2)
                        System.out.println(extramsg + message);
                    break ;
                case MSGERROREMO :
                    extramsg = "[ :( ] " ;
                    if (Globals.viewMessages>0)
                        System.out.println(extramsg + message);
                    break ;
                case MSGSYSTEMOKEMO :
                    extramsg = "[ :) ] " ;
                    System.out.println(extramsg + message);
                    break ;
                default :
                    System.out.println(extramsg + message);
            }
            //System.out.println(extramsg + message);
        }
        /**
         * Εμφανίζει στην οθόνη ένα Exception αναλυτικά.
         * @param ex Ένα {@link Exception} για να εμφανιστεί αναλυτικά στην οθόνη .
         */
        public static void printError(Exception ex)
        {
            //System.out.println(ex.getMessage());
            //ex.printStackTrace();
            print(ex.getMessage(), MSGERROREMO);
        }
        
        private static int mikeSpentCounter ;
        /**
         * Μια μέθοδος για βοήθεια στο debugging .
         */
        public static void Spent()
        {
            System.out.println("Spent here! :" + mikeSpentCounter++);
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