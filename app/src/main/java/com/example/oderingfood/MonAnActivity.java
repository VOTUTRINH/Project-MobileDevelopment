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
import com.example.oderingfood.models.NotificationItem;
import com.example.oderingfood.models.Table;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonAnActivity extends AppCompatActivity {

    RecyclerView dataList;
    int countWaiting = 0;
    AdapterMonAn adapter;
    Button btnOrder;
    FloatingActionButton addFood;
    String tablePath;
    String user;
    String idRes;

    Boolean isBooking = false;
    String ten = "";
    String phone = "";
    String date = "";
    String timeS = "";
    String timeE = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);
        dataList = findViewById(R.id.dishesList);
        btnOrder = findViewById(R.id.ac_btn_send);
        addFood = (FloatingActionButton)findViewById(R.id.am_button_add_food);

        Bottomnavigation bottomnavigation = new Bottomnavigation();


        Bundle b = getIntent().getExtras();

        if(b !=null){
            user = b.getString("idUser");
            idRes = b.getString("idRes");
            tablePath = b.getString("key");
            isBooking = b.getBoolean("isBooking");
            if(isBooking){
                ten = b.getString("ten");
                phone = b.getString("phone");
                date = b.getString("date");
                timeS = b.getString("timeS");
                timeE = b.getString("timeE");
            }
        }


        adapter = new AdapterMonAn(this, GlobalVariables.menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;

        String pathR = "/restaurant/" + GlobalVariables.pathRestaurentID;
        String pathU = "/user/" + user;
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





                for(int i=0; i< temp.size(); i++){
                    if (temp.get(i).getQuantity() != 0){
                        data.put(temp.get(i).getId(), temp.get(i));
                    }
                }
                if (data.size() == 0){
                    // khong co mon duoc dat
                    Toast.makeText(MonAnActivity.this,getString(R.string.chuachonmon), Toast.LENGTH_LONG).show();

                }
                else {
                    if (isBooking) {
                        DatabaseReference bookingDatabase;
                        DatabaseReference userBookingDatabase;
                        bookingDatabase = database.getReference(pathR + "/Bookings/"+ date);
                        userBookingDatabase = database.getReference(pathU + "/Bookings/"+idRes + "/"+ date);

                        String id = bookingDatabase.push().getKey();
                        Map<String,String> myMap = new HashMap<String,String>();
                        myMap.put("Ten",ten);
                        myMap.put("Phone",phone);
                        myMap.put("IdUser", user);
                        myMap.put("Date",date);
                        myMap.put("TimeS",timeS);
                        myMap.put("TimeE",timeE);
                        myMap.put("id",id);
                        bookingDatabase.child(id).setValue(myMap);
                        bookingDatabase.child(id).child("isConfirmed").setValue(false);
                        userBookingDatabase.child(id).setValue(myMap);

                        bookingDatabase.child(id).child("order").child(tablePath).setValue(data);
                        userBookingDatabase.child(id).child("isConfirmed").setValue(false);
                        userBookingDatabase.child(id).child("order").child(tablePath).setValue(data, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(MonAnActivity.this, "Booking thành công.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // notice user đã đặt bàn lúc ...
                        //TODO

                        FirebaseDatabase noti = FirebaseDatabase.getInstance();


                        noti.getReference("user/" + user ).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String avt = snapshot.child("avatar").getValue(String.class).toString();
                                String ad = snapshot.child("hoTen").getValue(String.class).toString();
                                String label = "<b> Đặt bàn  <b>";
                                String content = ad +"đã đặt bàn ăn lúc "+timeS;
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
                                String currentDate = format.format(calendar.getTime());
                                String _id =  noti.getReference("restaurant/" + idRes).child("notification").push().getKey().toString();
                                NotificationItem notificationItem = new NotificationItem(_id,avt, label, content, currentDate);

                                noti.getReference("restaurant/" + idRes).child("notification").child(_id).setValue(notificationItem);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {


                            }
                        });                        //
                        finish();

                    } else {
                        DatabaseReference tableDatabase;
                        tableDatabase = database.getReference(pathR + "/BanAn/"+ tablePath);

                        //check co bao nhieu bang dang doi
                        //set priority
                        GlobalVariables.priority++;
                        tableDatabase.child("Priority").setValue(GlobalVariables.priority);
                        tableDatabase.child("TrangThai").setValue(getString(R.string.waiting_state));
                        tableDatabase.child("Order").setValue(data, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(MonAnActivity.this, getString(R.string.datmonthanhcong), Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                }
            }
        });

        // nut add
        if(isBooking){
            addFood.setVisibility(View.GONE);
        }
        //nhan nut add
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonAnActivity.this,AddFoodToMenu.class);
                Bundle b = new Bundle();
                b.putString("idRes", idRes);
                b.putString("idUser", user);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
    public void plusCountWating(){
        countWaiting++;
    };


}