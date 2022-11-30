package com.example.oderingfood;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.example.oderingfood.models.Employee;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeManageActivity extends Fragment {
    RecyclerView listEmployees;
    RecyclerView listEmployeesWorking;

    TextView txtTotalEmployees;
    TextView txtTotalEmployeesWorking;

    Context context;

    TextInputEditText txtDateChosen;
    List<Employee> employees = new ArrayList<Employee>();
    List<Employee> employeesWorking = new ArrayList<Employee>();

    // Adapter
    EmployeesManagerAdapter adapterListEmployees;
    EmployeesManagerAdapter adapterListEmployeesWorking;

    EmployeeManageActivity employeeManageActivity;
    String idRestaurent = "xzxHmkiUMHVjqNu67Ewzsv2TQjr2";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static EmployeeManageActivity newInstance(String param1, String param2) {
        EmployeeManageActivity fragment = new EmployeeManageActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EmployeeManageActivity() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        try {
            context = getActivity();
            // tablesActivity = (TablesActivity) getActivity();

            employeeManageActivity = new EmployeeManageActivity();
        }catch (Exception e)
        {
        }
        adapterListEmployees = new EmployeesManagerAdapter(getActivity(), employees);
        adapterListEmployeesWorking = new EmployeesManagerAdapter(getActivity(), employeesWorking);


        // Get data list employee from firebase
        // Do Something
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefEmployee = database.getReference("restaurant/" + idRestaurent + "/NhanVien");



        dbRefEmployee.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employees.clear();
                employeesWorking.clear();

                List<Pair<String,String>> listEmployee = new ArrayList<Pair<String,String>>(); // First: TrangThai, Second: Sdt
                Map<String,List<String>> mapUsers = new HashMap<String,List<String>>();

                // Duyet danh sach nhan vien
                for (DataSnapshot postSnapshotNhanVien: snapshot.getChildren()) {
                    String trangThai = postSnapshotNhanVien.child("TrangThai").getValue(String.class);
                    String sdt =String.valueOf(postSnapshotNhanVien.child("Sdt").getValue(Long.class));

                    Pair<String, String> employee = new Pair<>(trangThai,sdt);
                    listEmployee.add(employee);
                }

                // Duyet trong danh sach User de lay thong tin (ID, AVATAR, NAME) cua nhan vien dua vao SDT
                DatabaseReference dbRefUser = database.getReference("user");
                dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshotUser: snapshot.getChildren()) {
                            List<String> baseInfoOfUser = new ArrayList<String>();

                            String id = postSnapshotUser.child("id").getValue(String.class);
                            String hoTen = postSnapshotUser.child("HoTen").getValue(String.class);
                            String avatar = postSnapshotUser.child("Avatar").getValue(String.class);

                            String sdt =String.valueOf(postSnapshotUser.child("Sdt").getValue(Long.class));

                            baseInfoOfUser.add(id);
                            baseInfoOfUser.add(hoTen);
                            baseInfoOfUser.add(avatar);

                            mapUsers.put(sdt, baseInfoOfUser);
                        }

                        // Find employee and assign to two lists
                        for(int i = 0; i<listEmployee.size();i++)
                        {
                            Pair<String,String> pair = listEmployee.get(i);
                            String sdt = pair.second;

                            List<String> info = mapUsers.get(sdt);

                            Employee employee = new Employee(info.get(0), info.get(1), info.get(2));
                            employees.add(employee);
                            if(pair.first.equals("DangLamViec"))
                            {
                                employeesWorking.add(employee);
                            }
                        }
                        txtTotalEmployees.setText("Số lượng nhân viên: " + employees.size());
                        txtTotalEmployeesWorking.setText("Số nhân viên đang làm việc: " + employeesWorking.size());
                        adapterListEmployees.notifyDataSetChanged();
                        adapterListEmployeesWorking.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View employeeManagerFragment = inflater.inflate(R.layout.activity_employee_manage, container, false);


        txtDateChosen = (TextInputEditText) employeeManagerFragment.findViewById(R.id.txt_date_chosen);
        listEmployees = (RecyclerView) employeeManagerFragment.findViewById(R.id.list_employees);
        listEmployeesWorking = (RecyclerView) employeeManagerFragment.findViewById(R.id.employee_working);
        txtTotalEmployees = (TextView) employeeManagerFragment.findViewById(R.id.txt_total_employees);
        txtTotalEmployeesWorking = (TextView) employeeManagerFragment.findViewById(R.id.txt_total_employees_working);

        txtDateChosen.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar calender =Calendar.getInstance();
                int year= calender.get(calender.YEAR);
                int month= calender.get(calender.MONTH);
                int day=calender.get(calender.DATE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calender.set(i,i1,i2);
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                        txtDateChosen.setText(simpleDateFormat.format(calender.getTime()));
                    }
                },year, month,day);
                datePickerDialog.show();
            }
        });




        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        listEmployees.setLayoutManager(layoutManager1);
        listEmployees.setItemAnimator(new DefaultItemAnimator());
        listEmployees.setAdapter(adapterListEmployees);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        listEmployeesWorking.setLayoutManager(layoutManager2);
        listEmployeesWorking.setItemAnimator(new DefaultItemAnimator());
        listEmployeesWorking.setAdapter(adapterListEmployeesWorking);

        return employeeManagerFragment;
    }

}