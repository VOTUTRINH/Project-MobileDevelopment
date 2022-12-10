package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.example.oderingfood.models.Employee;
import com.example.oderingfood.models.GlobalVariables;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailListEmployeesActivity extends AppCompatActivity {
    ListEmployeesAdapter adapterListEmployees;
    GridView gv;
    List<Employee> employees = new ArrayList<Employee>();

    String idRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list_employees);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            idRes = bundle.getString("idRes");
        }


        adapterListEmployees = new ListEmployeesAdapter(this, R.layout.custom_item_employee, employees);
        gv = (GridView) findViewById(R.id.grid_view_nhan_vien);
        gv.setAdapter(adapterListEmployees);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefEmployee = database.getReference("restaurant/" + idRes + "/NhanVien");

        dbRefEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employees.clear();
                // Duyet danh sach nhan vien
                for (DataSnapshot postSnapshotNhanVien: snapshot.getChildren()) {
                    String id = postSnapshotNhanVien.child("id").getValue(String.class);
                    String trangThai = postSnapshotNhanVien.child("TrangThai").getValue(String.class);

                    // Duyet trong danh sach User de lay thong tin (ID, AVATAR, NAME) cua nhan vien dua vao SDT
                    DatabaseReference dbRefUser = database.getReference("user/" + id);
                    dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                                String hoTen = snapshotUser.child("HoTen").getValue(String.class);
                                String avatar = snapshotUser.child("Avatar").getValue(String.class);

                                Employee employee = new Employee(id, hoTen, avatar);

                                employees.add(employee);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                adapterListEmployees.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}