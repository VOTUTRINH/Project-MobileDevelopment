package com.example.oderingfood;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class EmployeesManagerAdapter extends RecyclerView.Adapter<EmployeesManagerAdapter.MyViewHolder> {
    List<Employee> employees;
    LayoutInflater inflater;
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        ImageView avatar;
        TextView name;
        MyViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.item_employee);
            avatar = view.findViewById(R.id.employee_avatar);
            name = view.findViewById(R.id.employee_name);
        }
    }


    public EmployeesManagerAdapter(Context context, List<Employee>employees)
    {
        this.inflater =LayoutInflater.from(context);
        this.employees = employees;
    }

    @NonNull
    @Override
    public EmployeesManagerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_employee,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeesManagerAdapter.MyViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.avatar.setImageResource(employee.avatar);
        holder.name.setText(employee.name);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Click Vao Nhan Vien", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

}
