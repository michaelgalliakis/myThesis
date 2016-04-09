<div id="header">
    <div id="logo"></div>
    <div id="<?php 
       if($_SESSION['username'])  
        {  
       $type = $_SESSION['Type'] ;        
       if ($type == "V")
           echo "contactVIP" ;
       else if ($type == "S")
           echo "contactSuper" ;
       else
           echo "contactUser" ;
        }
        else echo "contact" ;
           ?>">
      <!--<p><center><span class="btext">Πτυχιακή : <br/>Ολοκληρωμένο Σύστημα διαχείρισης μικρο-ελεγκτών απομακρυσμένα σε πραγματικό χρόνο</span> <br/>Galliakis Michael(michaelgalliakis@yahoo.gr)</center></p>-->
        
            <?php  
    session_start();  
       $parentFileName = basename($parentFilePath); 
    if(!$_SESSION['username'])  
    {  
         
       echo '<p><b><center><span class="btext"><a href="Login.php" title=""><img src="myImages/Login-icon.png" alt ="Login" height="36" width="36"/></a><a href="Registration.php" title=""><img src="myImages/signUp.png" alt ="Sign up" height="36" width="36"/></a></b></span></center></p>' ;
    }  
    else
    {                      
        echo '<p><b><center><span class="btext"><u>';
        echo $_SESSION['username']; 
        echo '</u>&nbsp;<a href="Profile.php" title=""><img src="myImages/profile.png" alt ="Profile" height="36" width="36"/></a><a href="Logout.php" title=""><img src="myImages/Logout-icon.png" alt ="Logout" height="36" width="36"/></a></b></span></center></p>' ;
    }
    ?>  
        
    </div>
    <div class="corner">     
        <div id="navcontainer">
            <div id="smashnav">
                <ul>
                    <li><a href="index.php" title="" <?php if ($parentFileName=="index.php") echo 'class="current"' ?>><span>HOME</span></a></li>
   <?php if ($_SESSION['username']) { ?>
                    <li><a href="ManageDevices.php" title="" <?php if ($parentFileName=="ManageDevices.php" ||$parentFileName=="EditDevice.php"||$parentFileName=="Invite.php"||$parentFileName=="NewDevice.php" ) echo 'class="current"' ?>><span>Manage Devices</span></a></li>
                    <li><a href="MyRequests.php" title="" <?php if ($parentFileName=="MyRequests.php") echo 'class="current"' ?>><span>My Requests</span></a></li>
                    <li><a href="Search.php" title="" <?php if ($parentFileName=="Search.php") echo 'class="current"' ?>><span>Search other Devices</span></a></li>
   <?php } else { ?>  
                    
                    <li><label><span>Manage Devices</span></label></li>
                    <li><label><span>My Requests</span></label></li>
                    <li><label><span>Search other Devices</span></label></li>      
                    
   <?php } ?>
                    <li><a href="Download.php" title="" <?php if ($parentFileName=="Download.php") echo 'class="current"' ?>><span>Download</span></a></li>
                    <li><a href="InfoHelp.php" title="" <?php if ($parentFileName=="InfoHelp.php") echo 'class="current"' ?>><span>Info-Help</span></a></li>
                    <li><a href="Thesis.php" title="" <?php if ($parentFileName=="Thesis.php") echo 'class="current"' ?>><span>Thesis</span></a></li>
                    
                </ul>
            </div>
        </div>
    </div>
</div>
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
