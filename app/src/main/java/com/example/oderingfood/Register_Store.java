package com.example.oderingfood;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Register_Store extends Activity {

    ImageView add_image1;
    ImageView add_image2;
    ImageView add_image3;
    Button button;
    int SELECT_IMAGE_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);
        add_image1=(ImageView) findViewById(R.id.add_image);
        add_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"title"),SELECT_IMAGE_CODE);

            }
        });
        add_image2=(ImageView) findViewById(R.id.add_image1);
        add_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"title"),2);
            }
        });
        add_image3=(ImageView) findViewById(R.id.add_image2);
        add_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"title"),3);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            Uri uri=data.getData();
            add_image1.setImageURI(uri);
        }
        else if(requestCode==2){
            Uri uri=data.getData();
            add_image2.setImageURI(uri);
        }
        else if(requestCode==3){
            Uri uri=data.getData();
            add_image3.setImageURI(uri);
        }
    }
}