<?php

require $_SERVER['DOCUMENT_ROOT'] . '/vendor/autoload.php';

use Karriere\JsonDecoder\JsonDecoder;
use objects\Barcode;
use objects\Item;
use repositories\barcodeRepository;
use repositories\categoryRepository;
use repositories\itemRepository;

spl_autoload_register(function ($name) {
    require $document_root = $_SERVER['DOCUMENT_ROOT'] . '/' . $name . '.php';
});

header("Access-Control-Allow-Origin: *");

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    $data = json_decode('{"function":"insertItemByBarcode","data":{"barcode":"6410530086117","barcodeType":"test_12","item":{"Item_name":"Dezinfekatorius"},"categoryID":1}}');
    insertItemByBarcode($data->data);
    if (!empty($_GET['function'])) {
        switch ($_GET['function']) {
            case 'getItemByBarcode':
                getItemByBarcode($_GET['barcode']);
                break;
        }
    }
} else if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $parsedBody = json_decode(file_get_contents("php://input"));
    if (empty($parsedBody->function)) {
        echo 'No function served';
        return;
    }
    switch ($parsedBody->function) {
        case 'insertItemByBarcode':
            insertItemByBarcode($parsedBody->data);
            break;
        default:
            echo 'function not found';
            break;
    }
}

function getItemByBarcode($barcode)
{
    $item = new itemRepository();
    $item = $item->getItemByBarcode($barcode);

    if (!empty($item)) {
        http_response_code(200);
        echo json_encode([$item]);
    } else {
        http_response_code(404);
        echo json_encode(
            []
        );
    }
}

function insertItemByBarcode($parsedData)
{
    //TODO validation for repeating barcode
    $jsonDecoder = new JsonDecoder();
    $itemRep = new itemRepository();
    $barcodeRep = new barcodeRepository();
    $categoryRep = new categoryRepository();

    $itemId = $itemRep->insertItem((object)$parsedData->item);
    $barcodeId = $barcodeRep->insertBarcode($itemId, $parsedData);
    $categoryRep->insertItemIntoCategory($itemId, $parsedData->categoryID);

    if (!empty($barcodeId)) {
        http_response_code(200);
    } else {
        //TODO maybe
        http_response_code(204);
    }
}
