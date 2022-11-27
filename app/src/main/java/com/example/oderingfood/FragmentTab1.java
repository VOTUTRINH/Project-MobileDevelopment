package com.example.oderingfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.models.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentTab1 extends Fragment {

    RecyclerView recyclerView;
    Context context;

    String user;
    ListRestaurant listRestaurant;
    ArrayList<Restaurant> Restaurants = new ArrayList<>();
    AdapterTab adapter ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        listRestaurant =(ListRestaurant) getActivity();
        user = listRestaurant.getUser();

        adapter = new AdapterTab(getContext(),Restaurants,user);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurant");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    String name =  postSnapshot.child("TenQuan").getValue(String.class).toString();
                    String diaChi = postSnapshot.child("DiaChi").getValue(String.class).toString();
                    String urlImage;
                    try {
                        urlImage = postSnapshot.child("HinhAnh").child("1").getValue(String.class).toString();
                    }catch (Exception e){
                        urlImage = "https://firebasestorage.googleapis.com/v0/b/orderingfood-ab91f.appspot.com/o/store_default.png?alt=media&token=de6a404a-dd66-4a21-b6ae-eda751d79983";

                    }
                    String id= postSnapshot.getKey();
                    Restaurant restaurant = new Restaurant(name,diaChi,urlImage,id);
                    Restaurants.add(restaurant);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       LinearLayout fragmentTab1 = (LinearLayout) inflater.inflate(R.layout.fragment_tab1, container, false);

        // Getting reference of recyclerView
        recyclerView = (RecyclerView) fragmentTab1.findViewById(R.id.recyclerView);
        // Setting the layout as linear
        // layout for vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        // Sending reference and data to Adapter

        // Setting Adapter to RecyclerView
        recyclerView.setAdapter(adapter);



        return fragmentTab1;
    }
}