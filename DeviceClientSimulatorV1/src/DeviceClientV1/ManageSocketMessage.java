package DeviceClientV1 ;

import java.util.ArrayList;
/**
 * Κλάση που διαχειρίζεται εξ ολοκλήρου τα διάφορα μηνύματα που μεταφέρονται
 * μέσα από τα socket .
 * Δημιουργήθηκε ένα “πρωτόκολλο” επικοινωνίας για να γίνεται δομημένα και
 * αυτοματοποιημένα η επικοινωνία με τη χρήση των socket
 * Τα μηνύματα έχουν μια συγκεκριμένη μορφή :
 * #{SystemID}@{Τύπος Αποστολέα(S,D,UD)}${Εντολή}:{Πλήθος παραμέτρων}*{(Παράμετρος0.0)}{(Παράμετρος1.0|Παράμετρος1.1});
 * @author Michael Galliakis
 */
public class ManageSocketMessage
{
    CommandType commandType ;
    String strCommand ;
    int intSumOfParameters ;
    String strSenderType ;
    String strSenderDBID ;
    ArrayList<ArrayList<String>> alParameters ;
    String message ;
    boolean correct ;
    /**
     * Διάφοροι τύποι μηνυμάτων .
     */
    enum CommandType{
        Error
        ,Failed
        ,Login
        ,LoginReply
        ,certification
        ,GetDevicesInfo
        ,PutDevicesInfo
        ,BringMeDevice
        ,InitControllers
        ,NewController
        ,AlterController
        ,DeleteController
        ,NewValues
        ,InitializationFinished
        ,ChangeValues
        ,ChangeModes
        ,Anwser
        ,GetTypeUser
        ,PutTypeUser
        ,TSNC
    }
    /**
     * Μέθοδος που ψάχνει να βρει αν υπάρχει κάποιος τύπος.
     * @param comm λέξη κλειδί που θα αναζητηθεί .
     * @return Επιστρέφετε ο τύπος που βρέθηκε και αν δεν βρέθηκε επιστρέφετε το TSNC
     */
    private CommandType findCommandType(String comm)
    {
        for (CommandType co : CommandType.values())
            if (co.name().equals(comm))
                return co ;
        return CommandType.TSNC ;
    }
    /**
     * Στατική μέθοδος που δημιουργεί με βάση κάποιους παραμέτρους ένα έγκυρο
     * μήνυμα χωρίς συντακτικά λάθη για να μπορεί να μεταφερθεί μέσα από socket .
     * @param IDAndType Μια συμβολοσειρά που έχει το ID και το Τύπο του αποστολέα.
     * @param command Μια συμβολοσειρά που έχει το όνομα της εντολής του μηνύματος.
     * @param parameters Ένας δισδιάστατος πίνακας που θα έχει μέσα όλους τους παραμέτρους του μηνύματος.
     * @return Επιστρέφετε μια συμβολοσειρά με όλο το μήνυμα σωστά συνταγμένο .
     */
    public static String newMessage(String IDAndType,CommandType command,String[][] parameters)
    {
        String message = "#"+ IDAndType + "$" + command.name() +":" + parameters.length + "*" ;
        
        for (int i = 0 ; i<parameters.length;i++)
        {
            message +="(" ;
            if (parameters[i].length > 0)
                message += parameters[i][0] ;
            
            for (int j = 1 ; j<parameters[i].length;j++)
                message += "|" + parameters[i][j] ;
            
            message +=")" ;
        }
        message +=";" ;
        
        return message ;
    }
    /**
     * Στατική μέθοδος που δημιουργεί με βάση κάποιους παραμέτρους ένα έγκυρο
     * μήνυμα χωρίς συντακτικά λάθη για να μπορεί να μεταφερθεί μέσα από socket .
     * @param IDAndType Μια συμβολοσειρά που έχει το ID και το Τύπο του αποστολέα.
     * @param command Μια συμβολοσειρά που έχει το όνομα της εντολής του μηνύματος.
     * @param parameters Μια λίστα μέσα σε μια λίστα από συμβολοσειρές που θα έχει μέσα όλους τους παραμέτρους του μηνύματος.
     * @return Επιστρέφετε μια συμβολοσειρά με όλο το μήνυμα σωστά συνταγμένο .
     */
    public static String newMessage(String IDAndType,CommandType command,ArrayList<ArrayList<String>> parameters)
    {
        String message = "#"+ IDAndType + "$" + command.name() +":" + parameters.size() + "*" ;
        
        for (int i = 0 ; i<parameters.size();i++)
        {
            message +="(" ;
            if (parameters.get(i).size() > 0)
                message += parameters.get(i).get(0) ;
            
            for (int j = 1 ; j<parameters.get(i).size();j++)
                message += "|" +  parameters.get(i).get(j) ;
            
            message +=")" ;
        }
        message +=";" ;
        
        return message ;
    }
    /**
     * Στατική μέθοδος που δημιουργεί με βάση κάποιους παραμέτρους ένα έγκυρο
     * μήνυμα χωρίς συντακτικά λάθη για να μπορεί να μεταφερθεί μέσα από socket .
     * @param IDAndType Μια συμβολοσειρά που έχει το ID και το Τύπο του αποστολέα.
     * @param command Μια συμβολοσειρά που έχει το όνομα της εντολής του μηνύματος.
     * @param sumOfParameters Πλήθος των παραμέτρων .
     * @param parameters Μια συμβολοσειρά με όλους τους παραμέτρους (Μαζί με παρενθέσεις και '|' όπου χρειάζεται)
     * @return Επιστρέφετε μια συμβολοσειρά με όλο το μήνυμα σωστά συνταγμένο .
     */
    public static String newMessage(String IDAndType,CommandType command,int sumOfParameters,String parameters)
    {
        return "#"+ IDAndType + "$" + command.name() +":" + sumOfParameters + "*" + parameters + ";" ;
    }
    
    /**
     * Μέθοδος που φορτώνει μια συμβολοσειρά και (εφόσον είναι σύμφωνη με το πρωτόκολλο)
     * βρίσκει και διαχωρίζει όλα τα στοιχεία του μηνύματος , για να μπορούμε να παίρνουμε
     * στοχευμένα την πληροφορία που θέλουμε κάθε φορά.
     * @param mess Συμβολοσειρά που έχει ένα μήνυμα πρωτοκόλλου για επικοινωνία μέσα από socket.
     * @return True αν διαβαστεί κανονικά το μήνυμα και False αν δεν είναι σωστό το μήνυμα.
     */
    public boolean reload(String mess)
    {
        correct = true ;
        message = mess ;
        try {
            
            strCommand = mess.substring(mess.indexOf("$")+1,mess.indexOf(":")) ;
            commandType = findCommandType(strCommand) ;
            intSumOfParameters =  Integer.parseInt(mess.substring(mess.indexOf(":")+1,mess.indexOf("*"))) ;
            
            if (message.contains("@"))
            {
                strSenderType = mess.substring(mess.indexOf("@")+1,mess.indexOf("$")) ;
                strSenderDBID = mess.substring(mess.indexOf("#")+1,mess.indexOf("@")) ;
            }
            else
            {
                strSenderType =  mess.substring(mess.indexOf("#")+1,mess.indexOf("$")) ;
                strSenderDBID = "-1" ;
            }
            
            alParameters = new ArrayList() ;
            int j ;
            for (int i = 0 ; i < intSumOfParameters;i++)
            {
                alParameters.add(new ArrayList()) ;
                
                String tmpString = mess.substring(mess.indexOf("(")+1,mess.indexOf(")")) ;
                j = 0 ;
                while (tmpString.contains("|"))
                {
                    
                    alParameters.get(i).add(tmpString.substring(0,tmpString.indexOf("|"))) ;
                    tmpString = tmpString.substring(tmpString.indexOf("|")+1,tmpString.length()) ;
                }
                alParameters.get(i).add(tmpString) ;
                mess = mess.substring(mess.indexOf(")")+1,mess.length()) ;
            }
        }
        catch (Exception ex)  {
            
            strCommand  = "ERROR";
            correct = false ;
            return false ;
        }
        return true ;
    }
    /**
     * Επιστρέφει την εντολή ενός μηνύματος .
     * @return Εντολή σε συμβολοσειρά
     */
    public String getCommand()
    {
        return strCommand ;
    }
    /**
     * Επιστρέφει την εντολή ενός μηνύματος .
     * @return  Εντολή σε {@link CommandType}
     */
    public CommandType getCommandType() {
        return commandType;
    }
    /**
     * Επιστρέφει την εντολή ενός μηνύματος .
     * @return  Εντολή σε συμβολοσειρά (παίρνοντας υπόψιν το {@link CommandType})
     */
    public String getCommandTypeStr() {
        return commandType.name() ;
    }
    /**
     * Επιστρέφει το πλήθος των παραμέτρων .
     * @return Πλήθος με int .
     */
    public int getSumParameters()
    {
        return intSumOfParameters ;
    }
    /**
     * Επιστρέφει το τύπο του αποστολέα .
     * @return Συμβολοσειρά με το τύπο .
     */
    public String getSenderType()
    {
        return strSenderType ;
    }
    /**
     * Επιστρέφει το ID που έχει στη βάση δεδομένων ο αποστολέας.
     * @return Σειμβολοσειρά με το ID της βάσης.
     */
    public String getDBID()
    {
        return strSenderDBID ;
    }
    /**
     * Επιστρέφει μια λίστα που έχει λίστες από συμβολοσειρές με τους παραμέτρους.
     * @return Παράμετροι μέσα σε λίστα {@link ArrayList} που έχει λίστες από συμβολοσειρές
     */
    public ArrayList<ArrayList<String>> getParameters()
    {
        return alParameters ;
    }
    /**
     * Επιστρέφει ατόφιο το μήνυμα όπως το διάβασε αρχικά η reload .
     * @return Συμβολοσειρά που έχει μέσα ατόφιο το αρχικό μήνυμα .
     */
    public String getMessage()
    {
        return message ;
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