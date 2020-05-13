<?php

namespace objects;

spl_autoload_register(function ($name) {
    require $document_root = $_SERVER['DOCUMENT_ROOT'] . '/' . $name . '.php';
});

class Barcode
{
    public $id;
    public $barcode;
    public $barcodeType;
    public $item;

    /**
     * Barcode constructor.
     * @param $id
     * @param $barcode
     * @param $barcodeType
     * @param $item
     */
    public function __construct($id, $barcode, $barcodeType, $item)
    {
        $this->id = $id;
        $this->barcode = $barcode;
        $this->barcodeType = $barcodeType;
        $this->item = $item;
    }


}