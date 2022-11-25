package com.example.oderingfood;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentTab2 extends Fragment {


    RecyclerView recyclerView2;
    Context context; FloatingActionButton fab;
    String idOwner;
    ListRestaurant listRestaurant;
    AdapterTab adapter;


    // Using ArrayList to store images data
    ArrayList courseImg = new ArrayList<>();
    ArrayList courseName = new ArrayList<>();
    ArrayList address = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        listRestaurant =(ListRestaurant) getActivity();
        idOwner = listRestaurant.getIdOwner();

        adapter = new AdapterTab(getContext(), courseImg, courseName, address);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurant");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String id = postSnapshot.child("ChuQuan").getValue(String.class).toString();
                    if(id.equals(idOwner)){
                        String name =  postSnapshot.child("TenQuan").getValue(String.class).toString();
                        String diaChi = postSnapshot.child("DiaChi").getValue(String.class).toString();
                        String urlImage = postSnapshot.child("HinhAnh").child("1").getValue(String.class).toString();

                        courseName.add(name);
                        address.add(diaChi);
                        courseImg.add(urlImage);

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

        // Sending reference and data to dapter


        // Setting Adapter to RecyclerView
        recyclerView2.setAdapter(adapter);
        fab = (FloatingActionButton) fragmentTab2.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("restaurant");

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            String id = postSnapshot.child("ChuQuan").getValue(String.class).toString();

                            if(id.equals(idOwner)){
                                String name =  postSnapshot.child("TenQuan").getValue(String.class).toString();
                                String diaChi = postSnapshot.child("DiaChi").getValue(String.class).toString();
                                String urlImage = postSnapshot.child("HinhAnh").child("1").getValue(String.class).toString();

                                Toast.makeText(context,urlImage , Toast.LENGTH_SHORT).show();

                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        //Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        });

        return fragmentTab2;
    }


}