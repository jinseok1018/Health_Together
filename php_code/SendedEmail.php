<?php 
    $con = mysqli_connect("localhost", "pjpj1018", "wlsgml112", "pjpj1018");
    mysqli_query($con,'SET NAMES utf8');

    $id = $_POST["id"];

    $statement = mysqli_prepare($con, "SELECT * FROM email WHERE sender = ? order by num desc");
    mysqli_stmt_bind_param($statement, "s", $id);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $num, $sender, $receiver, $content);

    
    $response = array();
    $response['read'] = array();

    while(mysqli_stmt_fetch($statement)) {
        $h['receiver'] = $receiver;
        $h['content'] = $content;
        array_push($response['read'], $h);
    }
    $response['success'] = "1";
 

    echo json_encode($response);

?>