package com.example.oderingfood;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;


import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EmployeeManageActivity extends Fragment {
    RecyclerView listEmployees;
    RecyclerView listEmployeesWorking;
    TextInputEditText txtDateChosen;
    List<Employee> employees = new ArrayList<Employee>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View employeeManagerFragment = inflater.inflate(R.layout.activity_employee_manage, container, false);


        txtDateChosen = (TextInputEditText) employeeManagerFragment.findViewById(R.id.txt_date_chosen);
        listEmployees = (RecyclerView) employeeManagerFragment.findViewById(R.id.list_employees);
        listEmployeesWorking = (RecyclerView) employeeManagerFragment.findViewById(R.id.employee_working);

        txtDateChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender =Calendar.getInstance();
                int year= calender.get(calender.YEAR);
                int month= calender.get(calender.MONTH);
                int day= calender.get(calender.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        calender.set(year,month,date);
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy/mm/dd");
                        txtDateChosen.setText(simpleDateFormat.format(calender.getTime()));
                    }
                },year, month, day);
                datePickerDialog.show();
            }
        });

        employees.add(new Employee(0,"Toai Nguyen", R.drawable.img_1));
        employees.add(new Employee(1,"Nguyen Toai", R.drawable.img_1));
        employees.add(new Employee(0,"Toai Nguyen",R.drawable.img_1));
        employees.add(new Employee(1,"Nguyen Toai", R.drawable.img_1));
        employees.add(new Employee(0,"Toai Nguyen",R.drawable.img_1));
        employees.add(new Employee(1,"Nguyen Toai", R.drawable.img_1));
        employees.add(new Employee(0,"Toai Nguyen",R.drawable.img_1));
        employees.add(new Employee(1,"Nguyen Toai", R.drawable.img_1));

        EmployeesManagerAdapter adapterListEmployees = new EmployeesManagerAdapter(getActivity(), employees);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        listEmployees.setLayoutManager(layoutManager1);
        listEmployees.setItemAnimator(new DefaultItemAnimator());
        listEmployees.setAdapter(adapterListEmployees);

        EmployeesManagerAdapter adapterListEmployeesWorking = new EmployeesManagerAdapter(getActivity(), employees);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        listEmployeesWorking.setLayoutManager(layoutManager2);
        listEmployeesWorking.setItemAnimator(new DefaultItemAnimator());
        listEmployeesWorking.setAdapter(adapterListEmployeesWorking);

        return employeeManagerFragment;
    }

}