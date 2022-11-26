package com.example.oderingfood;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register_Store extends Activity {

    ImageView add_image1;
    ImageView add_image2;
    ImageView add_image3;
    EditText edt_name,edt_address, edt_discription,edt_tables;

    Button btn_submit;
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
        id =database.push().getKey();
        name = edt_name.getText().toString();
        address = edt_address.getText().toString();
        discription = edt_discription.getText().toString();
        soban = Integer.valueOf(edt_tables.getText().toString());


        if(name.isEmpty()){
            Toast.makeText(this, "Tên quán không dược để trống", Toast.LENGTH_SHORT).show();
        }else if (address.isEmpty()){
            Toast.makeText(this, "Địa chỉ không dược để trống", Toast.LENGTH_SHORT).show();
        }else {
            database.child(id).child("TenQuan").setValue(name);
            database.child(id).child("DiaChi").setValue(address);
            database.child(id).child("SoBan").setValue(soban);
            database.child(id).child("MoTa").setValue(discription);
            database.child(id).child("ChuQuan").setValue(idOwner);
            database.child(id).child("id").setValue(id);
            if(Urlimages.size()==0){
                Urlimages.add("https://firebasestorage.googleapis.com/v0/b/orderingfood-ab91f.appspot.com/o/store_default.png?alt=media&token=de6a404a-dd66-4a21-b6ae-eda751d79983");
            }
            for(int i =0;i<Urlimages.size();i++){
                DatabaseReference db = database.child(id).child("HinhAnh").child(String.valueOf(i+1));
                db.setValue(Urlimages.get(i));
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            Uri uri=data.getData();
            if(uri != null ){
                add_image1.setImageURI(uri);
                load_image(uri);
            }else{
                Toast.makeText(this, "Thêm hình ảnh đại diện.", Toast.LENGTH_SHORT).show();
            }

        }
        else if(requestCode==2){
            Uri uri=data.getData();
            if(uri != null ){
                add_image2.setImageURI(uri);
                load_image(uri);
            }else{
                Toast.makeText(this, "Thêm hình ảnh thất bại.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==3){
            Uri uri=data.getData();
            if(uri != null ) {
                add_image3.setImageURI(uri);
                load_image(uri);
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
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
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