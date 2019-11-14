package com.dtasmuk.multilevelexpandablerecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        List<Item> adapterList = new ArrayList<>();

        List<Item> children1List = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Item item = new Item();
            item.setText("(1) Item #" + i + " Level2");
            children1List.add(item);
        }

        List<Item> children2List = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Item item = new Item();
            item.setText("(2) Item #" + i + " Level2");
            children2List.add(item);
        }

        List<Item> children3List = new ArrayList<>();
        for (int i = 1; i < 60; i++) {
            Item item = new Item();
            item.setText("(3) Item #" + i + " Level2");
            children3List.add(item);
        }

        Item itemLevel3 = new Item();
        itemLevel3.setText("(2) Item #1 Level3");

        Item itemLevel41 = new Item();
        itemLevel41.setText("(2) Item #1 Level4");

        Item itemLevel42 = new Item();
        itemLevel42.setText("(2) Item #2 Level4");

        itemLevel3.addChild(itemLevel41);
        itemLevel3.addChild(itemLevel42);

        Item item1 = new Item(children1List);
        item1.setText("(1) Item #1 Level1");

        Item item2 = new Item(children2List);
        item2.setText("(2) Item #2 Level1");
        item2.getChildren().get(1).addChild(itemLevel3);
        item2.getChildren().get(1).setCheckType(ItemSelectableType.NONE);

        Item item3 = new Item(children3List);
        item3.setText("(3) Item #3 Level1");

        adapterList.add(item1);
        adapterList.add(item2);
        adapterList.add(item3);

        adapter = new MyAdapter(this, adapterList);
        adapter.expandAll();
        rv.setAdapter(adapter);
    }

    public void buttonClick(View view) {
        adapter.collapseAll();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    public void buttonGetSelectedClicked(View view) {
        Toast.makeText(this, "" + adapter.getSelectedItems(null), Toast.LENGTH_SHORT).show();
    }
}
