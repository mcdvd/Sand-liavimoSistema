<?php

spl_autoload_register(function ($name) {
    require $document_root = $_SERVER['DOCUMENT_ROOT'] . '/' . $name . '.php';
});

use repositories\barcodeRepository;
use repositories\categoryRepository;
use repositories\itemRepository;

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    $test = new categoryRepository();
    switch ($_GET['function']) {
        case 'getCategories':
            getCategories();
            break;
    }
} else if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents("php://input"));
    switch ($data->function) {
        case 'insertCategory':
            insertCategory($data->data);
            break;
        case 'updateCategory':
            updateCategory($data->data);
            break;
        case 'deleteCategory':
            deleteCategory($data->id);
            break;
    }
}

function getCategories()
{
    $categoryRep = new categoryRepository();
    $stmt = $categoryRep->getCategories();
    $num = $stmt->rowCount();

    if ($num > 0) {
        $categoriesArray = [];
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            extract($row);
            $categoryRow = [
                "id" => $id,
                "category_name" => $category_name,
                "parentID" => $parentID,
            ];
            array_push($categoriesArray, $categoryRow);
        }

        http_response_code(200);
        echo json_encode($categoriesArray);
    } else {
        http_response_code(404);

        echo json_encode(
            ["message" => "No categories found."]
        );
    }
}

function insertCategory($category)
{
    $categoryRep = new categoryRepository();
    $categoryRep->insertCategory($category);
}

function deleteCategory($categoryID)
{
    $categoryRep = new categoryRepository();
    $categoryRep->deleteCategory($categoryID);
}

function updateCategory($category)
{
    $categoryRep = new categoryRepository();
    $categoryRep->updateCategory($category);
}