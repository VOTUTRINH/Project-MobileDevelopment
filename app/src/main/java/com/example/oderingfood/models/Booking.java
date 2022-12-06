package com.example.oderingfood.models;

public class Booking {
    String startTime;
    String endTime;
    String id;
    String date;
    String name;

    public Booking(String startTime, String endTime, String id, String date, String name) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.id = id;
        this.date = date;
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
}


