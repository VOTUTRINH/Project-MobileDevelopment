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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Register2 extends Activity {
    ImageView add_image1;
    Button btn_submit;
    Uri uri;
    String avatar;
    EditText regName,regDofBirth,regGender, regPhone, regAddress;

    private StorageTask mUploadTask;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    StorageReference storageReference,strg;

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
        strg= FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id");
        String email = bundle.getString("email");

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
                User user=new User(avatar,email,name,doBirth,gender,phone,address);

                reference.child(id).setValue(user);

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null && data.getData() != null) {
            uri = data.getData();
            add_image1.setImageURI(uri);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            Date now = new Date();
            String fileName = formatter.format(now)+".jpg";
            storageReference = FirebaseStorage.getInstance().getReference(fileName);
            storageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //add_image1.setImageURI(null);
                            Toast.makeText(Register2.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();

                            strg.child(fileName).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    //getdownloadUrl
                                    avatar=task.getResult().toString();
//                                    dbref.push().child("imageurl").setValue(downloadUrl);
//                                    progressDialog.dismiss();
                                    Toast.makeText(Register2.this,"Image uploaded",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Register2.this, "Failed to Upload", Toast.LENGTH_SHORT).show();


                        }
                    });

        }
    }



    private void uploadFile(){

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}