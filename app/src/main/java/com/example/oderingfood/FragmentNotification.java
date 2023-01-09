package com.example.oderingfood;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.AdapterNotification;
import com.example.oderingfood.R;
import com.example.oderingfood.Bottomnavigation;
import com.example.oderingfood.models.NotificationItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentNotification extends Fragment {

    RecyclerView recyclerView;
    Context context;
    ArrayList<NotificationItem> items;

    Bottomnavigation bottomnavigation ;
    String user;
    String idRes;
    String role;

    FirebaseDatabase database ;
    DatabaseReference myRef ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        items = new ArrayList<NotificationItem>();

        bottomnavigation = (Bottomnavigation) getActivity();
        user= bottomnavigation.getUser();
        idRes = bottomnavigation.getIdRes();
        role = bottomnavigation.getRole();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("restaurant/"+idRes+"/notification");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout fragmentNtf = (LinearLayout) inflater.inflate(R.layout.fragment_tab1, container, false);
        // Getting reference of recyclerView
        recyclerView = (RecyclerView) fragmentNtf.findViewById(R.id.recyclerView);
        // Setting the layout as linear
        // layout for vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        // Sending reference and data to Adapter
        AdapterNotification adapter = new AdapterNotification(getContext(), items);
        // Setting Adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for(DataSnapshot postsnapshot: snapshot.getChildren()){
                    String id = postsnapshot.getKey().toString();
                    String img = postsnapshot.child("noticeImg").getValue(String.class).toString();
                    String noticeLabel = postsnapshot.child("noticeLabel").getValue(String.class).toString();
                    String noticeContent = postsnapshot.child("noticeContent").getValue(String.class).toString();
                    String timeString = postsnapshot.child("timeString").getValue(String.class).toString();
                    boolean isRead =  postsnapshot.child("isRead").getValue(Boolean.class);
                    NotificationItem item = new NotificationItem(id,img,noticeLabel,noticeContent,timeString);
                    item.setRead(isRead);
                    items.add(item);
                }
                Collections.reverse(items);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return fragmentNtf;
    }
}
