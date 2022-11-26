package com.example.oderingfood.models;

import java.security.PublicKey;

public class Restaurant {
    private String IDRes;
    private String TenQuan;
    private String DiaChi;
    private String urlImage;
    private String ChuQuan;

    public Restaurant(String IDRes, String name, String address, String urlImage, String id){
        this.IDRes = IDRes;
        this.TenQuan = name;
        this.DiaChi = address;
        this.urlImage = urlImage;
        this.ChuQuan= id;
    }

    public String getAddress() {
        return DiaChi;
    }

    public String getId() {
        return ChuQuan;
    }

    public String getName() {
        return TenQuan;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getIDRes() {
        return IDRes;
    }
}
