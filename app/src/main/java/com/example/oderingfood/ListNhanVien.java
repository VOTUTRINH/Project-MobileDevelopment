package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.oderingfood.models.Employee;
import com.example.oderingfood.models.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListNhanVien extends AppCompatActivity {
    ListEmployeesAdapter adapterListEmployees;
    GridView gv;
    List<Employee> employees = new ArrayList<Employee>();

    EditText edtSearchEmployee;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String idRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nhan_vien);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            idRes = bundle.getString("idRes");
        }

        edtSearchEmployee = (EditText) findViewById(R.id.edt_search_employee);

        adapterListEmployees = new ListEmployeesAdapter(this, R.layout.custom_item_employee, employees, idRes, "ListNV");
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

                    // Duyet trong danh sach User de lay thong tin (ID, AVATAR, NAME) cua nhan vien dua vao SDT
                    DatabaseReference dbRefUser = database.getReference("user/" + id);
                    dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                            String hoTen = snapshotUser.child("hoTen").getValue(String.class);
                            String avatar = snapshotUser.child("avatar").getValue(String.class);

                            Employee employee = new Employee(id, hoTen, avatar);

                            if(!checkDup(employees,employee)){
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
                    searchAllEmployees();
                }else{
                    searchEmployeesByName(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchAllEmployees(){
        DatabaseReference dbRefEmployee = database.getReference("restaurant/" + idRes + "/NhanVien");
        dbRefEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employees.clear();
                // Duyet danh sach nhan vien
                for (DataSnapshot postSnapshotNhanVien: snapshot.getChildren()) {
                    String id = postSnapshotNhanVien.getKey();

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

    private void searchEmployeesByName(String name){
        DatabaseReference dbRefEmployee = database.getReference("restaurant/" + idRes + "/NhanVien");
        dbRefEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employees.clear();
                // Duyet danh sach nhan vien
                for (DataSnapshot postSnapshotNhanVien: snapshot.getChildren()) {
                    String id = postSnapshotNhanVien.getKey();

                    // Duyet trong danh sach User de lay thong tin (ID, AVATAR, NAME) cua nhan vien dua vao SDT
                    DatabaseReference dbRefUser = database.getReference("user/" + id);
                    dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                            String hoTen = snapshotUser.child("hoTen").getValue(String.class);
                            String avatar = snapshotUser.child("avatar").getValue(String.class);
                            if(hoTen.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))){
                                Employee employee = new Employee(id, hoTen, avatar);
                                if(!checkDup(employees,employee)){
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}