package DeviceClientV1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * Κλάση που έχει μέσα Static συναρτήσεις - εργαλεία με σκοπό να χρησιμοποιούνται
 * από παντού μέσα στο project .
 * @author Michael Galliakis
 */
public class Tools {
    
    /**
     * Μέθοδος που αναλαμβάνει να κάνει την διαδικασία που χρειάζεται για να πιστοποιηθεί
     * ο Device Client (Simulator) από το Server.
     * @param ms Ένα {@link ManageSocket} που έχει έτοιμα και ανοιχτά τα αντικείμενα
     * in και out του ενεργού socket για να μπορεί η μέθοδος να επικοινωνήσει αμφίδρομα
     * με το server και κατά επέκταση να πιστοποιηθεί ότι είναι αποδεκτός-σωστός client από το Server.
     * @return True αν έχει πραγματοποιηθεί η πιστοποίηση από το Server και False
     * σε οποιαδήποτε άλλη περίπτωση .
     */
    public static boolean certificationProcess(ManageSocket ms)
    {
        try {
            
            String message ;
            ManageSocketMessage msm  = new ManageSocketMessage();
            String tmp[][] = {{Globals.CERTIFICATIONDEVICE}} ;
            message = ManageSocketMessage.newMessage("D", ManageSocketMessage.CommandType.certification, tmp);
            Tools.send(message,ms.out) ;
            
            msm.reload(ms.in.readLine()) ;
            
            if (Tools.isCorrectCommand(msm.getCommand(),"Answer",Globals.messageFailed,msm.getMessage(),ms.out))
            {
                if ("FAILED".equals(msm.getParameters().get(0).get(0)))
                {
                    Tools.Debug.print("certifification Failed!");
                    Tools.send(Globals.messageFailed,ms.out);
                    return false;
                }
                else
                    Tools.Debug.print("certifification OK!");
            }
            else
                return false;
        } catch (IOException ex) {
            //Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            Tools.Debug.print("certifification exception!");
            return false ;
        }
        return true ;
    }
    /**
     * Μέθοδος που κάνει όλη την απαραίτητη διαδικασία για να γίνει πιστοποίηση
     * της συσκευής από το Server μας .
     * @param ms Ένα {@link ManageSocket} που έχει έτοιμα και ανοιχτά τα αντικείμενα
     * in και out του ενεργού socket για να μπορεί η μέθοδος να επικοινωνήσει αμφίδρομα
     * με το server και κατά επέκταση να πραγματοποιήσει την πιστοποίηση και να πάρει το
     * System Device ID από το server.
     * @param deviceName Το Devicename που έχει η συσκευή και έχει γράψει ο χρήστης.
     * @param passWord Το password που έχει η συσκευή και έχει γράψει ο χρήστης.
     * @return Επιστρέφει "X" αν υπάρξει κάποιο λάθος ή αλλιώς το System Device ID
     * που παίρνει από το Server.
     */
    public static String loginProcess(ManageSocket ms,String deviceName,String passWord)
    {
        try
        {
            String message ;
            ManageSocketMessage msm  = new ManageSocketMessage();
            
            String[][] tmp = {{deviceName},{passWord}};
            message = ManageSocketMessage.newMessage("D", ManageSocketMessage.CommandType.Login, tmp);
            Tools.send(message,ms.out) ;
            
            msm.reload(ms.in.readLine()) ;
            if (Tools.isCorrectCommand(msm.getCommand(),"LoginReply",Globals.messageFailed,msm.getMessage(),ms.out))
            {
                if ("FAILED".equals(msm.getParameters().get(0).get(0)))
                {
                    Tools.Debug.print("DeviceName or/and Password incorrect - Login Failed");
                    Tools.send(Globals.messageFailed,ms.out);
                    return "X";
                }
                else
                    Tools.Debug.print("DeviceName and Password Correct - Login OK!");
            }
            else
                return "X";
            
            if (msm.getParameters().get(1).size()>0)
                return msm.getParameters().get(1).get(0) ;
            else
                return "X" ;
        } catch (IOException ex) {
            //Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
            Tools.Debug.print("Login exception!");
            return "X" ;
        }
    }
    
    /**
     * Μια μέθοδος που κάνει με την σειρά σύνδεση στο server , πιστοποίηση εφαρμογής και τέλος
     * πιστοποίηση συσκευής .
     * Επιστρέφει ένα πλήρως συμπληρωμένο ManageSocket με όλες τις απαραίτητες πληροφορίες ή null
     * αν κάτι δεν πάει κάλα σε κάποιο από τα 3 βήματα .
     * @param devicename Το Devicename που έχει η συσκευή και έχει.
     * @param password Το password που έχει η συσκευή και έχει .
     * @param address Η διεύθυνση ip του Server που έχει .
     * @param port Η πόρτα του Server που έχει.
     * @return Ένα πλήρως συμπληρωμένο {@link ManageSocket} με όλες τις απαραίτητες πληροφορίες.
     */
    public static ManageSocket getFullManageSocket(String devicename,String password, String address,int port) //Return status
    {
        ManageSocket ms = Tools.connectProcess(address, port);
        if (ms==null)
        {
            Tools.Debug.print("Connect Failed!");
            
            return null;
        }
        if (!Tools.certificationProcess(ms))
        {
            Tools.Debug.print("certifification Failed!");
            
            return null ;
        }
        
        
        String SysDeviceID = Tools.loginProcess(ms, devicename, password);
        if (SysDeviceID.equals("X"))
        {
            Tools.Debug.print("Login Failed!");
            
            return null ;
        }
        ms.ID = SysDeviceID ;
        return ms ;
    }
    /**
     * Μέθοδος που επιχειρεί να στήσει μια σύνδεση - socket με το Server.
     * Αν πραγματοποιηθεί η σύνδεση τότε δημιουργούνται και τα κατάλληλα αντικείμενα
     * για να διαβάζει και να γράφει από και προς το socket(Server) και στην συνέχεια
     * επιστρέφεται ένα {@link ManageSocket} με όλες αυτές τις πληροφορίες
     * @param address Διεύθυνση IP για την σύνδεση με το Server.
     * @param port πόρτα που "ακούει" ο Server.
     * @return Ένα συμπληρωμένο {@link ManageSocket} με όλες τις απαραίτητες πληροφορίες.
     */
    public static ManageSocket connectProcess(String address,int port)
    {
        try
        {
            Socket userClientSocket = new Socket(address, port);
            
            PrintWriter out = new PrintWriter(userClientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(userClientSocket.getInputStream()));
            
            ManageSocket ms = new ManageSocket(userClientSocket, out, in, null, null) ;
            return ms ;
        }
        catch (Exception ex)
        {
            return null ;
        }
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
    
    
    
    public static final int MSGNOTHING = 0 ; //Είναι ένας συμβολικός αριθμός για να μην δείχνει κανένα face .
    //public static final int MSGNOTHINGEMO = 1 ; //Είναι ένας συμβολικός αριθμός για ένα "κανονικό" face .
    //public static final int MSGOKEMO = 2 ; //Είναι ένας συμβολικός αριθμός για ένα "χαμογελαστό" face .
    //public static final int MSGERROREMO = 3 ;//Είναι ένας συμβολικός αριθμός για ένα "στεναχωρημένο" face .
    
    public static final int MSGFROMSERVER = 4 ; //Είναι ένας συμβολικός αριθμός για ένα μήνυμα από το Server .
    public static final int MSGFROMARDUINO = 5 ; //Είναι ένας συμβολικός αριθμός για ένα μήνυμα από κάποιο Arduino .
    
    
    
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
         * Τυπώνει ένα μήνυμα στην οθόνη (μαζί με ένα "πρόσωπο" ή σύμβολο κατάστασης) .
         * @param message Το μήνυμα
         * @param msgoption Χαρακτηριστικό για να εμφανιστεί μαζί με το μήνυμα και ένα 'face' ή σύμβολο κατάστασης.
         */
        public static void print(String message,int msgoption)
        {
            try
            {
                if (Globals.viewMessage)
                {
                    String extramsg = "";
                    switch(msgoption)
                    {
                        case MSGNOTHING :
                            extramsg = "[ |P|: " ;
                            break ;
                        case MSGFROMSERVER :
                            extramsg = "[ |S|: " ;
                            break ;
                        case MSGFROMARDUINO :
                            extramsg = "[ |A|: " ;
                            break ;
                        default :
                            //Nothing
                    }
                    System.out.println(extramsg + message+" ]");
                    
                }
                else
                {
                    String extramsg = "";
                    switch(msgoption)
                    {
                        case MSGNOTHING :
                            extramsg = "[ |P|: " ;
                            System.out.println(extramsg + message+" ]");
                            break ;
                        default :
                            //Nothing
                    }
                }
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