<!DOCTYPE html>
 <?php  
session_start();
if(!$_SESSION['username'])  
    {        
        header("Location: Login.php");
    }  
if(isset($_GET['deviceID']))  
{     
    include("Db_conection.php"); 
    $deviceID = $_GET['deviceID'] ;       
    $user_ID = $_SESSION['ID'] ;    
     
    $view_devices_query="SELECT COUNT(*) 
                     FROM devices d
                     LEFT JOIN deviceaccess da ON d.DeviceID = da.DeviceID AND da.USERID = '$user_ID' AND da.isDeleted = false
                     WHERE (d.Owner = '$user_ID'
                     OR da.UserID = '$user_ID') AND d.isDeleted = false AND d.DeviceID = '$deviceID'";
            
            $run=mysqli_query($dbcon,$view_devices_query);
           if($row=mysqli_fetch_array($run))
            {                 
                $totalLines = $row[0];                
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

 <?php  
    
if(empty($_POST['changeProfile']))  
  {        
        $run_query="Select DeviceName,Password,Comment,(SELECT Username FROM users WHERE UserID = devices.Owner) AS Owner from devices where DeviceID='$deviceID'";
    $run=mysqli_query($dbcon,$run_query);//here run the sql query.  
        
   if($row=mysqli_fetch_array($run))
            {  
                
            $devicename=$row['DeviceName'];
            $dev_pass=$row['Password'];
            $dev_pass2=$row['Password'];
            $comment=$row['Comment'];
            $owner=$row['Owner'];
                
            }      
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
                
                <form action="EditDevice.php" method="post" onSubmit="return editDeviceformValidation();" name="editDevice">    
                    <input type="hidden" name="lastPassword" value="<?php echo $dev_pass?>" /><br />
                    <h3>Διαχείριση συσκευής:</h3><?php echo $devicename?> 
                    <br/>                    
                    <hr/>                                               
                    
                    <label for="Password1">Κωδικός συσκευής:</label><br />
                    <input type="password" id="passwordinput" name="Password1" placeholder="Password" class="textinput" maxlength="25" value="<?php echo $dev_pass?>"/><br />
                    <label for="Password2">Επιβεβαίωση κωδικού:</label><br />
                    <input type="password" id="passwordinput" name="Password2" placeholder="Confirm Password" class="textinput" maxlength="25" value="<?php echo $dev_pass2?>" /><br />
                    <label for="Comment">Σχόλιο:</label><br />                        
                    <textarea id="textareainput" name="Comment" placeholder="Comment" class="textarea" maxlength="50" rows="" cols=""><?php echo $comment?></textarea><br /> 
                    <label for="Owner">Ιδιοκτήτης:</label> <?php echo $owner?>
                    <input type="hidden" value="<?php echo $deviceID?>" name="deviceID"  />                    
                    <br />                    
                    <input type="submit" value=""  class="changeDevice" name="editDevice"  />                    
                    
                </form>                                                                             
                <br/>
                <form action="EditDevice.php" method="post" onSubmit="return deleteMessage('τη συσκευή');">                                             
                    <input type="hidden" value="<?php echo $deviceID?>" name="deviceID"  />                    
                    <input type="submit" value=""  name="deleteDevice" class="removeDevice" />                                        
                </form>  
                <br/>
                <form action="ManageDevices.php">                        
                    <input type="image" src="myImages/back.png" alt="Previous" width="96" height="48">
                </form>                    
                
                
            </div>
            
  <?php include("boxes.php"); ?>
            
        </div>
        <?php include("footer.php");?>
    </body>
</html>

   <?php  
   include("Db_conection.php"); 
if(isset($_POST['editDevice']))  
{          //Na ginei elegxos!
            $pass=$_POST['Password1']; 
            $user_lastPass=$_POST['lastPassword'];
            $comment=$_POST['Comment'];                                   
            $deviceID = $_POST['deviceID'];
                        
            $userID=$_SESSION['ID'];    
                   
    if ($user_lastPass==$pass)     
        $update_user="Update devices set Comment = '$comment', UserModified = '$userID' WHERE DeviceID ='$deviceID'" ;
    else
    {
        $cryptoPass = hash('md5',$pass) ;        
        $update_user="Update devices set Password = '$cryptoPass' , Comment = '$comment', UserModified = '$userID' WHERE DeviceID ='$deviceID'" ;
    }
        
        
    if(mysqli_query($dbcon,$update_user))  
    {  
        echo"<script>window.open('ManageDevices.php','_self')</script>";  
    }
}
else if(isset($_POST['deleteDevice']))  
{
     $user_ID = $_SESSION['ID'] ;
    $deviceID = $_POST['deviceID'];
    $update_device="Update devices set isDeleted = true , UserModified = '$user_ID' WHERE DeviceID ='$deviceID'" ;        
    if(mysqli_query($dbcon,$update_device))  
    {  
        echo"<script>window.open('ManageDevices.php','_self')</script>";  
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
