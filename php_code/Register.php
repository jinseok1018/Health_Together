<?php 
    $con = mysqli_connect("localhost", "pjpj1018", "wlsgml112", "pjpj1018");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    $userAge = $_POST["userAge"];
    $userSex = $_POST["userSex"];
    $gymName = $_POST["gymName"];

    $query = "SELECT * FROM user where userID = '$userID' ";
    $data = mysqli_query($con, $query);
    $total_rows = mysqli_num_rows($data);


    if($total_rows > 0){
        $response["success"] = false;
    } else {
        $statement = mysqli_prepare($con, "INSERT INTO user VALUES (?,?,?,?,?)");
        mysqli_stmt_bind_param($statement, "ssiss", $userID, $userPassword, $userAge, $userSex, $gymName);
        mysqli_stmt_execute($statement);
    
        $response = array();
        $response["success"] = true;
    }
    
 
    echo json_encode($response);
?>