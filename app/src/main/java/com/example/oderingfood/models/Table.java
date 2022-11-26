package com.example.oderingfood.models;

import com.example.oderingfood.models.Food;

import java.util.ArrayList;
import java.util.List;


public class Table {
    //    private String name;
    private float tongTien;
    private String state;
    private List<Food> orderFood;

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
    public List<Food> OrderFood()
    {
        return orderFood;
    }

    public Table()
    {
//        this.name = name;
        tongTien = 0;
        state = "";
        orderFood = new ArrayList<Food>();
    }

    public void AddFood(Food food)
    {
        orderFood.add(food);
    }

    public void ChangeState(String state)
    {
        this.state = state;
    }

    public float CalculateTotalMoney()
    {
        float totalMoney = 0;
        for (int i = 0; i < orderFood.size(); i++) {
            totalMoney += orderFood.get(i).getPrice();
        }
        return totalMoney;
    }


}
