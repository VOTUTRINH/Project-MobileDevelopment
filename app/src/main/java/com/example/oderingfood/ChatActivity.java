package com.example.oderingfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;



import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    RecyclerView chatList;
    List<Object> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatList = findViewById(R.id.ac_list_chat);
        mData = new ArrayList<>();
        mData.add(new messageFromOther());
        mData.add(new messageToOther());
        mData.add(new messageFromOther());
        mData.add(new messageFromOther());
        mData.add(new messageFromOther());
        mData.add(new messageToOther());
        mData.add(new messageFromOther());
        mData.add(new messageFromOther());
        mData.add(new messageToOther());

        ChatAdapter chatAdapter = new ChatAdapter(this, mData);
        chatList.setAdapter(chatAdapter);
        chatList.setLayoutManager(new LinearLayoutManager(this));
    }
}

