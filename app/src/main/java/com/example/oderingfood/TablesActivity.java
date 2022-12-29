package com.example.oderingfood;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.oderingfood.models.NotificationItem;
import com.example.oderingfood.models.Table;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TablesActivity extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FloatingActionButton btnAddTable;

    Bottomnavigation bottomnavigation ;
    String user;
    String idRes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomnavigation = (Bottomnavigation) getActivity();
        user= bottomnavigation.getUser();
        idRes = bottomnavigation.getIdRes();
    }

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
        adapter.notifyDataSetChanged();
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
        TextInputEditText edtTableName = (TextInputEditText) dialog.findViewById(R.id.edt_add_table);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tableName = String.valueOf(edtTableName.getText());

                if(tableName.isEmpty())
                {
                    Toast.makeText(getActivity(), "Tên bàn không được để trống", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AddTableToFireBase(tableName.trim());
                }
                dialog.dismiss();
            }
        });
    }

    private void AddTableToFireBase(String tableName)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;

        mDatabase = database.getReference("/restaurant/" + idRes);
        Log.i("ID BAN", idRes);
        Table newTable = new Table(tableName);
        DatabaseReference mDatabaseBanAn = mDatabase.child("BanAn");
        mDatabaseBanAn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapShot: snapshot.getChildren())
                {
                    if(postSnapShot.getKey().equals(tableName))
                    {
                        Toast.makeText(getActivity(),"Tên bàn ăn đã tồn tại",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                mDatabaseBanAn.child(tableName).child("TrangThai").setValue(newTable.getState());
                mDatabaseBanAn.child(tableName).child("Order").setValue("");

                // Update count table
                DatabaseReference mDatabaseSoBanAn = mDatabase.child("SoBanAn");
                mDatabaseSoBanAn.setValue(snapshot.getChildrenCount() + 1);
                Toast.makeText(getActivity(),"Thêm bàn thành công",Toast.LENGTH_SHORT).show();

                database.getReference("user/" + user ).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String avt = snapshot.child("avatar").getValue(String.class).toString();
                        String ad = snapshot.child("hoTen").getValue(String.class).toString();
                        String label = "<b> Thêm bàn mới <b>";
                        String content = ad + " vừa thêm bàn "+ tableName;
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
                        String currentDate = format.format(calendar.getTime());

                        String _id = database.getReference("restaurant/" + idRes).child("notification").push().getKey().toString();
                        NotificationItem notificationItem = new NotificationItem(_id,avt, label, content, currentDate);

                        database.getReference("restaurant/" + idRes).child("notification").child(_id).setValue(notificationItem);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}