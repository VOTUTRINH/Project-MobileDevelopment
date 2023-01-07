package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailWorkTimeActivity extends AppCompatActivity {
    // Get data list employee from firebase
    // Do Something
    FirebaseDatabase database;
    DatabaseReference dbRefEmployee;

    private ListView lv_detailWorkingTime;
    private TextView txt_tongSoCaLam;

    private List<String> listTimeWorking;
    ArrayAdapter<String> adapter;

    String idRes;
    String idUser;
    String dateChosen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_work_time);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            idRes = bundle.getString("idRes");
            idUser = bundle.getString("idUser");
            dateChosen = bundle.getString("dateChosen");
        }
        listTimeWorking = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listTimeWorking);
        lv_detailWorkingTime = (ListView) findViewById(R.id.detail_working_time);
        lv_detailWorkingTime.setAdapter(adapter);

        txt_tongSoCaLam = (TextView) findViewById(R.id.txt_tong_so_ca_lam);

        database = FirebaseDatabase.getInstance();
        dbRefEmployee = database.getReference("restaurant/"+ idRes + "/NhanVien/" + idUser);

        dbRefEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("LamViec")){
                    if(snapshot.child("LamViec").hasChild(dateChosen)){
                        for (DataSnapshot postSnapshot:snapshot.child("LamViec").child(dateChosen).getChildren()) {
                            String start = postSnapshot.child("Start").getValue(String.class);
                            String end = postSnapshot.child("End").getValue(String.class);

                            String time = "Ca " + String.valueOf(listTimeWorking.size() + 1) + ":  Bắt đầu: " + start + "    -    Kết thúc: " + (end.equals("0")?"Hiện tại":end);
                            listTimeWorking.add(time);
                        }
                        txt_tongSoCaLam.setText("Tổng số ca làm: " + String.valueOf(listTimeWorking.size()));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}