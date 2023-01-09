package com.example.oderingfood.Restaurant.More;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oderingfood.Support.AdapterEditImage;
import com.example.oderingfood.Support.FullScreenActivity;
import com.example.oderingfood.R;
import com.example.oderingfood.models.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Res_infor extends AppCompatActivity {

    String idUser,idRes,role;
    ImageView add_image,add_image1;
    EditText edt_name,edt_address,edt_description,edt_tables;
    RecyclerView recyclerView;
    Button btn_add_image,btn_edit,btn_submit;
    ProgressBar progress_bar;

    FirebaseDatabase database ;
    DatabaseReference myRef ;

    int SELECT_IMAGE_CODE=1;
    Uri uri, avt;
    boolean isEditable = false;

    ArrayList<Image> Img =new ArrayList<>();
    ArrayList Img2 =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_infor);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle !=null){
            idUser = bundle.getString("idUser");
            idRes = bundle.getString("idRes");
            role = bundle.getString("role");
        }

        add_image = (ImageView) findViewById(R.id.add_image);
        add_image1 = (ImageView) findViewById(R.id.add_image1);

        edt_name =(EditText) findViewById(R.id.edt_name);
        edt_address =(EditText) findViewById(R.id.edt_address);
        edt_description =(EditText) findViewById(R.id.edt_description);
        edt_tables= (EditText) findViewById(R.id.edt_tables);

        btn_add_image =(Button) findViewById(R.id.btn_add_image);
        btn_edit =(Button) findViewById(R.id.btn_edit);
        btn_submit =(Button) findViewById(R.id.btn_submit);

        progress_bar =(ProgressBar) findViewById(R.id.progress_bar);
        add_image1.setVisibility(View.INVISIBLE);
        btn_add_image.setVisibility(View.INVISIBLE);
        if(!role.equals(("ChuQuan"))) {
            btn_edit.setVisibility(View.INVISIBLE);
            btn_submit.setVisibility(View.INVISIBLE);
        }

        progress_bar.setVisibility(View.INVISIBLE);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("restaurant/"+idRes);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_list_image_1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Res_infor.this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Sending reference and data to Adapter
        AdapterEditImage adapter = new AdapterEditImage(Res_infor.this, Img,idRes);
        // Setting Adapter to RecyclerView
        recyclerView.setAdapter(adapter);



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                edt_name.setText(snapshot.child("TenQuan").getValue().toString());
                edt_address.setText(snapshot.child("DiaChi").getValue().toString());
                edt_description.setText(snapshot.child("MoTa").getValue().toString());
                edt_tables.setText(snapshot.child("SoBanAn").getValue().toString());


                String avt;
                try {
                    avt = snapshot.child("HinhAnh/1").getValue(String.class).toString();
                }
                catch (Exception exception){
                    avt = "https://firebasestorage.googleapis.com/v0/b/orderingfood-ab91f.appspot.com/o/store_default.png?alt=media&token=de6a404a-dd66-4a21-b6ae-eda751d79983";
                }
                Glide.with(getApplication()).load(avt).into(add_image);
                Img.clear();
                for(DataSnapshot postSnapshot: snapshot.child("HinhAnh").getChildren()){
                    try{
                        String urlImage = postSnapshot.getValue().toString();
                        String id = postSnapshot.getKey().toString();

                        Image image = new Image(id,urlImage);
                        Img.add(image);
                    }catch(Exception e){
                        //not have images
                    }
                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditable = true;

                edt_name.setEnabled(true);
                edt_address.setEnabled(true);
                edt_description.setEnabled(true);
                add_image1.setImageResource(R.drawable.add_image);

                add_image1.setVisibility(View.VISIBLE);
                edt_name.setInputType(InputType.TYPE_CLASS_TEXT);
                edt_address.setInputType(InputType.TYPE_CLASS_TEXT);
                edt_description.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                edt_description.setLines(4);
            }
        });

        add_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"title"),SELECT_IMAGE_CODE);


            }
        });



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEditable) {
                  save();
            }}
        });

        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri != null && !Img2.contains(uri)) {
                    Img2.add((uri));
                    load_image((uri));
                }
            }
            });
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEditable) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "title"), 2);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                uri = data.getData();
                if (uri != null){
                    btn_add_image.setVisibility(View.VISIBLE);
                    add_image1.setImageURI(uri);
                }
                else {
                    Toast.makeText(this, "Thêm hình ảnh thất bại.", Toast.LENGTH_SHORT).show();
                }
            }else if(requestCode == 2){
                avt = data.getData();
                if (avt != null){
                    add_image.setImageURI(avt);
                }
                else {
                    Toast.makeText(this, "Thêm hình ảnh thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        if(isEditable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("Bạn có muốn lưu thông tin đã chỉnh sửa ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
                    save();
                   // finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    finish();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }else{
            finish();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if(isEditable) {
            int clickedItemPosition = item.getGroupId();

            if (item.getItemId() == R.id.menu_delete_item) {

                Image image = Img.get(clickedItemPosition);

                myRef.child("HinhAnh").child(image.getId()).removeValue();

                return true;
            }
            else if (item.getItemId() == R.id.menu_edit_item) {

                Image image = Img.get(clickedItemPosition);


                        // get id to acess database
                Intent intent = new Intent(Res_infor.this, FullScreenActivity.class);
                intent.putExtra("id", image.getUrl());
                startActivity(intent);

               // myRef.child("HinhAnh").child(image.getId()).removeValue();

                return true;
            }
        }

        return super.onContextItemSelected(item);

    }
    private void save(){
        String name = edt_name.getText().toString();
        String address = edt_address.getText().toString();
        String description = edt_description.getText().toString();
        if(avt!=null){
            load_image_avt(avt);
        }

        if (name.isEmpty()) {
            Toast.makeText(Res_infor.this, "Tên quán không dược để trống", Toast.LENGTH_SHORT).show();
        } else if (address.isEmpty()) {
            Toast.makeText(Res_infor.this, "Địa chỉ không dược để trống", Toast.LENGTH_SHORT).show();
        } else {

            myRef.child("TenQuan").setValue(name);
            myRef.child("DiaChi").setValue(address);
            myRef.child("MoTa").setValue(description);

            add_image1.setVisibility(View.INVISIBLE);
            btn_add_image.setVisibility(View.INVISIBLE);

            edt_name.setEnabled(false);
            edt_address.setEnabled(false);
            edt_description.setEnabled(false);
            isEditable = false;

        }
    }

    private void load_image(Uri uri) {
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        myRef.child("HinhAnh").push().setValue(uri.toString());
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
                Toast.makeText(Res_infor.this, "Upload image fail !!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void load_image_avt(Uri uri) {
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        myRef.child("HinhAnh").child("1").setValue(uri.toString());
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
                Toast.makeText(Res_infor.this, "Upload image fail !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  String getFileExtension(Uri mUri){
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}


