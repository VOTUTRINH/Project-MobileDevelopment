package com.example.oderingfood;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.Employee;
import com.example.oderingfood.models.Table;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListEmployeesAdapter extends ArrayAdapter<Employee> {
    private  Context context;
    List<Employee> employeeList;
    String idRes = "";
    String typeList = "";

    public ListEmployeesAdapter(@NonNull Context context, int resource, List<Employee> employeeList, String idRes, String typeList) {
        super(context, resource, employeeList);
        this.context = context;
        this.employeeList = employeeList;
        this.idRes = idRes;
        this.typeList = typeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View item = inflater.inflate(R.layout.custom_item_nhanvien_type2, null);
        item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(typeList.equals("ListNV"))
                {
                    ShowDialogDeleteEmployee(employeeList.get(position).getId(), employeeList.get(position).getName());
                    return true;
                }
                return false;
            }
        });

        ImageView avt = (ImageView) item.findViewById(R.id.employee_avatar_type2);
        TextView name = (TextView) item.findViewById(R.id.employee_name_type2);

        Employee employee = employeeList.get(position);

        Glide.with(context).load(employee.getAvatar()).into(avt);
        name.setText(employee.getName());

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (typeList){
                    case "ListNV":
                        Intent intent1 = new Intent(view.getContext(),a2g18Activity.class);
                        intent1.putExtra("idUser",employee.getId());
                        view.getContext().startActivity(intent1);
                        break;

                    case "ListNVLamViec":
                        String dateChosen = ((ListNhanVienDangLamViec) context).GetDate();
                        Intent intent2 = new Intent(view.getContext(), DetailWorkTimeActivity.class);
                        intent2.putExtra("idUser", employee.getId());
                        intent2.putExtra("idRes", idRes);
                        intent2.putExtra("dateChosen", dateChosen);
                        view.getContext().startActivity(intent2);
                        break;

                    default:
                        break;
                }
            }
        });
        return (item);
    }

    // Show dialog to add table
    private void ShowDialogDeleteEmployee(String id, String name)
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.confirm_delete_table);
        dialog.show();

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);
        TextView txt_confirm = (TextView) dialog.findViewById(R.id.txt_confirm);

        txt_confirm.setText("Xóa nhân viên " + name + "?");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiem tra neu dang no luong thi khong duoc xoa
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRefEmployee = database.getReference("restaurant/" + idRes + "/NhanVien/" + id);
                dbRefEmployee.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Long timeWorked = snapshot.child("ThoiGianLamViec").getValue(Long.class);
                        String trangThai = snapshot.child("TrangThai").getValue(String.class);
                        if(timeWorked > 0 || trangThai.equals("DangLamViec"))
                        {
                            Toast.makeText(context, "Thanh toán tiền lương trước khi xóa", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dbRefEmployee.setValue(null);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dialog.dismiss();
            }
        });
    }
}
