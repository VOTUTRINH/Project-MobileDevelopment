package com.example.oderingfood;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MonAnActivity extends AppCompatActivity {

    RecyclerView dataList;
    List<String> titles;
    List<Integer> images;
    List<String> prices;

    AdapterMonAn adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);
        dataList = findViewById(R.id.dishesList);
        titles = new ArrayList<>();
        images = new ArrayList<>();
        prices = new ArrayList<>();
        titles.add("First Dish");
        titles.add("Second Dish");
        titles.add("Third Dish");
        titles.add("Fourth Dish");
        titles.add("Fifth Dish");
        titles.add("Sixth Dish");
        titles.add("Sixth Dish");


        images.add(R.drawable.img);
        images.add(R.drawable.img_2);
        images.add(R.drawable.img_1);
        images.add(R.drawable.img_3);
        images.add(R.drawable.img);
        images.add(R.drawable.img_4);
        images.add(R.drawable.img_2);


        prices.add("10 USD");
        prices.add("11 USD");
        prices.add("12 USD");
        prices.add("13 USD");
        prices.add("14 USD");
        prices.add("15 USD");
        prices.add("15 USD");

        adapter = new AdapterMonAn(this, titles, images, prices);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);

    }


}