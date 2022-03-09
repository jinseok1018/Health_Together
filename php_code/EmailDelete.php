<?php
    $con = mysqli_connect("localhost", "pjpj1018", "wlsgml112", "pjpj1018");
    mysqli_query($con,'SET NAMES utf8');

    $id = $_POST['id'];

    $statement = mysqli_prepare($con, "DELETE FROM emailnew WHERE id = ? ");
    mysqli_stmt_bind_param($statement, "s", $id);
    if(mysqli_stmt_execute($statement)){
        $response["success"] = true;
    } else {
        $response["success"] = false;
    }

    echo json_encode($response);
?>