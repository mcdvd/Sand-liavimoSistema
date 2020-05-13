<?php


namespace repositories;


use objects\Category;
use PDO;

class categoryRepository extends repository
{
    private $table_name = 'category';

    public function getCategories()
    {
        $query = "select * from " . $this->table_name;
        $stmt = $this->dbConn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    public function insertItemIntoCategory($itemID, $categoryID)
    {
        $stmt = $this->dbConn->prepare('Insert into item_category (pid, cid) values (?, ?)');
        $stmt->bindParam(1, $itemID);
        $stmt->bindParam(2, $categoryID);
        $stmt->execute();

        return $this->dbConn->lastInsertId();
    }

    public function updateItemCategory($itemID, $categoryID)
    {
        $stmt = $this->dbConn->prepare('Update item_category set cid = ? where pid = ?');
        $stmt->bindParam(1, $categoryID);
        $stmt->bindParam(2, $itemID);

        $result = $stmt->execute();
        $result = $stmt->rowCount();
        return $result;
    }

    public function getCategoryByItem($itemID)
    {
        $stmt = $this->dbConn->prepare('SELECT c.* from item_category ic
                                                    Inner JOIN category c ON c.id=ic.cid
                                                    where pid = ?');
        $stmt->bindParam(1, $itemID);
        $stmt->execute();
        $row = (object)$stmt->fetch(PDO::FETCH_ASSOC);

        if ($row == false) {
            return null;
        } else {
            return $row;
        }
    }

    /**
     * @param Category $category
     * @return string
     */
    public function insertCategory($category)
    {
        $stmt = $this->dbConn->prepare('Insert into category (category_name, parentID) values (?, ?)');
        $stmt->bindParam(1, $category->category_name);
        $stmt->bindParam(2, $category->parentID);
        $stmt->execute();

        return $this->dbConn->lastInsertId();
    }

    public function deleteCategory($categoryId)
    {
        $stmt = $this->dbConn->prepare('Delete from category where id = ?');
        $stmt->bindParam(1, $categoryId);

        $result = $stmt->execute();
        return $result;
    }

    /**
     * @param Category $category
     * @return bool
     */
    public function updateCategory($category)
    {
        $stmt = null;
        if (!empty($category->parentID)) {
            $stmt = $this->dbConn->prepare('Update category set category_name = ?, parentID = ? where ID = ?');
            $stmt->bindParam(1, $category->category_name);
            $stmt->bindParam(2, $category->parentID);
            $stmt->bindParam(3, $category->id);
        } else {
            $stmt = $this->dbConn->prepare('Update category set category_name = ? where ID = ?');
            $stmt->bindParam(1, $category->category_name);
            $stmt->bindParam(2, $category->id);
        }

        $result = $stmt->execute();
        return $result;
    }
}