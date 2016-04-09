package ServerV1;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Από τη κλάση Server ξεκινάει και ανοίγει ο εξυπηρετητής μας .
 * Η κλάση Server (μέσα από τη main μέθοδο) αρχικά προετοιμάζει τη σύνδεση με τη βάση δεδομένων .
 * Στη συνέχεια αφού ανοίγει ένα Server Socket περιμένει να δεχθεί συνδέσεις από άλλα socket (client) .
 * Μόλις συνδεθεί πάνω του κάποιο άλλο socket δημιουργείται και ξεκινάει ένα thread (αντικείμενο της κλάσης {@link ClientServiceThread})
 * που αναλαμβάνει να εξυπηρετήσει το socket .
 * O Server πάντα συνεχίζει να περιμένει νέα socket για μελλοντικές συνδέσεις.
 * @author Michael Galliakis
 */

public class Server {
    /**
     * Η main μέθοδος που ξεκινάει το project .
     * Ελέγχει την σύνδεση με την βάση και στη συνέχεια ανοίγει το Socket Server μας για να ακούει άλλα socket.
     * Αργότερα για κάθε socket που συνδέεται στο server μας ανοίγει ένα thread για το χειρισμό του
     * και συνεχίζει να περιμένει για νέες συνδέσεις άλλων socket .
     * @param args Μπορούμε όταν το τρέχουμε από κονσόλα να δώσουμε έναν αριθμό για τη πόρτα στη πρώτη παράμετρο .
     */
    public static void main(String[] args)
    {
        Globals.printMyInfo();
        Tools.Debug.print("***********************************************************",Tools.MSGSYSTEMOKEMO) ;
        ServerSocket m_ServerSocket = null ;
        Tools.Debug.print("Starting Server ...",Tools.MSGSYSTEMOKEMO) ;
        if (checkDatabaseAndLoadConfig())
        {
            if (args.length>0)
                if (Tools.isNumeric(args[0]))
                    Globals.serverPort = Integer.parseInt(args[0]) ;
            if (args.length>1)
                if (Tools.isNumeric(args[1]))
                    Globals.viewMessages = Integer.parseInt(args[1]) ;
            
            Tools.Debug.print("Successfully connected to the database.",Tools.MSGSYSTEMOKEMO) ;
            try {
                m_ServerSocket = new ServerSocket(Globals.serverPort);
            }catch(Exception ex)
            {
                Tools.Debug.print("Could not open the server at the door "+Globals.serverPort + ".",Tools.MSGERROREMO) ;
                terminateSuccessServer() ;
            }
            
            if (m_ServerSocket==null) terminateSuccessServer() ;
            
            Tools.Debug.print("Opened successfully the server at the door "+Globals.serverPort + ".",Tools.MSGSYSTEMOKEMO) ;
            Tools.Debug.print("Started Server.",Tools.MSGSYSTEMOKEMO) ;
            long ID = 0 ;
            Tools.Debug.print("Listen ...",Tools.MSGSYSTEMOKEMO) ;
            while (true)
            {
                Socket clientSocket = null ;
                try
                {
                    clientSocket = m_ServerSocket.accept();
                    
                }catch(Exception ex)
                {
                    Tools.Debug.print("Accept socket Failed.",Tools.MSGERROREMO) ;
                    terminateSuccessServer() ;
                }
                try
                {
                    ClientServiceThread cliThread = new ClientServiceThread(clientSocket,ID++);
                    cliThread.start();
                }catch(Exception ex)
                {
                    Tools.Debug.print("Client Service Thread Failed.",Tools.MSGERROREMO) ;
                }
            }
        }
        else
            Tools.Debug.print("unSuccessfully Connect Database",Tools.MSGERROREMO);
    }
    
    /**
     * Μία μέθοδος που τερματίζει τελείως το server και ουσιαστικά και το πρόγραμμα
     */
    private static void terminateSuccessServer()
    {
        Tools.Debug.print("Terminate Success Server.",Tools.MSGOKEMO) ;
        Tools.Debug.print("***********************************************************",Tools.MSGERROREMO) ;
        System.exit(0);
    }
    
    /**
     * Μέθοδος που κάνει την διαδικασία για να δημιουργηθεί σύνδεση με τη βάση δεδομένων.
     * Πρακτικά αποκτούν περιεχόμενο οι 3 static μεταβλητές : η st (τύπου {@link java.sql.Statement}),
     * η conn (τύπου {@link Connection}) και η staticRes (τύπου {@link ResultSet})
     * @return True αν όλα πήγαν καλά και false αν δεν επιτεύχθηκε η σύνδεση
     */
    private static boolean checkDatabaseAndLoadConfig()
    {
        if (!Globals.loadConfig()) return false ;
        try
        {
            Class.forName(Globals.db_driver).newInstance();
            Connection conn = DriverManager.getConnection(Globals.db_url+Globals.db_dbName,Globals.db_username,Globals.db_password);
            java.sql.Statement st = conn.createStatement();
            ResultSet res = st.getResultSet();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e)
        {
            Tools.Debug.print("unSuccessfully check Database",Tools.MSGERROREMO);
            return false ;
        }
        Tools.Debug.print("Successfully check Database",Tools.MSGSYSTEMOKEMO);
        return true ;
    }
    
    /**
     * Μέθοδος που κάνει την διαδικασία για να δημιουργηθεί σύνδεση με τη βάση δεδομένων
     * @param ms Ένα {@link ManageSocket} για να αποκτήσουν περιεχόμενο οι 3 μεταβλητές του : η dbSt (τύπου {@link java.sql.Statement}),
     * η dbConn (τύπου {@link Connection}) και η dbRes (τύπου {@link ResultSet})
     * @return True αν όλα πήγαν καλά και false αν δεν επιτεύχθηκε η σύνδεση
     */
    public static boolean connectDatabase(ManageSocket ms)
    {
        Connection conn ;
        java.sql.Statement st ;
        ResultSet res ;
        try
        {
            Class.forName(Globals.db_driver).newInstance();
            conn = DriverManager.getConnection(Globals.db_url+Globals.db_dbName,Globals.db_username,Globals.db_password);
            st = conn.createStatement();
            res = st.getResultSet() ;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e)
        {
            Tools.Debug.print("unSuccessfully Connect Database",Tools.MSGERROREMO);
            return false ;
        }
        Tools.Debug.print("Successfully Connect Database",Tools.MSGOKEMO);
        ms.dbConn = conn ;
        ms.dbSt = st ;
        ms.dbRes = res ;
        return true ;
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