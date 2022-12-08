package com.example.oderingfood;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddFoodToMenu extends Activity {

    ImageView add_image1;

    EditText edt_name,edt_price, edt_id;

    Button btn_submit;
    String name ="",id ="", urlImage = "";
    double price =0;


    DatabaseReference database ;
    StorageReference reference ;
    int SELECT_IMAGE_CODE=1;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_to_menu_layout);

//        Intent intent = getIntent();
//        idOwner = intent.getStringExtra("idOwner");

        database = FirebaseDatabase.getInstance().getReference("/restaurant/" + GlobalVariables.pathRestaurentID);
        database = database.child("Menu");
                reference = FirebaseStorage.getInstance().getReference();

        edt_name= (EditText) findViewById(R.id.af_input_namefood);
        edt_id=(EditText) findViewById(R.id.af_input_idmon);
        edt_price=(EditText) findViewById(R.id.af_input_price);

        add_image1=(ImageView) findViewById(R.id.af_add_image_food);
        add_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"title"),SELECT_IMAGE_CODE);

            }
        });


        btn_submit = (Button) findViewById(R.id.af_btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_data();
            }
        });
    }

    private void load_data() {

        name = edt_name.getText().toString();
        id = edt_id.getText().toString();
        price = Double.valueOf(edt_price.getText().toString());


        if(id.isEmpty()){
            Toast.makeText(this, "ID món ăn không dược để trống", Toast.LENGTH_SHORT).show();
        }else if (name.isEmpty()){
            Toast.makeText(this, "Tên món ăn không dược để trống", Toast.LENGTH_SHORT).show();
        }else if (price<=0){
            Toast.makeText(this, "Giá không hợp lệ.", Toast.LENGTH_SHORT).show();
        }else {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapShot: snapshot.getChildren())
                {
                    if(postSnapShot.getKey().equals(id))
                    {
                        Toast.makeText(AddFoodToMenu.this,"ID món ăn đã tồn tại",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Food food = new Food(id, name, price, url );
                database.child(id).setValue(food, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(AddFoodToMenu.this,getString(R.string.themmonthanhcong), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//            id = database.push().getKey();



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
    }

    private void load_image(Uri uri) {

        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        url = uri.toString();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddFoodToMenu.this, "Upload image fail !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  String getFileExtension(Uri mUri){
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}