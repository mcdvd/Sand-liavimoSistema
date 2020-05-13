<?php


namespace repositories;

use objects\Attribute;
use PDO;

spl_autoload_register(function ($name) {
    require $document_root = $_SERVER['DOCUMENT_ROOT'] . '/' . $name . '.php';
});

class attributeRepository extends repository
{
    private $table_name = 'attribute';

    public function getAttributes()
    {
        $query = "select * from " . $this->table_name;
        $stmt = $this->dbConn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    /**
     * @param Attribute $attribute
     * @return string
     */
    public function insertAttribute($attribute, $attributeLOV)
    {
        $stmt = $this->dbConn->prepare('Insert into attribute (attribute_name) values (?)');
        $stmt->bindParam(1, $attribute->attribute_name);
        $stmt->execute();

        $lastInsertedAttributeId = $this->dbConn->lastInsertId();

        foreach ($attributeLOV as $attributeValue) {
            $stmt = $this->dbConn->prepare('Insert into attribute_values (aid, value) values (?, ?)');
            $stmt->bindParam(1, $lastInsertedAttributeId);
            $stmt->bindParam(2, $attributeValue);
            $stmt->execute();
        }

        return $this->dbConn->lastInsertId();
    }

    public function getAttributeValues($attributeID)
    {
        $stmt = $this->dbConn->prepare('select * from attribute_values where aid = ?');
        $stmt->bindParam(1, $attributeID);
        $stmt->execute();

        $attributesArray = [];
        if($stmt->rowCount() > 0)
        {
            while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                extract($row);

                $attributeValueRow = [
                    "id" => $id,
                    "aid" => $aid,
                    "value" => $value
                ];
                array_push($attributesArray, $attributeValueRow);
            }
            return $attributesArray;
        }
        return null;
    }

    public function deleteAttribute($attributeId)
    {
        $stmt = $this->dbConn->prepare('Delete from attribute where id = ?');
        $stmt->bindParam(1, $attributeId);

        $result = $stmt->execute();
        return $result;
    }

    /**
     * @param Attribute $attribute
     * @return bool
     */
    public function updateAttribute($attribute)
    {
        $stmt = $this->dbConn->prepare('Update attribute set attribute_name = ? where ID = ?');
        $stmt->bindParam(1, $attribute->attribute_name);
        $stmt->bindParam(2, $attribute->id);
        $result = $stmt->execute();

        $originalAttributeValues = [];
        foreach($this->getAttributeValues($attribute->id) as $lov){
            $originalAttributeValues[] = $lov['value'];
        }

        $newAttributeValues = [];
        foreach($attribute->attribute_lov as $lov){
            $newAttributeValues[] = $lov->value;
        }

        $toBeDeletedAttributeValues = array_diff($originalAttributeValues, $newAttributeValues);
        $toBeCreatedAttributeValues = array_diff($newAttributeValues, $originalAttributeValues);

        foreach($toBeDeletedAttributeValues as $deletableValue){
            $stmt = $this->dbConn->prepare('Delete from attribute_values where value = ? and aid = ?');
            $stmt->bindParam(1, $deletableValue);
            $stmt->bindParam(2, $attribute->id);
            $stmt->execute();
        }

        foreach($toBeCreatedAttributeValues as $creatableValue){
            $stmt = $this->dbConn->prepare('Insert into attribute_values (aid, value) values (?, ?)');
            $stmt->bindParam(1, $attribute->id);
            $stmt->bindParam(2, $creatableValue);
            $stmt->execute();
        }

        return $result;
    }
}