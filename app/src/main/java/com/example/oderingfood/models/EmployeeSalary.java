package com.example.oderingfood.models;

public class EmployeeSalary {

    private String name;
    private String salary;
    private Long thoiGianLamViec;

    public String GetResName() {
        return name;
    }

    public void SetResName(String name) {
        this.name = name;
    }

    public String GetBaseSalary() {
        return salary;
    }

    public void SetBaseSalary(String salary) {
        this.salary = salary;
    }

    public Long GetTimeWork(){return thoiGianLamViec;}
    public void SetTimeWork(Long timeWork){this.thoiGianLamViec = timeWork;}

    public EmployeeSalary(String name, String salary, Long thoiGianLamViec) {
        this.name = name;
        this.salary = salary;
        this.thoiGianLamViec = thoiGianLamViec;
    }








    }
