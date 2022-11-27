package com.example.oderingfood.models;

import android.util.Pair;

import com.example.oderingfood.models.Food;

import java.util.ArrayList;
import java.util.List;


public class Table {
    private String name;
    private String state;
    private List<Pair<String,Integer>> menu; // Su dung Pair: String la ten mon an, Interger la so luong ordered


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Pair<String, Integer>> getMenu() {
        return menu;
    }

    public void setMenu(List<Pair<String, Integer>> menu) {
        this.menu = menu;
    }
    public Table(String name)
    {
        this.name = name;
        state = "Empty";
        menu = new ArrayList<Pair<String,Integer>>();
    }

    public void AddFood(Pair<String,Integer> food)
    {
        menu.add(food);
    }
}
