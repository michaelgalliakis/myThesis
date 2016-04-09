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
                        <center>                          
                            <h3>ΠΤΥΧΙΑΚΗ ΕΡΓΑΣΙΑ </h3><br/>
                            <b><u>με θέμα :</u></b>
                            <hr/>
                            <div id='Subject'
                                 <p>
                                    <u>Μελέτη ενός ολοκληρωμένου συστήματος για τη διαχείριση μικρο-ελεγκτών απομακρυσμένα <br/>  
                                    σε πραγματικό χρόνο από εγγεγραμμένους χρήστες (πχ για τον έλεγχο "έξυπνων" σπιτιών). </u><br/>
                                    Πιλοτικά θα υλοποιηθεί client-server εφαρμογή που θα επιτρέπει την διαχείριση διάφορων   μονάδων (Αισθητήρες,φώτα,διακόπτες) κάποιων μικρο-ελεγκτών(Arduino).  <br/>
                                    Έμφαση θα δοθεί στη χρήση τεχνολογιών Java,MySQL,Arduino sketch,PHP,Javascript,CSS,HTML .
                                </p>
                            </div>
                            <hr/>
                            <i>Technological Educational Institute of Athens - IT department.</i><br/>
                            Φοιτητής : <b>Γαλλιάκης Μιχαήλ</b> (<u>081001</u>)<br>
                            Επιβλέπων καθηγητές : <u>Σκουρλάς Χρήστος</u> & <u>Τσολακίδης Αναστάριος</u> <br/>
                            Email : <label id="papaki3">cs081001[papaki]teiath[telia]gr</label> & <label id="papaki4">michaelgalliakis[papaki]yahoo[telia]gr</label><br/>                                
                            Όλα τα αρχεία βρίσκονται στο : (<a href="https://github.com/michaelgalliakis/myThesis.git">Github</a>)<br/>
                            <u>Ημερομηνία παρουσίασης : 4/2016</u><br/>
                        </center>
                        <!-- <img src="images/logoPMGv2F_2016.png" width="48" height="48"  align = "right"/> -->
                        <script>   
                            document.getElementById("papaki3").innerHTML = document.getElementById("papaki3").innerHTML.replace("[papaki]" , "@");    
                            document.getElementById("papaki3").innerHTML = document.getElementById("papaki3").innerHTML.replace("[telia]" , ".");    
                            document.getElementById("papaki4").innerHTML = document.getElementById("papaki4").innerHTML.replace("[papaki]" , "@");    
                            document.getElementById("papaki4").innerHTML = document.getElementById("papaki4").innerHTML.replace("[telia]" , ".");    
                        </script>
                    </div>     
                    <div align="left">
                        <img src="images/logoPMGv2F_2016.png" width="300" height="300"  align = "center"/>
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