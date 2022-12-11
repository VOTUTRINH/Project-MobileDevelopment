package com.example.oderingfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.example.oderingfood.models.GlobalVariables;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ListRestaurant extends AppCompatActivity {
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter ;
    TabLayout tabLayout;

    String user ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_restaurant);

        Intent intent =getIntent();
        user= intent.getStringExtra("Uid");
        viewPager = (ViewPager2) findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if (bundle != null) {
//            user = bundle.getString("Uid");
//        }

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Vào quán");
                    break;
                case 1:
                    tab.setText("Quán của tôi");
                    break;
            }
        }).attach();
    }

    public String getUser() {
        return user;
    }
}