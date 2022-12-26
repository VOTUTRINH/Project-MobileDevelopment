package com.example.oderingfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
        TextView name = (TextView) row.findViewById(R.id.resSalary);
        TextView phone = (TextView) row.findViewById(R.id.employeeSalary);

        name.setText("Tên nhà hàng: " + dataList.get(position).getName());
        phone.setText("Lương: " + dataList.get(position).getSalary());

        return (row);
    }
}
