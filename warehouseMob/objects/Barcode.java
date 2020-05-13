package com.example.warehouse.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Barcode implements Serializable {
    @SerializedName("id")
    private Integer ID;
    @SerializedName("barcode")
    private String Barcode;
    @SerializedName("barcodeType")
    private String Barcode_type;
    @SerializedName("item")
    private Item item;

    public Barcode() {
    }

    public Barcode(Integer ID, String barcode, String barcode_type, Item item) {
        this.ID = ID;
        Barcode = barcode;
        Barcode_type = barcode_type;
        this.item = item;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getBarcode_type() {
        return Barcode_type;
    }

    public void setBarcode_type(String barcode_type) {
        Barcode_type = barcode_type;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
