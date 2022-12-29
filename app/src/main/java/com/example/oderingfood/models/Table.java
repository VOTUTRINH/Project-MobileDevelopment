package com.example.oderingfood.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Table implements Serializable {
    private String name;
    private String state;
    private int priority = 1000;
    private List<Food> order; // Su dung Pair: String la ten mon an, Interger la so luong ordered
    private Boolean isbooked = false;
    private List<Booking> bookings;



    // SETTER - GETTER
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

    public List<Food> getOrder() {
        return order;
    }

    public void setOrder(List<Food> order) {
        this.order = order;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
// END SETTER - GETTER

    public Table(String name)
    {
        this.name = name;
        state = "Empty";
        order = new ArrayList<Food>();
        isbooked = false;
    }
    public Table(String name, int pri)
    {
        this.name = name;
        state = "Empty";
        priority = pri;
        order = new ArrayList<Food>();
    }

    public void AddFood(Food food)
    {
        order.add(food);
    }

    public Boolean getIsbooked() {
        return isbooked;
    }

    public void Book() {
        this.isbooked = true;
    }
    public void unBooked(){
        this.isbooked = false;
    }
}
