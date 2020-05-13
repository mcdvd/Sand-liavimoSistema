<?php

namespace repositories;

use database\Database;
use PDO;

spl_autoload_register(function ($name) {
    include $name . '.php';
});


class repository
{
    /**
     * @var Database;
     */
    private $database;

    /**
     * @var PDO
     */
    protected $dbConn;

    /**
     * apiRequest constructor.
     */
    public function __construct()
    {
        $this->database = new Database();
        $this->dbConn = $this->database->getConnection();
    }
}