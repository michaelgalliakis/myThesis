<?php
session_start();  
    
    if(!$_SESSION['username'])  
    {        
        header("Location: Login.php");//redirect to login page to secure the welcome page without login access.  
    }  
include("Db_conection.php"); 
    
if(isset($_GET['requestID']))  {
    $user_ID = $_SESSION['ID'] ;        
    $requestID = $_GET['requestID'];
        
    $query="DELETE FROM deviceaccess WHERE UserID = (SELECT UserTo FROM requests WHERE RequestID ='$requestID') AND DeviceID = (SELECT DeviceID FROM requests WHERE RequestID ='$requestID');" ; 
    if(mysqli_query($dbcon,$query))  
    {
    $query="INSERT INTO deviceaccess (UserID,DeviceID,TypeAccess,UserModified) SELECT UserTo,DeviceID,TypeAccess,'$user_ID' FROM requests WHERE RequestID = '$requestID' ;" ;     
    echo $query ;
    if(mysqli_query($dbcon,$query))  
    {  
        $query="DELETE FROM requests WHERE RequestID = '$requestID' ;" ;     
            
        mysqli_query($dbcon,$query) ;
        echo"<script>window.open('MyRequests.php','_self')</script>";  
    }
    }
} else { echo"<script>window.open('MyRequests.php','_self')</script>";  }
?>
    
<?php /*    
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
                                          */ ?>