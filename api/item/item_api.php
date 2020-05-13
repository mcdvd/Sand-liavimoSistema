<?php

spl_autoload_register(function ($name) {
    require $document_root = $_SERVER['DOCUMENT_ROOT'] . '/' . $name . '.php';
});

use objects\Item;
use repositories\barcodeRepository;
use repositories\categoryRepository;
use repositories\itemRepository;

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    switch ($_GET['function']) {
        case 'getItems':
            getItems();
            break;
    }
} else if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents("php://input"));
    switch ($data->function) {
        case 'insertItemByBarcode':
            insertItemWithBarcode($data->item);
            break;
        case 'updateItem':
            updateItem($data->data);
            break;
        case 'deleteItem':
            deleteItem($data->id);
            break;
        case 'insertItemIntoCategory':
            if (empty($data->categoryID)) {
                insertItemIntoCategory($data->itemID, null);
            } else {
                insertItemIntoCategory($data->itemID, $data->categoryID);
            }
            break;
    }
}

function getItems()
{
    $item = new itemRepository();
    $category = new categoryRepository();
    $stmt = $item->getItems();
    $num = $stmt->rowCount();

    if ($num > 0) {

        $items_arr = [];

        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            extract($row);
            $item_row = [
                "ID" => $ID,
                "Item_name" => $Item_name,
                "Category" => $category->getCategoryByItem($ID),
            ];
            array_push($items_arr, $item_row);
        }

        http_response_code(200);

        echo json_encode($items_arr);
    } else {
        http_response_code(404);

        echo json_encode(
            ["message" => "No items found."]
        );
    }
}

function insertItemWithBarcode($item)
{
    $itemRep = new itemRepository();
    $barcode = new barcodeRepository();
    $category = new categoryRepository();

    $itemId = $item->insertItem($item);
    $barcode->insertBarcode($itemId, $item->BarcodeObj);
    $category->insertItemIntoCategory($itemId, $item->category);

    http_response_code(200);
}

/**
 * @param Item $item
 * @param $categoryID
 */
function updateItem($item)
{
    $itemRep = new itemRepository();
    $itemRep->updateItem($item);
    $categoryRep = new categoryRepository();
    if (!empty($item->Category->id)) {
        $result = $categoryRep->updateItemCategory($item->ID, $item->Category->id);
        if (!$result) {
            $categoryRep->insertItemIntoCategory($item->ID, $item->Category->id);
        }
    }
}

function deleteItem($itemId)
{
    $itemRep = new itemRepository();
    $itemRep->deleteItem($itemId);
}

function insertItemIntoCategory($itemID, $categoryID)
{
    $categoryRep = new categoryRepository();
    $categoryRep->insertItemIntoCategory($itemID, $categoryID);
}

