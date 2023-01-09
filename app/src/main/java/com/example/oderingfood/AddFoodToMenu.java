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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;
import com.example.oderingfood.models.NotificationItem;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddFoodToMenu extends Activity {

    ImageView add_image1;
    TextView title;
    EditText edt_name,edt_price, edt_id;

    Button btn_submit;
    String name ="",id ="", urlImage = "";
    double price =0;
    String oldName = "";

    DatabaseReference database ;
    StorageReference reference ;
    int SELECT_IMAGE_CODE=1;
    String url;

    String user;
    String idRes;
    boolean edit;
    Food food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_to_menu_layout);


        Bundle b = getIntent().getExtras();

        if(b !=null){
            user = b.getString("idUser");
            idRes = b.getString("idRes");
            edit = b.getBoolean("edit");
            if(edit){
                food = (Food) b.getSerializable("food");
                oldName = food.getName();
            }

        }

        database = FirebaseDatabase.getInstance().getReference("/restaurant/" + GlobalVariables.pathRestaurentID);
        database = database.child("Menu");
                reference = FirebaseStorage.getInstance().getReference();

        edt_name= (EditText) findViewById(R.id.af_input_namefood);
        edt_id=(EditText) findViewById(R.id.af_input_idmon);
        edt_price=(EditText) findViewById(R.id.af_input_price);
        title = (TextView) findViewById(R.id.af_add_title);
        add_image1=(ImageView) findViewById(R.id.af_add_image_food);

        if(edit){
            title.setText("Chỉnh sửa thông tin món ăn");
            url = food.getUrlImage();
            Picasso.get().load(url).into(add_image1);
            edt_id.setText(food.getId());
            edt_id.setEnabled(false);
            edt_name.setText(food.getName());
            edt_price.setText(Double.toString(food.getPrice()));
        }
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
        String priceString = edt_price.getText().toString();
        if (priceString.isEmpty()){
            Toast.makeText(this, "Giá không để trống", Toast.LENGTH_SHORT).show();
        }
        else {
            price = Double.valueOf(priceString);
        }


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
                    if(edit == false) {
                        if (postSnapShot.getKey().equals(id)) {
                            Toast.makeText(AddFoodToMenu.this, "ID món ăn đã tồn tại", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                if(edit){
                    food.setName(name);
                    food.setPrice(price);
                    food.setUrlImage(url);
                }
                else {
                    food = new Food(id, name, price, url);
                }
                database.child(id).setValue(food, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                        FirebaseDatabase noti = FirebaseDatabase.getInstance();


                        noti.getReference("user/" + user ).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String avt = snapshot.child("avatar").getValue(String.class).toString();
                                String ad = snapshot.child("hoTen").getValue(String.class).toString();
                                String label = "", content ="";
                                if(edit){
                                    label = "<b>Chỉnh sửa món <b>";
                                    content = ad + " vừa thay đổi món " + oldName;
                                }
                                else {
                                    label = "<b> Thêm món <b>";
                                    content = ad + " vừa thêm món " + name;
                                }
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
                                String currentDate = format.format(calendar.getTime());
                                String _id =  noti.getReference("restaurant/" + idRes).child("notification").push().getKey().toString();
                                NotificationItem notificationItem = new NotificationItem(_id,avt, label, content, currentDate);

                                noti.getReference("restaurant/" + idRes).child("notification").child(_id).setValue(notificationItem);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {


                            }
                        });
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

        try {
            if (requestCode == 1) {
                Uri uri = data.getData();
                if (uri != null) {
                    load_image(uri);
                    add_image1.setImageURI(uri);
                } else {
                    Toast.makeText(this, "Thêm hình ảnh thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){

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