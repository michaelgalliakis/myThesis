<!DOCTYPE html>
<?php  
session_start();//session starts here    
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
                <h3>Δημιουργία Νέου λογαριασμού χρήστη:</h3>
                <br/>
                <form action="Registration.php" method="post" onSubmit="return registationformValidation();" name="registration">                    
                    <hr/>  
                    <label for="Username">Όνομα χρήστη:</label><br />                    
                    <input type="text" id="textinput" name="UsernameR" placeholder="Username" class="textinput" maxlength="25" value="" autofocus/><br />
                    <label for="Password1">Κωδικός:</label><br />
                    <input type="password" id="passwordinput" name="Password1" placeholder="Password" class="textinput" maxlength="25" value="" /><br />
                    <label for="Password2">Επιβεβαίωση κωδικού:</label><br />
                    <input type="password" id="passwordinput" name="Password2" placeholder="Confirm Password" class="textinput" maxlength="25" value="" /><br />
                    <label for="Name">Όνομα:</label><br />
                    <input type="text" id="textinput" name="Name" placeholder="Name" class="textinput" maxlength="25" value=""/><br />
                    <label for="Surname">Επίθετο:</label><br />
                    <input type="text" id="textinput" name="Surname" placeholder="Surname" class="textinput" maxlength="25" value=""/><br />            
                    <label for="Email">Email:</label><br />
                    <input type="text" id="textinput" name="Email" placeholder="Email" class="textinput" maxlength="25" value=""/><br />      
                    <br />
                    <input type="submit" value="" name="registration" class="createAccount" />
                        
                    <div id="stylesheetTest"></div>
                </form>
                <hr/>
                <center><b>Already registered ?</b> <br></b><a href="Login.php"><input type="image" src="myImages/login2.png" height="48" width="48"/></a></center><!--for centered text-->  
                    
                    
                    
                    
                    
            </div>
                
  <?php include("boxes.php"); ?>
      
        </div>
        <?php include("footer.php");?>
    </body>
</html>
    
    
<?php  
    
include("Db_conection.php"); 
if(isset($_POST['registration']))  
{      
    $user_username=$_POST['UsernameR'];
    $user_pass=$_POST['Password1'];
    $user_pass2=$_POST['Password2'];    
    $user_name=$_POST['Name'];
    $user_surname=$_POST['Surname'];
    $user_email=$_POST['Email'];
        
    $checkUser = strtoupper($user_username) ;
    $check_username_query="select * from users WHERE UPPER(Username)='$checkUser'";  
    $run_query=mysqli_query($dbcon,$check_username_query);  
        //echo $check_username_query ;
    if(mysqli_num_rows($run_query)>0)  
    {  
        echo "<script>alert('Το Username $user_username υπάρχει ήδη στη βάση δεδομένων, Παρακαλώ βάλτε κάποιο άλλο!')</script>";  
        exit();  
    }  
          $cryptoPass = hash('md5',$user_pass) ;
    $insert_user="insert into users (Username,Password,Name,Surname,Email,Type) VALUE ('$user_username','$cryptoPass','$user_name','$user_surname','$user_email','U')";  
     //echo $insert_user ;
    if(mysqli_query($dbcon,$insert_user))  
    {  
        echo"<script>window.open('Login.php','_self')</script>";  
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