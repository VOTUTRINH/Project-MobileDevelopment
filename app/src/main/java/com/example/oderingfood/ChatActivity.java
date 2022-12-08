package com.example.oderingfood;

import androidx.annotation.Nullable;
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
        mData.add(new messageFromOther("Toai","1", R.drawable.img_1));
        mData.add(new messageToOther("2",R.drawable.img_1));
        mData.add(new messageFromOther("Toai","3", R.drawable.img_1));
        mData.add(new messageFromOther("Toai","4", R.drawable.img_1));
        mData.add(new messageFromOther("Toai","5", R.drawable.img_1));
        mData.add(new messageToOther("6",R.drawable.img_1));
        mData.add(new messageFromOther("Toai","7", R.drawable.img_1));
        mData.add(new messageFromOther("Toai","8", R.drawable.img_1));
        mData.add(new messageToOther("9",R.drawable.img_1));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        chatList.setLayoutManager(linearLayoutManager);

        ChatAdapter chatAdapter = new ChatAdapter(this, mData);
        chatList.setAdapter(chatAdapter);


    }
}

