package com.example.oderingfood;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TablesActivity extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FloatingActionButton btnAddTable;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View orderFragment = inflater.inflate(R.layout.activity_tables, container, false);

        viewPager2 = (ViewPager2) orderFragment.findViewById(R.id.view_pager_list_table);
        tabLayout = (TabLayout) orderFragment.findViewById(R.id.tab_list_table);
        btnAddTable = (FloatingActionButton) orderFragment.findViewById(R.id.add_table_button);

        ListTableViewPagerAdapter adapter = new ListTableViewPagerAdapter(getActivity());

        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position)
                        {
                            case 0:
                            {
                                tab.setText("Tất cả");
                                break;
                            }
                            case 1:
                            {
                                tab.setText("Sử dụng");
                                break;
                            }
                            case 2:
                            {
                                tab.setText("Còn trống");
                                break;
                            }
                            default: tab.setText("");
                        }
                    }
                }).attach();

        btnAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogAddTable();
            }
        });

        return  orderFragment;
    }

    // Show dialog to add table
    private void ShowDialogAddTable()
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_table_layout);
        dialog.show();

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Accept", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}