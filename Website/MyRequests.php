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
                    <table>  
                        <thead>  
                        <hr/>
                        <tr>                        
                            <th colspan="6">Αιτήσεις Χρηστών που θέλουν να αποκτήσουν δικαιώματα στις συσκευές μου :</th>                                  
                        </tr>  
                        <tr>
                            <th>Device:</th>                  
                            <th>User:</th>                  
                            <th>Right:</th>  
                            <th>Request:</th>                                                    
                            <th>Accept:</th>  
                            <th>Reject:</th>  
                        </tr>  
                        </thead>  
                            
            <?php  
            include("Db_conection.php");  
            $user_username=$_SESSION['username'];
            $user_ID=$_SESSION['ID'];
                
            $view_users_query="SELECT RequestID,(SELECT Username From users WHERE UserID=UserTo)AS UserTo,
                TypeAccess,
                (SELECT d.DeviceNAme From devices d WHERE d.DeviceID=r.DeviceID)AS DeviceName,Text
                FROM requests r WHERE DeviceID IN (
                    SELECT d.DeviceID FROM devices d
                    LEFT JOIN deviceaccess da ON d.DeviceID = da.DeviceID AND da.USERID = '$user_ID' AND da.isDeleted = false
                    WHERE (d.Owner = '$user_ID' OR da.UserID = '$user_ID') AND d.isDeleted = false AND (da.TypeAccess = 'A' OR d.Owner = '$user_ID'))
                AND UserTo = UserFrom ORDER BY DeviceName, TypeAccess,UserTo";
            $run=mysqli_query($dbcon,$view_users_query);//here run the sql query.  
            $thereIsNotRecordFlag = true ;
            while($row=mysqli_fetch_array($run))//while look to fetch the result and store in a array $row.  
            {  
                $thereIsNotRecordFlag = false; 
                $requestID = $row['RequestID'] ;
                $userTo = $row['UserTo'] ;                
                $access = $row['TypeAccess'] ;
                $deviceID = $row['DeviceName'] ;
                $text = $row['Text'] ;                
            ?>  
                
                        <tr>  
                            
                            <td><?php echo $deviceID;  ?></td> 
                            <td><?php echo $userTo;  ?></td>                                 
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
                        
                            <td><?php echo $text;  ?></td>
                                
                            <td><a href="ConfirmRequest.php?requestID=<?php echo $requestID ?>"><button class="confirm"></button></a></td>                                     
                            <td><a href="RemoveRequest.php?requestID=<?php echo $requestID ?>"><button class="deleteRequest1"></button></a></td>                                     
                        </tr>  
                            
            <?php } if ($thereIsNotRecordFlag) echo "<tr><td colspan='6'>Δεν υπάρχουν αιτήσεις!</td></tr>"; ?> 
                
                    </table>  
                    <hr/>         
                    <br/>
                        
                    <table>  
                        <thead>  
                            <tr>                        
                                <th colspan="6">Προσκλήσεις από άλλους χρήστες για να δεχτώ δικαιώματα σε νέες συσκευές:</th>                                  
                            </tr>  
                            <tr>                        
                                <th>Device:</th>  
                                <th>Right:</th>  
                                <th>User:</th>                  
                                <th>Invite:</th>                                    
                                <th>Accept:</th>  
                                <th>Reject:</th>  
                            </tr>  
                        </thead>  
                            
            <?php              
                
            $view_users_query="SELECT RequestID,(SELECT Username From users WHERE UserID=UserFrom)AS UserFrom,
                TypeAccess,
                (SELECT d.DeviceNAme From devices d WHERE d.DeviceID=r.DeviceID) AS DeviceName,Text FROM requests r WHERE UserFrom != '$user_ID' And UserTo = '$user_ID' ORDER BY DeviceName, TypeAccess,UserTo" ;
                    
            $run=mysqli_query($dbcon,$view_users_query);//here run the sql query.  
                
            $thereIsNotRecordFlag = true ;
            while($row=mysqli_fetch_array($run))//while look to fetch the result and store in a array $row.  
            {  
                $thereIsNotRecordFlag = false ;
                $requestID = $row['RequestID'] ;
                $userFrom = $row['UserFrom'] ;                
                $access = $row['TypeAccess'] ;
                $deviceID = $row['DeviceName'] ;
                $text = $row['Text'] ;                
            ?>  
                
                        <tr>  
                            <!--here showing results in the table -->  
                            <td><?php echo $deviceID;  ?></td>                           
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
                        
                            <td><?php echo $userFrom;  ?></td>    
                            <td><?php echo $text;  ?></td>
                                
                            <td><a href="ConfirmRequest.php?requestID=<?php echo $requestID ?>"><button class="confirm"></button></a></td>                                     
                            <td><a href="RemoveRequest.php?requestID=<?php echo $requestID ?>"><button class="deleteRequest1"></button></a></td>                                     
                        </tr>  
                            
            <?php } if ($thereIsNotRecordFlag) echo "<tr><td colspan='6'>Δεν υπάρχουν προσκλήσεις!</td></tr>"; ?>  
                
                    </table> 
                        
                    <hr/>         
                    <br/>                
                    <table>  
                        <thead>  
                            <tr>                        
                                <th colspan="5">Προσκλήσεις που έχω κάνει για να πάρουν δικαιώματα κάποιοι άλλοι χρήστες στις συσκευές μου:</th>                                  
                            </tr>  
                            <tr>                        
                                <th>Device:</th>  
                                <th>User</th>                  
                                <th>Right:</th>                  
                                <th>Invite:</th>                                                     
                                <th>Delete:</th>  
                            </tr>  
                        </thead>  
                            
            <?php                          
                
            $view_users_query="SELECT RequestID,(SELECT Username From users WHERE UserID=UserTo)AS UserTo,
                TypeAccess,
                (SELECT d.DeviceNAme From devices d WHERE d.DeviceID=r.DeviceID) AS DeviceName,Text FROM requests r WHERE UserFrom = '$user_ID' And UserTo != '$user_ID' ORDER BY DeviceName, TypeAccess,UserTo" ;
                    
            $run=mysqli_query($dbcon,$view_users_query);//here run the sql query.  
            $thereIsNotRecordFlag = true ;
            while($row=mysqli_fetch_array($run))//while look to fetch the result and store in a array $row.  
            {  
                $thereIsNotRecordFlag = false ;
                $requestID = $row['RequestID'] ;
                $userTo = $row['UserTo'] ;                
                $access = $row['TypeAccess'] ;
                $deviceID = $row['DeviceName'] ;
                $text = $row['Text'] ;                
            ?>  
                
                        <tr>      
                            <td><?php echo $deviceID;  ?></td> 
                            <td><?php echo $userTo;  ?></td>                                 
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
                        
                            <td><?php echo $text;  ?></td>
                                
                            <td><a href="RemoveRequest.php?requestID=<?php echo $requestID ?>"><button class="deleteRequest2"></button></a></td>                                     
                        </tr>  
                            
            <?php }  if ($thereIsNotRecordFlag) echo "<tr><td colspan='5'>Δεν υπάρχουν προσκλήσεις!</td></tr>";  ?>  
                
                    </table> 
                    <hr/>         
                    <br/>
                        
                    <table>  
                        <thead>  
                            <tr>                        
                                <th colspan="4">Αιτήσεις που έχω κάνειγια να πάρω δικαιώματα σε μια νέα συσκεύη:</th>                                  
                            </tr>  
                            <tr>                        
                                <th>Device:</th>  
                                <th>Right:</th>                  
                                <th>Request:</th>                                                     
                                <th>Delete:</th>  
                            </tr>  
                        </thead>  
                            
            <?php                          
                
            $view_users_query="SELECT RequestID,TypeAccess,
                (SELECT d.DeviceNAme From devices d WHERE d.DeviceID=r.DeviceID) AS DeviceName,Text FROM requests r WHERE UserFrom = '$user_ID' And UserTo = '$user_ID' ORDER BY DeviceName, TypeAccess,UserTo" ;
                    
            $run=mysqli_query($dbcon,$view_users_query);//here run the sql query.  
                
            $thereIsNotRecordFlag = true ;
            while($row=mysqli_fetch_array($run))//while look to fetch the result and store in a array $row.  
            {  
                $thereIsNotRecordFlag = false ;
                $requestID = $row['RequestID'] ;                       
                $access = $row['TypeAccess'] ;
                $deviceID = $row['DeviceName'] ;
                $text = $row['Text'] ;                
            ?>  
                
                        <tr>  
                            
                            <td><?php echo $deviceID;  ?></td>                 
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
                        
                            <td><?php echo $text;  ?></td>
                                
                            <td><a href="RemoveRequest.php?requestID=<?php echo $requestID ?>"><button class="deleteRequest2"></button></a></td>                                     
                        </tr>  
                            
            <?php } if ($thereIsNotRecordFlag) echo "<tr><td colspan='4'>Δεν υπάρχουν αιτήσεις!</td></tr>"; ?>  
                
                    </table> 
                    <hr/>
                </center>
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