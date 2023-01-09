
package com.example.oderingfood;

import android.os.Bundle;

import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentActivity;
        import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ListTableViewPagerAdapter extends FragmentStateAdapter {

    public ListTableViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
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
        TableListPage1 tbl1 = new TableListPage1();
        tbl3.setArguments(b);
        tbl1.setArguments(b);



        switch (position){
            case 0: return tbl1;
            case 1: return new TableListPage2();

            case 2: return tbl3;
            default: return new TableListPage1();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}