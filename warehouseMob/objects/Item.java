package com.example.warehouse.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item implements Serializable {

    @SerializedName("ID")
    private Integer ID;
    @SerializedName("Item_name")
    private String ItemName;
    @SerializedName("Category")
    private Category Category;


    public Item(Integer ID, String itemName) {
        this.ID = ID;
        this.ItemName = itemName;
    }

    public Item(Integer ID, String itemName, com.example.warehouse.objects.Category category) {
        this.ID = ID;
        ItemName = itemName;
        Category = category;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public com.example.warehouse.objects.Category getCategory() {
        return Category;
    }

    public void setCategory(com.example.warehouse.objects.Category category) {
        Category = category;
    }
}