package com.example.oderingfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailWorkTimeActivity extends AppCompatActivity {
    // Get data list employee from firebase
    // Do Something
    FirebaseDatabase database;
    DatabaseReference dbRefEmployee;
    private ListView lv_detailWorkingTime;

    String idRes;
    String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_work_time);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            idRes = bundle.getString("idRes");
            idUser = bundle.getString("idUser");
        }


        lv_detailWorkingTime = (ListView) findViewById(R.id.detail_working_time);

        database = FirebaseDatabase.getInstance();
        dbRefEmployee = database.getReference("restaurant/"+ idRes + "/NhanVien/" + idUser);
    }
}