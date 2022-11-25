package com.example.oderingfood.models;

public class Food {
    int id;
    String name;
    double price;
    int quantity;
    double total;


    public Food(int id) {
        this.id = id;
    }

    public Food(int id, String name, double price, int quantity, double total) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

