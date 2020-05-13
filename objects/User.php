<?php


namespace objects;


class User
{
    public $id;
    public $username;
    public $password;
    public $timecreated;
    public $lastlogin;
    public $role;

    /**
     * User constructor.
     * @param $id
     * @param $username
     * @param $password
     * @param $timecreated
     * @param $lastlogin
     * @param $role
     */
    public function __construct($id, $username, $password, $timecreated, $lastlogin, $role)
    {
        $this->id = $id;
        $this->username = $username;
        $this->password = $password;
        $this->timecreated = $timecreated;
        $this->lastlogin = $lastlogin;
        $this->role = $role;
    }
}