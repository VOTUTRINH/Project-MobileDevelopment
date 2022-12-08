package com.example.oderingfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ListRestaurant extends AppCompatActivity {
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter ;
    TabLayout tabLayout;
    String user = "Ky0a3h3coIbKvBapDSpNiqsOfrQ2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_restaurant);
        viewPager = (ViewPager2) findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

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