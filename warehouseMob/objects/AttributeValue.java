package com.example.warehouse.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttributeValue implements Serializable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("aid")
    private Integer aid;
    @SerializedName("value")
    private String value;

    public AttributeValue(Integer id, Integer aid, String value) {
        this.id = id;
        this.aid = aid;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
