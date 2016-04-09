package DeviceClientV1;

import static DeviceClientV1.Tools.MSGFROMARDUINO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Κλάση που με λίγα λόγια αναλαμβάνει την διαχείριση της επικοινωνίας του προγράμματος μας
 * με τα arduino που είναι συνδεδεμένα πάνω στον υπολογιστή .
 * @author Michael Galliakis
 */
public class ReadArduino implements SerialPortEventListener {
    SerialPort serialPort;
    /** The port we're normally going to use. */
    public static final String PORT_NAMES[] = {
        "/dev/ttyACM0", // Raspberry Pi
        "/dev/ttyACM1", // Raspberry Pi
        "/dev/ttyACM2", // Raspberry Pi
        "/dev/ttyACM3", // Raspberry Pi
        "/dev/ttyACM4", // Raspberry Pi
        "/dev/ttyACM5", // Raspberry Pi
        "/dev/ttyACM6", // Raspberry Pi
        "/dev/ttyACM7", // Raspberry Pi
        "/dev/ttyUSB0", // Linux
        "/dev/ttyUSB1", // Linux
        "/dev/ttyUSB2", // Linux
        "/dev/ttyUSB3", // Linux
        "/dev/ttyUSB4", // Linux
        "/dev/ttyUSB5", // Linux
        "/dev/ttyUSB6", // Linux
        "/dev/ttyUSB7", // Linux
        "COM3", // Windows
        "COM4", // Windows
        "COM5", // Windows
        "COM6", // Windows
        "COM7", // Windows
        "COM8", // Windows
        "COM9", // Windows
        "COM10", // Windows
        "/dev/tty.usbserial-A9007UX1", // Mac OS X
        "/dev/tty.usbserial-A9007UX2", // Mac OS X
        "/dev/tty.usbserial-A9007UX3", // Mac OS X
        "/dev/tty.usbserial-A9007UX4", // Mac OS X
        "/dev/tty.usbserial-A9007UX5", // Mac OS X
        "/dev/tty.usbserial-A9007UX6", // Mac OS X
        "/dev/tty.usbserial-A9007UX7", // Mac OS X
        "/dev/tty.usbserial-A9007UX8", // Mac OS X
    };
    /**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     */
    public BufferedReader input;
    /** The output stream to the port */
    public OutputStream output;
    /** Milliseconds to block while waiting for port open */
    private static final int TIME_OUT = 5000;
    /** Default bits per second for COM port. */
    private static final int DATA_RATE = 9600;
    
    /**
     * Συνάρτηση που ψάχνει και βρίσκει όλα τα arduino που είναι συνδεδεμένα στον
     * υπολογιστή και στην συνέχεια επιστρέφει μια ArrayList με όλα τα arduino .
     * @return ArrayList με όλα τα arduino.
     */
    public static ArrayList<CommPortIdentifier> findAllArduino()
    {
        ArrayList<CommPortIdentifier> ar = new ArrayList();
        for (String test : PORT_NAMES) {
            System.setProperty("gnu.io.rxtx.SerialPorts", test);
            
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
            
            //First, Find an instance of serial port as set in PORT_NAMES.
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                for (String portName : PORT_NAMES) {
                    if (currPortId.getName().equals(portName)) {
                        portId = currPortId;
                        ar.add(portId);
                        //break;
                    }
                }
            }
        }
        return ar ;
    }
    /**
     * Συνάρτηση που ψάχνει και βρίσκει όλα τα arduino που είναι συνδεδεμένα στον
     * υπολογιστή και στην συνέχεια επιστρέφει μια HashMap με όλα τα arduino .
     * @return HashMap με όλα τα arduino.
     */
    public static HashMap<CommPortIdentifier,String> findAllArduinoHM()
    {
        HashMap<CommPortIdentifier,String> hm = new HashMap();
        for (String test : PORT_NAMES) {
            System.setProperty("gnu.io.rxtx.SerialPorts", test);
            
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
            
            //First, Find an instance of serial port as set in PORT_NAMES.
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                for (String portName : PORT_NAMES) {
                    if (currPortId.getName().equals(portName)) {
                        portId = currPortId;
                        hm.put(portId, "") ;
                        //break;
                    }
                }
            }
        }
        return hm ;
    }
    /**
     * Μέθοδος που αναλαμβάνει να προετοιμάσει κατάλληλα την επικοινωνία του προγράμματος
     * με την ανάλογη θύρα του υπολογιστή που είναι συνδεδεμένο το εκάστοτε arduino .
     * @param portId Ένα CommPortIdentifier που αφορά το συνδεδεμένο arduino πάνω στον υπολογιστή
     * @return True αν όλα πήγαν καλά και false αν υπήρξε εξαίρεση .
     */
    public boolean initialize(CommPortIdentifier portId) {
        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);
            
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            
            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();
            
            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            
            //output.write('b');
            //output.flush();
        } catch (Exception e) {
            System.err.println(e.toString());
            return false ;
        }
        return true ;
    }
    /**
     * Μέθοδος που κλείνει ολοκληρωμένα το Serial Port .
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }
    
    public boolean startRead = false ;
    private ManageSocketMessage msm  = new ManageSocketMessage();
    private String conName = "" ;
    private CommPortIdentifier currentArduinoPort ;
    
    /**
     * Μέθοδος που καταχωρεί το όνομα του Ελεγκτή και την τρέχον "πόρτα" του εκάστοτε arduino.
     * @param conName Όνομα του ελεγκτή
     * @param currentArduinoPort CommPortIndentifier με το τρέχον Arduino .
     */
    public void setConNameAndArduinoPort(String conName,CommPortIdentifier currentArduinoPort) {
        this.conName = conName;
        this.currentArduinoPort = currentArduinoPort;
    }
    
    /**
     * Μέθοδος που αναλαμβάνει να διαβάζει συνεχώς από τη σειριακή θύρα και κατά επέκταση
     * από την άλλη άκρη του καλωδίου δηλαδή από το εκάστοτε arduino .
     * @param oEvent Default παράμετρος που μας βοηθάει να δούμε αν περνάνε δεδομένα
     * από το σειριακό μας μέσο και αναλόγως αν περνάνε να τα διαβάσουμε .
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE && startRead) {
            try {
                
                String inputLine=input.readLine();
                Tools.Debug.print(inputLine,MSGFROMARDUINO);
                msm.reload(inputLine);
                if (msm.getCommand().equals("NewValues"))
                {
                    Globals.device.setValuesOfDevice(inputLine);
                    
                    if (Globals.manSocket != null)
                        Tools.send(inputLine, Globals.manSocket.out);
                }
                //Tools.Debug.print(inputLine);
                //reloadDevice(inputLine);
            } catch (Exception e) {
                //System.err.println(e.toString());
                
                if (Globals.manSocket != null)
                    Tools.send(Globals.device.createMessageDeleteController(conName), Globals.manSocket.out);
                Globals.hmAllArduino.remove(currentArduinoPort) ;
                Globals.hmAllReadArduino.remove(conName);
                Globals.device.deleteController(conName);
                Tools.Debug.print(conName + " Left!!");
                
                startRead = false ;
            }
        }
    }
    /**
     * Μεταβλητή που χρησιμοποιείται αποκλειστικά από τη παρακάτω μέθοδο : "stopWaftOfUSBCable".
     */
    public boolean glFlagFor_stopWaftOfUSBCable = false;
    /**
     * Μέθοδος που αναλαμβάνει να σταματήσει τη ροή μηνυμάτων που έρχονται από κάποιο arduino .
     * Σκοπός είναι να υπάρχει πλήρης συγχρονισμός της επικοινωνίας μεταξύ του arduino και
     * του προγραμματός μας (Device Client) .
     * @return True αν κατάφερε να σταματήσει τη ροή επιτυχημένα ή false αν δεν κατάφερε επιτυχημένα .
     */
    public boolean stopWaftOfUSBCable()
    {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                glFlagFor_stopWaftOfUSBCable = false ;
                String stopped = "" ;
                try {
                    output.write('e');
                    output.flush();
                    do {
                        stopped = input.readLine();
                        Tools.Debug.print("s" + stopped);
                    } while (!stopped.toUpperCase().equals("STOPPED!")) ;
                    glFlagFor_stopWaftOfUSBCable = true ;
                    timer.cancel();
                    timer.purge();
                } catch (IOException ex) {
                    Logger.getLogger(ReadArduino.class.getName()).log(Level.SEVERE, null, ex);
                    timer.cancel();
                    timer.purge();
                }
            }
        };
        timer.schedule(task,1800);
        try {
            Thread.sleep(2400);
        } catch (InterruptedException ex) {
            Logger.getLogger(ReadArduino.class.getName()).log(Level.SEVERE, null, ex);
        }
        timer.cancel();
        timer.purge();
        return glFlagFor_stopWaftOfUSBCable;
    }
    
    
    /**
     * Μέθοδος που αναλαμβάνει να προετοιμάσει την επικοινωνία του προγράμματος
     * με το Arduino . Πιο συγκεκριμένα , σε επίπεδο δικού μας πρωτοκόλλου , κάνει
     * όλα τα απαραίτητα βήματα για να στηθεί και να ξεκινήσει η επικοινωνία.
     * @param arduinoPort Ένα CommPortIdentifier του εκάστοτε Arduino
     * @return Επιστρέφει το όνομα του νέου arduino-ελεγκτή ή αν κάτι δεν
     * πάει καλά επιστρέφει null .
     */
    public String prepare(CommPortIdentifier arduinoPort)
    {
        try
        {
            currentArduinoPort = arduinoPort ;
            
            if (!stopWaftOfUSBCable())
                if (!stopWaftOfUSBCable())
                    return null ;
            
            output.write('v');
            output.flush();
            String version = input.readLine();
            Tools.Debug.print("Version : " + version,MSGFROMARDUINO);
            
            output.write(("d" + Globals.device.getSenderFullID()).getBytes());
            output.flush();
            String devicenameConfirm = input.readLine();
            Tools.Debug.print("deviceNameConfirm : " + devicenameConfirm,MSGFROMARDUINO);
            
            output.write('i');
            output.flush();
            
            String info = input.readLine();
            Tools.Debug.print("info : " + info,MSGFROMARDUINO);
            
            String correntCName = "";
            
            msm.reload(info) ;
            if (msm.getCommand().equals("NewController"))
            {
                Tools.Debug.print("Einai new controller!");
                String cName = msm.getParameters().get(0).get(0) ;
                correntCName = Globals.checkConNameIfthereIS(cName) ;
                if (!cName.equals(correntCName))
                {
                    info = info.replace("(" +cName+")", "(" +correntCName+")") ;
                    Tools.Debug.print("cName = " + cName +" correctCName = " + correntCName);
                    output.write('n');
                    output.write(correntCName.getBytes());
                    output.flush();
                    String OK = input.readLine();
                    Tools.Debug.print(OK,MSGFROMARDUINO);
                    
                }
                conName = correntCName ;
                
            }
            else
                return null ;
            //cName = Globals.checkConNameIfthereIS(cName) ;
            Globals.device.addController(info);
            Globals.hmAllArduino.replace(arduinoPort,conName) ;
            
            output.write('b');
            
            output.flush();
            
            //startRead = true ;
            
            Globals.hmAllReadArduino.put(conName, this);
        }
        catch (Exception ex)
        {
            Tools.Debug.print(ex.getMessage());
            return null ;
        }
        return conName ;
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