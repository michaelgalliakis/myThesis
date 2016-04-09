package DeviceClientV1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/**
 * Κλάση που μας βοηθάει να κάνουμε καλύτερη διαχείριση του περιεχομένου μιας συσκευής .
 * Είναι όλες οι πληροφορίες που αφορούν μια συσκευή μαζί με μεθόδους που μας δίνουν
 * την δυνατότητα να κάνουμε ομαδοποιημένα πράγματα πολύ εύκολα και στοχευμένα
 * να επηρεάσουμε το περιεχόμενο της συσκευής μας .
 * @author Michael Galliakis
 */
public class Device {
    private String deviceName ;
    private String devicePassword ;
    private String deviceID ;
    private String senderFullID ;
    
    private HashMap<String,HashMap<String,Unit>> allUnits ;
    private ManageSocketMessage msm ;
    int sumOfControllers ;
    /**
     * Αρχικοποιούμε τη συσκευή μας .
     * @param dn Συμβολοσειρά του Device Name
     * @param dp Συμβολοσειρά του Device Password
     * @param id Συμβολοσειρά του Device ID της βάσης
     * @param sfID Συμβολοσειρά του Device ID του συστήματος όπως φαίνεται από τον αποστολέα .
     */
    public Device(String dn,String dp,String id,String sfID) {
        deviceName = dn ;
        devicePassword = dp ;
        deviceID = id ;
        senderFullID = sfID ;
        allUnits = new HashMap<>() ;
        msm = new ManageSocketMessage() ;
        sumOfControllers = 0 ;
    }
    
    /**
     * Μέθοδος που με βάση μια λίστα από μηνύματα πρωτοκόλλου γίνεται η προετοιμασία
     * της συσκευής μας και ουσιαστικά γεμίζει με περιεχόμενο το Device μας .
     * @param newControllers Μια λίστα με συμβολοσειρές με μηνύματα πρωτοκόλλου
     * που ουσιαστικά προσθέτουν στο Device μας ελεγκτές μαζί με τις μονάδες τους .
     */
    public void prepareDevice(ArrayList<String> newControllers)
    {
        sumOfControllers = newControllers.size() ;
        allUnits.clear();
        
        String conName,unitName,unitType,unitValue ;
        String unitMode;
        String unitLimit;
        String unitMax ;
        String unitMin;
        String unitTag ;
        HashMap<String, Unit> hmUnits ;
        for (String mess : newControllers)
        {
            msm.reload(mess);
            conName = msm.getParameters().get(0).get(0) ;
            hmUnits = new HashMap<>() ;
            System.out.println(msm.getMessage()) ;
            for (int i = 1 ; i<msm.getParameters().size();i++)
            {
                unitName = msm.getParameters().get(i).get(0) ;
                unitType = msm.getParameters().get(i).get(1) ;
                unitMode = msm.getParameters().get(i).get(2) ;
                unitTag = msm.getParameters().get(i).get(3) ;
                if (msm.getParameters().get(i).size()>4)
                    unitValue = msm.getParameters().get(i).get(4) ;
                else
                    unitValue = "0" ;
                
                
                unitMax = msm.getParameters().get(i).get(5) ;
                unitMin = msm.getParameters().get(i).get(6) ;
                unitLimit = msm.getParameters().get(i).get(7) ;
                Unit newUnit = new Unit(unitName,unitType,unitValue,unitMode,unitLimit,unitMax,unitMin,unitTag) ;
                
                hmUnits.put(unitName, newUnit) ;
            }
            allUnits.put(conName,hmUnits) ;
        }
    }
    /**
     * Μέθοδος που επιστρέφει μια λίστα από μηνύματα για την αρχικοποίηση
     * των ελεγκτών της συσκευής (μαζί με τις μονάδες τους)
     * @return Μια λίστα με συμβολοσειρές .
     */
    public ArrayList<String> getNewControllerMessages()
    {
        ArrayList<String> result = new ArrayList<>() ;
        
        ArrayList<ArrayList <String>> parameters = new ArrayList<>();
        ArrayList<String> alCon ;
        Iterator<String> keyIterCon = allUnits.keySet().iterator();
        String keyCon;
        
        while(keyIterCon.hasNext())
        {
            alCon  = new ArrayList<>();
            keyCon = keyIterCon.next() ;
            alCon.add(keyCon) ;
            alCon.add(String.valueOf(allUnits.get(keyCon).size())) ;
            parameters.add(alCon) ;
        }
        result.add(ManageSocketMessage.newMessage(senderFullID, ManageSocketMessage.CommandType.InitControllers, parameters)) ;
        
        
        String keyUnit  ;
        Iterator<String> keyIterUnit ;
        keyIterCon = allUnits.keySet().iterator();
        while(keyIterCon.hasNext())
        {
            keyCon = keyIterCon.next() ;
            parameters = new ArrayList<>();
            alCon = new ArrayList<>();
            alCon.add(keyCon) ;
            parameters.add(alCon) ;
            
            keyIterUnit = allUnits.get(keyCon).keySet().iterator() ;
            while(keyIterUnit.hasNext())
            {
                keyUnit = keyIterUnit.next() ;
                alCon = new ArrayList<>();
                alCon.add(keyUnit) ;
                alCon.add(allUnits.get(keyCon).get(keyUnit).getType()) ;
                alCon.add(allUnits.get(keyCon).get(keyUnit).mode) ;
                alCon.add(allUnits.get(keyCon).get(keyUnit).tag) ;
                alCon.add(allUnits.get(keyCon).get(keyUnit).getValue()) ;
                alCon.add(allUnits.get(keyCon).get(keyUnit).max) ;
                alCon.add(allUnits.get(keyCon).get(keyUnit).min) ;
                alCon.add(allUnits.get(keyCon).get(keyUnit).limit) ;
                
                parameters.add(alCon) ;
            }
            result.add(ManageSocketMessage.newMessage(senderFullID, ManageSocketMessage.CommandType.NewController, parameters)) ;
        }
        
        return result ;
    }
    /**
     * Μέθοδος που με βάση ένα μήνυμα πρωτοκόλλου (που μεταφέρετε μέσα από socket δηλαδή)
     * αλλάζει στοχευμένα το value σε ένα ή περισσότερα Unit της συσκευής .
     * @param valuesOfMessage Συμβολοσειρά που έχει μορφή μηνύματος πρωτοκόλλου .
     */
    public void setValuesOfDevice(String valuesOfMessage)
    {
        String conName,unitName,unitValue ;
        
        msm.reload(valuesOfMessage);
        for (int i = 0 ; i<msm.getParameters().size();i++)
        {
            conName = msm.getParameters().get(i).get(0) ;
            unitName = msm.getParameters().get(i).get(1) ;
            unitValue = msm.getParameters().get(i).get(2) ;
            
            allUnits.get(conName).get(unitName).setValue(unitValue);
        }
    }
    /**
     * Μέθοδος που με βάση ένα μήνυμα  πρωτοκόλλου (που μεταφέρετε μέσα από socket δηλαδή)
     * αλλάζει στοχευμένα το Mode σε ένα ή περισσότερα Unit της συσκευής .
     * @param modesOfMessage Συμβολοσειρά που έχει μορφή μηνύματος  πρωτοκόλλου .
     */
    public void setModesOfDevice(String modesOfMessage)
    {
        String conName,unitName,unitMode ;
        
        msm.reload(modesOfMessage);
        for (int i = 0 ; i<msm.getParameters().size();i++)
        {
            conName = msm.getParameters().get(i).get(0) ;
            unitName = msm.getParameters().get(i).get(1) ;
            unitMode = msm.getParameters().get(i).get(2) ;
            
            allUnits.get(conName).get(unitName).setMode(unitMode);
        }
    }
    /**
     * Μέθοδος που επιστρέφει ένα δισδιάστατο πίνακα με τα στοιχεία των μονάδων
     * της συσκευής για να γεμίσει ο πίνακας (JTable) του προσομοιωτή .
     * @return Δισδιάστατος πίνακας με τα στοιχεία των μονάδων της συσκευής .
     */
    public Object[][] getRowData()
    {
        int rows = 0 ;
        Iterator<String> keyIterCon = allUnits.keySet().iterator();
        Iterator<String> keyIterUnit ;
        
        String keyCon,keyUnit ;
        while(keyIterCon.hasNext())
        {
            keyCon = keyIterCon.next() ;
            rows += allUnits.get(keyCon).size() ;
        }
        
        String[][] rowDataTable = new String [rows][9] ;
        
        keyIterCon = allUnits.keySet().iterator();
        int counter = 0;
        while(keyIterCon.hasNext())
        {
            keyCon = keyIterCon.next() ;
            keyIterUnit = allUnits.get(keyCon).keySet().iterator() ;
            while(keyIterUnit.hasNext())
            {
                keyUnit = keyIterUnit.next() ;
                Unit un = allUnits.get(keyCon).get(keyUnit);
                rowDataTable[counter][0] = keyCon ;
                rowDataTable[counter][1] = un.getName() ;
                rowDataTable[counter][2] = un.getType();
                rowDataTable[counter][3] = un.mode ;
                rowDataTable[counter][4] = un.tag;
                rowDataTable[counter][5] = un.getValue() ;
                rowDataTable[counter][6] = un.max ;
                rowDataTable[counter][7] = un.min ;
                rowDataTable[counter][8] = un.limit;
                counter++ ;
            }
        }
        
        return rowDataTable ;
    }
    //Begin : (Διάφοροι απλοί get μέθοδοι)
    public String getSenderFullID() {
        return senderFullID;
    }
    
    public String getDeviceID() {
        return deviceID;
    }
    
    public String getDeviceName() {
        return deviceName;
    }
    
    public String getDevicePassword() {
        return devicePassword;
    }
    
    public HashMap<String, HashMap<String, Unit>> getAllUnits() {
        return allUnits;
    }
    //End : (Διάφοροι απλοί get μέθοδοι)
    
    /**
     * Κλάση που έχει όλη την πληροφορία για μια μονάδα μας .
     */
    public class Unit
    {
        private String name ;
        private String myType ;
        private String value ;
        
        public String mode ;
        public String tag ;
        
        public String limit,max,min ;
        public Unit(String n,String t) {
            name = n ;
            myType = t ;
            value = "0" ;
        }
        public Unit(String n,String t,String v,String mo,String l,String ma,String mi,String ta) {
            name = n ;
            myType = t ;
            value = v ;
            mode = mo ;
            max = ma ;
            min = mi ;
            limit = l ;
            tag = ta ;
        }
        //Begin : (Διάφοροι απλοί set μέθοδοι)
        public void setUnit(String n,String t,String v)
        {
            if (name!=null)
                name = n ;
            if (myType!=null)
                myType = t ;
            if (value!=null)
                value = v ;
        }
        
        public void setMode(String mode) {
            this.mode = mode;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
        //End : (Διάφοροι απλοί set μέθοδοι)
        
        //Begin : (Διάφοροι απλοί get μέθοδοι)
        public String getType() {
            return myType;
        }
        
        public String getName() {
            return name;
        }
        
        public String getValue() {
            return value;
        }
        public String getUnit()
        {
            return "("+name+"|"+myType+")" ;
        }
        //End : (Διάφοροι απλοί get μέθοδοι)
        
        @Override
        public String toString() {
            return "("+name+"|"+myType+"|"+value+")" ;
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