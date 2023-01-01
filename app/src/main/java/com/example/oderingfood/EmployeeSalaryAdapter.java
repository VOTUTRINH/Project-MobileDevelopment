package com.example.oderingfood;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.models.Booking;
import com.example.oderingfood.models.EmployeeSalary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.Value;

import java.util.List;


public class EmployeeSalaryAdapter extends ArrayAdapter<EmployeeSalary> {
    Context context;
    List<EmployeeSalary> dataList;
    String idUser;

    FirebaseDatabase database;
    ValueEventListener thanhtoanEvent;
    DatabaseReference refNhanVien;

    public EmployeeSalaryAdapter(Context context,int resource, List<EmployeeSalary> dataList, String idUser){
        super(context,R.layout.item_nhanvien,dataList);
        this.context=context;
        this.dataList = dataList;
        this.idUser = idUser;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.item_nhanvien, null);
        TextView txttenCuaHang = (TextView) row.findViewById(R.id.tenNhaHang);
        TextView txtluongCoBan = (TextView) row.findViewById(R.id.luongCoBan);
        TextView txttongLuong = (TextView) row.findViewById(R.id.tongLuong);
        CardView itemLayout = (CardView) row.findViewById(R.id.item_layout);



        txttenCuaHang.setText("Tên nhà hàng: " + dataList.get(position).GetResName());
        txtluongCoBan.setText("Lương: " + dataList.get(position).GetBaseSalary() + "/h");
        Long tongLuong = Long.parseLong(dataList.get(position).GetBaseSalary()) * dataList.get(position).GetTimeWork() ;

        txttongLuong.setText("Tổng lương: " + tongLuong + " VNĐ");


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                refNhanVien = database.getReference("restaurant/" + dataList.get(position).GetIDRes() + "/NhanVien/" + idUser);

                    thanhtoanEvent = refNhanVien.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DatabaseReference refThanhToan;

                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.confirm_payment_layout);
                            Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                            Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);
                            TextView txt_tenCuaHang = (TextView) dialog.findViewById(R.id.txt_cuahang);
                            TextView txt_soTien = (TextView) dialog.findViewById(R.id.txt_sotien);

                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cancelPayment(dataList.get(position).GetIDRes());
                                    refNhanVien.removeEventListener(thanhtoanEvent);
                                    dialog.dismiss();
                                }
                            });

                            btn_accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    confirmPayment(dataList.get(position).GetIDRes());
                                    refNhanVien.removeEventListener(thanhtoanEvent);
                                    dialog.dismiss();
                                }
                            });

                            if(snapshot.hasChild("ThanhToan")){
                                boolean bossConfirm = snapshot.child("ThanhToan").child("ChuXacNhan").getValue(Boolean.class);
                                boolean nvXacNhan = snapshot.child("ThanhToan").child("NhanVienXacNhan").getValue(Boolean.class);
                                if(bossConfirm && !nvXacNhan){
                                    // Ten cua hang
                                    txt_tenCuaHang.setText(dataList.get(position).GetResName());
                                    // So tien thanh toan
                                    txt_soTien.setText("Số tiền: " + snapshot.child("ThanhToan").child("SoTienThanhToan").getValue(String.class));

                                    dialog.show();
                                }
                            }
                            else
                            {
                                refNhanVien.removeEventListener(thanhtoanEvent);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }
        });
        return (row);
    }

    public void cancelPayment(String idRes){
        DatabaseReference mDatabase;
        mDatabase = database.getReference("/restaurant/" + idRes +"/NhanVien/" + idUser);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("ThanhToan")){
                    mDatabase.child("ThanhToan").child("ChuXacNhan").setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void confirmPayment(String idRes){
        DatabaseReference mDatabase;
        mDatabase = database.getReference("/restaurant/" + idRes +"/NhanVien/" + idUser);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("ThanhToan")){
                    mDatabase.child("ThanhToan").child("NhanVienXacNhan").setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
