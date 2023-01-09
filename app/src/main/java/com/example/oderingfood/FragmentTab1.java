package com.example.oderingfood;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.AdapterTab;
import com.example.oderingfood.ListRestaurant;
import com.example.oderingfood.R;
import com.example.oderingfood.models.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class FragmentTab1 extends Fragment {

    RecyclerView recyclerView;
    Context context;

    String user;
    EditText searchView;
    ListRestaurant listRestaurant;
    ArrayList<Restaurant> Restaurants = new ArrayList<>();
    AdapterTab adapter ;
    FirebaseDatabase database ;
    DatabaseReference myRef ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        listRestaurant =(ListRestaurant) getActivity();
        user = listRestaurant.getUser();

        adapter = new AdapterTab(getContext(),Restaurants,user);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("restaurant");

        searchAllRes();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       LinearLayout fragmentTab1 = (LinearLayout) inflater.inflate(R.layout.fragment_tab1, container, false);
        recyclerView = (RecyclerView) fragmentTab1.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        searchView = (EditText) fragmentTab1.findViewById(R.id.search);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    searchAllRes();
                }else{
                    searchResByName(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return fragmentTab1;
    }

    private void searchResByName(String name) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Restaurants.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String str = postSnapshot.child("TenQuan").getValue(String.class).toString();
                    if(str.toLowerCase(Locale.ROOT).contains(name)){
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
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public  void searchAllRes(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Restaurants.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String name =  postSnapshot.child("TenQuan").getValue(String.class);
                    String diaChi = postSnapshot.child("DiaChi").getValue(String.class);
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
}