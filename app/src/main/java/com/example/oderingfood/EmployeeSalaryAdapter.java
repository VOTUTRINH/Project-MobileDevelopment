package com.example.oderingfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.models.Booking;
import com.example.oderingfood.models.EmployeeSalary;

import java.util.List;


public class EmployeeSalaryAdapter extends ArrayAdapter<EmployeeSalary> {
    Context context;
    List<EmployeeSalary> dataList;
    public EmployeeSalaryAdapter(Context context,int resource, List<EmployeeSalary> dataList){
        super(context,R.layout.item_nhanvien,dataList);
        this.context=context;
        this.dataList = dataList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.item_nhanvien, null);
        TextView txttenCuaHang = (TextView) row.findViewById(R.id.tenNhaHang);
        TextView txtluongCoBan = (TextView) row.findViewById(R.id.luongCoBan);
        TextView txttongLuong = (TextView) row.findViewById(R.id.tongLuong);
        LinearLayout itemLayout = (LinearLayout) row.findViewById(R.id.item_layout);

        if(position % 2 == 1){
            itemLayout.setBackgroundColor(Color.parseColor("#F85F6A"));
        }else{
            itemLayout.setBackgroundColor(Color.parseColor("#ff8992"));
        }

        txttenCuaHang.setText("Tên nhà hàng: " + dataList.get(position).GetResName());
        txtluongCoBan.setText("Lương: " + dataList.get(position).GetBaseSalary());
        Long tongLuong = Long.parseLong(dataList.get(position).GetBaseSalary()) * dataList.get(position).GetTimeWork();

        txttongLuong.setText("Tổng lương: " + tongLuong);

        return (row);
    }
}
