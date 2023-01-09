package com.example.oderingfood.Account;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oderingfood.ListRestaurant.ListRestaurant;
import com.example.oderingfood.R;
import com.example.oderingfood.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Register2 extends Activity {
    ImageView add_image1;
    Button btn_submit;
    Uri uri;
    String avatar;
    EditText regName,regDofBirth,regGender, regPhone, regAddress;
    CheckBox truth;
    public int check=1;
    private int checkPic=0;
    private int temp=0;
    private int flag=0;

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

        truth=(CheckBox)findViewById(R.id.checkbox_true);
        strg= FirebaseStorage.getInstance().getReference();
        Intent i=getIntent();
        String id=i.getStringExtra("id");
        String email=i.getStringExtra("email");
        String type=i.getStringExtra("type");

        add_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"title"),SELECT_IMAGE_CODE);
            }
        });

        regDofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonNgay();
            }
        });
        truth.setOnClickListener(new View.OnClickListener() {
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
        btn_submit = findViewById(R.id.submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name=regName.getEditableText().toString();
                String doBirth=regDofBirth.getEditableText().toString();
                String gender=regGender.getEditableText().toString();
                String phone=regPhone.getEditableText().toString();
                String address=regAddress.getEditableText().toString();
                checkphone(phone);




                if(name.isEmpty() || doBirth.isEmpty() || gender.isEmpty()||phone.isEmpty()||address.isEmpty()){
                    Toast.makeText(Register2.this, "Thông tin chưa đầy đủ.", Toast.LENGTH_SHORT).show();
                }
                else if(checkPic==0 ) {
                    Toast.makeText(Register2.this, "Vui lòng thêm ảnh đại diện", Toast.LENGTH_SHORT).show();
                }
                else if(check==0){
                    Toast.makeText(Register2.this, "Số điện thoại này đã được sử dụng", Toast.LENGTH_SHORT).show();
                    check=1;
                }else if(flag==0)
                {
                    Toast.makeText(Register2.this, "Bạn cần xác nhận thông tin", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    rootNode=FirebaseDatabase.getInstance();
                    reference=rootNode.getReference("user");
                    User user=new User(avatar,email,name,doBirth,gender,phone,address);
                    reference.child(id).setValue(user);
                    Toast.makeText(Register2.this, "Hoàn tất đăng kí", Toast.LENGTH_SHORT).show();

                    if(type.equals("signInWithGoogle") || type.equals("noInfo")){
                        Intent intent=new Intent(Register2.this, ListRestaurant.class);
                        intent.putExtra("Uid",id);
                        startActivity(intent);
                        finish();
                    }
                    Intent intent=new Intent(Register2.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null && data.getData() != null) {
            uri = data.getData();
            add_image1.setImageURI(uri);
            checkPic=1;

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

    private  void chonNgay(){
        Calendar calendar=Calendar.getInstance();
        int ngay=calendar.get(Calendar.DATE);
        int thang=calendar.get(Calendar.MONDAY);
        int nam=calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");

                regDofBirth.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },nam,thang, ngay);
        datePickerDialog.show();
    }



    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();

        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void checkphone(String phone){

        Query checkPhone=FirebaseDatabase.getInstance().getReference("user").orderByChild("dienThoai").equalTo(phone);
        checkPhone.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    check=0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}