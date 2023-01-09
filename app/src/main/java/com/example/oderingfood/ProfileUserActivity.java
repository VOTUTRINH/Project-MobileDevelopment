package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.SessionManagement;
import com.example.oderingfood.models.EmployeeSalary;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUserActivity extends AppCompatActivity {

    String user;
    List<EmployeeSalary> dataList = new ArrayList<EmployeeSalary>();
    EmployeeSalaryAdapter adapter;
    CircleImageView img_avatar, img_add_avatar;
    EditText edt_name, edt_phone, edt_sex, edt_address, edt_birthday;
    TextView txt_danhsachluong;
    ListView lv_danhsachluong;
    Button btn_editinfo;
    LinearLayout group_button_edit;
    String oldAvatar = "", oldName, oldPhone, oldSex, oldAddress, oldBirthday;
    Button btn_cancel_edit, btn_confirm_edit,btn_out;
    ProgressBar progress_bar;


    boolean isEditing = false;int SELECT_IMAGE_CODE=1;Uri uri;

    FirebaseDatabase database;
    DatabaseReference refUser;
    DatabaseReference refRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        Intent intent = getIntent();
        user = intent.getStringExtra("Uid");

        img_avatar = (CircleImageView) findViewById(R.id.afo_avatar);
        img_add_avatar = (CircleImageView) findViewById(R.id.afo_add_avatar);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_phone = (EditText) findViewById(R.id.edt_sdt);
        edt_sex = (EditText) findViewById(R.id.edt_sex);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_birthday = (EditText) findViewById(R.id.edt_birthday);
        txt_danhsachluong = (TextView) findViewById(R.id.txt_danhsachluong);
        lv_danhsachluong = (ListView) findViewById(R.id.lv_danh_sach_luong);
        btn_editinfo = (Button) findViewById(R.id.btn_edit);
        group_button_edit = (LinearLayout) findViewById(R.id.group_btn_edit);
        progress_bar =(ProgressBar)findViewById(R.id.progress_bar);

        btn_cancel_edit = (Button) findViewById(R.id.btn_cancel);
        btn_confirm_edit = (Button) findViewById(R.id.btn_submit);
        btn_out =(Button) findViewById(R.id.btn_out);
        adapter = new EmployeeSalaryAdapter(this,R.layout.item_nhanvien, dataList, user);
        lv_danhsachluong.setAdapter(adapter);
        lv_danhsachluong.setScrollContainer(false);

        database = FirebaseDatabase.getInstance();
        refRes = database.getReference("restaurant/");
        refUser = database.getReference("user/"+user);
        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String avatar = snapshot.child("avatar").getValue(String.class);
                String name = snapshot.child("hoTen").getValue(String.class);
                String phone = snapshot.child("dienThoai").getValue(String.class);
                String sex = snapshot.child("gioiTinh").getValue(String.class);
                String address = snapshot.child("diaChi").getValue(String.class);
                String birthday = snapshot.child("ngaySinh").getValue(String.class);
                birthday = birthday.replaceAll("/","-");
                if(!oldAvatar.equals(avatar)){
                    oldAvatar = avatar;
                    Glide.with(ProfileUserActivity.this).load(avatar).into(img_avatar);
                }
                edt_name.setText(name);
                edt_phone.setText(phone);
                edt_sex.setText(sex);
                edt_address.setText(address);
                edt_birthday.setText(birthday);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        progress_bar.setVisibility(View.GONE);

        refRes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    // Get data
                    if(postSnapShot.hasChild("NhanVien"))
                    {
                        if(postSnapShot.child("NhanVien").hasChild(user)){
                            String idRes = postSnapShot.getKey();
                            String name = postSnapShot.child("TenQuan").getValue(String.class);
                            String salary = postSnapShot.child("NhanVien").child(user).child("Luong").getValue(String.class);
                            Long tgLamViec = postSnapShot.child("NhanVien").child(user).child("ThoiGianLamViec").getValue(Long.class);

                            EmployeeSalary employeeSalary = new EmployeeSalary(idRes, name, salary, tgLamViec);
                            dataList.add(employeeSalary);
                        }
                    }
                }
                if(dataList.size() > 0){
                    justifyListViewHeightBasedOnChildren(lv_danhsachluong);
                    txt_danhsachluong.setVisibility(View.VISIBLE);
                    lv_danhsachluong.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
//                try {
//                    dataList.clear();
//                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
//                        // Get data
//                        if(postSnapShot.hasChild("NhanVien"))
//                        {
//                            if(postSnapShot.child("NhanVien").hasChild(user)){
//                                String idRes = postSnapShot.getKey();
//                                String name = postSnapShot.child("TenQuan").getValue(String.class);
//                                String salary = postSnapShot.child("NhanVien").child(user).child("Luong").getValue(String.class);
//                                Long tgLamViec = postSnapShot.child("NhanVien").child(user).child("ThoiGianLamViec").getValue(Long.class);
//
//                                EmployeeSalary employeeSalary = new EmployeeSalary(idRes, name, salary, tgLamViec);
//                                dataList.add(employeeSalary);
//                            }
//                        }
//                    }
//                    if(dataList.size() > 0){
//                        justifyListViewHeightBasedOnChildren(lv_danhsachluong);
//                        txt_danhsachluong.setVisibility(View.VISIBLE);
//                        lv_danhsachluong.setVisibility(View.VISIBLE);
//                    }
//                    adapter.notifyDataSetChanged();
//                }catch (Exception e){
//                    Intent intent = new Intent(ProfileUserActivity.this, ListRestaurant.class);
//                    Log.i("Test","1");
//                    intent.putExtra("Uid",user);
//                    startActivity(intent);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lv_danhsachluong.setAdapter(adapter);
        btn_editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditing = true;

                // Edit avatar
                img_avatar.setVisibility(View.GONE);

                img_add_avatar.setVisibility(View.VISIBLE);

                // Edit another info
                edt_name.setEnabled(true);
                edt_phone.setEnabled(true);
                edt_sex.setEnabled(true);
                edt_address.setEnabled(true);
                edt_birthday.setEnabled(true);

                edt_name.setInputType(InputType.TYPE_CLASS_TEXT);
                edt_phone.setInputType(InputType.TYPE_CLASS_NUMBER);
                edt_sex.setInputType(InputType.TYPE_CLASS_TEXT);
                edt_address.setInputType(InputType.TYPE_CLASS_TEXT);

                lv_danhsachluong.setVisibility(View.GONE);
                txt_danhsachluong.setVisibility(View.GONE);

                group_button_edit.setVisibility(View.VISIBLE);

                // Get old value
                oldName = edt_name.getText().toString();
                oldPhone = edt_phone.getText().toString();
                oldSex = edt_sex.getText().toString();
                oldAddress = edt_address.getText().toString();
                oldBirthday = edt_birthday.getText().toString();
            }
        });

        btn_cancel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditing = false;

                // Edit avatar
                img_avatar.setVisibility(View.VISIBLE);

                img_add_avatar.setVisibility(View.GONE);

                edt_name.setText(oldName);
                edt_phone.setText(oldPhone);
                edt_sex.setText(oldSex);
                edt_address.setText(oldAddress);
                edt_birthday.setText(oldBirthday);

                // Edit another info
                edt_name.setEnabled(false);
                edt_phone.setEnabled(false);
                edt_sex.setEnabled(false);
                edt_address.setEnabled(false);
                edt_birthday.setEnabled(false);

                lv_danhsachluong.setVisibility(View.VISIBLE);

                group_button_edit.setVisibility(View.GONE);

            }
        });

        img_add_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent= new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"title"),SELECT_IMAGE_CODE);
                }catch (Exception e) {
                    Intent intent = new Intent(ProfileUserActivity.this, ListRestaurant.class);
                    Log.i("Test","2");
                    intent.putExtra("Uid",user);
                    startActivity(intent);
                }
            }
        });

        btn_confirm_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Edit avatar
                try {
                    DatabaseReference refUserCheckSdt = database.getReference("user");
                    refUserCheckSdt.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!oldPhone.equals(edt_phone.getText().toString().trim())){
                                for(DataSnapshot postSnapShot: snapshot.getChildren()) {
                                    if(postSnapShot.child("dienThoai").getValue(String.class).equals(edt_phone.getText().toString().trim())){
                                        Toast.makeText(ProfileUserActivity.this,"Số điện thoại đã được đăng ký ở tài khoản khác",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }

                            String name = edt_name.getText().toString().trim();
                            String phone = edt_phone.getText().toString().trim();
                            String sex = edt_sex.getText().toString().trim();
                            String address = edt_address.getText().toString().trim();
                            String ngaySinh = edt_birthday.getText().toString().trim();
                            if(name.isEmpty()){
                                Toast.makeText(ProfileUserActivity.this,"Tên không được để trống",Toast.LENGTH_SHORT).show();
                            }
                            if(phone.isEmpty()){
                                Toast.makeText(ProfileUserActivity.this,"Điện thoại không được để trống",Toast.LENGTH_SHORT).show();

                            }
                            if(sex.isEmpty()){
                                Toast.makeText(ProfileUserActivity.this,"Giới tính không được để trống",Toast.LENGTH_SHORT).show();
                            }
                            if(address.isEmpty()){
                                Toast.makeText(ProfileUserActivity.this,"Địa chỉ không được để trống",Toast.LENGTH_SHORT).show();
                            }

                            refUser.child("hoTen").setValue(name);
                            refUser.child("dienThoai").setValue(phone);
                            refUser.child("gioiTinh").setValue(sex);
                            refUser.child("diaChi").setValue(address);
                            refUser.child("ngaySinh").setValue(ngaySinh);

                            // Cap nhat lai so dien thoai cho nhan vien trong restaurants
                            DatabaseReference refNhanVienInRes = database.getReference("restaurant");
                            ValueEventListener eventRefNhanVien = refNhanVienInRes.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot postSnapShot: snapshot.getChildren()) {
                                        if(postSnapShot.child("NhanVien") != null)
                                        {
                                            if(postSnapShot.child("NhanVien").hasChild(user))
                                            {
                                                refNhanVienInRes.child(postSnapShot.getKey()).child("NhanVien").child(user).child("Sdt").setValue(edt_phone.getText().toString());
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            if(uri!=null){
                                load_image_avt(uri);
                            }

                            // Edit another info
                            img_avatar.setVisibility(View.VISIBLE);

                            img_add_avatar.setVisibility(View.GONE);
                            edt_name.setEnabled(false);
                            edt_phone.setEnabled(false);
                            edt_sex.setEnabled(false);
                            edt_address.setEnabled(false);
                            edt_birthday.setEnabled(false);

                            lv_danhsachluong.setVisibility(View.VISIBLE);

                            group_button_edit.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }catch (Exception e){

                }

            }
        });

        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ProfileUserActivity.this);
                builder.setCancelable(false);
                builder.setMessage("Bạn có muốn đăng xuất ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        SessionManagement sessionManagement = new SessionManagement(ProfileUserActivity.this);
                        String id = sessionManagement.getSession();
                        removeToken(id);
                        sessionManagement.removeSession();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(ProfileUserActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        edt_birthday.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(!isEditing)
                    return;
                Calendar calender = Calendar.getInstance();
                int year = calender.get(calender.YEAR);
                int month = calender.get(calender.MONTH);
                int day = calender.get(calender.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileUserActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calender.set(i, i1, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String newDate = simpleDateFormat.format(calender.getTime());

                        edt_birthday.setText(newDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                uri = data.getData();
                if (uri != null) {
                    img_add_avatar.setImageURI(uri);
                } else {
                    Toast.makeText(this, "Thêm hình ảnh thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){

        }

    }

    private void load_image_avt(Uri uri) {
        if(uri == null)
            return;
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        refUser.child("avatar").setValue(uri.toString());
                        progress_bar.setVisibility(View.GONE);
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
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(ProfileUserActivity.this, "Upload image fail !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  String getFileExtension(Uri mUri){

        ContentResolver cr= getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    private void removeToken(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refUser = database.getReference("user/" + id);
        refUser.child("Token").setValue(null);
    }
}