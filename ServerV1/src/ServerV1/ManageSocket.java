package ServerV1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Κλάση για να διαχειριζόμαστε ένα socket και να έχουμε όλα τα απαραίτητα 
 * στοιχεία που χρειαζόμαστε μαζί .
 * @author Michael Galliakis
 */
public class ManageSocket {
    
    java.sql.Statement dbSt ;
    Connection dbConn ;
    ResultSet dbRes ;
    
    String socketType ="";
    String DBID ,SID ;
    String messageFailed = "" ;
    String messageOK = "" ;
    String clientSystemMessage = "" ;
    
    Socket clientSocket ;
    PrintWriter out ;
    BufferedReader in ;
    ManageSocket(Socket s,PrintWriter o,BufferedReader i,String n,String id)
    {
        clientSocket = s  ;
        out  = o ;
        in = i ;
        DBID = n ;
        SID =  id ;
        messageFailed = Globals.messageFailed ;
        messageOK = Globals.messageOK ;
    }
    
    /**
     * Μέθοδος που κλείνει σωστά ένα socket και κάνει πιο καλή αποδέσμευση της μνήμης 
     * @throws IOException Αν πάμε να κλείσουμε το socket ή το BufferedReader και υπάρξει σφάλμα
     */
    public void close() throws IOException
    {
        if (out!=null)
            out.close() ;
        if (in!=null)
            in.close() ;
        if (clientSocket!=null)
            clientSocket.close() ;
        try {
            if (dbRes!=null)
                dbRes.close();
            if (dbSt!=null)
                dbSt.close();
            if (dbConn!=null)
                dbConn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ManageSocket.class.getName()).log(Level.SEVERE, null, ex);
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