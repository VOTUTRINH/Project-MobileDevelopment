package com.example.oderingfood;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register2 extends Activity {
    ImageView add_image1;
    Button btn_submit;
    EditText regName,regDofBirth,regGender, regPhone, regAddress;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    int SELECT_IMAGE_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        add_image1=(ImageView)findViewById(R.id.add_img);
        regName=(EditText) findViewById(R.id.reg_name);
        regDofBirth=(EditText)findViewById(R.id.reg_dofBirth);
        regGender=(EditText)findViewById(R.id.reg_gender);
        regPhone=(EditText)findViewById(R.id.reg_phone);
        regAddress=(EditText)findViewById(R.id.reg_address);

        Intent i=getIntent();
        String id=i.getStringExtra("id");
        add_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"title"),SELECT_IMAGE_CODE);
            }
        });

        btn_submit = findViewById(R.id.submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("user");
                String name=regName.getEditableText().toString();
                String doBirth=regDofBirth.getEditableText().toString();
                String gender=regGender.getEditableText().toString();
                String phone=regPhone.getEditableText().toString();
                String address=regAddress.getEditableText().toString();
                User user=new User(id,name,doBirth,gender,phone,address);

                reference.child(id).setValue(user);

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Uri uri = data.getData();
            add_image1.setImageURI(uri);
        }
    }
}