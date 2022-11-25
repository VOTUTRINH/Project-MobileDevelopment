package com.example.oderingfood;

import java.util.ArrayList;
import java.util.List;


public class Table {
//    private String name;
    private float tongTien;
    private String state;
    private List<Food> menu;

    public void TongTien(float tongTien)
    {
        this.tongTien = tongTien;
    }

    public void State(String state)
    {
        this.state = state;
    }
//    public String Name(){return name;}
    public float TongTien()
    {
        return tongTien;
    }
    public String State()
    {
        return state;
    }
    public List<Food> Menu()
    {
        return menu;
    }

    public Table()
    {
//        this.name = name;
        tongTien = 0;
        state = "";
        menu = new ArrayList<Food>();
    }

    public void AddFood(Food food)
    {
        menu.add(food);
    }

    public void ChangeState(String state)
    {
        this.state = state;
    }

    public float CalculateTotalMoney()
    {
        float totalMoney = 0;
        for (int i = 0; i < menu.size(); i++) {
            totalMoney += menu.get(i).price;
        }
        return totalMoney;
    }


}
