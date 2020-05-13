<?php

namespace repositories;

spl_autoload_register(function ($name) {
    require $document_root = $_SERVER['DOCUMENT_ROOT'] . '/' . $name . '.php';
});

class barcodeRepository extends repository
{
    public function insertBarcode($itemId, $barcodeObj)
    {
        $query = 'Insert into barcode (pid, barcode, barcode_type) values (?, ?, ?)';
        $stmt = $this->dbConn->prepare($query);
        $stmt->execute([$itemId, $barcodeObj->barcode, $barcodeObj->barcodeType]);

        return $this->dbConn->lastInsertId();
    }
}