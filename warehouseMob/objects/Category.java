package com.example.warehouse.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("category_name")
    private String category_name;
    @SerializedName("parentID")
    private Integer parentID;

    public Category(Integer id, String category_name, Integer parentID) {
        this.id = id;
        this.category_name = category_name;
        this.parentID = parentID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }
}
