package com.example.oderingfood.models;

public class DetailEmployee {
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public DetailEmployee(String id, String name, String avatar, String date, String address, String sex, String phone) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.date = date;
        this.address = address;
        this.sex = sex;
        this.phone = phone;
    }

    private String id;
    private String name;
    private String avatar;
    private String date;
    private String address;
    private String sex;
    private String phone;
}
