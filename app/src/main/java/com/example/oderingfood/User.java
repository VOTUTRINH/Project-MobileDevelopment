package com.example.oderingfood;

public class User {
    String Email,HoTen,NgaySinh,GioiTinh, DienThoai, DiaChi;

    public User() {
    }

    public User(String email, String hoTen, String ngaySinh, String gioiTinh, String dienThoai, String diaChi) {
        this.Email = email;
        this.HoTen = hoTen;
        this.NgaySinh = ngaySinh;
        this.GioiTinh = gioiTinh;
        this.DienThoai = dienThoai;
        this.DiaChi = diaChi;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        this.HoTen = hoTen;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.NgaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.GioiTinh = gioiTinh;
    }

    public String getDienThoai() {
        return DienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.DienThoai = dienThoai;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        this.DiaChi = diaChi;
    }
}
