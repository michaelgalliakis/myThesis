package ServerV1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Κλάση που αναλαμβάνει να εξυπηρετήσει το εκάστοτε socket ανάλογα με το τύπο εφαρμογής που το δημιούργησε.
 * Έχει διάφορους μεθόδους που γίνονται κάποια βασικά βήματα όπως πιστοποίηση εφαρμογής και λογαριασμού
 * αλλά και διάφορες άλλες διαδικασίες .
 * @author Michael Galliakis
 */
public class ClientServiceThread extends Thread {
    public static ArrayList<ClientServiceThread> arrayWithClientServiceThreads = new ArrayList<>() ;
    public static HashMap<String,ManageSocket> hmUserSockets = new HashMap<>();
    static ArrayList<String> alAllUsers = new ArrayList<>() ;
    public long clientID = -1;
    private String clientSystemMessage  ;
    
    
    ManageSocketMessage msm ;
    ManageSocket manSocket ;
    ClientServiceThread(Socket s, long i) throws IOException {
        Socket clientSocket = s;
        clientID = i;
        clientSystemMessage = " temporaryID : "  + clientID +" ";
        PrintWriter   out ;
        BufferedReader   in ;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        manSocket = new ManageSocket(clientSocket,out,in,null,null) ;
        Server.connectDatabase(manSocket) ;
        msm = new ManageSocketMessage() ;
    }
    
    @Override
    public void run() {
        
        arrayWithClientServiceThreads.add(this);
        
        Tools.Debug.print("Accepted Client |"+clientSystemMessage+" , Address - " + manSocket.clientSocket.getInetAddress().getHostName(),Tools.MSGOKEMO);
        try {
            String typeOfClient = certificationProccess() ;
            if (!typeOfClient.equals("X"))
            {
                String DBID = loginProccess() ;
                if (typeOfClient.equals("D") && !DBID.equals("X"))
                {
                    manSocket.clientSystemMessage = clientSystemMessage ;
                    new ManageDevice(manSocket) ;
                }
                else if (typeOfClient.equals("UD") && !DBID.equals("X"))
                {
                    if (userClientProccess().equals("X")) ;
                    //(DBID) ;
                }
                else
                    manSocket.close();
            }
            else
                manSocket.close();
            
        } catch (Exception ex)
        {
            Tools.Debug.printError(ex);
        }
        
        Tools.Debug.print("Finished clientServiceThread | temporaryID : " + clientID + " SysID :"+manSocket.SID +" , Address : " + manSocket.clientSocket.getInetAddress().getHostName(),Tools.MSGOKEMO);
        arrayWithClientServiceThreads.remove(this);
    }
    
    /**
     * Μέθοδος που ελέγχει αν είναι πιστοποιημένο το socket .
     * Δηλαδή αν το socket προέρχεται από ένα "Device Client" ή "User Client" που μπορεί να επικοινωνήσει
     * στη συνέχεια ο Server μας .
     * @return Επιστρέφει το τύπο του Socket (D ή UD) ή Χ για να δείξει ότι δεν εγκρίθηκε η πιστοποίηση .
     */
    private String certificationProccess()
    {
        try {
            msm.reload(manSocket.in.readLine()) ;
            if (Tools.isCorrectCommand(msm.getCommand(),"certification",clientSystemMessage,msm.getMessage(),manSocket.out))
            {
                switch (msm.getSenderType())
                {
                    case ("D") :
                        if (Globals.CERTIFICATIONDEVICE.equals(msm.getParameters().get(0).get(0)))
                        {
                            Tools.Debug.print("Client |"+clientSystemMessage+ " certification OK! Type : Device",Tools.MSGOKEMO);
                            Tools.send(Globals.messageOK, manSocket.out);
                            return "D" ;
                        }
                        else
                        {
                            Tools.Debug.print("Client |"+clientSystemMessage+ " certification Failed!",Tools.MSGERROREMO);
                            Tools.send(Globals.messageFailed, manSocket.out);
                            return "X" ;
                        }
                    //break ;
                    case ("UD") :
                        if (Globals.CERTIFICATIONUSERDESKTOP.equals(msm.getParameters().get(0).get(0)))
                        {
                            Tools.Debug.print("Client |"+clientSystemMessage + " certification OK! Type : UserDesktop",Tools.MSGOKEMO);
                            Tools.send(Globals.messageOK, manSocket.out);
                            return "UD" ;
                        }
                        else
                        {
                            Tools.Debug.print("Client |"+clientSystemMessage + " certification Failed!",Tools.MSGERROREMO);
                            Tools.send(Globals.messageFailed, manSocket.out);
                            return "X" ;
                        }
                    //break ;
                    default :
                        Tools.Debug.print("Client |"+clientSystemMessage+ " certification Failed ERROR!",Tools.MSGERROREMO);
                        Tools.send(Globals.messageFailed, manSocket.out);
                        return "X" ;
                }
            }
            else
                return "X" ;
        } catch (Exception ex)
        {
            Tools.Debug.printError(ex);
            return "X" ;
        }
    }
    /**
     * Μέθοδος που στέλνει σε όλους του χρήστες που είναι συνδεδεμένοι στο server ότι προστέθηκε και
     * είναι έτοιμη για εποπτεία μία νέα συσκευή . Βέβαια στέλνετε μόνο στους χρήστες που έχουν
     * δικαιώματα στη συσκευή .
     * @param ms Ένα {@link ManageSocket} για να γίνουν τα κατάλληλα ερωτήματα στη βάση μέσα από την ανοικτή σύνδεση που έχει.
     * @param DBIDDevice  Το ID της βάσης για τη συσκευή που προστέθηκε .
     */
    public static synchronized void sendAllUserNewDevice(ManageSocket ms,String DBIDDevice)
    {
        try {
            Boolean send = false ;
            
            ms.dbRes = ms.dbSt.executeQuery("SELECT Owner as User" +
                    " FROM devices d" +
                    " WHERE d.DeviceID = " + DBIDDevice +
                    " UNION" +
                    " SELECT UserID as User" +
                    " FROM deviceaccess " +
                    " WHERE  DeviceID = " + DBIDDevice );
            
            while (ms.dbRes.next())
            {
                String DBUserID = String.valueOf(ms.dbRes.getInt("User")) ;
                
                Iterator<String> keyIterUserSocket ;
                keyIterUserSocket = hmUserSockets.keySet().iterator();
                String userID ;
                while(keyIterUserSocket.hasNext())
                {
                    userID = keyIterUserSocket.next() ;
                    if (userID.startsWith(DBUserID))
                    {
                        ManageSocket tmpMS = hmUserSockets.get(userID) ;
                        if (tmpMS!=null)
                            GetDevicesInfoProccess(tmpMS) ;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Διαδικασία που χρειάζεται για να πιστοποιηθεί το socket και κατά επέκταση
     * ο λογαριασμός είτε χρήστη είτε συσκευής .
     * @return Το ID συστήματος ή X αν κάτι δεν πήγε καλά και δεν έγινε πιστοποίηση .
     * @throws SQLException Αν υπάρξει πρόβλημα με ερώτημα στη βάση .
     */
    private String loginProccess() throws SQLException
    {
        try {
            msm.reload(manSocket.in.readLine()) ;
            if (Tools.isCorrectCommand(msm.getCommand(),"Login",clientSystemMessage,msm.getMessage(),manSocket.out))
            {
                switch (msm.getSenderType())
                {
                    case ("D") :
                        String deviceName = msm.getParameters().get(0).get(0) ;
                        String devicePass = msm.getParameters().get(1).get(0) ;
                        manSocket.dbRes = manSocket.dbSt.executeQuery("SELECT DeviceID FROM devices where DeviceName = '" + deviceName+"' and Password = '"+Tools.getHashCode_MD5_Algorithm(devicePass)+"'");
                        if (manSocket.dbRes.next())
                        {
                            manSocket.DBID = String.valueOf(manSocket.dbRes.getInt("DeviceID")) ;
                            
                            manSocket.SID = manSocket.DBID + "D" ;
                            
                            boolean thereIs = false;
                            for (ManageDevice item : ManageDevice.listWithConnectedDevices )
                            {
                                if(item.deviceManSocket.SID.equals(manSocket.SID))
                                {
                                    thereIs = true ;
                                    break ;
                                }
                            }
                            if (!thereIs)
                            {
                                clientSystemMessage += ", SysID : "+manSocket.SID + " " ;
                                Tools.Debug.print("Device Client |"+clientSystemMessage + "| Login OK!",Tools.MSGOKEMO);
                                Tools.send("#S$LoginReply:2*(OK)("+manSocket.SID+");", manSocket.out);
                                sendAllUserNewDevice(manSocket,manSocket.DBID) ;
                                return manSocket.SID ;
                            }
                            else
                            {
                                Tools.Debug.print("Device Client |"+clientSystemMessage + ", DBID : "+manSocket.DBID + " | Login Failed - Already exists!",Tools.MSGNOTHINGEMO);
                                Tools.send("#S$LoginReply:2*(FAILED)(Already exists);", manSocket.out);
                                return "X" ;
                            }
                        }
                        else
                        {
                            Tools.Debug.print("Device Client |"+clientSystemMessage + ", DBID : "+manSocket.DBID + " | Login Failed!",Tools.MSGNOTHINGEMO);
                            Tools.send("#S$LoginReply:1*(FAILED);", manSocket.out);
                            return "X" ;
                        }
                    case ("UD") :
                        String userName = msm.getParameters().get(0).get(0) ;
                        String userPass = msm.getParameters().get(1).get(0) ;
                        manSocket.dbRes =  manSocket.dbSt.executeQuery("SELECT UserID FROM users where UserName = '" + userName+"' and Password = '"+Tools.getHashCode_MD5_Algorithm(userPass)+"'");
                        if (manSocket.dbRes.next())
                        {
                            manSocket.DBID = String.valueOf(manSocket.dbRes.getInt("UserID"));
                            manSocket.SID = manSocket.DBID + "UD" ;
                            Integer counter = 0 ;
                            for (String item : alAllUsers)
                            {
                                if (item.equals(manSocket.SID +counter))
                                    counter++ ;
                            }
                            
                            manSocket.SID += counter.toString() ;
                            alAllUsers.add(manSocket.SID) ;
                            clientSystemMessage += " , SysID : "+manSocket.SID + " ";
                            Tools.Debug.print("UserDesktop Client |"+clientSystemMessage+"| User Login OK!",Tools.MSGOKEMO);
                            Tools.send("#S$LoginReply:2*(OK)("+manSocket.SID+");", manSocket.out);
                            return manSocket.SID ;
                        }
                        else
                        {
                            Tools.send("#S$LoginReply:1*(FAILED);", manSocket.out);
                            Tools.Debug.print("UserDesktop Client |"+clientSystemMessage + ", DBID : "+manSocket.DBID + " | User Login Failed ERROR(1)!!!",Tools.MSGNOTHINGEMO);
                            Tools.Debug.print("UserDesktop Client |"+clientSystemMessage + ", DBID : "+manSocket.DBID + " | Receiver the message :("+msm.getMessage()+ ") - ERROR(2)!!!",Tools.MSGNOTHINGEMO);
                            return "X" ;
                        }
                    default :
                        Tools.printSMError(" Login Failed SenderType", clientSystemMessage,msm.getMessage(),manSocket.out);
                        return "X" ;
                }
            }
            else
                return "X" ;
        } catch (Exception ex)
        {
            Tools.Debug.printError(ex);
            return "X" ;
        }
    }
    
    /**
     * Διαδικασία που αναλαμβάνει να στείλει σε κάποιο χρήστη , όλες τις συσκευές που έχει
     * για εποπτεία και πληροφορίες της κατάστασης τους και τα δικαιώματα προσπέλασης.
     * @param ms Ένα {@link ManageSocket} για να γίνουν τα κατάλληλα ερωτήματα στη βάση μέσα από την ανοικτή σύνδεση που έχει
     * αλλά και για να στείλει τις πληροφορίες των συσκευών στο συγκεκριμένο socket-χρήστη που τον αφορά.
     * @throws SQLException Αν υπάρξει πρόβλημα με ερώτημα στη βάση .
     */
    public static void GetDevicesInfoProccess(ManageSocket ms) throws SQLException
    {
        if (!ms.clientSocket.isClosed())
        {
            ResultSet res ;
            ms.dbRes = ms.dbSt.executeQuery("SELECT d.DeviceID , d.DeviceName, da.TypeAccess" +
                    " FROM devices d" +
                    " LEFT JOIN deviceaccess da ON d.DeviceID = da.DeviceID AND da.USERID = "+ms.DBID +
                    " WHERE d.Owner = "+ms.DBID +
                    " OR da.UserID = "+ms.DBID) ;
            
            if (ms.dbRes.next())
            {
                boolean thereIs ;
                int counter = 0 ;
                String enable = "";
                String parameters = "";
                do {
                    
                    thereIs = false;
                    for (ManageDevice item : ManageDevice.listWithConnectedDevices )
                    {
                        if(item.deviceManSocket.SID.equals(ms.dbRes.getString("DeviceID")+"D"))
                        {
                            thereIs = true ;
                            break ;
                        }
                    }
                    if (thereIs)
                        enable = "U"; //Up
                    else
                        enable = "D" ; //Down
                    
                    parameters += "(" +
                            ms.dbRes.getString("DeviceID") + "|" +
                            ms.dbRes.getString("DeviceName") + "|" +
                            (ms.dbRes.getString("TypeAccess")==null ? "O" : ms.dbRes.getString("TypeAccess")) + "|" +
                            enable + ")" ;
                    counter++;
                } while (ms.dbRes.next()) ;
                String message = ManageSocketMessage.newMessage("S", ManageSocketMessage.CommandType.PutDevicesInfo,counter, parameters) ;
                Tools.send(message, ms.out);
                Tools.Debug.print(message,Tools.MSGOKEMO);
            }
        }
        else
            hmUserSockets.remove(ms.SID) ;
    }
    
    
    /**
     * Η διαδικασία εκτελείτε στη περίπτωση που το socket προέρχεται από ένα User Client .
     * Και ουσιαστικά στέλνει συνοπτικά τις συσκευές που ανήκουν στον χρήστη ή όλες τις πληροφορίες μιας online συσκευής
     * για να την εποπτεύσει με βάση βέβαια με το τι έχει επιλέξει ο χρήστης μέσα από το "User Client".
     * @return Επιστρέφει OK ή X ανάλογα αν τα πράγματα πήγαν καλά ή όχι .
     * @throws SQLException
     */
    private String userClientProccess() throws SQLException
    {
        try
        {
            msm.reload(manSocket.in.readLine()) ;
            
            if (Tools.isCorrectCommand(msm.getCommand(),"GetDevicesInfo",clientSystemMessage,msm.getMessage(),manSocket.out,false))
            {
                Tools.Debug.print("UserDesktop Client |"+clientSystemMessage +" | GetDevicesInfo get message :"+msm.getMessage(),Tools.MSGOKEMO);
                manSocket.dbRes = manSocket.dbSt.executeQuery("SELECT d.DeviceID , d.DeviceName, da.TypeAccess" +
                        " FROM devices d" +
                        " LEFT JOIN deviceaccess da ON d.DeviceID = da.DeviceID AND da.USERID = "+manSocket.DBID +
                        " WHERE d.Owner = "+manSocket.DBID +
                        " OR da.UserID = "+manSocket.DBID) ;
                
                
                if (manSocket.dbRes.next())
                {
                    boolean thereIs ;
                    int counter = 0 ;
                    String enable = "";
                    String parameters = "";
                    do {
                        thereIs = false;
                        for (ManageDevice item : ManageDevice.listWithConnectedDevices )
                        {
                            if(item.deviceManSocket.SID.equals(manSocket.dbRes.getString("DeviceID")+"D"))
                            {
                                thereIs = true ;
                                break ;
                            }
                        }
                        if (thereIs)
                            enable = "U"; //Up
                        else
                            enable = "D" ; //Down
                        
                        parameters += "(" +
                                manSocket.dbRes.getString("DeviceID") + "|" +
                                manSocket.dbRes.getString("DeviceName") + "|" +
                                (manSocket.dbRes.getString("TypeAccess")==null ? "A" : manSocket.dbRes.getString("TypeAccess")) + "|" +
                                enable + ")" ;
                        counter++;
                    } while (manSocket.dbRes.next()) ;
                    String message = ManageSocketMessage.newMessage("S", ManageSocketMessage.CommandType.PutDevicesInfo,counter, parameters) ;
                    Tools.send(message, manSocket.out);
                    Tools.Debug.print("UserDesktop Client |"+clientSystemMessage +" | GetDevicesInfo put message :"+message,Tools.MSGSYSTEMOKEMO);
                    
                    ManageSocket us = manSocket ;
                    manSocket.SID += "C" ;
                    hmUserSockets.put(manSocket.SID,us) ;
                    return "OK" ;
                }
                else
                {
                    Tools.printSMError(" There is not device - Failed !", clientSystemMessage,msm.getMessage(),manSocket.out);
                    return "X" ;
                }
            }
            else if (Tools.isCorrectCommand(msm.getCommand(),"BringMeDevice",clientSystemMessage,msm.getMessage(),manSocket.out,false))
            {
                Tools.Debug.print("UserDesktop Client |"+clientSystemMessage +" | BringMeDevice get message :"+msm.getMessage(),Tools.MSGOKEMO);
                String devID = Tools.getOnlyDBID(msm.getParameters().get(0).get(0)) ;
                
                manSocket.dbRes = manSocket.dbSt.executeQuery("SELECT u.Type AS TA FROM devices d" +
                        " INNER JOIN users u on u.UserID = d.Owner " +
                        " WHERE d.Owner = "+manSocket.DBID+
                        " AND d.isDeleted = 0 AND u.isDeleted = 0 AND u.isEnabled = 1" +
                        " AND  d.DeviceID = "+ devID) ;
                
                if (manSocket.dbRes.next())
                    manSocket.socketType = manSocket.dbRes.getString("TA") ;
                else
                {
                    manSocket.dbRes = manSocket.dbSt.executeQuery("SELECT TypeAccess AS TA FROM deviceaccess da" +
                            " WHERE da.UserID = " + manSocket.DBID +
                            " AND  da.DeviceID = " + devID +
                            " AND da.iSDeleted = 0") ;
                    if (manSocket.dbRes.next())
                        manSocket.socketType = manSocket.dbRes.getString("TA") ;
                    else
                    {
                        Tools.printSMError(" There is not device - Failed !", clientSystemMessage,msm.getMessage(),manSocket.out);
                        return "X" ;
                    }
                }
                String devSID = devID +"D" ;
                for (ManageDevice item : ManageDevice.listWithConnectedDevices)
                {
                    if (item.deviceManSocket.SID.equals(devSID))
                    {
                        Tools.Debug.print("UserDesktop Client |"+clientSystemMessage + " BringMeDevice OK!",Tools.MSGSYSTEMOKEMO);
                        item.add(manSocket);
                        return "OK" ;
                    }
                }
                Tools.printSMError(" There is not device - Failed !", clientSystemMessage,msm.getMessage(),manSocket.out);
                return "X" ;
            }
            else if (Tools.isCorrectCommand(msm.getCommand(),"GetTypeUser",clientSystemMessage,msm.getMessage(),manSocket.out))
            {
                Tools.Debug.print("UserDesktop Client |"+clientSystemMessage +" | GetTypeUser get message :"+msm.getMessage(),Tools.MSGOKEMO);
                
                manSocket.dbRes = manSocket.dbSt.executeQuery("SELECT Type FROM users" +
                        " WHERE UserID = "+manSocket.DBID +
                        " AND isDeleted = 0 AND isEnabled = 1") ;
                String typeUser = "X" ;
                if (manSocket.dbRes.next())
                    typeUser = manSocket.dbRes.getString("Type") ;
                if (!typeUser.equals("X"))
                {
                    String[][] tmp = {{typeUser}};
                    String message = ManageSocketMessage.newMessage("S", ManageSocketMessage.CommandType.PutTypeUser, tmp);
                    Tools.send(message,manSocket.out) ;
                    Tools.Debug.print("UserDesktop Client |"+clientSystemMessage +" | GetTypeUser put message :"+message,Tools.MSGOKEMO);
                    try {
                        msm.reload(manSocket.in.readLine()) ;
                    } catch (IOException ex) {
                        return "X" ;
                    }
                    return "OK" ;
                }
                else
                {
                    Tools.printSMError(" There is not device - Failed !", clientSystemMessage,msm.getMessage(),manSocket.out);
                    return "X" ;
                }
            }
            else
            {
                Tools.Debug.print("UserDesktop Client |"+clientSystemMessage +" | Unknwon get message :"+msm.getMessage(),Tools.MSGERROREMO);
                return "X" ;
            }
        } catch (Exception ex)
        {
            Tools.Debug.printError(ex);
            return "X" ;
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