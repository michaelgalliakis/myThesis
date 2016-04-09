<!DOCTYPE html>
<?php  
session_start();//session starts here    
if(!$_SESSION['username'])  
    {        
        header("Location: Login.php");//redirect to login page to secure the welcome page without login access.  
    }  
if(isset($_GET['deviceID']))  
{     
    include("Db_conection.php"); 
    $deviceID = $_GET['deviceID'] ;             
    $user_ID = $_SESSION['ID'] ;    
     
    $view_devices_query="SELECT COUNT(*) , d.deviceName
                     FROM devices d
                     LEFT JOIN deviceaccess da ON d.DeviceID = da.DeviceID AND da.USERID = '$user_ID' AND da.isDeleted = false
                     WHERE (d.Owner = '$user_ID'
                     OR da.UserID = '$user_ID') AND d.isDeleted = false AND d.DeviceID = '$deviceID'";
            
            $run=mysqli_query($dbcon,$view_devices_query);
           if($row=mysqli_fetch_array($run))
            {                 
                $totalLines = $row[0];    
                $device_name = $row[1];
                if ($totalLines==0)
                    echo"<script>window.open('ManageDevices.php','_self')</script>";   
            }    
            else {
                 echo"<script>window.open('ManageDevices.php','_self')</script>";   
            }
}
else
{    
     echo"<script>window.open('ManageDevices.php','_self')</script>";     
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
                <center>
                    <br/>
                    <h3>Δικαιώματα συσκευής:</h3><?php echo $device_name?> 
                    <br/>
                    <hr/>  
                    <table>  
                        <thead>  
                            
                            <tr>       
                                <th>User:</th>  
                                <th>Current Right:</th>
                                <th>Owner:</th>  
                                <th>Admin:</th>  
                                <th>Full Access:</th>                                  
                                <th>Read only:</th>  
                                <th>None:</th>  
                                <th>Option:</th>  
                                <th>Request Status:</th>  
                            </tr>  
                        </thead>  
                            
            <?php  
            include("Db_conection.php");  
            $user_username=$_SESSION['username'];
            $user_ID=$_SESSION['ID'];
                
            $view_users_query="SELECT u.Username,d.DeviceName, da.TypeAccess ,u.UserID , 
                (SELECT TypeAccess FROM requests r WHERE r.DeviceID = $deviceID AND r.UserTo = u.UserID AND r.UserFrom = $user_ID AND r.isDeleted = false) AS RequestAccessType
                    FROM users u 
                    LEFT JOIN devices d ON d.OWner = u.UserID AND d.isDeleted = false AND d.DeviceID = '$deviceID' 
                    LEFT JOIN deviceaccess da ON da.UserID = u.UserID AND da.DeviceID = '$deviceID' AND da.isDeleted = false
                    WHERE u.isDeleted = false AND u.isEnabled = true
                    ORDER BY 2 DESC , 3 DESC,1;";
                        
            $run=mysqli_query($dbcon,$view_users_query);//here run the sql query.  
                
            while($row=mysqli_fetch_array($run))//while look to fetch the result and store in a array $row.  
            {  
                $username=$row['Username'];  
                 $userID=$row['UserID'];  
                $devicename=$row['DeviceName'];                  
                $access=$row['TypeAccess'];
                $requestAccessType=$row['RequestAccessType'];
                    
            ?>  
                
                        <tr>  
                            <!--here showing results in the table -->  
                                
                            <td><?php if ($username!=$user_username) echo $username; else echo "<b><u>$username</u></b>"; ?></td>  
                        <input type="hidden" name="userID" value="<?php echo $user_ID?>">                
                        <input type="hidden" name="deviceID" value="<?php echo $deviceID?>">                
                            
                 <?php if (!is_null($devicename)) {?>
                        <td><input type='image' src='myImages/owner.png' height='24' width='48'/></td>
                        <td><input type="radio" name="<?php echo $username?>" value="O" checked disabled></td>         
                        <td><input type="radio" name="<?php echo $username?>" value="A" disabled=""></td>        
                        <td><input type="radio" name="<?php echo $username?>" value="F" disabled=""></td>        
                        <td><input type="radio" name="<?php echo $username?>" value="R" disabled=""></td>  
                        <td><input type="radio" name="<?php echo $username?>" value="N" disabled=""></td>  
                        <td><input type="submit" class="noUsed" value="" name="Noused" disabled=""> </td>                   
                 <?php } else if (is_null($access)) {?>
                        <td><input type='image' src='myImages/noneAccess.png' height='24' width='24'/></td>
                        <td><input type="radio" name="<?php echo $username?>" value="O" disabled ></td>        
                        <td><input type="radio" name="<?php echo $username?>" value="A" onclick="changePath('<?php echo $username?>','A');"></td>        
                        <td><input type="radio" name="<?php echo $username?>" value="F" onclick="changePath('<?php echo $username?>','F');"></td>        
                        <td><input type="radio" name="<?php echo $username?>" value="R" onclick="changePath('<?php echo $username?>','R');"></td>  
                        <td><input type="radio" name="<?php echo $username?>" value="N" onclick="changePath('<?php echo $username?>','N');" checked></td>  
                        <td><a id="Button<?php echo $username?>" href="DeviceAccessOption.php?<?php echo "DeviceID=$deviceID&Option=Request&deviceName=$device_name&UserID=$userID&TypeAccess=N" ?>"><button class="invite"></button></a></td>                 
                            
                    <?php } else { ?>
                    <?php 
                        switch ($access) {
                            case "A" :
                            echo "<td><input type='image' src='myImages/admin.png' height='24' width='48'/></td>" ;
                            break ;
                            case "F" :
                            echo "<td><input type='image' src='myImages/full.png' height='24' width='48'/></td>" ;
                            break; 
                            case "R" :
                            echo "<td><input type='image' src='myImages/readonly.png' height='24' width='48'/></td>" ;
                            break; 
                            default :
                                echo "<td>---</td>" ;
                        }
                    ?>
                        
                        <td><input type="radio" name="<?php echo $username?>" value="O" disabled></td>        
                        <td><input type="radio" name="<?php echo $username?>" value="A" <?php if ($access=="A") echo "checked" ;?> onclick="changePath('<?php echo $username?>','A');"></td>        
                        <td><input type="radio" name="<?php echo $username?>" value="F" <?php if ($access=="F") echo "checked"; ?> onclick="changePath('<?php echo $username?>','F');"></td>        
                        <td><input type="radio" name="<?php echo $username?>" value="R" <?php if ($access=="R") echo "checked" ;?> onclick="changePath('<?php echo $username?>','R');"></td>  
                        <td><input type="radio" name="<?php echo $username?>" value="N" onclick="changePath('<?php echo $username?>','N');"></td>                      
                        <td><a id="Button<?php echo $username?>" href="DeviceAccessOption.php?<?php echo "DeviceID=$deviceID&Option=Change&deviceName=$device_name&UserID=$userID&TypeAccess=N" ?>"><button class="changeAccess"></button></a></td>                 
                    <?php } ?>
                        
                        
                     <?php 
                        switch ($requestAccessType) {
                            case "A" :
                            echo "<td>Administrator</td>" ;
                            break ;
                            case "F" :
                            echo "<td>Full Access</td>" ;
                            break; 
                            case "R" :
                            echo "<td>Readonly</td>" ;
                            break; 
                            default :
                                echo "<td>No Request</td>" ;
                        }
                    ?>
                        
                        </tr>  
                            
            <?php } ?>  
                        </form>
                    </table>  
                        
                    <form action="ManageDevices.php">                        
                        <input type="image" src="myImages/back.png" alt="Previous" width="96" height="48">
                    </form>                    
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