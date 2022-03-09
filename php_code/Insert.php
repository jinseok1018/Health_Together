<?php 
    $con = mysqli_connect("localhost", "pjpj1018", "wlsgml112", "pjpj1018");
    mysqli_query($con,'SET NAMES utf8');
    $num = 1;
    $id = $_POST["id"];
    $time = $_POST["time"];
    $content = $_POST["content"];
    $gymName = $_POST["gymName"];

    $statement = mysqli_prepare($con, "INSERT INTO list(id, time, content, gymName) VALUES (?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssss", $id, $time, $content, $gymName);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
 

    echo json_encode($response);

?>