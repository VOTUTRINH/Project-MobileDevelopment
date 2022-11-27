
package com.example.oderingfood;

import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentActivity;
        import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ListTableViewPagerAdapter extends FragmentStateAdapter {

    public ListTableViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new TableListPage1();
            case 1: return new TableListPage2();
            case 2: return new TableListPage3();
            default: return new TableListPage1();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}