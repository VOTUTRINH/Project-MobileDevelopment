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

                loginUser();
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
                        Account account = new Account(uid,email,pass);
                        SessionManagement sessionManagement=new SessionManagement(MainActivity.this);
                        sessionManagement.saveSession(account);
                        Intent intent = new Intent(MainActivity.this, ListRestaurant.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Override
    protected void onStart(){
        super.onStart();
        checkSession();
    }

    private void checkSession(){
        SessionManagement sessionManagement=new SessionManagement(MainActivity.this);

        String uid=sessionManagement.getSession();

        if(!uid.equals("emptyString")){
            Intent intent = new Intent(MainActivity.this, ListRestaurant.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Uid",uid);
            startActivity(intent);

        }else {
            //do nothing
        }
    }

}