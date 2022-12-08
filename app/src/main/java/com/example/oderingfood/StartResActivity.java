package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StartResActivity extends AppCompatActivity {

    String user,idRes;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList list = new ArrayList<>();

    String role = "null";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_res);
        ProgressBar progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            user = bundle.getString("user");
            idRes = bundle.getString("restaurant");
        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("restaurant/" + idRes);

        setRole();

        new Thread(new Runnable() {
            public void run() {
                while (role.equals("null")) {
                }
                startActivityFromMainThread();
            }

        }).start();
    }
    private void changeRole(String role){
        this.role = role;
    }
    public void setRole(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String owner = snapshot.child("ChuQuan").getValue(String.class).toString();

                if(owner.equals(user)){
                    changeRole("ChuQuan");
                }else{
                    for(DataSnapshot postsnapshot: snapshot.child("NhanVien").getChildren()){
                        if(postsnapshot.getKey().equals(user)){
                            changeRole("NhanVien");
                            return;
                        }
                    }
                    changeRole("KhachHang");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    private class MyThread extends Thread{
//        @Override
//        public void run() {
//            while (!role.equals("null")){
//            }
//
//            Intent intent = new Intent(StartResActivity.this, Bottomnavigation.class);
//            intent.putExtra("role",role);
//            startActivity(intent);
//        }
//    }
    public void startActivityFromMainThread(){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (StartResActivity.this, Bottomnavigation.class);
                intent.putExtra("role",role);
                intent.putExtra("user",user);
                intent.putExtra("restaurant", idRes);
                startActivity(intent);
            }
        });
    }

}
