<?php


namespace objects;


class Category
{
    public $id;
    public $category_name;
    public $parentID;

    /**
     * Category constructor.
     * @param $id
     * @param $category_name
     * @param $parent_id
     */
    public function __construct($id, $category_name, $parent_id)
    {
        $this->id = $id;
        $this->category_name = $category_name;
        $this->parentID = $parent_id;
    }
}