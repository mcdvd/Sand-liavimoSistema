<?php

namespace repositories;

use objects\Barcode;
use objects\Item;
use PDO;

spl_autoload_register(function ($name) {
    require $document_root = $_SERVER['DOCUMENT_ROOT'] . '/' . $name . '.php';
});

class itemRepository extends repository
{
    private $table_name = "item";

    public function getItems()
    {
        $query = "select * from " . $this->table_name;
        $stmt = $this->dbConn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    public function getItemByBarcode($barcode)
    {
        $query = "select b.ID, b.PID, b.Barcode, b.Barcode_type, p.Item_name from barcode b
        inner join item p on p.ID = b.PID
        where b.barcode = " . $barcode;
        $result = $this->dbConn->query($query)->fetch(PDO::FETCH_ASSOC);

        if ($result !== false) {
            $itemObject = new Item($result['PID'], $result['Item_name']);
            $barcodeObject = new Barcode($result['ID'], $result['Barcode'], $result['Barcode_type'], $itemObject);

            return $barcodeObject;
        }

        return null;
    }

    /**
     * @param Item $item
     * @return string
     */
    public function insertItem($item)
    {
        $stmt = $this->dbConn->prepare('Insert into item (Item_name) values (?)');
        $stmt->bindParam(1, $item->Item_name);
        $stmt->execute();

        return $this->dbConn->lastInsertId();
    }

    /**
     * @param Item $item
     * @return bool
     */
    public function updateItem($item)
    {
        $stmt = $this->dbConn->prepare('Update item set Item_name = ? where ID = ?');
        $stmt->bindParam(1, $item->Item_name);
        $stmt->bindParam(2, $item->ID);

        $result = $stmt->execute();
        return $result;
    }

    public function deleteItem($itemId)
    {
        $stmt = $this->dbConn->prepare('Delete from item where id = ?');
        $stmt->bindParam(1, $itemId);

        $result = $stmt->execute();
        return $result;
    }
}