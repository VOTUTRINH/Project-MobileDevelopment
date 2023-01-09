package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TuyChon_Menu extends AppCompatActivity {

    RecyclerView dataList;
    AdapterMenu adapter;
    String tablePath;
    String user;
    String idRes;
    List<Food> menu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menu = new ArrayList<>();
        setContentView(R.layout.table_list);
        dataList = findViewById(R.id.list_booking);

        Bundle b = getIntent().getExtras();

        if(b !=null){
            user = b.getString("idUser");
            idRes = b.getString("idRes");
            tablePath = b.getString("key");
        }


        adapter = new AdapterMenu(this, menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;

        String pathR = "/restaurant/" + GlobalVariables.pathRestaurentID;
        mDatabase = database.getReference(pathR +"/Menu") ;
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (menu.size() > 0) {
                    menu.clear();
                }

                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    // Get data
                    // Get data
                    String foodName = postSnapShot.getKey();
                    Food food = postSnapShot.getValue(Food.class);

                    // Add food ordered to table

                    String urlImage;
                    try {
                        urlImage = postSnapShot.child("urlImage").getValue(String.class).toString();
                    } catch (Exception e) {
                        urlImage = "https://firebasestorage.googleapis.com/v0/b/orderingfood-ab91f.appspot.com/o/store_default.png?alt=media&token=de6a404a-dd66-4a21-b6ae-eda751d79983";
                        food.setUrlImage(urlImage);
                    }

                    menu.add(food);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}