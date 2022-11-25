package com.example.oderingfood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.example.oderingfood.models.NhanVien;

import java.util.ArrayList;

public class ListNhanVien extends AppCompatActivity {
    GridView gridView;
    ArrayList<NhanVien> arrayList;
    AdapterNhanVien adapterNhanVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nhan_vien);
        gridView=(GridView) findViewById(R.id.gvNhanvien);
        arrayList=new ArrayList<>();
        arrayList.add(new NhanVien(" Phuc", R.drawable.avt2));
        arrayList.add(new NhanVien("Phuc", R.drawable.avt2));
        arrayList.add(new NhanVien(" Phuc", R.drawable.avt2));
        arrayList.add(new NhanVien("Phuc", R.drawable.avt2));
        arrayList.add(new NhanVien(" Phuc", R.drawable.avt2));
        arrayList.add(new NhanVien(" Phuc", R.drawable.avt2));
        arrayList.add(new NhanVien(" Phuc", R.drawable.avt2));
        arrayList.add(new NhanVien(" Phuc", R.drawable.avt2));
        arrayList.add(new NhanVien(" Phuc", R.drawable.avt2));

        adapterNhanVien=new AdapterNhanVien(this,R.layout.item_nhanvien,arrayList);
        gridView.setAdapter(adapterNhanVien);
    }
}