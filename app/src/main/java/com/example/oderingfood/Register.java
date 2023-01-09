package com.example.oderingfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SignInMethodQueryResult;

public class Register extends Activity {

    Button button_continue;
    Button button2;
    EditText email,password,confirm_pass;
    CheckBox agree;
    FirebaseAuth auth;
    private int temp=0;
    private int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button_continue=(Button) findViewById(R.id.btncontinue);
        button2 =(Button) findViewById(R.id.btnlogin);
        email =(EditText) findViewById(R.id.signup_email);
        password=(EditText) findViewById(R.id.signup_pass);
        confirm_pass =(EditText) findViewById(R.id.confirm_pass);
        agree =(CheckBox) findViewById(R.id.checkbox_agree);



        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp++;
                if(temp%2==0){
                    flag=0;
                }else{
                    flag=1;
                }
            }
        });

        auth =FirebaseAuth.getInstance();


        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email = email.getText().toString();
                String text_pass = password.getText().toString();
                String text_confirm = confirm_pass.getText().toString();
                if(text_email.isEmpty() || text_pass.isEmpty() || text_confirm.isEmpty()){
                    Toast.makeText(Register.this, "Thông tin chưa đầy đủ.", Toast.LENGTH_SHORT).show();
                }else if(!text_confirm.equals(text_pass)){
                    Toast.makeText(Register.this, "Mật khẩu không trùng khớp.", Toast.LENGTH_SHORT).show();
                }else if(text_pass.length()<6){
                    Toast.makeText(Register.this, "Mật khẩu phải chứa ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                }
                else if(flag==0)
                {
                    Toast.makeText(Register.this, "Bạn cần đồng ý điều khoản sử dụng", Toast.LENGTH_SHORT).show();
                } else{
                    register(text_email,text_pass);
                }

            }
        });
        button2=(Button) findViewById(R.id.btnlogin);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register(String email, String pass) {

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id=  auth.getUid();
                    Toast.makeText(Register.this, "Đăng kí thành công.", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Register.this,Register2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",id);
                    bundle.putString("email",email);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else {
                    String str = ((FirebaseAuthException) task.getException()).getErrorCode();
                    if (str.equals("ERROR_INVALID_EMAIL")) {
                        Toast.makeText(Register.this, "Email không đúng định dạng.", Toast.LENGTH_SHORT).show();

                    }
                    if (str.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                        Toast.makeText(Register.this, "Email đã tồn tại.", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Register.this, "Đăng kí không thành công.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Register.this, ((FirebaseAuthException) task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}