<?php

spl_autoload_register(function ($name) {
    require $document_root = $_SERVER['DOCUMENT_ROOT'] . '/' . $name . '.php';
});

use repositories\attributeRepository;
use repositories\barcodeRepository;
use repositories\categoryRepository;
use repositories\itemRepository;

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    switch ($_GET['function']) {
        case 'getAttributes':
            getAttributes();
            break;
    }
} else if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents("php://input"));
    switch ($data->function) {
        case 'insertAttribute':
            insertAttribute($data->data);
            break;
        case 'updateAttribute':
            updateAttribute($data->data);
            break;
        case 'deleteAttribute':
            deleteAttribute($data->id);
            break;
    }
}

function getAttributes()
{
    $attributeRep = new attributeRepository();
    $stmt = $attributeRep->getAttributes();
    $num = $stmt->rowCount();

    if ($num > 0) {
        $attributesArray = [];
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            extract($row);

            $attributeRow = [
                "id" => $id,
                "attribute_name" => $attribute_name,
                "attribute_lov" => $attributeRep->getAttributeValues($id),
            ];
            array_push($attributesArray, $attributeRow);
        }

        http_response_code(200);
        echo json_encode($attributesArray);
    } else {
        http_response_code(404);

        echo json_encode(
            ["message" => "No attributes found."]
        );
    }
}

function insertAttribute($attribute)
{
    $attributeRep = new attributeRepository();
    $attributeRep->insertAttribute($attribute, $attribute->attribute_lov);
}

function deleteAttribute($attributeID){
    $attributeRep = new attributeRepository();
    $attributeRep->deleteAttribute($attributeID);
}

function updateAttribute($attribute){
    $attributeRep = new attributeRepository();
    $attributeRep->updateAttribute($attribute);
}