package DeviceClientV1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 * Κλάση που έχει μέσα Static συναρτήσεις - εργαλεία με σκοπό να χρησιμοποιούνται
 * από παντού μέσα στο project .
 * @author Michael Galliakis
 */
public class Tools {
    public static final Random random = new Random();
    
    /**
     * Μέθοδος που αναλαμβάνει ομαδοποιημένα να επιστρέφει ένα τυχαίο μήνυμα πρωτοκόλλου
     * τύπου "NewValues" . Δηλαδή πρακτικά επιστρέφει ένα κατάλληλα συμπληρωμένο μήνυμα
     * με την εικονική αλλαγή τιμών κάποιων τυχαίων μονάδων(Units) από τη συσκευή(Device) μας.
     * Παίρνει υπόψιν του και τον αριθμό 3 που καθορίζει την πιθανότητα που υπάρχει
     * για να προκληθεί κάποια εικονική αλλαγή (δηλαδή 1 προς 3 για να υπάρξει αλλαγή) .
     * Στην περίπτωση που δεν υπάρξει εικονική αλλαγή ούτε μίας μονάδας επιστρέφει "X" .
     * @param dev Η συσκευή από την οποία θα προκύψουν εικονικές αλλαγές .
     * @return Επιστρέφει το μήνυμα τύπου 'NewValues' με τις εικονικές αλλαγές που έχουν
     * "προκληθεί" στο Device μας ή αν δεν έχουν προκληθεί αλλαγές επιστρέφεται το "X" .
     */
    public static String getNewRandomDataForDevice(Device dev)
    {
        return getNewRandomDataForDevice(dev,3) ;
    }
    /**
     * Μέθοδος που αναλαμβάνει ομαδοποιημένα να επιστρέφει ένα τυχαίο μήνυμα πρωτοκόλλου
     * τύπου "NewValues" . Δηλαδή πρακτικά επιστρέφει ένα κατάλληλα συμπληρωμένο μήνυμα
     * με την εικονική αλλαγή τιμών κάποιων τυχαίων μονάδων(Units) από τη συσκευή(Device) μας.
     * Παίρνει υπόψιν του και μια παράμετρο(change) που καθορίζει την πιθανότητα που υπάρχει
     * για να προκληθεί κάποια εικονική αλλαγή .
     * Στην περίπτωση που δεν υπάρξει εικονική αλλαγή ούτε μίας μονάδας επιστρέφει "X" .
     * @param dev Η συσκευή από την οποία θα προκύψουν εικονικές αλλαγές .
     * @param chance Η πιθανότητα που υπάρχει για να προκληθεί κάποια αλλαγή
     * Αν είναι 1 το chance τότε θα υπάρχει σε κάθε χρονική στιγμή που εκτελείτε η μέθοδος αλλαγή
     * τιμών αλλιώς η πιθανότητα είναι 1 προς τη τιμή της chance .
     * @return Επιστρέφει το μήνυμα τύπου 'NewValues' με τις εικονικές αλλαγές που έχουν
     * "προκληθεί" στο Device μας ή αν δεν έχουν προκληθεί αλλαγές επιστρέφεται το "X" .
     */
    public static String getNewRandomDataForDevice(Device dev,int chance)
    {
        Double max,min,diff ;
        String result = "" ;
        ArrayList<ArrayList <String>> parameters ;
        ArrayList<String> alCon ;
        Iterator<String> keyIterCon = dev.getAllUnits().keySet().iterator();
        String keyCon;
        
        String keyUnit  ;
        Iterator<String> keyIterUnit ;
        String rVal,unitMode ;
        parameters = new ArrayList<>();
        while(keyIterCon.hasNext())
        {
            keyCon = keyIterCon.next() ;
            
            
            keyIterUnit = dev.getAllUnits().get(keyCon).keySet().iterator() ;
            while(keyIterUnit.hasNext())
            {
                keyUnit = keyIterUnit.next() ;
                unitMode = dev.getAllUnits().get(keyCon).get(keyUnit).mode ;
                if (random.nextInt(chance) == 0 && !unitMode.equals("1") && !unitMode.equals("4"))
                {
                    max = Double.parseDouble(dev.getAllUnits().get(keyCon).get(keyUnit).max) ;
                    min = Double.parseDouble(dev.getAllUnits().get(keyCon).get(keyUnit).min) ;
                    diff = max-min ;
                    alCon = new ArrayList<>();
                    alCon.add(keyCon) ;
                    alCon.add(keyUnit) ;
                    rVal = String.valueOf(min+random.nextInt(diff.intValue())) ;
                    alCon.add(rVal) ;
                    parameters.add(alCon) ;
                }
            }
            
        }
        if (parameters.size()>0)
            result = ManageSocketMessage.newMessage(dev.getSenderFullID(), ManageSocketMessage.CommandType.NewValues, parameters) ;
        else
            result = "X" ;
        return result ;
    }
    /**
     * Μέθοδος που ουσιαστικά με βάση κάποιο "πίνακα" που έχει όλα τα Units μας , μια "λίστα"
     * με τους Controllers και ένα ID συστήματος μιας συσκευής επιστρέφει μια λίστα με μηνύματα
     * πρωτοκόλλου τύπου 'NewController' με ενσωματωμένες όλες τις παραπάνω πληροφορίες .
     * Πρακτικά χρησιμοποιείται για να δημιουργεί τα κατάλληλα μηνύματα 'NewController' με
     * βάση το πίνακα των μονάδων και τη λίστα των ελεγκτών που υπάρχει στην {@link Fmain} μας
     * όπως ακόμη και με το System ID της συσκευής μας που έχουμε πάρει από το Server από
     * παλαιότερη διαδικασία.
     * Αυτά τα μηνύματα χρησιμοποιούνται για την αρχικοποίηση του {@link Device} μας .
     * @param dtm Ένα DefaultTableModel που περιέχει όλα τα απαραίτητα στοιχεία όλων των μονάδων μας
     * @param dlm Ένα DefaultListModel που περιέχει όλους τους ελεγκτές της συσκευής .
     * @param SysDeviceID Το ID συστήματος της συσκευής μας που έχουμε πάρει από το Server .
     * @return Μια λίστα με όλα τα κατάλληλα μηνύματα πρωτοκόλλου τύπου 'NewController' .
     */
    public static ArrayList<String> convertTableOfDataToNewControllersParammeters(DefaultTableModel dtm,DefaultListModel dlm,String SysDeviceID)
    {
        ArrayList<String> newControllers = new ArrayList<>() ;
        Vector<Vector<String>> vItems = dtm.getDataVector() ;
        String IDAndType  = SysDeviceID +"@D" ;
        
        for (int i = 0 ; i<dlm.getSize();i++)
        {
            String conName = (String) dlm.getElementAt(i);
            boolean firstTime = true ;
            String parameters = "" ;
            int countSubParameters = 1 ;
            for (int j = 0 ; j< vItems.size();j++)
            {
                String itemConName = vItems.get(j).get(0) ;
                
                if (conName.equals(itemConName))
                {
                    if (firstTime)
                    {
                        parameters += "(" +conName+")" ;
                        firstTime = false ;
                    }
                    countSubParameters ++ ;
                    String subParameter = "(";
                    
                    int k = 1 ;
                    for(;k<vItems.get(j).size()-1;k++)
                        subParameter += vItems.get(j).get(k) + "|" ;
                    subParameter += vItems.get(j).get(k) + ")" ;
                    System.out.println(subParameter) ;
                    parameters += subParameter  ;
                }
            }
            if (!firstTime)
                newControllers.add(ManageSocketMessage.newMessage(IDAndType, ManageSocketMessage.CommandType.NewController,countSubParameters, parameters)) ;
        }
        return newControllers ;
    }
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
     * αν κάτι δεν πάει καλά σε κάποιο από τα 3 βήματα .
     * @param mainFrame Το κύριο frame μας ({@link Fmain}) για να μπορούμε μέσω της μεθόδου να εμφανίσουμε
     * τα όποια μηνύματα βοήθειας για κάποιο πιθανόν σφάλμα .
     * @param devicename Το Devicename που έχει η συσκευή και έχει γράψει ο χρήστης.
     * @param password Το password που έχει η συσκευή και έχει γράψει ο χρήστης.
     * @param address Η διεύθυνση ip του Server που έχει γράψει ο χρήστης .
     * @param strPort Η πόρτα του Server που έχει γράψει ο χρήστης.
     * @return Ένα πλήρως συμπληρωμένο {@link ManageSocket} με όλες τις απαραίτητες πληροφορίες.
     */
    public static ManageSocket getFullManageSocket(Fmain mainFrame,String devicename,String password, String address,String strPort) //Return status
    {
        int port ;
        try
        {
            port = Integer.parseInt(strPort) ;
        }
        catch(Exception ex)
        {
            return null ;
        }
        
        ManageSocket ms = Tools.connectProcess(address, port);
        if (ms==null)
        {
            Tools.Debug.print("Connect UnsuccessFully!");
            JOptionPane.showMessageDialog(mainFrame,"Connect Failed","ThesisV1",JOptionPane.PLAIN_MESSAGE);
            return null;
        }
        if (!Tools.certificationProcess(ms))
        {
            Tools.Debug.print("certifification UnsuccessFully!");
            JOptionPane.showMessageDialog(mainFrame,"certifification Failed","ThesisV1",JOptionPane.PLAIN_MESSAGE);
            return null ;
        }
        
        
        String SysDeviceID = Tools.loginProcess(ms, devicename, password);
        if (SysDeviceID.equals("X"))
        {
            Tools.Debug.print("Login UnsuccessFully!");
            JOptionPane.showMessageDialog(mainFrame,"Login Failed","ThesisV1",JOptionPane.PLAIN_MESSAGE);
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
     * Συνάρτηση που επιστρέφει ένα πίνακα αλφαριθμητικών (τυχαίου πλήθους)
     * με τα ονόματα κάποιων τυχαίων ελεγκτών κάθε φορά.
     * @return Ένας αλφαριθμητικός πίνακας με τα ονόματα όλων των τυχαίων ελεγκτών.
     */
    public static String[] getRandomControllers()
    {
        int sumCon = random.nextInt(5)+1 ;
        String tmp[] = new String[sumCon] ;
        
        for (int i = 0 ; i<sumCon;i++)
            tmp[i] = new String();
        
        return tmp ;
    }
    /**
     * Συνάρτηση που επιστρέφει ένα δισδιάστατο πίνακα αλφαριθμητικών με τυχαία
     * δεδομένα για να συμπληρωθεί αυτόματα ο πίνακας των Units στην φόρμα {@link Fmain}.
     * @param controllers Ένας πίνακας αλφαριθμητικών με τα ονόματα όλως των ελεγκτών.
     * @return Ο δισδιάστατος πίνακας με όλα τα τυχαία Units μαζί με τα υπόλοιπα στοιχεία τους .
     */
    public static String[][] getRandomData(String[] controllers)
    {
        int[] sumOfUnitsArray = new int[controllers.length];
        int sumOfAllUnits = 0 ;
        for (int i = 0 ;i<controllers.length;i++)
        {
            sumOfUnitsArray[i] = random.nextInt(5)+1 ;
            sumOfAllUnits += sumOfUnitsArray[i] ;
        }
        
        
        String[][] data = new String[sumOfAllUnits][Globals.ColumnNames.length];
        for (int i = 0;i<controllers.length;i++)
            for (int j = 0 ;j<Globals.ColumnNames.length;j++)
                data[i][j] = new String() ;
        
        int counter = 0;
        for (int i = 0 ;i<controllers.length;i++)
        {
            String conName = "Con"+ i ;
            controllers[i] = conName ;
            int sumUnits = sumOfUnitsArray[i] ;
            for (int j = 0 ;j<sumUnits;j++)
            {
                String unitName = "Unit"+ j ;
                String UnType =  getRandomType() ;
                String unMode = getRandomMode(UnType) ;
                String unTag ="" ;
                String unValue = String.valueOf(random.nextInt(256)) ;
                String unMax = "255";
                String unMin = "0" ;
                String unLimit = getRandomLimit(UnType);
                
                data[counter][0] = conName ;
                data[counter][1] = unitName ;
                data[counter][2] = UnType ;
                data[counter][3] = unMode ;
                data[counter][4] = unTag ;
                data[counter][5] = unValue ;
                data[counter][6] = unMax ;
                data[counter][7] = unMin ;
                data[counter][8] = unLimit ;
                counter++ ;
            }
        }
        return data ;
    }
    
    /**
     * Συνάρτηση που επιστρέφει ένα τυχαίο τύπο μιας μονάδας .
     * Υπάρχουν "βάρη" για να έχουμε περισσότερες ή λιγότερες πιθανότητες να
     * εμφανιστεί κάποιος συγκεκριμένος τύπος .
     * Έχει φτιαχτεί έτσι για να είναι όσο γίνεται πιο κοντά στην πραγματικότητα
     * η περίπτωση μιας τυχαίας προσομοίωσης μας .
     * @return Ένα αλφαριθμητικό με το τυχαίο τύπο κάθε φορά .
     */
    private static String getRandomType()
    {
        String returnValue = "2" ;
        int tmp = random.nextInt(30) ;
        switch(tmp)
        {
            case 0 :
                returnValue = "0" ;
                break ;
            case 1 :
            case 2 :
            case 3 :
                returnValue = "1" ;
                break ;
            case 4 :
            case 5 :
            case 6 :
            case 28 : //Μπήκε μετά
            case 29 : //Μπήκε μετά
                returnValue = "2" ;
                break ;
            case 7 :
            case 8 :
                returnValue = "3" ;
                break ;
            case 9 :
            case 10 :
                returnValue = "4" ;
                break ;
            case 11 :
            case 12 :
                returnValue = "5" ;
                break ;
            case 13 :
            case 14 :
                returnValue = "6" ;
                break ;
            case 15 :
            case 16 :
                returnValue = "7" ;
                break ;
            case 17 :
            case 18 :
                returnValue = "8" ;
                break ;
            case 19 :
            case 20 :
                returnValue = "9" ;
                break ;
            case 21 :
            case 22 :
                returnValue = "10" ;
                break ;
            case 23 :
            case 24 :
            case 25 :
            case 26 :
            case 27 :
                returnValue = "11" ;
                break ;
            default :
                //Nothing
        }
        return returnValue ;
    }
    
    /**
     * Συνάρτηση που επιστρέφει ένα τυχαίο mode μιας μονάδας .
     * @param type Ένας τύπος για να μην επιστραφεί κάποιο mode που δεν ταιριάζει με
     * τον εκάστοτε τύπο . Για παράδειγμα ένα θερμόμετρο δεν μπορεί να έχει mode 3 - δηλαδή
     * ότι μπορεί να αλλάξει την κατάσταση του κάποιος χρήστης .
     * @return Ένα αλφαριθμητικό με το τυχαίο mode (με βάση και το τύπο) μιας μονάδας κάθε φορά .
     */
    private static String getRandomMode(String type)
    {
        String returnValue = "2" ;
        int tmp ;
        switch(type)
        {
            case "0" :
                returnValue = "0" ;
                break ;
            case "1" :
                tmp = random.nextInt(11) ;
                if (tmp==0)
                    returnValue = "0" ;
                else if (tmp>=1 && tmp<=3)
                    returnValue = "1" ;
                else if (tmp>=4 && tmp<=6)
                    returnValue = "2" ;
                else if (tmp>=7 && tmp<=8)
                    returnValue = "3" ;
                else if (tmp>=9 && tmp<=10)
                    returnValue = "4" ;
                //returnValue = String.valueOf(random.nextInt(5)) ;
                break ;
            case "2" :
                tmp = random.nextInt(11) ;
                if (tmp==0)
                    returnValue = "0" ;
                else if (tmp>=1 && tmp<=3)
                    returnValue = "1" ;
                else if (tmp>=4 && tmp<=6)
                    returnValue = "2" ;
                else if (tmp>=7 && tmp<=8)
                    returnValue = "3" ;
                else if (tmp>=9 && tmp<=10)
                    returnValue = "4" ;
                //returnValue = String.valueOf(random.nextInt(5)) ;
                break ;
            case "3" :
                returnValue = "0" ;
                break ;
            case "4" :
                returnValue = "0" ;
                break ;
            case "5" :
                returnValue = "0" ;
                break ;
            case "6" :
                returnValue = "0" ;
                break ;
            case "7" :
                returnValue = "0" ;
                break ;
            case "8" :
                returnValue = "0" ;
                break ;
            case "9" :
                returnValue = "0" ;
                break ;
            case "10" :
                returnValue = "0" ;
                break ;
            case "11" :
                tmp = random.nextInt(11) ;
                if (tmp==0)
                    returnValue = "0" ;
                else if (tmp>=1 && tmp<=3)
                    returnValue = "1" ;
                else if (tmp>=4 && tmp<=6)
                    returnValue = "2" ;
                else if (tmp>=7 && tmp<=8)
                    returnValue = "3" ;
                else if (tmp>=9 && tmp<=10)
                    returnValue = "4" ;
                //returnValue = String.valueOf(random.nextInt(5)) ;
                break ;
            default :
                //Nothing
                
        }
        return returnValue ;
        
    }
    
    /**
     * Συνάρτηση που επιστρέφει ένα τυχαίο λεκτικό για το limit μιας μονάδας .
     * @param type Ένας τύπος για να μην επιστραφεί κάποιο "limit" που δεν ταιριάζει με
     * τον εκάστοτε τύπο . Για παράδειγμα ένα θερμόμετρο δεν μπορεί να έχει Limit = SW
     * γιατί ο χρήστης δεν δεν μπορεί να εναλλάξει την κατάσταση του απομακρυσμένα.
     * @return Ένα αλφαριθμητικό με το τυχαίο Limit λεκτικό (με βάση και το τύπο) μιας μονάδας κάθε φορά .
     */
    private static String getRandomLimit(String type)
    {
        String returnValue = "NL" ;
        switch(type)
        {
            case "0" :
                returnValue = "NL" ;
                break ;
            case "1" :
                returnValue = (random.nextBoolean())?"128SW":"1TB" ;
                break ;
            case "2" :
                returnValue = (random.nextBoolean())?"128SW":"1TB" ;
                break ;
            case "3" :
                returnValue = "NL" ;
                break ;
            case "4" :
                returnValue = "NL" ;
                break ;
            case "5" :
                returnValue = "NL" ;
                break ;
            case "6" :
                returnValue = "NL" ;
                break ;
            case "7" :
                returnValue = "NL" ;
                break ;
            case "8" :
                returnValue = "NL" ;
                break ;
            case "9" :
                returnValue = "NL" ;
                break ;
            case "10" :
                returnValue = "NL" ;
                break ;
            case "11" :
                returnValue = "128SW" ;
                break ;
            default :
                //Nothing
        }
        return returnValue ;
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
                //  System.out.println(message);
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