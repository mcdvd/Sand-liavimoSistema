<?php

namespace repositories;

use objects\User;

spl_autoload_register(function ($name) {
    require $document_root = $_SERVER['DOCUMENT_ROOT'] . '/' . $name . '.php';
});

class userRepository extends repository
{
    private $table_name = 'users';

    public function getUsers()
    {
        $query = "select * from " . $this->table_name;
        $stmt = $this->dbConn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    /**
     * @param User $user
     * @return string
     */
    public function insertUser($user)
    {
        $stmt = $this->dbConn->prepare('Insert into users (username, password, role) values (?, ?, ?)');
        $stmt->bindParam(1, $user->username);
        $stmt->bindParam(2, $user->password);
        $stmt->bindParam(3, $user->role);
        $stmt->execute();

        return $this->dbConn->lastInsertId();
    }

    public function deleteUser($userID)
    {
        $stmt = $this->dbConn->prepare('Delete from users where id = ?');
        $stmt->bindParam(1, $userID);

        $result = $stmt->execute();
        return $result;
    }

    /**
     * @param User $user
     * @return bool
     */
    public function updateUser($user)
    {
        $stmt = $this->dbConn->prepare('Update users set username = ?, password = ?, role = ? where ID = ?');
        $stmt->bindParam(1, $user->username);
        $stmt->bindParam(2, $user->password);
        $stmt->bindParam(3, $user->role);
        $stmt->bindParam(4, $user->id);

        $result = $stmt->execute();
        return $result;
    }

    public function validateUser($username, $password){
        $query = "select * from " . $this->table_name . " where username = ? and password = ?";
        $stmt = $this->dbConn->prepare($query);
        $stmt->bindParam(1, $username);
        $stmt->bindParam(2, $password);
        $stmt->execute();
        $result = $stmt->rowCount();
        return $result;
    }
}