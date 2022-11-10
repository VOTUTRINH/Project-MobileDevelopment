package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Bottomnavigation extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    TablesActivity orderFragment = new TablesActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomnavigation);
        bottomNavigationView=findViewById(R.id.buttom_navigation);
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.booking:
                        //      getSupportFragmentManager().beginTransaction().replace(R.id.container,notificationFragment).commit();
                        return true;
                    case R.id.order:
                              getSupportFragmentManager().beginTransaction().replace(R.id.container,orderFragment).commit();
                        return true;
                    case R.id.employee:
                        //      getSupportFragmentManager().beginTransaction().replace(R.id.container,notificationFragment).commit();
                        return true;
                    case R.id.menu:
                        //      getSupportFragmentManager().beginTransaction().replace(R.id.container,notificationFragment).commit();
                        return true;

                }



                return false;
            }
        });
    }

}