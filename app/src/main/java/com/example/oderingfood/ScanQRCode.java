package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;


import java.io.ByteArrayOutputStream;


public class ScanQRCode extends AppCompatActivity {

    ImageView img_qrcode;
    Button btn_scan,button_generateQrcode;
    String idRes ="",idUser;
    DatabaseReference database ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle !=null){
            idUser = bundle.getString("idUser");
            idRes = bundle.getString("idRes");
        }
        //chua lam: neu la nhan vien, an nut tao qr

        img_qrcode =(ImageView) findViewById(R.id.img_qrcode);
        btn_scan =(Button) findViewById(R.id.button_scanQr);
        button_generateQrcode =(Button) findViewById(R.id.button_generateQrcode);
        database = FirebaseDatabase.getInstance().getReference("restaurant/"+idRes);

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentIntegrator.initiateScan();
            }
        });

        button_generateQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = System.currentTimeMillis() + idRes;

                QRCodeWriter writer = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    if(bmp != null){
                        ((ImageView) findViewById(R.id.img_qrcode)).setImageBitmap(bmp);
                        database.child("Qrcode").setValue(data);
                    }

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String contents = result.getContents();
                    Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();

                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String str = snapshot.child("Qrcode").getValue(String.class).toString();
                            if(str.equals(contents)){


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setAttendance(){
       DatabaseReference mRef = database.child("NhanVien");

       mRef.addValueEventListener(new ValueEventListener() {
           boolean rs = false;
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                   if(postSnapshot.getKey().equals(idUser)){
                       String status = postSnapshot.child("TrangThai").getValue(String.class).toString();
                       if(status == "KhongLamViec"){
                           //ghi nhan ngay, gio lam viec bat dau
                           //sua trang thai
                           //tinh thoi gian lam viec trong ngay
                       }else{
                           //ghi nhan gio ket thuc
                           // chot thoi gian lam viec
                           // them thoi gian lam viec vao thuoc tinh ThoiGianLamViec
                           //sua trang thai
                       }
                   }
               }
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
    public void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imagesRef = storageRef.child(System.currentTimeMillis() + "QR.jpg");

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String UrlImage = imagesRef.getDownloadUrl().toString();
                Toast.makeText(ScanQRCode.this, UrlImage, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScanQRCode.this, "Load QRcode thất bại.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}