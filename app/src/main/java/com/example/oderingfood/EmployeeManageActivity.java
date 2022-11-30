package com.example.oderingfood;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.example.oderingfood.models.Employee;
import com.example.oderingfood.models.Table;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Date;
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
    FloatingActionButton btnAddEmployee;
    TextView txtNoEmployee;
    TextView txtNoEmployeeWorking;

    List<Employee> employees = new ArrayList<Employee>();
    List<Employee> employeesWorking = new ArrayList<Employee>();

    // Adapter
    EmployeesManagerAdapter adapterListEmployees;
    EmployeesManagerAdapter adapterListEmployeesWorking;

    EmployeeManageActivity employeeManageActivity;

    Bottomnavigation bottomnavigation ;
    String user;
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

        bottomnavigation = (Bottomnavigation) getActivity();
        user= bottomnavigation.getUser();
        idRestaurent = bottomnavigation.getIdRes();
        Log.i("IDRES", idRestaurent);
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

                List<List<String>> listEmployee = new ArrayList<List<String>>(); // First: TrangThai, Second: Sdt
                Map<String,List<String>> mapUsers = new HashMap<String,List<String>>();

                // Duyet danh sach nhan vien
                for (DataSnapshot postSnapshotNhanVien: snapshot.getChildren()) {
                    String trangThai = postSnapshotNhanVien.child("TrangThai").getValue(String.class);
                    String sdt =String.valueOf(postSnapshotNhanVien.child("Sdt").getValue(Long.class));
                    List<String> employee = new ArrayList<String>();
                    employee.add(trangThai);
                    employee.add(sdt);

                    listEmployee.add(employee);
                }
                Log.i("Size", String.valueOf(employees.size()));
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
                            List<String> info = mapUsers.get(listEmployee.get(i).get(1));

                            Employee employee = new Employee(info.get(0), info.get(1), info.get(2));
                            employees.add(employee);
                            if(listEmployee.get(i).get(0).equals("DangLamViec"))
                            {
                                employeesWorking.add(employee);
                            }
                        }
                        if(employees.size() == 0)txtNoEmployee.setVisibility(View.VISIBLE);
                        else txtNoEmployee.setVisibility(View.GONE);
                        if(employeesWorking.size() == 0) txtNoEmployeeWorking.setVisibility(View.VISIBLE);
                        else txtNoEmployeeWorking.setVisibility(View.GONE);

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
        btnAddEmployee = (FloatingActionButton) employeeManagerFragment.findViewById(R.id.add_employee_floating_button);
        txtNoEmployee = (TextView) employeeManagerFragment.findViewById(R.id.txt_no_employee);
        txtNoEmployeeWorking = (TextView) employeeManagerFragment.findViewById(R.id.txt_no_employee_working);

        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogAddTable();
            }
        });

        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        txtDateChosen.setText(simpleDateFormat.format( Calendar.getInstance().getTime()));
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

    // Show dialog to add table
    private void ShowDialogAddTable()
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_employee_layout);
        dialog.show();

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);
        TextInputEditText edtEmployeeName = (TextInputEditText) dialog.findViewById(R.id.edt_add_table);
        TextInputEditText edtLuong = (TextInputEditText) dialog.findViewById(R.id.edt_luong_nhanvien);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strSdt = String.valueOf(edtEmployeeName.getText());
                String strLuong = String.valueOf(edtLuong.getText());
                if(strSdt.isEmpty())
                {
                    Toast.makeText(context, "Vui lòng nhập số điện thoại",Toast.LENGTH_SHORT).show();
                }
                else if(strLuong.isEmpty())
                {
                    Toast.makeText(context, "Vui lòng nhập lương ban đầu cho nhân viên",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Long sdt = Long.parseLong(strSdt);
                    Long luong = Long.parseLong(strLuong);

                    if(luong <= 0)
                    {
                        Toast.makeText(context, "Lương phải lớn hơn 0",Toast.LENGTH_SHORT).show();

                    }

                    AddEmployeeToRestaurant(sdt, luong);
                    dialog.dismiss();
                }
            }
        });
    }

    private void AddEmployeeToRestaurant(Long employeeSdt, Long luong)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference mDatabaseNhanVien = database.getReference("/restaurant/" + idRestaurent + "/NhanVien");
        mDatabaseNhanVien.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapShot: snapshot.getChildren())
                {
                    if(postSnapShot.child("Sdt").getValue(Long.class).equals(employeeSdt))
                    {
                        Toast.makeText(getActivity(),"Nhân viên đã tồn tại",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                DatabaseReference dbRefUser = database.getReference("user");
                dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    boolean canAdd = false;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshotUser: snapshot.getChildren())
                        {
                            if(String.valueOf(postSnapshotUser.child("Sdt").getValue(Long.class)).equals(String.valueOf(employeeSdt)))
                            {
                                canAdd = true;
                            }
                        }
                        if(canAdd == true)
                        {

                            String id = mDatabaseNhanVien.push().getKey();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("Sdt", employeeSdt);
                            map.put("Luong", luong);
                            map.put("ThoiGianLamViec", 0);
                            map.put("TrangThai", "KhongLamViec");

                            mDatabaseNhanVien.child(id).setValue(map);
                        }
                        else
                        {
                            Toast.makeText(context, "Số điện thoại chưa được đăng kí tài khoản",Toast.LENGTH_SHORT).show();
                        }
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
}