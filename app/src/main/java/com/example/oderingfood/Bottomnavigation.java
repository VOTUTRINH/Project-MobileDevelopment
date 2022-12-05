package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bottomnavigation extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();

    ChatActivity chatActivity = new ChatActivity();
    Toolbar toolbar;

    String user,idRes;

    static String[] role = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomnavigation);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle !=null){
            user = bundle.getString("user");
            idRes = bundle.getString("restaurant");
        }
        role[0] = "NULL";
        setRole();

        bottomNavigationView=findViewById(R.id.buttom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                            return true;
                    case R.id.booking:
                        BookingFragment bookingFragment = new BookingFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,bookingFragment).commit();
                        return true;
                    case R.id.order:
                        TablesActivity orderFragment = new TablesActivity();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,orderFragment).commit();
                        return true;
                    case R.id.employee:
                        EmployeeManageActivity employeeManagerFragment = new EmployeeManageActivity();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,employeeManagerFragment).commit();
                        return true;

                    case R.id.menu:
                        TuyChon_Fragment tuyChon_fragment = new TuyChon_Fragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,tuyChon_fragment).commit();
                        return true;
                }

                return false;
            }
        });
        toolbar = findViewById(R.id.bna_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

    }


    public void setRole(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;
        mDatabase = database.getReference("/restaurant/"+idRes);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String owner = snapshot.child("ChuQuan").getValue(String.class);

                if(owner.equals(user)){
                    role[0]="ChuQuan";

                }else{
                    for(DataSnapshot postsnapshot: snapshot.child("NhanVien").getChildren()){
                        if(postsnapshot.getKey().equals(user)){
                            role[0]="NhanVien";
                            return;
                        }
                    }
                    role[0]="KhachHang";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public String getRole(){
        return role[0];
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.notification: {
                FragmentNotification noticeFragment = new FragmentNotification();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, noticeFragment).commit();
                break;
            }
            case R.id.message:{
                Intent intent = new Intent(bottomNavigationView.getContext(), ChatActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public String getUser(){
        return user;
    }

    public String getIdRes(){
        return idRes;
    }
}