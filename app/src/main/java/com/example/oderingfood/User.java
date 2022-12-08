package com.example.oderingfood;

public class User {
    String id,name,dofBirth,gender, phone, address;

    public User() {
    }

    public User(String id,String name, String dofBirth, String gender, String phone, String address) {
        this.id=id;
        this.name = name;
        this.dofBirth = dofBirth;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
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

    public String getDofBirth() {
        return dofBirth;
    }

    public void setDofBirth(String dofBirth) {
        this.dofBirth = dofBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
