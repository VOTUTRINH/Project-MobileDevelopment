package com.example.oderingfood.models;

import java.io.Serializable;

public class Food implements Serializable {
    String id;
    String name;
    double price;
    int quantity;
    int total;
    int totalOrder;
    String urlImage;

    public Food(){
    }



    public Food(String id) {
        this.id = id;
    }

    public Food(String name, int quantity)
    {
        this.name = name;
        this.quantity = quantity;
    }

    public Food(String id, String name, double price, int quantity, int total) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }
    public Food(String id, String name, double price, int totalOrder, String urlImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.totalOrder = totalOrder;
        this.urlImage = urlImage;
        quantity = 0;
    }
    public Food(String id, String name, double price, String urlImage){
        this.id=id;
        this.name = name;
        this.price = price;
        this.totalOrder =0;
        this.quantity = 0;
        this.urlImage=urlImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUrlImage() {
        return urlImage;
    }
    public void plusQuantity(){
        quantity++;
    }
    public void minusQuantity(){
        if (quantity>0) {
            quantity--;
        }
    }

    @Override
    public String toString() {
        return "Food{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", total=" + total +
                ", totalOrder=" + totalOrder +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}

