<?php
    $con = mysqli_connect("localhost", "pjpj1018", "wlsgml112", "pjpj1018");
    mysqli_query($con,'SET NAMES utf8');
    $id = $_POST["id"];
    $statement = mysqli_prepare($con, "SELECT IFNULL(id, 'll') AS id
    FROM `emailnew` A
RIGHT OUTER JOIN (SELECT '') AS id
      ON A.id = ? ");
    
    mysqli_stmt_bind_param($statement, "s", $id);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $nid);

    $response = array();
    
    while(mysqli_stmt_fetch($statement)) {
    $response["success"] = true;
    $response["id"] = $nid;
    }

    echo json_encode($response);
    
?>