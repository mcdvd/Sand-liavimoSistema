package com.example.warehouse.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Array;

public class Attribute implements Serializable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("attribute_name")
    private String attribute_name;
    @SerializedName("attribute_lov")
    private AttributeValue[] attribute_lov;

    public Attribute(Integer id, String attribute_name, AttributeValue[] attribute_lov) {
        this.id = id;
        this.attribute_name = attribute_name;
        this.attribute_lov = attribute_lov;
    }

    public Attribute(Integer id, String attribute_name) {
        this.id = id;
        this.attribute_name = attribute_name;
    }

    public AttributeValue[] getAttribute_lov() {
        return attribute_lov;
    }

    public void setAttribute_lov(AttributeValue[] attribute_lov) {
        this.attribute_lov = attribute_lov;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }
}
