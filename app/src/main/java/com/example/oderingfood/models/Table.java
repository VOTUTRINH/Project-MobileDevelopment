package com.example.oderingfood.models;

import android.util.Pair;

import com.example.oderingfood.models.Food;

import java.util.ArrayList;
import java.util.List;


public class Table {
    private String name;
    private String state;
    private List<Food> menu; // Su dung Pair: String la ten mon an, Interger la so luong ordered


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

    public List<Food> getMenu() {
        return menu;
    }

    public void setMenu(List<Food> menu) {
        this.menu = menu;
    }
    public Table(String name)
    {
        this.name = name;
        state = "Empty";
        menu = new ArrayList<Food>();
    }

    public void AddFood(Food food)
    {
        menu.add(food);
    }
}
