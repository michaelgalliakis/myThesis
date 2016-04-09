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
    <body onload="document.registration.Username.focus();"> 
        <div id="container">
          <?php $parentFilePath = __FILE__ ; include("header.php");?>
              
            <div id="middle">
                <br/>
                <h3>Δημιουργία Νέου λογαριασμού Συσκευής:</h3>
                <br/>
                <hr/>  
                <form action="NewDevice.php" method="post" onSubmit="return newDeviceformValidation();" name="newDevice">
                    <label for="Devicename">Όνομα συσκευής:</label><br />
                    <input type="text" id="textinput" name="Devicename" placeholder="Device name" class="textinput" maxlength="25" value="" autofocus/><br />
                    <label for="Password1">Κωδικός συσκευής:</label><br />
                    <input type="password" id="passwordinput" name="Password1" placeholder="Password" class="textinput" maxlength="25" value="" /><br />
                    <label for="Password2">Επιβεβαίωση κωδικού:</label><br />
                    <input type="password" id="passwordinput" name="Password2" placeholder="Confirm Password" class="textinput" maxlength="25" value="" /><br />
                    <label for="Comment">Σχόλιο:</label><br />                        
                    <textarea id="textareainput" name="Comment" placeholder="Comment" class="textarea" maxlength="50" rows="" cols=""></textarea><br /> 
                    <br />                    
                    <input type="submit"  class="createDevice" value="" name="newDevice"  />                    
                        
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
if(isset($_POST['newDevice']))  
{      
    $devicename=$_POST['Devicename'];
    $dev_pass=$_POST['Password1'];
    $dev_pass2=$_POST['Password2'];
    $comment=$_POST['Comment'];
    $owner=$_SESSION['ID'];
        
        
    $check_devicename_query="select * from devices WHERE Devicename='$devicename'";  
    $run_query=mysqli_query($dbcon,$check_devicename_query);  
        
    if(mysqli_num_rows($run_query)>0)  
    {  
        echo "<script>alert('Το DeviceName $devicename υπάρχει ήδη στη βάση δεδομένων, Παρακαλώ βάλτε κάποιο άλλο!')</script>";  
        exit();  
    }  
    //Elegxos
    $cryptoPass = hash('md5',$dev_pass) ;
    $insert_device="insert into devices (Devicename,Password,Comment,Owner,UserModified) VALUE ('$devicename','$cryptoPass','$comment','$owner','$owner')";  
    if(mysqli_query($dbcon,$insert_device))  
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