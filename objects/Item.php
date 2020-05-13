<?php

namespace objects;

class Item
{
    public $ID;
    public $Item_name;

    /**
     * Item constructor.
     * @param $ID
     * @param $Item_name
     */
    public function __construct($ID, $Item_name)
    {
        $this->ID = $ID;
        $this->Item_name = $Item_name;
    }
}
