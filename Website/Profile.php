<!DOCTYPE html>
 <?php  
session_start();
if(!$_SESSION['username'])  
    {        
        header("Location: Login.php");
    }  
?>
    
 <?php  
    include("Db_conection.php"); 
if(empty($_POST['changeProfile']))  
  {
        $username=$_SESSION['username'];        
        $run_query="Select Username,Password,Name,Surname,Email from users where Username ='$username'";
    $run=mysqli_query($dbcon,$run_query);//here run the sql query.  
        
   while($row=mysqli_fetch_array($run))//while look to fetch the result and store in a array $row.  
            {  
                
            $user_username=$row[0];
            $user_pass=$row[1];
            $user_pass2=$row[1];
            $user_name=$row[2];
            $user_surname=$row[3];
            $user_email=$row[4];
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
                <br/>
                <h3>Διαχείριση λογαριασμού χρήστη:</h3>
                <br/>
                <hr/>
                    
                <form action="Profile.php" method="post" onSubmit="return changeProgileformValidation();" name="changeProfile">
                    
                    <label for="Username">Όνομα χρήστη:</label> <u><?php echo $user_username?></u> <br />                        
                    <input type="hidden" name="lastPassword" value="<?php echo $user_pass?>" /><br />
                    <label for="Password1">Κωδικός:</label><br />                    
                    <input type="password" id="passwordinput" name="Password1" placeholder="Password" class="textinput" maxlength="25" value="<?php echo $user_pass?>" /><br />
                    <label for="Password2">Επιβεβαίωση κωδικού:</label><br />
                    <input type="password" id="passwordinput" name="Password2" placeholder="Confirm Password" class="textinput" maxlength="25" value="<?php echo $user_pass2?>" /><br />
                    <label for="Name">Όνομα:</label><br />
                    <input type="text" id="textinput" name="Name" placeholder="Name" class="textinput" maxlength="25" value="<?php echo $user_name?>"/><br />
                    <label for="Surname">Επίθετο:</label><br />
                    <input type="text" id="textinput" name="Surname" placeholder="Surname" class="textinput" maxlength="25" value="<?php echo $user_surname?>"/><br />            
                    <label for="Email">Email:</label><br />
                    <input type="text" id="textinput" name="Email" placeholder="Email" class="textinput" maxlength="25" value="<?php echo $user_email?>"/><br />      
                    <br />
                    <input type="submit" value="" class="changeAccount" name="changeProfile" />
                        
                        
                </form>
                <br/>
                <form action="Profile.php" method="post" onSubmit="return deleteMessage('το Χρήστη');" name="Profile">                                             
                    <input type="submit" class="deleteAccount" value="" name="deleteAccount"/>                                        
                </form>   
                <br/>
                <a href="index.php"><input type="image" src="myImages/home.png" value="" height="36" width="36"/></a>
                    
                    
                    
            </div>
                
  <?php include("boxes.php"); ?>
      
        </div>
        <?php include("footer.php");?>
    </body>
</html>
    
   <?php  
    include("Db_conection.php"); 
if(isset($_POST['changeProfile']))  
{      
    $user_username=$_SESSION['username'];
    $user_pass=$_POST['Password1'];
    $user_pass2=$_POST['Password2'];
    $user_lastPass=$_POST['lastPassword'];
    $user_name=$_POST['Name'];
    $user_surname=$_POST['Surname'];
    $user_email=$_POST['Email'];
        //Elegxos!!
    
    if ($user_lastPass==$user_pass)
        $update_user="Update users set Name = '$user_name' , Surname = '$user_surname' , Email = '$user_email' WHERE Username ='$user_username'" ;
    else
    {
        $cryptoPass = hash('md5',$user_pass) ;
        $update_user="Update users set Password = '$cryptoPass' , Name = '$user_name' , Surname = '$user_surname' , Email = '$user_email' WHERE Username ='$user_username'" ;                
    }
        
    if(mysqli_query($dbcon,$update_user))  
    {  
        echo"<script>window.open('index.php','_self')</script>";  
    }
}
else if(isset($_POST['deleteAccount']))  
{
    $user_username=$_SESSION['username'];
    $update_user="Update users set isDeleted = true WHERE Username ='$user_username'" ;        
    if(mysqli_query($dbcon,$update_user))  
    {  
        echo"<script>window.open('Logout.php','_self')</script>";  
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