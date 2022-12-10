package com.example.oderingfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.Employee;
import com.example.oderingfood.models.Table;

import java.util.List;

public class ListEmployeesAdapter extends ArrayAdapter<Employee> {
    private  Context context;
    List<Employee> employeeList;

    public ListEmployeesAdapter(@NonNull Context context, int resource, List<Employee> employeeList) {
        super(context, resource);
        this.context = context;
        this.employeeList = employeeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View item = inflater.inflate(R.layout.custom_item_employee, null);

        ImageView avt = (ImageView) item.findViewById(R.id.employee_avatar);
        TextView name = (TextView) item.findViewById(R.id.employee_name);

        Employee employee = employeeList.get(position);

        Glide.with(context).load(employee.getAvatar()).into(avt);
        name.setText(employee.getName());

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),a2g18Activity.class);
                intent.putExtra("idUser",employee.getId());
                view.getContext().startActivity(intent);
            }
        });
        return (item);
    }
}
