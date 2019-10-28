package com.dtasmuk.multilevelexpandablerecyclerview;

import android.support.annotation.NonNull;

//import androidx.annotation.NonNull;

import java.util.List;

public class Item extends RecyclerViewItem {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Item() {
        super();
    }

    public Item(List<Item> children) {
        super(children);
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}
