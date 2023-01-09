package com.example.oderingfood.ListRestaurant;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.oderingfood.ListRestaurant.FragmentTab1;
import com.example.oderingfood.ListRestaurant.FragmentTab2;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag = new FragmentTab1();
                break;
            case 1:
                frag = new FragmentTab2();
                break;
        }
        return frag;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}