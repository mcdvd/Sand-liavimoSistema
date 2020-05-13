<?php

use repositories\userRepository;

spl_autoload_register(function ($name) {
    require $document_root = $_SERVER['DOCUMENT_ROOT'] . '/' . $name . '.php';
});

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    switch ($_GET['function']) {
        case 'getUsers':
            getUsers();
            break;
    }
} else if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents("php://input"));
    switch ($data->function) {
        case 'validateUser':
            validateUser($data->username, $data->password);
            break;
        case 'insertUser':
            insertUser($data->data);
            break;
        case 'updateUser':
            updateUser($data->data);
            break;
        case 'deleteUser':
            deleteUser($data->id);
            break;
    }
}

function getUsers()
{
    $userRep = new userRepository();
    $stmt = $userRep->getUsers();
    $num = $stmt->rowCount();

    if ($num > 0) {
        $usersArray = [];
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            extract($row);
            $userRow = [
                "id" => $id,
                "username" => $username,
                "password" => $password,
                "timecreated" => $timecreated,
                "lastlogin" => $lastlogin,
                "role" => $role,
            ];
            array_push($usersArray, $userRow);
        }

        http_response_code(200);
        echo json_encode($usersArray);
    } else {
        http_response_code(404);

        echo json_encode(
            ["message" => "No users found."]
        );
    }
}

function insertUser($user){
    $userRep = new userRepository();
    $userRep->insertUser($user);
}

function deleteUser($userID)
{
    $userRep = new userRepository();
    $userRep->deleteUser($userID);
}

function updateUser($user)
{
    $userRep = new userRepository();
    $userRep->updateUser($user);
}

function validateUser($username, $password)
{
    $userRep = new userRepository();
    $isValid = $userRep->validateUser($username, $password);
    if($isValid == 0) {
        http_response_code(404);
    } else {
        http_response_code(200);
    }
}