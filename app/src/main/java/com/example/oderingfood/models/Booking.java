package com.example.oderingfood.models;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Booking {
    String timeStart;
    String timeEnd;
    String id;
    String date;
    String name;
    String phone;
    String IdUser;
    Pair<String, List<Food>> tableBook;
    boolean isConfirm = false;


    public Booking(String timeStart, String timeEnd, String id, String date, String name, String phone, String idu, boolean cf) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.id = id;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.IdUser = idu;
        this.isConfirm = cf;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }

    public Booking(String timeStart, String timeEnd, String id, String date, String name) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.id = id;
        this.date = date;
        this.name = name;
    }

    public Pair<String, List<Food>> getTableBook() {
        return tableBook;
    }

    public void addTableBook(String key, List<Food> tableBook) {
        this.tableBook = new Pair<>(key, tableBook);
    }
}


