package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    RecyclerView chatList;
    List<Object> mData;

    EditText newMessage;
    Button btnSend;
    TextView txtTenQuan;

    String user;
    String idRes;
    String role;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

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
        mData = new ArrayList<Object>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chatList.setLayoutManager(linearLayoutManager);
        ChatAdapter chatAdapter = new ChatAdapter(this, mData ,idRes);
        chatList.setAdapter(chatAdapter);


        // Get list message
        DatabaseReference dbRefListMessage = database.getReference("message/" + idRes);
        DatabaseReference dbRefUser = database.getReference("user");
        dbRefListMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mData.clear();
                for (DataSnapshot postSnapshotNhanVien: snapshot.getChildren()) {
                    String id = postSnapshotNhanVien.getKey();
                    String msg = postSnapshotNhanVien.child("msg").getValue(String.class);
                    String sendby = postSnapshotNhanVien.child("sendby").getValue(String.class);

                    dbRefUser.child(sendby).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                            String avt = snapshotUser.child("avatar").getValue(String.class);
                            String name = snapshotUser.child("hoTen").getValue(String.class);

                            Object msgObject;
                            if(sendby.equals(user))
                            {
                                msgObject = new messageToOther(id,msg,avt);
                            }else
                            {
                                msgObject = new messageFromOther(id,name,msg,avt);
                            }

                            mData.add(0,msgObject);
                            chatAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }


    private void sendMessage()
    {
        String newMsg = newMessage.getText().toString();
        if(newMsg.equals(""))
            return;

        newMessage.setText("");


        DatabaseReference dbRefMessage = database.getReference("message");

        dbRefMessage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String idMsg = dbRefMessage.push().getKey();

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("msg", newMsg);
                map.put("sendby", user);

                dbRefMessage.child(idRes).child(idMsg).setValue(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

