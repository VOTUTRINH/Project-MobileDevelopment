package com.example.oderingfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.oderingfood.models.GlobalVariables;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.Table;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonAnActivity extends AppCompatActivity {

    RecyclerView dataList;

    AdapterMonAn adapter;
    Button btnOrder;
    FloatingActionButton addFood;
    String tablePath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);
        dataList = findViewById(R.id.dishesList);
        btnOrder = findViewById(R.id.ac_btn_send);
        addFood = (FloatingActionButton)findViewById(R.id.am_button_add_food);

        Bottomnavigation bottomnavigation = new Bottomnavigation();
        String user= bottomnavigation.getUser();
        String idRes = bottomnavigation.getIdRes();

        Bundle b = getIntent().getExtras();

        if(b != null) {
            tablePath = b.getString("key");
        }

        adapter = new AdapterMonAn(this, GlobalVariables.menu);
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
                if (GlobalVariables.menu.size() > 0) {
                    GlobalVariables.menu.clear();
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

                    GlobalVariables.menu.add(food);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // nhan nut order
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Food> temp = adapter.getMenu();
                Map<String, Food> data = new HashMap<>();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mDatabase;

                mDatabase = database.getReference(pathR + "/BanAn/"+ tablePath);
                for(int i=0; i< temp.size(); i++){
                    if (temp.get(i).getQuantity() != 0){
                        data.put(temp.get(i).getId(), temp.get(i));
                    }
                }
                if (data.size() == 0){
                    // khong co mon duoc dat
                    Toast.makeText(MonAnActivity.this,getString(R.string.chuachonmon), Toast.LENGTH_LONG).show();

                }
                else
                {
                    mDatabase.child("TrangThai").setValue(getString(R.string.using_state));
                    mDatabase.child("Order").setValue(data, new DatabaseReference.CompletionListener() {
                      @Override
                      public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                          Toast.makeText(MonAnActivity.this,getString(R.string.datmonthanhcong), Toast.LENGTH_SHORT).show();
                      }
                  });
                    finish();
                }

            }
        });
        //nhan nut add
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonAnActivity.this,AddFoodToMenu.class);
                startActivity(intent);
            }
        });
    }


}