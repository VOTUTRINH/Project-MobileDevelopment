package com.example.oderingfood.Restaurant.More;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oderingfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Voucher extends AppCompatActivity {
    String idUser,idRes,role;
    EditText edt_voucher;
    Button btn_edit,btn_submit;

    FirebaseDatabase database ;
    DatabaseReference myRef ;
    boolean isEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle !=null){
            idUser = bundle.getString("idUser");
            idRes = bundle.getString("idRes");
            role = bundle.getString("role");
        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("restaurant/"+idRes);
        isEditable =false;

        edt_voucher = (EditText) findViewById(R.id.edt_voucher);
        btn_edit =(Button) findViewById(R.id.btn_edit);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    edt_voucher.setText(snapshot.child("Voucher").getValue(String.class).toString());
                }catch (Exception exception){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(!role.equals("ChuQuan")){
            btn_edit.setVisibility(View.INVISIBLE);
            btn_submit.setVisibility(View.INVISIBLE);
        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_voucher.setEnabled(true);
                isEditable = true;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEditable) {
                    save();
                }}
        });


    }
    @Override
    public void onBackPressed() {
        if(isEditable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Bạn có muốn lưu thông tin đã chỉnh sửa ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    save();
                    // finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    finish();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            finish();
        }
    }

    private void save(){
        String voucher = edt_voucher.getText().toString();
        myRef.child("Voucher").setValue(voucher);
        edt_voucher.setEnabled(false);
        isEditable = false;
    }
}