package com.example.oderingfood.TransferActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import com.example.oderingfood.ListRestaurant.ListRestaurant;
import com.example.oderingfood.R;
import com.example.oderingfood.Restaurant.Bottomnavigation;
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

    private float maxTimeRemain = 5000f;
    private float countTime = 0;
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
                try {
                    while (role.equals("null")) {
                        Thread.sleep(150);
                        countTime += 150;
                        if(countTime > maxTimeRemain) {
                            returnToListRes();
                            return;
                        }
                    }
                    startActivityFromMainThread();
                    return;
                }catch (InterruptedException e)
                {

                }
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

    public void returnToListRes()
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (StartResActivity.this, ListRestaurant.class);
                intent.putExtra("Uid",user);
                startActivity(intent);
                finish();
            }
        });
    }
    public void startActivityFromMainThread(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (StartResActivity.this, Bottomnavigation.class);
                Bundle bundle = new Bundle();
                bundle.putString("role",role);
                bundle.putString("user",user);
                bundle.putString("restaurant", idRes);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

}

