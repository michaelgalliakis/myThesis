function newDeviceformValidation()  
{  
    var uid = document.newDevice.Devicename;  
    var pass1 = document.newDevice.Password1;  
    var pass2 = document.newDevice.Password2;      
    //var comment = document.newDevice.Comment;  
    
    if(userid_validation(uid,5,12))  
    
    {  
        if(passid_validation(pass1,pass2,5,12))  
        {  
            return true;
        }  
    }  
    
    return false;  
}  
function changePath(username,AccessType)
{
    
    var lastHREF = document.getElementById("Button"+username).href ;
    lastHREF = lastHREF.substring(0, lastHREF.length - 1);
    document.getElementById("Button"+username).href = lastHREF + AccessType ;    
}
function editDeviceformValidation()  
{  
    //var uid = document.newDevice.Devicename;  
    var pass1 = document.editDevice.Password1;  
    var pass2 = document.editDevice.Password2;  
    var lastPass = document.editDevice.lastPassword;  
    //var comment = document.newDevice.Comment;       
    if ((lastPass.value!==pass1.value)||(lastPass.value!==pass2.value))
    {        
        if(passid_validation(pass1,pass2,5,12))  
        {  
            return true;
        }          
    } 
    else
        return true ;

    return false;  
}  
function registationformValidation()  
{  
    var uid = document.registration.UsernameR;  
    var pass1 = document.registration.Password1;  
    var pass2 = document.registration.Password2;  
    var uname = document.registration.Name;  
    var usurname = document.registration.Surname;  
    var uemail = document.registration.Email;  
    
    if(userid_validation(uid,5,12))  
    
    {  
        if(passid_validation(pass1,pass2,5,12))  
        {  
            if(allLetter(uname))  
            {
                if(allLetter(usurname))  
                {  
                    if(ValidateEmail(uemail))  
                    {  
                        return true;
                    }   
                }  
            }   
        }  
    }  
    
    return false;  
}  
function deleteMessage(mess)  
{  
    var r = confirm("Είσαι σίγουρος ότι θέλεις να διαγράψεις "+mess+";");
    if (r) {
        return true ;
    } else {
        return false ;
    } 
}
function changeProgileformValidation()  
{      
    var pass1 = document.changeProfile.Password1;  
    var pass2 = document.changeProfile.Password2;  
    var uname = document.changeProfile.Name;  
    var surname = document.changeProfile.Surname;  
    var uemail = document.changeProfile.Email;  
    
    
    if(passid_validation(pass1,pass2,5,12))  
    {  
        if(allLetter(uname))  
        {
            if(allLetter(surname))  
            {  
                if(ValidateEmail(uemail))  
                {  
                    return true;
                }   
            }  
        }   
    }  
        
    return false;  
}  
function userid_validation(uid,mx,my)  
{  
    var uid_len = uid.value.length;  
    if (uid_len == 0 || uid_len >= my || uid_len < mx)  
    {  
        alert("Το αναγνωριστικό χρήστη δεν πρέπει να είναι κενό / το μήκος πρέπει να είναι μεταξύ : "+mx+" και "+my);  
        uid.focus();  
        return false;  
    }  
    return true;  
} 
function passid_validation(pass1,pass2,mx,my)  
{  
    var passid_len = pass1.value.length;  
    if (passid_len === 0 ||passid_len >= my || passid_len < mx)  
    {  
        alert("Ο κωδικός δεν πρέπει να είναι άδειος / το μήκος πρέπει να είναι μεταξύ : "+mx+" και "+my);  
        pass1.focus();  
        return false;  
    }  
    passid_len = pass2.value.length;  
    if (passid_len === 0 ||passid_len >= my || passid_len < mx)  
    {  
        alert("Ο κωδικός επιβεβαίωσης δεν πρέπει να είναι άδειος / το μήκος πρέπει να είναι μεταξύ : "+mx+" και "+my);  
        pass2.focus();  
        return false;  
    }  
    if (pass2.value!==pass1.value)  
    {
        alert("Οι κωδικοί δεν είναι ίδιοι");  
        pass1.focus();  
        return false;  
    }        
    else
        return true;  
} 
function allLetter(uname)  
{   
    var letters = /^[A-Za-z]+$/;  
    if(uname.value.match(letters))  
    {  
        return true;  
    }  
    else  
    {  
        alert('Το όνομα και το επώνυμο του χρήστη πρέπει να περιέχουν μόνο χαρακτήρες');  
        uname.focus();  
        return false;  
    }  
} 
function ValidateEmail(uemail)  
{  
    var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;  
    if(uemail.value.match(mailformat))  
    {  
        return true;  
    }  
    else  
    {  
        alert("Καταχωρίσατε διεύθυνση email με άκυρη σύνταξη!");  
        uemail.focus();  
        return false;  
    }  
} 
/*    
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
                                          */ 