<?php
    // mysql login
    $con = mysqli_connect("localhost", "pjpj1018", "wlsgml112", "pjpj1018");
    // korean set
    mysqli_query($con,'SET NAMES utf8');

    // post로 android에서 보낸 값 받는 것 같음 -> 받아서 변수에 할당
    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    
    // statement변수에 sql query "문" 저장
    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE userID = ? AND userPassword = ?");
    
    // bind_param 이름 처럼 ? 한곳에 위에서 android에서 받은 parameters를 bind시킴
    // ss 는 string
    mysqli_stmt_bind_param($statement, "ss", $userID, $userPassword);
    
    // 위에서 준비된 query를 여기서 실행
    mysqli_stmt_execute($statement);

    // 결과 저장
    mysqli_stmt_store_result($statement);
    
    // The PHP mysqli_stmt_bind_result() function returns a boolean value which is true on success and false on failure.
    // After binding variables, you need to invoke the mysqli_stmt_fetch() function to get the values of the columns in the specified variables.
    mysqli_stmt_bind_result($statement, $userID, $userPassword, $userAge, $userSex, $gymName);

    $response = array();
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["userID"] = $userID;
        $response["userPassword"] = $userPassword;
        $response["userAge"] = $userAge;
        $response["userSex"] = $userSex;  
        $response["gymName"] = $gymName;    
    }

    echo json_encode($response);



?>