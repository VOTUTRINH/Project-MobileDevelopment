package com.example.oderingfood.Restaurant.Manage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.oderingfood.R;
import com.example.oderingfood.models.Employee;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ListNhanVienDangLamViec extends AppCompatActivity {
    ListEmployeesAdapter adapterListEmployees;
    GridView gv;
    List<Employee> employees = new ArrayList<Employee>();

    EditText edtSearchEmployee;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String idRes;

    TextInputEditText txtDateChosen;

    String dateChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nhan_vien_dang_lam_viec);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            idRes = bundle.getString("idRes");
        }

        edtSearchEmployee = (EditText) findViewById(R.id.edt_search_employee);
        txtDateChosen = (TextInputEditText) findViewById(R.id.txt_date_chosen);
        adapterListEmployees = new ListEmployeesAdapter(this, R.layout.custom_item_employee, employees, idRes, "ListNVLamViec");
        gv = (GridView) findViewById(R.id.gvNhanvien);
        gv.setAdapter(adapterListEmployees);

        DatabaseReference dbRefEmployee = database.getReference("restaurant/" + idRes + "/NhanVien");

        dbRefEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employees.clear();
                // Duyet danh sach nhan vien
                for (DataSnapshot postSnapshotNhanVien: snapshot.getChildren()) {
                    String id = postSnapshotNhanVien.getKey();
                    String dateChosen = txtDateChosen.getText().toString();
                    dateChosen = dateChosen.replaceAll("/","-");

                    if(postSnapshotNhanVien.hasChild("LamViec") && postSnapshotNhanVien.child("LamViec").hasChild(dateChosen)){
                        Toast.makeText(ListNhanVienDangLamViec.this, dateChosen,Toast.LENGTH_SHORT).show();
                        // Duyet trong danh sach User de lay thong tin (ID, AVATAR, NAME) cua nhan vien dua vao SDT
                        DatabaseReference dbRefUser = database.getReference("user/" + id);
                        dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                                String hoTen = snapshotUser.child("hoTen").getValue(String.class);
                                String avatar = snapshotUser.child("avatar").getValue(String.class);

                                Employee employee = new Employee(id, hoTen, avatar);

                                employees.add(employee);
                                adapterListEmployees.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edtSearchEmployee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    searchAllEmployeesWorking();
                }else{
                    searchEmployeesWorkingByName(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                String previousDate = txtDateChosen.getText().toString();

                DatePickerDialog datePickerDialog = new DatePickerDialog(ListNhanVienDangLamViec.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calender.set(i,i1,i2);
                        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                        String newDate = simpleDateFormat.format(calender.getTime());

                        txtDateChosen.setText(newDate);

                        // Nếu ngày chọn mới khác với ngày cũ, thì thực hiện lấy danh sách nhân viên đang làm việc của ngày mới
                        if(!newDate.equals(previousDate))
                        {
                            searchAllEmployeesWorking();
                        }
                    }
                },year, month,day);
                datePickerDialog.show();
            }
        });
    }

    private void searchAllEmployeesWorking(){
        dateChosen = txtDateChosen.getText().toString();
        dateChosen = dateChosen.replaceAll("/","-");
        DatabaseReference dbRefEmployee = database.getReference("restaurant/" + idRes + "/NhanVien");
        dbRefEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employees.clear();
                adapterListEmployees.notifyDataSetChanged();
                // Duyet danh sach nhan vien
                for (DataSnapshot postSnapshotNhanVien: snapshot.getChildren()) {
                    String id = postSnapshotNhanVien.getKey();
                    if(postSnapshotNhanVien.hasChild("LamViec") && postSnapshotNhanVien.child("LamViec").hasChild(dateChosen)){
                        // Duyet trong danh sach User de lay thong tin (ID, AVATAR, NAME) cua nhan vien dua vao SDT
                        DatabaseReference dbRefUser = database.getReference("user/" + id);
                        dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                                String hoTen = snapshotUser.child("hoTen").getValue(String.class);
                                String avatar = snapshotUser.child("avatar").getValue(String.class);

                                Employee employee = new Employee(id, hoTen, avatar);

                                if(!checkDup(employees, employee)) {
                                    employees.add(employee);
                                }
                                adapterListEmployees.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkDup(List<Employee> list, Employee b)
    {
        for (Employee employee: list) {
            if(employee.getId().equals(b.getId()))
            {
                return true;
            }
        }
        return false;
    }

    private void searchEmployeesWorkingByName(String name){
        DatabaseReference dbRefEmployee = database.getReference("restaurant/" + idRes + "/NhanVien");
        dbRefEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employees.clear();
                adapterListEmployees.notifyDataSetChanged();
                // Duyet danh sach nhan vien
                for (DataSnapshot postSnapshotNhanVien: snapshot.getChildren()) {
                    String id = postSnapshotNhanVien.getKey();

                    String dateChosen = txtDateChosen.getText().toString();
                    dateChosen = dateChosen.replaceAll("/","-");
                    if(postSnapshotNhanVien.hasChild("LamViec") && postSnapshotNhanVien.child("LamViec").hasChild(dateChosen)){
                        // Duyet trong danh sach User de lay thong tin (ID, AVATAR, NAME) cua nhan vien dua vao SDT
                        DatabaseReference dbRefUser = database.getReference("user/" + id);
                        dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                                String hoTen = snapshotUser.child("hoTen").getValue(String.class);
                                String avatar = snapshotUser.child("avatar").getValue(String.class);
                                if(hoTen.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))){
                                    Employee employee = new Employee(id, hoTen, avatar);
                                    if(!checkDup(employees,employee))
                                    {
                                        employees.add(employee);
                                    }
                                }
                                adapterListEmployees.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String GetDate()
    {
        String dateChosen = txtDateChosen.getText().toString();
        dateChosen = dateChosen.replaceAll("/","-");
        return dateChosen;
    }
}