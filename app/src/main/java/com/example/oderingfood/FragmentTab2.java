package com.example.oderingfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.models.Restaurant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentTab2 extends Fragment {


    RecyclerView recyclerView2;
    Context context; FloatingActionButton fab;
    String idOwner;
    ListRestaurant listRestaurant;
    AdapterTab adapter;

    ArrayList<Restaurant> Restaurants = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        listRestaurant =(ListRestaurant) getActivity();
        idOwner = listRestaurant.getUser();

        adapter = new AdapterTab(getContext(), Restaurants,idOwner);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurant");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String owner = postSnapshot.child("ChuQuan").getValue(String.class).toString();
                    if(owner.equals(idOwner)){
                        String name =  postSnapshot.child("TenQuan").getValue(String.class).toString();
                        String diaChi = postSnapshot.child("DiaChi").getValue(String.class).toString();
                        String urlImage = postSnapshot.child("HinhAnh").child("1").getValue(String.class).toString();

                        String id= postSnapshot.getKey();

                        Restaurant restaurant = new Restaurant(name,diaChi,urlImage,id);
                        Restaurants.add(restaurant);

                    }
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

        View fragmentTab2 = inflater.inflate(R.layout.fragment_tab2, container, false);
        // Getting reference of recyclerView
        recyclerView2 = (RecyclerView) fragmentTab2.findViewById(R.id.recyclerView2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView2.setLayoutManager(linearLayoutManager);

        // Setting Adapter to RecyclerView
        recyclerView2.setAdapter(adapter);
        fab = (FloatingActionButton) fragmentTab2.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Register_Store.class);
                intent.putExtra("idOwner",idOwner);
                context.startActivity(intent);

            }
        });

        return fragmentTab2;
    }


}