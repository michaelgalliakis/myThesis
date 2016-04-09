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
                <br/>                
                <p>Τεκμηρίωση της πτυχιακής<a href="otherFiles/Thesis_GalliakisMichael_AM081001.pdf"> (πατήστε εδώ για download)</a>:</p>
                <hr/>
                <object data="otherFiles/Thesis_GalliakisMichael_AM081001.pdf" type="application/pdf" width="80%" height="650px">                    
                </object>                                
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
