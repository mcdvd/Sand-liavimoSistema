package com.example.warehouse.MultiLevelList;

import com.multilevelview.models.RecyclerViewItem;

public class Itemer extends RecyclerViewItem {

    String text="";

    String secondText = "";

    public String getSecondText() {
        return secondText;
    }

    public void setSecondText(String secondText) {
        this.secondText = secondText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Itemer(int level) {
        super(level);
    }
}
