<!DOCTYPE html>
<?php  
session_start();   
if(!$_SESSION['username'])  
    {        
        header("Location: Login.php");
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
                <form action="Search.php" method="post" >
                    <hr/>
                    <label for="SearchDeviceName">Search Device Name :</label><br />
                    <input type="text" id="textinput" name="SearchDeviceName" placeholder="Device Name" class="textinput" maxlength="30" value="" <?php if(!isset($_POST['Search'])) echo "autofocus";?>/>
                    <input type="submit" value="" name="Search" class="search" />
                    <hr/>
                </form>
                <?php  
                    
include("Db_conection.php");    
if(isset($_POST['Search']))  
{  
    $searchDeviceName = $_POST['SearchDeviceName'];  
    $user_ID = $_SESSION['ID'];      
        
    $check_user="select DeviceID from devices WHERE DeviceName='$searchDeviceName'" ;  
        
    $run=mysqli_query($dbcon,$check_user);    
        
    if($row=mysqli_fetch_array($run))
            {     
        $deviceID = $row[0] ;
            echo "<br/><h3>Βρέθηκε η συσκευή :[$searchDeviceName]</h3>";
    $check_user="SELECT DeviceID , '0' AS TypeAccess From devices where isDeleted = false AND Owner = '$user_ID' AND DeviceID = '$deviceID' UNION SELECT DeviceID, TypeAccess FROM deviceaccess WHERE isDeleted = false AND UserID = '$user_ID' AND DeviceID = '$row[0]'" ;  
        //echo $check_user ;
    $run=mysqli_query($dbcon,$check_user);  
    if($row=mysqli_fetch_array($run))
            {     
                
        echo "<br/><h3>Έχετε ήδη αυτή τη συσκεύη</h3>";          
            
      }
    else
    { ?>
                <form action="Search.php" method="post" >
                    <br/>
                    <label for="SearchDeviceName">Γράψε κάποιο σχόλιο και κάνε αίτηση για να αποκτήσεις δικαιώματα στην συσκευή :</label><br />                    
                    <textarea id="textareainput" name="Comment" placeholder="Comment" class="textarea" maxlength="50" rows="" cols="" autofocus=""><?php echo $comment?></textarea><br /> 
                    <input type="hidden" name="deviceID" value="<?php echo $deviceID;?>" autofocus/>
                    <input type="submit" value="" name="Request" class="request" />
                    <br/>
                    <label for="SearchDeviceName">Administrator</label> <input type="radio" name="option" value="A" >
                    <label for="SearchDeviceName">Full Access</label>   <input type="radio" name="option" value="F" checked="" >     
                    <label for="SearchDeviceName">Read Only</label> <input type="radio" name="option" value="R" >
                </form>       
                    
    <?php }
        
            }  
    else
    {
        echo "<h3>Δεν βρέθηκε η συσκευή :[$searchDeviceName]</h3>";
            
    }
        
}
else if(isset($_POST['Request']))
{          
    $user_ID=$_SESSION['ID'];    
    $deviceID = $_POST['deviceID'];
    $accesstype = $_POST['option'];
    $comment = $_POST['Comment'];
        
        
    $check_user="select * from requests WHERE UserTo='$user_ID'AND UserFrom='$user_ID' AND DeviceID = '$deviceID'";  
  echo $check_user ;
    $run=mysqli_query($dbcon,$check_user);  
        
        if(mysqli_num_rows($run)>0)  
        $update_user="UPDATE requests SET TypeAccess = '$accesstype', Text = '$comment' WHERE UserTo='$user_ID'AND UserFrom='$user_ID' AND DeviceID = '$deviceID'" ;
    else    
        $update_user="insert into requests (UserTo,UserFrom,TypeAccess,DeviceID,Text) VALUE('$user_ID','$user_ID','$accesstype','$deviceID','$comment');" ;
        //  echo $update_user ;
         echo $update_user ;  
    if(mysqli_query($dbcon,$update_user))  
    {  
        echo"<script>window.open('MyRequests.php','_self')</script>";  
    }      
        
}
    
?> 
            </div>
                
  <?php include("boxes.php"); ?>
      
        </div>
        <?php include("footer.php");?>
    </body>
</html>
    
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