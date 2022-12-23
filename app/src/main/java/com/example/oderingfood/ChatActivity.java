package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    RecyclerView chatList;
    ChatAdapter chatAdapter;
    List<Message> dataListMsg;

    EditText newMessage;
    Button btnSend;
    TextView txtTenQuan;

    String user;
    String idRes;
    String role;

    ConstraintLayout loadMessageLayout;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRefListMessage;
    DatabaseReference dbRefUser;

    private boolean isCompletedLoadMessage = false;
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
        loadMessageLayout = findViewById(R.id.load_message);

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
        dataListMsg = new ArrayList<Message>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chatList.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(this, dataListMsg,idRes);
        chatList.setAdapter(chatAdapter);

        // Get reference to firebase
        dbRefListMessage = database.getReference("message/" + idRes);
        dbRefUser = database.getReference("user");
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm:ss");
                    dbRefListMessage.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            dataListMsg.clear();
                            for(DataSnapshot snapshotMessage: snapshot.getChildren()){
                                String id = snapshotMessage.getKey();
                                String sendby = snapshotMessage.child("sendby").getValue(String.class);
                                String msg = snapshotMessage.child("msg").getValue(String.class);
                                String dateSend = snapshotMessage.child("datesend").getValue(String.class);
                                String timeSend = snapshotMessage.child("timesend").getValue(String.class);

                                dbRefUser.child(sendby).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                                        String avt = snapshotUser.child("avatar").getValue(String.class);
                                        String name = snapshotUser.child("hoTen").getValue(String.class);

                                        Message msgObject;
                                        if(!sendby.equals(user))
                                        {
                                            msgObject = new messageFromOther(id,name,msg,avt,dateSend,timeSend);
                                        }else
                                        {
                                            msgObject = new messageToOther(id,msg,avt,dateSend, timeSend);
                                        }
                                        dataListMsg.add(0,msgObject);

                                        if(dataListMsg.size() >= snapshot.getChildrenCount())
                                        {
                                            Collections.sort(dataListMsg, new Comparator<Message>() {
                                                public int compare(Message s1, Message s2) {
                                                    try {
                                                        int cmpDate = currentDateFormat.parse(s1.getDate()).compareTo(currentDateFormat.parse(s2.getDate()));
                                                        if(cmpDate > 0)
                                                            return -1;
                                                        else if(cmpDate < 0)
                                                            return 1;
                                                        else{
                                                            if(currentTimeFormat.parse(s1.getTime()).compareTo(currentTimeFormat.parse(s2.getTime())) > 0)
                                                                return -1;
                                                            return 1;
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    return 0;
                                                }
                                            });
                                            chatAdapter.notifyDataSetChanged();
                                            loadMessageLayout.setVisibility(View.GONE);
                                            chatList.setVisibility(View.VISIBLE);
                                            isCompletedLoadMessage = true;
                                        }
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
                    Log.i("TestSize", String.valueOf(dataListMsg.size()));
                    chatAdapter.notifyDataSetChanged();

                }catch (Exception e)
                {
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbRefListMessage.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists() && isCompletedLoadMessage)
                {
                    displayMessage(snapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if(snapshot.exists())
//                {
//                    displayMessage(snapshot);
//                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.i("Delete","Delete");
                deleteMessage(snapshot);
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

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = currentDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = currentTimeFormat.format(calForTime.getTime());

        newMessage.setText("");
        String id = dbRefListMessage.push().getKey();

        Map<String, Object> content = new HashMap<String, Object>();
        content.put("msg",newMsg);
        content.put("sendby",user);
        content.put("timesend", currentTime);
        content.put("datesend",currentDate);
        dbRefListMessage.child(id).setValue(content);
    }

    private void displayMessage(DataSnapshot snapshot)
    {
        String id = snapshot.getKey();
        String sendby = snapshot.child("sendby").getValue(String.class);
        String msg = snapshot.child("msg").getValue(String.class);
        String dateSend = snapshot.child("datesend").getValue(String.class);
        String timeSend = snapshot.child("timesend").getValue(String.class);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm:ss");

        dbRefUser.child(sendby).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String avt = snapshot.child("avatar").getValue(String.class);
                String name = snapshot.child("hoTen").getValue(String.class);

                Message msgObject;
                if(!sendby.equals(user))
                {
                    msgObject = new messageFromOther(id,name,msg,avt,dateSend,timeSend);
                }
                else
                {
                    msgObject = new messageToOther(id,msg,avt,dateSend, timeSend);
                }
                dataListMsg.add(0,msgObject);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteMessage(DataSnapshot snapshot)
    {
        String id = snapshot.getKey();

        int index = -1;
        for(int i = 0; i< dataListMsg.size();i++)
        {
            if(dataListMsg.get(i).getId().equals(id))
                index = i;
        }

        if(index != -1)
            dataListMsg.remove(index);

        chatAdapter.notifyDataSetChanged();
    }
}

