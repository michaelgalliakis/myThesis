<?php  
    session_start();  
        
    if(!$_SESSION['username'])  
    {        
        header("Location: Login.php");
    }  
        
        
    include("Db_conection.php"); 
    $option=$_GET['Option'];
        
if($option=="Change")  
{   
    $user_username=$_SESSION['username'];
    $user_ID=$_SESSION['ID'];
    $accesstype=$_GET['TypeAccess'];
    $deviceID = $_GET['DeviceID'];
    $devicename = $_GET['deviceName'];
    $userID = $_GET['UserID'];
        
    if ($accesstype=="N")
        $update_user="DELETE FROM deviceaccess WHERE UserID ='$userID' AND DeviceID = '$deviceID'" ;
    else
        $update_user="Update deviceaccess set TypeAccess = '$accesstype' , UserModified = '$user_ID' WHERE UserID ='$userID' AND DeviceID = '$deviceID'" ;
    //    echo $update_user ;
    if(mysqli_query($dbcon,$update_user))  
    {  
        
        echo"<script>window.open('Invite.php?deviceID=$deviceID&deviceName=$devicename','_self')</script>";  
    }
        
}
else if($option=="Request")  
{
     $user_username=$_SESSION['username'];
    $user_ID=$_SESSION['ID'];
    $accesstype=$_GET['TypeAccess'];
    $deviceID = $_GET['DeviceID'];
    $userID = $_GET['UserID'];
      $devicename = $_GET['deviceName'];  
          
    $check_user="select * from requests WHERE UserTo='$userID'AND UserFrom='$user_ID' AND DeviceID = '$deviceID'";  
        
    $run=mysqli_query($dbcon,$check_user);  
        
        if(mysqli_num_rows($run)>0)  
        $update_user="UPDATE requests SET TypeAccess = '$accesstype' WHERE UserTo='$userID'AND UserFrom='$user_ID' AND DeviceID = '$deviceID'" ;
    else    
        $update_user="insert into requests (UserTo,UserFrom,TypeAccess,DeviceID,Text) VALUE('$userID','$user_ID','$accesstype','$deviceID','You want to have this device ?');" ;
        //  echo $update_user ;
            
    if(mysqli_query($dbcon,$update_user))  
    {  
        echo"<script>window.open('Invite.php?deviceID=$deviceID&deviceName=$devicename','_self')</script>";  
    }      
}
    
 // echo"<script>window.open('Invite.php?deviceID=$deviceID','_self')</script>";   
     
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