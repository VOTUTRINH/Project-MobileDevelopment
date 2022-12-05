package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StartResActivity extends AppCompatActivity {

    String user,idRes;
    static String[] Lrole =  new String[1];
    FirebaseDatabase database;
    DatabaseReference myRef;
    String role="abc";
    ArrayList list = new ArrayList<>();

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_res);
        ProgressBar progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            user = bundle.getString("user");
            idRes = bundle.getString("restaurant");
        }
        text = (TextView) findViewById(R.id.text);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("restaurant/" + idRes);

        setRole();

        role = text.getText().toString();

        Toast.makeText(this,role, Toast.LENGTH_SHORT).show();
    }

    public void setRole(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String owner = snapshot.child("ChuQuan").getValue(String.class).toString();

                if(owner.equals(user)){
                    text.setText("ChuQuan");

                }else{
                    for(DataSnapshot postsnapshot: snapshot.child("NhanVien").getChildren()){
                        if(postsnapshot.getKey().equals(user)){
                            text.setText("NhanVien");
                            return;
                        }
                    }
                    text.setText("KhachHang");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}

