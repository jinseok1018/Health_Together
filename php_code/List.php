<?php
    $con = mysqli_connect("localhost", "pjpj1018", "wlsgml112", "pjpj1018");
    mysqli_query($con,'SET NAMES utf8');

    $gymName = $_POST["gymName"];


    $statement = mysqli_prepare($con, "SELECT * FROM list where gymName = ? ORDER BY num desc");
    mysqli_stmt_bind_param($statement, "s", $gymName);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $num, $id, $time, $content, $gymName);

    $response = array();
    $response['read'] = array();

    while(mysqli_stmt_fetch($statement)) {
        $h['id'] = $id;
        $h['time'] = $time;
        $h['content'] = $content;
        array_push($response['read'], $h);
    }
    $response['success'] = "1";
    echo json_encode($response);
    
?>