package DeviceClientV1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * Κλάση για να διαχειριζόμαστε ένα socket και να έχουμε όλα τα απαραίτητα 
 * στοιχεία που χρειαζόμαστε μαζί .
 * @author Michael Galliakis
 */
public class ManageSocket {
    String name ,ID ;
    
    Socket clientSocket ;
    PrintWriter out ;
    BufferedReader in ;    
    ManageSocket(Socket s,PrintWriter o,BufferedReader i,String n,String id)
    {
        clientSocket = s  ;
        out  = o ;
        in = i ;
        name = n ;
        ID =  id ;
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