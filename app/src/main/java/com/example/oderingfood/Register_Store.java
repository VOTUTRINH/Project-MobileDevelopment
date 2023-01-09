package com.example.oderingfood;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register_Store extends Activity {

    ImageView add_image1;
    ImageView add_image2;
    ImageView add_image3;
    EditText edt_name,edt_address, edt_discription,edt_tables;
    Button btn_submit;
    ProgressBar progressBar;

    String name ="", address ="",discription="",id ="";
    int soban = 0;
    String idOwner;
    ArrayList Urlimages = new ArrayList<>();


    DatabaseReference database ;
    StorageReference reference ;
    int SELECT_IMAGE_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);

        Intent intent = getIntent();
        idOwner = intent.getStringExtra("idOwner");

        database = FirebaseDatabase.getInstance().getReference("restaurant");
        reference = FirebaseStorage.getInstance().getReference();

        edt_name= (EditText) findViewById(R.id.edt_name);
        edt_address=(EditText) findViewById(R.id.edt_address);
        edt_discription=(EditText) findViewById(R.id.edt_discription);
        edt_tables=(EditText) findViewById(R.id.edt_tables);
        add_image1=(ImageView) findViewById(R.id.add_image);
        progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        add_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
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

        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_data();
            }
        });
    }

    private void load_data() {

        name = edt_name.getText().toString();
        address = edt_address.getText().toString();
        discription = edt_discription.getText().toString();
        try{
            soban = Integer.valueOf(edt_tables.getText().toString());
        }catch(Exception exception){

            soban =0;
        }



        if(name.isEmpty()){
            Toast.makeText(this, "Tên quán không dược để trống", Toast.LENGTH_SHORT).show();
        }else if (address.isEmpty()){
            Toast.makeText(this, "Địa chỉ không dược để trống", Toast.LENGTH_SHORT).show();
        }else {

            id = database.push().getKey();
            Map<String,String> myMap = new HashMap<String,String>();
            myMap.put("TenQuan",name);
            myMap.put("DiaChi",address);
            myMap.put("MoTa",discription);
            myMap.put("ChuQuan",idOwner);
            myMap.put("id",id);

            database.child(id).setValue(myMap);
            database.child(id).child("SoBanAn").setValue(soban);
            database.child(id).child("DoanhThu").setValue(0);
            DatabaseReference db = database.child(id).child("HinhAnh");
            Map<String,String> mapImage = new HashMap<String,String>();

            for(int i =0;i<Urlimages.size();i++){
                mapImage.put(String.valueOf(i+1),Urlimages.get(i).toString());
            }
            db.setValue(mapImage);
            // Create table when create restaurant
            DatabaseReference dbTables = database.child(id).child("BanAn");

            Map<String, Map<String,String>> mapTables = new HashMap<String,Map<String,String>>();
            for (int i=1; i<= soban; i++)
            {
                Map<String, String> mapTableValue = new HashMap<String, String>();
                mapTableValue.put("Order","");
                mapTableValue.put("TrangThai", "Empty");

                mapTables.put(String.valueOf(i), mapTableValue);
            }
            dbTables.setValue(mapTables);

            finish();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            Uri uri=data.getData();
            if(uri != null ){
                load_image(uri);
                add_image1.setImageURI(uri);
            }else{
                Toast.makeText(this, "Thêm hình ảnh thất bại.", Toast.LENGTH_SHORT).show();
            }

        }
        else if(requestCode==2){
            Uri uri=data.getData();
            if(uri != null ){
                load_image(uri);
                add_image2.setImageURI(uri);
            }else{
                Toast.makeText(this, "Thêm hình ảnh thất bại.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==3){
            Uri uri=data.getData();
            if(uri != null ) {
                load_image(uri);
                add_image3.setImageURI(uri);
            }else{
                Toast.makeText(this, "Thêm hình ảnh thất bại.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void load_image(Uri uri) {

        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Urlimages.add(uri.toString());
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Register_Store.this, "Upload image fail !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  String getFileExtension(Uri mUri){
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}