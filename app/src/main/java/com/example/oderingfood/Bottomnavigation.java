package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Bottomnavigation extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    TablesActivity orderFragment = new TablesActivity();

    FragmentNotification noticeFragment = new FragmentNotification();
    ChatActivity chatActivity = new ChatActivity();
    Toolbar toolbar;
    BookingFragment bookingFragment = new BookingFragment();
    TuyChon_Fragment tuyChon_fragment = new TuyChon_Fragment();
    EmployeeManageActivity employeeManagerFragment = new EmployeeManageActivity();

    String user,idRes;
    static String[] Lrole =  new String[1];
    FirebaseDatabase database;
    DatabaseReference myRef;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomnavigation);
         database = FirebaseDatabase.getInstance();
         myRef = database.getReference("restaurant/"+  idRes);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle !=null) {
            user = bundle.getString("user");
            idRes = bundle.getString("restaurant");
        }


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
                       getSupportFragmentManager().beginTransaction().replace(R.id.container,bookingFragment).commit();
                        return true;
                    case R.id.order:
                              getSupportFragmentManager().beginTransaction().replace(R.id.container,orderFragment).commit();
                        return true;
                    case R.id.employee:
                              getSupportFragmentManager().beginTransaction().replace(R.id.container,employeeManagerFragment).commit();
                        return true;
                    case R.id.menu:
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
                getSupportFragmentManager().beginTransaction().replace(R.id.container, noticeFragment).commit();
                break;
            }
            case R.id.message:{
                Intent intent = new Intent(bottomNavigationView.getContext(), ChatActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.camera:{

                Intent intent = new Intent(bottomNavigationView.getContext(), ScanQRCode.class);
                Bundle bundle = new Bundle();
                bundle.putString("idRes",idRes);
                bundle.putString("idUser",user);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRole(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String owner = snapshot.child("ChuQuan").getValue(String.class).toString();

                if(owner.equals(user)){
                    Lrole[0]="ChuQuan";

                }else{
                    for(DataSnapshot postsnapshot: snapshot.child("NhanVien").getChildren()){
                        if(postsnapshot.getKey().equals(user)){
                            Lrole[0]="NhanVien";
                            return;
                        }
                    }
                    Lrole[0]="KhachHang";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public String getUser(){
        return user;
    }

    public String getIdRes(){
        return idRes;
    }

    public String getRole(){
        return Lrole[0];
    }

}