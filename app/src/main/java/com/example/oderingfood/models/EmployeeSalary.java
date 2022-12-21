package com.example.oderingfood.models;

public class EmployeeSalary {

    public String getName() {
        return name;
    }

    public void setIdRes(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public EmployeeSalary(String name, String salary) {
        this.name = name;
        this.salary = salary;
    }





    private String name;
    private String salary;



    }
