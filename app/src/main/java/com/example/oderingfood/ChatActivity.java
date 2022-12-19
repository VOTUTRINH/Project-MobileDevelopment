package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    RecyclerView chatList;
    ChatAdapter chatAdapter;
    List<Object> dataListMsg;

    EditText newMessage;
    Button btnSend;
    TextView txtTenQuan;

    String user;
    String idRes;
    String role;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRefListMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get info of user
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle !=null) {
            idRes = bundle.getString("idRes");
            user = bundle.getString("user");
            role = bundle.getString("role");
        }

        chatList = findViewById(R.id.ac_list_chat);
        newMessage = findViewById(R.id.ac_input_chat);
        btnSend = findViewById(R.id.ac_btn_send);
        txtTenQuan = findViewById(R.id.ac_txt_tenquan);

        // Set ten quan
        DatabaseReference dbRefTenQuan = database.getReference("restaurant/" + idRes + "/TenQuan");
        dbRefTenQuan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tenQuan = snapshot.getValue(String.class);
                txtTenQuan.setText(tenQuan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dataListMsg = new ArrayList<Object>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chatList.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(this, dataListMsg,idRes);
        chatList.setAdapter(chatAdapter);

        // Get reference to firebase
        dbRefListMessage = database.getReference("message/" + idRes);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbRefListMessage.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    Log.i("AddChild", snapshot.getKey());
                    displayMessage(snapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    Log.i("ChildChange", snapshot.getKey());
                    displayMessage(snapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage()
    {
        String newMsg = newMessage.getText().toString();
        if(newMsg.equals(""))
            return;

        newMessage.setText("");
        String id = dbRefListMessage.push().getKey();

        Map<String, Object> content = new HashMap<String, Object>();
        content.put("msg",newMsg);
        content.put("sendby",user);

        dbRefListMessage.child(id).setValue(content);
    }

    private void displayMessage(DataSnapshot snapshot)
    {
        String id = snapshot.getKey();
        String sendby = snapshot.child("sendby").getValue(String.class);
        String msg = snapshot.child("msg").getValue(String.class);

        DatabaseReference dbRefUser;
        dbRefUser = database.getReference("user/" + sendby);
        dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String avt = snapshot.child("avatar").getValue(String.class);
                String name = snapshot.child("hoTen").getValue(String.class);

                Object msgObject;
                if(!sendby.equals(user))
                {
                    msgObject = new messageFromOther(id,name,msg,avt);
                    Log.i("ORDER", "1");
                }else
                {
                    Log.i("ORDER", "2");
                    msgObject = new messageToOther(id,msg,avt);
                }
                dataListMsg.add(0,msgObject);
                Log.i("OBJECT",((Message)msgObject).id);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void deleteMessage(DataSnapshot snapshot)
    {

    }
}

