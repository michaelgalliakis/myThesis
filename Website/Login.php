<!DOCTYPE html>
<?php  
session_start();//session starts here    
session_destroy();
?>  
<html>
    <head>
    <?php include("htmlHead.php");?>        
    </head>
    <body>
        <div id="container">
          <?php $parentFilePath = __FILE__ ; include("header.php");?>
              
            <div id="middle">
                <div id="middle_inside">
                    <div id="slide">
                        
                    </div>                    
                    <div id="quick_note">
                        <center><h2><u>Σύνδεση :</u></h2></center>
                        <form role="form" method="post" action="Login.php">  
                            <label for="textinput">Username:</label> <br>
                            <input id="textinput" name="username"  class="textinput" maxlength="25" type="text" autofocus> <br>    
                            <label for="passwordinput">Password:</label><br>
                            <input id="passwordinput" name="pass" class="textinput" maxlength="25" type="password" value="">    <br><br/>
                            <center>
                                <input type="submit" class="loginNow" value="" name="login" >                               
                                    
                                <a href="Registration.php"> <input type="button" class="signupNow" value="" name="signup"> </a><br>   
                                <hr/>
                                <img src="images/logoPMGv2F_2016.png" width="96" height="96"  align = "center"/>
                                <hr/>
                            </center>
                        </form>  
                            
                    </div>
                </div>
                    
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
    $cryptoPass = hash('md5',$user_pass) ;
    $check_user="select UserID,Type,Username from users WHERE Username='$username'AND Password='$cryptoPass' AND isEnabled = true AND isDeleted = false";  
        
    $run=mysqli_query($dbcon,$check_user);  
        
    if($row=mysqli_fetch_array($run))//while look to fetch the result and store in a array $row.  
            {  
                
        echo "<script>window.open('index.php','_self')</script>";  
            
        $_SESSION['username']=$username;
        $_SESSION['ID']=$row['UserID']; 
        $_SESSION['Type']=$row['Type']; 
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