package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class activityBookingChooseTable extends FragmentActivity {
    ViewPager2 viewPager2;
    TabLayout tablayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_choose_table);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Intent it = getIntent();
//        boolean isBooking = it.getBooleanExtra("isBooking", true);
//        String ten = it.getStringExtra("ten");
//        String phone = it.getStringExtra("phone");
//        String date = it.getStringExtra("date");
//        String timeS = it.getStringExtra("timeS");
//        String timeE = it.getStringExtra("timeE");
//        Bundle bd = it.getBundleExtra("");
        Bundle bundle = new Bundle();
        bundle = it.getBundleExtra("bd");


        viewPager2 = (ViewPager2) findViewById(R.id.view_pager_list_table);
        tablayout = (TabLayout) findViewById(R.id.tab_list_table);
        bookingTableAdapter adapter = new bookingTableAdapter(this, bundle);
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tablayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                tab.setText("Chọn bàn đặt:");
                    }
                }).attach();




    }
}


class bookingTableAdapter extends FragmentStateAdapter {
    Bundle bundle;
    public bookingTableAdapter(@NonNull FragmentActivity fragmentActivity, Bundle b) {
        super(fragmentActivity);
        bundle = b;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = (simpleDateFormat.format(Calendar.getInstance().getTime()));
        date = String.join("-",date.split("/"));
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 2);
        String timeEnd = (simpleTimeFormat.format(c.getTime()));
        String timeStart = (simpleTimeFormat.format(Calendar.getInstance().getTime()));

        Bundle b = new Bundle();
        b.putBoolean("isBooking", false);
        b.putString("date", date);
        b.putString("timeS", timeStart);
        b.putString("timeE", timeEnd);

        TableListPage3 tbl3 = new TableListPage3();
        tbl3.setArguments(bundle);


        return tbl3;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}