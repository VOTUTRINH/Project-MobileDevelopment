package com.example.oderingfood;

import java.util.ArrayList;
import java.util.List;


enum TableState{
    IsUsing,
    Empty
}
public class Table {
    private String name;
    private float tongTien;
    private TableState state;
    private List<Food> menu;

    public String Name(){return name;}
    public float TongTien()
    {
        return tongTien;
    }
    public TableState State()
    {
        return state;
    }
    public List<Food> Menu()
    {
        return  menu;
    }

    public Table(String name)
    {
        this.name = name;
        tongTien = 0;
        state = TableState.Empty;
        menu = new ArrayList<Food>();
    }

    public void AddFood(Food food)
    {
        menu.add(food);
    }

    public void ChangeState(TableState state)
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
