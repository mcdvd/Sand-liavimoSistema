<?php

namespace api;

use database\Database;

class apiRequest
{
    /**
     * @var Database;
     */
    protected $database;

    /**
     * apiRequest constructor.
     */
    public function __construct()
    {
        header("Content-Type: application/json; charset=UTF-8");
        $database = new Database();
        $db = $database->getConnection();
    }
}