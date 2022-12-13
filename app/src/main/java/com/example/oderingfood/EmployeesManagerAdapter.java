package com.example.oderingfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.oderingfood.models.Employee;

import java.util.List;

public class EmployeesManagerAdapter extends RecyclerView.Adapter<EmployeesManagerAdapter.MyViewHolder> {
    List<Employee> employees;
    LayoutInflater inflater;
    Context context;
    String idRes;

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


    public EmployeesManagerAdapter(Context context, List<Employee>employees, String idRes)
    {
        this.context = context;
        this.inflater =LayoutInflater.from(context);
        this.employees = employees;
        this.idRes = idRes;
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
        Glide.with(context).load(employees.get(position).getAvatar()).into(holder.avatar);
        holder.name.setText(employee.getName());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(),a2g18Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idUser", employee.getId());
                bundle.putString("idRes", idRes);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

}
