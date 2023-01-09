package com.example.oderingfood.ListRestaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.oderingfood.ProfileUser.ProfileUserActivity;
import com.example.oderingfood.R;
import com.example.oderingfood.models.GlobalVariables;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListRestaurant extends AppCompatActivity {
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter ;
    TabLayout tabLayout;

    CircleImageView profileImgView;
    String user ="";
    String sdt = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_restaurant);

        Intent intent =getIntent();
        user= intent.getStringExtra("Uid");
        GlobalVariables.IDUser = user;
        viewPager = (ViewPager2) findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        profileImgView = (CircleImageView) findViewById(R.id.profile);


        // Lay avatar profile
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refUser = database.getReference("user/" + user);

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sdt = snapshot.child("dienThoai").getValue(String.class);
                String avatar = snapshot.child("avatar").getValue(String.class);
                Picasso.get().load(avatar).into(profileImgView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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


        profileImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ListRestaurant.this, ProfileUserActivity.class);
                intent.putExtra("Uid", user);
                startActivity(intent);
            }
        });
    }

    public String getUser() {
        return user;
    }
    public String getSdt() {return sdt;}
}