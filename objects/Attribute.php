<?php


namespace objects;


class Attribute
{
    public $id;
    public $attribute_name;
    public $attribute_lov;

    /**
     * Attribute constructor.
     * @param $id
     * @param $attribute_name
     * @param $attribute_lov
     */
    public function __construct($id, $attribute_name, $attribute_lov)
    {
        $this->id = $id;
        $this->attribute_name = $attribute_name;
        $this->attribute_lov = $attribute_lov;
    }
}