<!DOCTYPE html>
<?php  
session_start();//session starts here    
if(!$_SESSION['username'])  
    {        
        header("Location: Login.php");//redirect to login page to secure the welcome page without login access.  
    }  
?>  
<html>
    <head>
    <?php include("htmlHead.php");?>        
    </head>
    <body>
        <div id="container">
         <?php $parentFilePath = __FILE__ ; include("header.php");?>
             
            <div id="middle">
                <br/>
                <center>
                    <div align="left">
                        <form action="NewDevice.php">                          
                            <label for="newDevice"> <font size="6" color="green">&nbsp;&nbsp;&nbsp;New Device:</font></label>
                            <input type="submit" class="newDeviceButton" value="">                        
                        </form>     
                    </div>
                    <hr/>
                    <table>  
                        <thead>  
                            
                            <tr>                        
                                <th>Device:</th>  
                                <th>Comment:</th>  
                                <th>Right:</th>  
                                <th>Owner:</th>                  
                                <th>Edit Device</th>                                    
                                <th>Edit Access</th>  
                                <th>Release Device</th>  
                            </tr>  
                        </thead>  
                            
            <?php  
            include("Db_conection.php");  
            $user_username=$_SESSION['username'];
            $user_ID=$_SESSION['ID'];
                
            $view_devices_query="SELECT d.DeviceID ,d.DeviceName,d.Comment,(SELECT username FROM users WHERE UserID = d.Owner) AS Owner, da.TypeAccess
                     FROM devices d
                     LEFT JOIN deviceaccess da ON d.DeviceID = da.DeviceID AND da.USERID = '$user_ID' AND da.isDeleted = false
                     WHERE (d.Owner = '$user_ID'
                     OR da.UserID = '$user_ID') AND d.isDeleted = false ORDER BY 2";
            $run=mysqli_query($dbcon,$view_devices_query);//here run the sql query.  
                
            while($row=mysqli_fetch_array($run))//while look to fetch the result and store in a array $row.  
            {  
                $deviceID=$row['DeviceID'];  
                $devicename=$row['DeviceName'];  
                $comment=$row['Comment'];  
                $owner=$row['Owner'];  
                $access=$row['TypeAccess'];  
            ?>  
                
                        <tr>  
                            <!--here showing results in the table -->  
                            <td><?php echo $devicename;  ?></td>  
                            <td><?php echo $comment;  ?></td>  
                                
                <?php 
                        switch ($access) {
                            case "A" :
                                echo "<td><input type='image' src='myImages/admin.png' height='24' width='48'/><br>(Admin)</td>" ;
                                break ;
                            case "F" :
                                echo "<td><input type='image' src='myImages/full.png' height='24' width='48'/><br>(Full)</td>" ;
                                break; 
                            case "R" :
                                echo "<td><input type='image' src='myImages/readonly.png' height='24' width='48'/><br>(Read)</td>" ;
                                break; 
                            default :
                                echo "<td><input type='image' src='myImages/owner.png' height='24' width='48'/><br>(Owner)</td>" ;
                        }
                    ?>
                            <td><?php echo $owner;  ?></td> 
                <?php if ($access=="R" || $access == "F") { ?>
                            <td><button disabled><input  type="image" src="myImages/editDeviceNU.png" alt="Edit Device" height="36" width="36" disabled="" /></button></td>                 
                            <td><button disabled><input type="image" src="myImages/editAccessNU.png" alt="Edit/View Access" height="36" width="36" disabled /></button</td>                 
                            <td><a href="ReleaseDevice.php?deviceID=<?php echo $deviceID ?>"><button  onClick="return deleteMessage('τη συσκευή (από εσένα μόνο)');" ><input type="image" src="myImages/release.png" alt="Release" height="36" width="36"></button></a></td>                     
                <?php  } else if (is_null ($access)) { ?>
                            <td><a href="EditDevice.php?deviceID=<?php echo $deviceID ?>"><button><input type="image" src="myImages/editDevice.png" alt="Edit Device" height="36" width="36"></button></a></td>                 
                            <td><a href="Invite.php?deviceID=<?php echo $deviceID ?>"><button ><input type="image" src="myImages/editAccess.png" alt="Edit/View Access" height="36" width="36"/></button></a></td>                 
                            <td><button disabled=""><input type="image" src="myImages/releaseNU.png" alt="Release" height="36" width="36" disabled=""></button></td>                     
                    <?php  } else  { ?>
                            <td><a href="EditDevice.php?deviceID=<?php echo $deviceID ?>"><button><input type="image" src="myImages/editDevice.png" alt="Edit Device" height="36" width="36"/></button></a></td>                 
                            <td><a href="Invite.php?deviceID=<?php echo $deviceID ?>"><button><input type="image" src="myImages/editAccess.png" alt="Edit/View Access" height="36" width="36"/></button></a></td>                 
                            <td><a href="ReleaseDevice.php?deviceID=<?php echo $deviceID ?>"><button  onClick="return deleteMessage('τη συσκευή (από εσένα μόνο)');" ><input type="image" src="myImages/release.png" alt="Release" height="36" width="36"></button></a></td>                     
                <?php  }  ?>
                        </tr>  
                            
            <?php } ?>  
                
                    </table>  
                </center> 
            </div>
                
  <?php include("boxes.php"); ?>
      
        </div>
        <?php include("footer.php");?>
    </body>
</html>
<?php  
    
include("Db_conection.php");    
if(isset($_POST['login']))  
{  
    $username=$_POST['username'];  
    $user_pass=$_POST['pass'];  
        
    $check_user="select * from users WHERE Username='$username'AND Password='$user_pass'";  
        
    $run=mysqli_query($dbcon,$check_user);  
        
    if(mysqli_num_rows($run))  
    {  
        echo "<script>window.open('index.php','_self')</script>";  
            
        $_SESSION['username']=$username;//here session is used and value of $user_email store in $_SESSION.  
            
    }  
    else  
    {  
      echo "<script>alert('Username or password is incorrect!')</script>";  
    }  
}  
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