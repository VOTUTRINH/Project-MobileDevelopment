package com.example.oderingfood;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    Button button_register, button_signin;
    EditText username,password;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth =FirebaseAuth.getInstance();

        button_register=(Button) findViewById(R.id.btnregister);
        button_signin =(Button) findViewById(R.id.btn_signin);

        username =(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String text_username = username.getText().toString();
//                String text_password = password.getText().toString();

                loginUser();
//                Intent intent=new Intent(MainActivity.this,ListRestaurant.class);
//                startActivity(intent);

            }
        });
        

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });


    }

    private void loginUser() {
        String email=username.getText().toString();;
        String pass=password.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(MainActivity.this,"Vui long nhap email!!",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(MainActivity.this,"Vui long nhap password!!",Toast.LENGTH_SHORT).show();
        }
//        auth.signInWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                Toast.makeText(MainActivity.this,"Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(MainActivity.this,Register.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,ListRestaurant.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Đăng nhập không thành công",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}