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
import com.google.firebase.auth.FirebaseAuthException;


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
            Toast.makeText(MainActivity.this,"Vui lòng nhập email!!",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(MainActivity.this,"Vui lòng nhập password!!",Toast.LENGTH_SHORT).show();
        }

        else {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String uid=   auth.getCurrentUser().getUid();
                        Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, ListRestaurant.class);
                        intent.putExtra("Uid",uid);
                        startActivity(intent);
                        finish();
                    } else {
                        String str = ((FirebaseAuthException) task.getException()).getErrorCode();

                        if (str.equals("ERROR_INVALID_EMAIL")) {
                            Toast.makeText(MainActivity.this, "Email không đúng định dạng.", Toast.LENGTH_SHORT).show();

                        }
                        else if(str.equals("ERROR_WRONG_PASSWORD")){
                            Toast.makeText(MainActivity.this, "Mật khẩu không chính xác.", Toast.LENGTH_SHORT).show();

                        }
                        else if (str.equals("ERROR_USER_NOT_FOUND")) {
                            Toast.makeText(MainActivity.this, "Tài khoản không tồn tại.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Đăng kí không thành công.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, ((FirebaseAuthException) task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}