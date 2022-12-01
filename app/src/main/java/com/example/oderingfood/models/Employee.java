package com.example.oderingfood.models;

// This class is used in EmployeeManager
// It just use to display Avatar and Name
public class Employee {

    private String id;
    private String name;
    private String avatar;


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

    public Employee(String id, String name, String avatar)
    {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

}
