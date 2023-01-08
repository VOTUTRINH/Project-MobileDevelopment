package com.example.oderingfood;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.GlobalVariables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class a2g18Activity extends Activity {
    private Button buttonOpenDialog;
    private TextView txtName;
    private TextView txtID;
    private TextView txtPhone;
    private TextView txtEmail;
    private TextView txtAddress;
    private TextView txtSalary;
    private TextView txtSalaryPerHour;
    private TextView txtTotalSalary;
    private CircleImageView txtAvatar;
    private TextView txtSotienThanhtoan;
    private Button btnThanhToan;
    TextView txt_countdowntime;
    String idUser;
    String idRes;

    int maxTimeWaiting = 60;
    int countDownTime;

    Long soTienThanhToan = Long.valueOf(0);
    FirebaseDatabase database;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2g18activity_main);
//
//        context = getBaseContext();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            idUser = intent.getStringExtra("idUser");
            idRes = intent.getStringExtra("idRes");
        }

        txtName = this.findViewById(R.id.afo_txtName);
        txtPhone = this.findViewById(R.id.afo_txtPhone);
        txtEmail = this.findViewById(R.id.afo_txtEmail);
        txtAddress = this.findViewById(R.id.afo_txtAddress);
        txtSalary = this.findViewById(R.id.afo_txtSalary);
        txtAvatar = this.findViewById(R.id.afo_avatar);
        txtSalaryPerHour = this.findViewById(R.id.afo_txtSalaryPerHour);
        txtTotalSalary = this.findViewById(R.id.afo_txtTotalSalary);
        btnThanhToan = this.findViewById(R.id.afo_btnThanhToan);

        database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;
        DatabaseReference mDatabaseUser;
        mDatabaseUser = database.getReference("/user/" + idUser);
        mDatabase = database.getReference("/restaurant/" + idRes +"/NhanVien/" + idUser);

        mDatabase.child("ThanhToan").setValue(null);
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soTienThanhToan <=0){
                    ToastWithMessage("Không nợ tiền nhân viên");
                    return;
                }

                String label = GlobalVariables.TenNhaHang+" - "+ "Thanh toán lương";
                String content = "Chủ quán đã gửi yêu cầu thanh toán lương cho bạn. Bạn có 1 phút để xác nhân.";
                GlobalVariables.SendNotificationToOther(a2g18Activity.this, idUser, label, content);

                Map<String, Object> objThanhToan = new HashMap<String, Object>();
                objThanhToan.put("ChuXacNhan", true);
                objThanhToan.put("NhanVienXacNhan", false);
                objThanhToan.put("SoTienThanhToan",String.valueOf(soTienThanhToan));

                mDatabase.child("ThanhToan").setValue(objThanhToan);

                ShowDialogWaitEmployeeConfirm(soTienThanhToan);
            }
        });



        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Get data
                String name = snapshot.child("hoTen").getValue(String.class);
                String id = snapshot.child("id").getValue(String.class);
                String phone = snapshot.child("dienThoai").getValue(String.class);
                String email = snapshot.child("gioiTinh").getValue(String.class);
                String address = snapshot.child("diaChi").getValue(String.class);
                String salary = snapshot.child("ngaySinh").getValue(String.class);

                String urlImage;
                try {
                    urlImage = snapshot.child("avatar").getValue(String.class).toString();
                }catch (Exception e){
                    urlImage = "https://firebasestorage.googleapis.com/v0/b/orderingfood-ab91f.appspot.com/o/store_default.png?alt=media&token=de6a404a-dd66-4a21-b6ae-eda751d79983";
                }
                Glide.with(getApplicationContext()).load(urlImage).into(txtAvatar);

                txtName.setText(name);
                txtPhone.setText(phone);
                txtEmail.setText(email);
                txtAddress.setText(address);
                txtSalary.setText(salary);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get data
                String salaryPerHour = snapshot.child("Luong").getValue(String.class);
                Long tgLamViec = snapshot.child("ThoiGianLamViec").getValue(Long.class);
                Long totalSalary = tgLamViec * Long.parseLong(salaryPerHour);

                soTienThanhToan = totalSalary;
                txtSalaryPerHour.setText(salaryPerHour + " VNĐ/h");
                txtTotalSalary.setText(String.valueOf(totalSalary) + " VNĐ");
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

    // Show dialog to add table
    private void ShowDialogWaitEmployeeConfirm(Long sotien)
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.cho_nhanvien_xacnhan_layout);
        dialog.show();

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        txt_countdowntime = (TextView) dialog.findViewById(R.id.txt_countdowntime);
        txtSotienThanhtoan = (TextView) dialog.findViewById(R.id.sotien);
        txtSotienThanhtoan.setText("Số tiền: " + String.valueOf(sotien) + " VNĐ");

        DatabaseReference mDatabase;
        mDatabase = database.getReference("/restaurant/" + idRes +"/NhanVien/" + idUser);
        new Thread(new Runnable() {
            public void run() {
                try {
                    countDownTime = maxTimeWaiting;
                    ValueEventListener thanhtoanEvent;
                    thanhtoanEvent = mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild("ThanhToan")){
                                boolean chuXacNhan = snapshot.child("ThanhToan").child("ChuXacNhan").getValue(Boolean.class);
                                boolean nvXacNhan = snapshot.child("ThanhToan").child("NhanVienXacNhan").getValue(Boolean.class);
                                if(!chuXacNhan){
                                    ToastWithMessage("Nhân viên không chấp nhận thanh toán");
                                    countDownTime = 0;
                                    return;
                                }
                                else if(nvXacNhan){
                                    // Thuc hien tru tien
                                    subtractTotalSalary();
                                    countDownTime = 0;
                                    return;
                                }
                            }
                            else {
                                countDownTime = 0;
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    while (countDownTime > 0) {
                        updateUI();
                        countDownTime -= 1;
                        Thread.sleep(1000);
                    }

                    cancelPayment();
                    dialog.dismiss();
                    return;
                }catch (InterruptedException e)
                {
                }
            }
        }).start();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelPayment();
                dialog.dismiss();
            }
        });
    }

    void ToastWithMessage(String msg){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(a2g18Activity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void buttonOpenDialogClicked() {

        CustomDialog.EmployeeListener listener = new CustomDialog.EmployeeListener() {
            @Override
            public void salaryEntered(String salary) {
                txtSalaryPerHour.setText(salary);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mDatabase;
                mDatabase = database.getReference("/restaurant/" + idRes +"/NhanVien/" + idUser);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        mDatabase.child("Luong").setValue(String.valueOf(salary));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        };
        final CustomDialog dialog = new CustomDialog(this, listener, idUser, idRes);

        dialog.show();
    }

    public void updateUI(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                txt_countdowntime.setText(String.valueOf(countDownTime));
            }
        });
    }

    public void cancelPayment(){
        DatabaseReference mDatabase;
        mDatabase = database.getReference("/restaurant/" + idRes +"/NhanVien/" + idUser);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mDatabase.child("ThanhToan").setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void subtractTotalSalary(){
        DatabaseReference refNhanVien;
        refNhanVien = database.getReference("/restaurant/" + idRes +"/NhanVien/" + idUser);
        refNhanVien.child("ThoiGianLamViec").setValue(0);
        ToastWithMessage("Thanh toán thành công");
    };
    @Override
    protected void onStop() {
        super.onStop();
        cancelPayment();
    }
}
