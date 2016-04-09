<!DOCTYPE html>
<?php  
session_start();//session starts here    
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
                    <div id="slideText">                    
                        <a href="otherFiles/UserClientV1.zip"> <p align="center"><img src="myImages/userClient.png" width="48" height="28"  align = "middle"/><br/>Java User Client (V1)</p></a><hr/> 
                        <a href="otherFiles/DeviceClientV1.zip"><p align="center"><img src="myImages/deviceClient.png" width="48" height="28"  align = "middle"/><br/> Java Device Client (V1)</p></a><hr/> 
                        <a href="otherFiles/rxtx-2.2pre2.zip"><p align="center"><img src="myImages/library.png" width="48" height="28"  align = "middle"/><br/> rxtx-2.2pre2 Library</p></a><hr/> 
                        <a href="otherFiles/templateSketches.zip"><p align="center"><img src="myImages/arduino.png" width="48" height="28"  align = "middle"/><br/> Arduino templates sketches (V1)</p></a><hr/> 
                        <a href="otherFiles/DeviceClientSimulatorV1.zip"><p align="center"><img src="myImages/simulator.png" width="48" height="28"  align = "middle"/> <br/>Java Simulator Device Client (V1)</p></a><hr/> 
                    </div>
                    <div align="right">
                        <img src="myImages/Downloads.png" width="300" height="300"  align = "center"/>
                    </div>
                        
                </div>
                    
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