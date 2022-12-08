package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ScanQRCode extends AppCompatActivity {

    ImageView img_qrcode;
    Button btn_scan,button_generateQrcode;
    String idRes ="",idUser,role;
    DatabaseReference database ;
    ProgressBar progress_bar;
    TextView result;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle !=null){
            idUser = bundle.getString("idUser");
            idRes = bundle.getString("idRes");
            role = bundle.getString("role");
        }


        img_qrcode =(ImageView) findViewById(R.id.img_qrcode);
        btn_scan =(Button) findViewById(R.id.button_scanQr);
        button_generateQrcode =(Button) findViewById(R.id.button_generateQrcode);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        result = (TextView) findViewById(R.id.result);
        progress_bar.setVisibility(View.INVISIBLE);

        if(!role.equals("ChuQuan")){
            button_generateQrcode.setVisibility(View.INVISIBLE);
        }
        database = FirebaseDatabase.getInstance().getReference("restaurant/"+idRes);
        if(role.equals("ChuQuan")) {
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        String imgQrcode = snapshot.child("ImgQrcode").getValue(String.class).toString();
                        Glide.with(getApplication()).load(imgQrcode).into(img_qrcode);
                    } catch (Exception e) {

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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
                        //((ImageView) findViewById(R.id.img_qrcode)).setImageBitmap(bmp);
                        //uploadImage(bmp);
                        database.child("Qrcode").setValue(data);
                        Uri uri = getImageUri(ScanQRCode.this,bmp);
                        load_image(uri);
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
        if (result != null && !role.equals("KhachHang")) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String contents = result.getContents();

                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String str = snapshot.child("Qrcode").getValue(String.class).toString();
                            if(str.equals(contents)){
                               setAttendance();
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
        }else{
            Toast.makeText(this, "Bạn không phải là nhân viên của quán.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setAttendance(){
       DatabaseReference mRef = database.child("NhanVien");
       // Toast.makeText(ScanQRCode.this, idUser, Toast.LENGTH_SHORT).show();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(postSnapshot.getKey().equals(idUser)){
                        String status = postSnapshot.child("TrangThai").getValue(String.class).toString();

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        String currentDate = format.format(calendar.getTime());

                        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
                        String currentTime = formatTime.format(calendar.getTime());

                        DatabaseReference db = database.child("NhanVien").child(idUser);

                        if(status.equals("KhongLamViec") ){
                            //ghi nhan ngay, gio lam viec bat dau
                            String key;
                            key = db.child("LamViec").child(currentDate).push().getKey().toString();
                            db.child("LamViec").child(currentDate).child(key).child("Start").setValue(currentTime);
                            db.child("LamViec").child(currentDate).child(key).child("End").setValue("0");
                            //sua trang thai
                            db.child("TrangThai").setValue("DangLamViec");
                            result.setText("Bắt đầu: "+ currentDate+'-'+currentTime);
                        }else if(status.equals("DangLamViec"))
                        {

                            for(DataSnapshot data: postSnapshot.child("LamViec").child(currentDate).getChildren()){

                                String str =data.child("End").getValue().toString();
                                if(str.equals("0")) {
                                    String key = data.getKey().toString();
                                    String start = data.child("Start").getValue(String.class).toString();
                                    db.child("LamViec").child(currentDate).child(key).child("End").setValue(currentTime);

                                    String bf = postSnapshot.child("ThoiGianLamViec").getValue(String.class).toString();

                                    try {
                                        int hour = (int) ((formatTime.parse(currentTime).getTime() - formatTime.parse(start).getTime()) / 3600000);
                                        int rs = (Integer.valueOf(bf) + (hour));
                                        db.child("ThoiGianLamViec").setValue(String.valueOf(rs));

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    db.child("TrangThai").setValue("KhongLamViec");

                                    result.setText("Kết thúc: " + currentDate + '-' + currentTime);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG,100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);

    }

    public void load_image(Uri uri) {
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "QR." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        database.child("ImgQrcode").setValue(uri.toString());
                        progress_bar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
               progress_bar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress_bar.setVisibility(View.INVISIBLE);
                Toast.makeText(ScanQRCode.this, "Upload image fail !!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private  String getFileExtension(Uri mUri){
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}