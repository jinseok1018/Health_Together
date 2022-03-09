<?php 
    $con = mysqli_connect("localhost", "pjpj1018", "wlsgml112", "pjpj1018");
    mysqli_query($con,'SET NAMES utf8');

    $sender = $_POST["sender"];
    $receiver = $_POST["receiver"];
    $content = $_POST["content"];
    
    $query = "SELECT * FROM user where userID = '$receiver' ";
    $data = mysqli_query($con, $query);
    $total_rows = mysqli_num_rows($data);


    if($total_rows > 0){
        $statement2 = mysqli_prepare($con, "INSERT INTO email(sender, receiver, content) VALUES ('$sender','$receiver','$content')");
        $statement3 = mysqli_prepare($con, "INSERT INTO emailnew(id) VALUES ('$receiver')");

        mysqli_stmt_execute($statement2);
        mysqli_stmt_execute($statement3);

        $response = array();
        $response["success"] = true;

    } else {
        
        $response["success"] = false;
    }

    echo json_encode($response);
?>