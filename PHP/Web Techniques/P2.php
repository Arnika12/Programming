<!-- Write a PHP script to keep track of number of times the web page has been access. [Use Session] -->

<?php
    session_start();
    if(isset($_SESSION['count']))
        $_SESSION['count']=$_SESSION['count']+1;
    else
        $_SESSION['count']=1;
    echo "<h3>This page is accessed for </h3>".$_SESSION['count'];
?>


