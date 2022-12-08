package com.example.oderingfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.Booking;
import com.example.oderingfood.models.DetailEmployee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class a2g18Activity extends Activity {
    Activity activity;
    Context context;
    private Button buttonOpenDialog;
    private TextView txtName;
    private TextView txtID;
    private TextView txtPhone;
    private TextView txtEmail;
    private TextView txtAddress;
    private TextView txtSalary;
    private CircleImageView txtAvatar;
    private TextView txtDebt;
    private EditText editTextFullName;
    private EditText editTextID;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextAddress;
    private EditText editTextSalary;
    String idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2g18activity_main);
//
//        context = getBaseContext();
        Intent intent = getIntent();
        idUser = intent.getStringExtra("idUser");
        txtName = this.findViewById(R.id.afo_txtName);
        txtID = this.findViewById(R.id.afo_txtId);
        txtPhone = this.findViewById(R.id.afo_txtPhone);
        txtEmail = this.findViewById(R.id.afo_txtEmail);
        txtAddress = this.findViewById(R.id.afo_txtAddress);
        txtSalary = this.findViewById(R.id.afo_txtSalary);
        txtAvatar = this.findViewById(R.id.afo_avatar);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;

        mDatabase = database.getReference("/user/" + idUser);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Get data
                String name = snapshot.child("HoTen").getValue(String.class);
                String id = snapshot.child("id").getValue(String.class);
                String phone = String.valueOf(snapshot.child("Sdt").getValue(Long.class));
                String email = snapshot.child("GioiTinh").getValue(String.class);
                String address = snapshot.child("DiaChi").getValue(String.class);
                String salary = snapshot.child("NgaySinh").getValue(String.class);
                String urlImage;
                try {
                    urlImage = snapshot.child("Avatar").getValue(String.class).toString();
                }catch (Exception e){
                    urlImage = "https://firebasestorage.googleapis.com/v0/b/orderingfood-ab91f.appspot.com/o/store_default.png?alt=media&token=de6a404a-dd66-4a21-b6ae-eda751d79983";
                }
                Glide.with(getApplicationContext()).load(urlImage).into(txtAvatar);

                txtName.setText("Họ và tên: " + name);
                txtID.setText("ID: " + id);
                txtPhone.setText("Số điện thoại: " + phone);
                txtEmail.setText("Giới tính: " + email);
                txtAddress.setText("Địa chỉ: " + address);
                txtSalary.setText("Ngày sinh: " + salary);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        this.buttonOpenDialog = (Button) this.findViewById(R.id.afo_txtEdit);

        this.buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOpenDialogClicked();
            }
        });
    }

    private void buttonOpenDialogClicked() {

//        TextView name = this.findViewById(R.id.afo_txtName);
//        TextView id = this.findViewById(R.id.afo_txtId);
//        TextView phone1 = this.findViewById(R.id.afo_txtPhone);
//        TextView email1 = this.findViewById(R.id.afo_txtEmail);
//        TextView address1 = this.findViewById(R.id.afo_txtAddress);
//        TextView salary1 = this.findViewById(R.id.afo_txtSalary);
//        TextView debt1 = this.findViewById(R.id.afo_txtDebt);
        CustomDialog.EmployeeListener listener = new CustomDialog.EmployeeListener() {
            @Override
            public void fullNameEntered(String fullName) {
                txtName.setText("Họ và tên: " + fullName);
            }

//            public void IDEntered(String ID) {
//                txtID.setText("ID: " + ID);
//            }

            public void phoneEntered(String phone) {
                txtPhone.setText("Số điện thoại: " + phone);
            }

            public void emailEntered(String email) {
                txtEmail.setText("Giới tính: " + email);
            }

            public void addressEntered(String address) {
                txtAddress.setText("Địa chỉ: " + address);
            }

            public void salaryEntered(String salary) {
                txtSalary.setText("Ngày sinh: " + salary);
            }

//            public void debtEntered(String debt) {
//                debt1.setText("Tổng tiền lương còn nợ: " + debt);
//            }
        };
        final CustomDialog dialog = new CustomDialog(this, listener, idUser);

        dialog.show();
    }
}
