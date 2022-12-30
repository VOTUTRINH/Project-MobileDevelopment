package com.example.oderingfood;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.example.oderingfood.models.Employee;
import com.example.oderingfood.models.NotificationItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeManageActivity extends Fragment {
    RecyclerView listEmployees;
    RecyclerView listEmployeesWorking;

    TextView txtTotalEmployees;
    TextView txtTotalEmployeesWorking;

    Context context;

    TextInputEditText txtDateChosen;
    FloatingActionButton btnAddEmployee;
    TextView txtNoEmployee;
    TextView txtNoEmployeeWorking;
    Button btnViewListEmployees;
    Button btnViewListEmployeesWorking;

    List<Employee> employees = new ArrayList<Employee>();
    List<Employee> employeesWorking = new ArrayList<Employee>();

    // Adapter
    EmployeesManagerAdapter adapterListEmployees;
    EmployeesManagerAdapter adapterListEmployeesWorking;

    EmployeeManageActivity employeeManageActivity;

    Bottomnavigation bottomnavigation;
    String user;
    String idRestaurent;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static EmployeeManageActivity newInstance(String param1, String param2) {
        EmployeeManageActivity fragment = new EmployeeManageActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EmployeeManageActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        try {
            context = getActivity();

            employeeManageActivity = new EmployeeManageActivity();
        } catch (Exception e) {
        }

        bottomnavigation = (Bottomnavigation) getActivity();
        user = bottomnavigation.getUser();
        idRestaurent = bottomnavigation.getIdRes();

        adapterListEmployees = new EmployeesManagerAdapter(getActivity(), employees, idRestaurent);
        adapterListEmployeesWorking = new EmployeesManagerAdapter(getActivity(), employeesWorking, idRestaurent);


        // Get data list employee from firebase
        // Do Something
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefEmployee = database.getReference("restaurant/" + idRestaurent + "/NhanVien");
        dbRefEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employees.clear();
                employeesWorking.clear();
                // Duyet danh sach nhan vien
                for (DataSnapshot postSnapshotNhanVien : snapshot.getChildren()) {
                    String id = postSnapshotNhanVien.getKey();
                    String trangThai = postSnapshotNhanVien.child("TrangThai").getValue(String.class);

                    // Duyet trong danh sach User de lay thong tin (ID, AVATAR, NAME) cua nhan vien dua vao SDT
                    DatabaseReference dbRefUser = database.getReference("user");
                    dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String avt = snapshot.child(id).child("avatar").getValue(String.class);
                            String name = snapshot.child(id).child("hoTen").getValue(String.class);

                            Employee employee = new Employee(id, name, avt);
                            employees.add(employee);
                            if (trangThai.equals("DangLamViec"))
                                employeesWorking.add(employee);

                            if (employees.size() == 0) txtNoEmployee.setVisibility(View.VISIBLE);
                            else txtNoEmployee.setVisibility(View.GONE);
                            if (employeesWorking.size() == 0)
                                txtNoEmployeeWorking.setVisibility(View.VISIBLE);
                            else txtNoEmployeeWorking.setVisibility(View.GONE);

                            txtTotalEmployees.setText("Số lượng nhân viên: " + employees.size());
                            txtTotalEmployeesWorking.setText("Số nhân viên đang làm việc: " + employeesWorking.size());

                            adapterListEmployees.notifyDataSetChanged();
                            adapterListEmployeesWorking.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View employeeManagerFragment = inflater.inflate(R.layout.activity_employee_manage, container, false);

        txtDateChosen = (TextInputEditText) employeeManagerFragment.findViewById(R.id.txt_date_chosen);
        listEmployees = (RecyclerView) employeeManagerFragment.findViewById(R.id.list_employees);
        listEmployeesWorking = (RecyclerView) employeeManagerFragment.findViewById(R.id.employee_working);
        txtTotalEmployees = (TextView) employeeManagerFragment.findViewById(R.id.txt_total_employees);
        txtTotalEmployeesWorking = (TextView) employeeManagerFragment.findViewById(R.id.txt_total_employees_working);
        btnAddEmployee = (FloatingActionButton) employeeManagerFragment.findViewById(R.id.add_employee_floating_button);
        txtNoEmployee = (TextView) employeeManagerFragment.findViewById(R.id.txt_no_employee);
        txtNoEmployeeWorking = (TextView) employeeManagerFragment.findViewById(R.id.txt_no_employee_working);
        btnViewListEmployees = (Button) employeeManagerFragment.findViewById(R.id.btn_view_list_employees);
        btnViewListEmployeesWorking = (Button) employeeManagerFragment.findViewById(R.id.btn_list_employees_working);

        btnViewListEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListNhanVien.class);
                intent.putExtra("idRes", idRestaurent);
                startActivity(intent);
            }
        });

        btnViewListEmployeesWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListNhanVienDangLamViec.class);
                intent.putExtra("idRes", idRestaurent);
                startActivity(intent);
            }
        });

        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogAddEmployee();
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        txtDateChosen.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));
        txtDateChosen.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();
                int year = calender.get(calender.YEAR);
                int month = calender.get(calender.MONTH);
                int day = calender.get(calender.DATE);
                String previousDate = txtDateChosen.getText().toString();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calender.set(i, i1, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String newDate = simpleDateFormat.format(calender.getTime());

                        // Nếu ngày chọn mới khác với ngày cũ, thì thực hiện lấy danh sách nhân viên đang làm việc của ngày mới
                        if (!newDate.equals(previousDate)) {
                            newDate = newDate.replaceAll("/", "-");
                            getListEmployeesAreWorkingWithDate(newDate);
                        }

                        txtDateChosen.setText(newDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        listEmployees.setLayoutManager(layoutManager1);
        listEmployees.setItemAnimator(new DefaultItemAnimator());
        listEmployees.setAdapter(adapterListEmployees);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listEmployeesWorking.setLayoutManager(layoutManager2);
        listEmployeesWorking.setItemAnimator(new DefaultItemAnimator());
        listEmployeesWorking.setAdapter(adapterListEmployeesWorking);

        return employeeManagerFragment;
    }

    private void getListEmployeesAreWorkingWithDate(String date) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseNhanVien = database.getReference("/restaurant/" + idRestaurent + "/NhanVien");

        mDatabaseNhanVien.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                employeesWorking.clear();
                adapterListEmployeesWorking.notifyDataSetChanged();
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    if (postSnapShot.child("LamViec") == null) continue;

                    if (postSnapShot.child("LamViec").hasChild(date)) {
                        String id = postSnapShot.getKey();

                        DatabaseReference dbRefUser = database.getReference("user");
                        dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String avt = snapshot.child(id).child("avatar").getValue(String.class);
                                String name = snapshot.child(id).child("hoTen").getValue(String.class);

                                Employee employee = new Employee(id, name, avt);
                                employeesWorking.add(employee);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                if (employeesWorking.size() == 0)
                    txtNoEmployeeWorking.setVisibility(View.VISIBLE);
                else txtNoEmployeeWorking.setVisibility(View.GONE);

                adapterListEmployeesWorking.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Show dialog to add table
    private void ShowDialogAddEmployee() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_employee_layout);
        dialog.show();

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);
        TextInputEditText edtEmployeeName = (TextInputEditText) dialog.findViewById(R.id.edt_add_table);
        TextInputEditText edtLuong = (TextInputEditText) dialog.findViewById(R.id.edt_luong_nhanvien);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strSdt = String.valueOf(edtEmployeeName.getText()).trim();
                String strLuong = String.valueOf(edtLuong.getText()).trim();
                if (strSdt.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                } else if (strLuong.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập lương ban đầu cho nhân viên", Toast.LENGTH_SHORT).show();
                } else {
                    Long sdt = Long.parseLong(strSdt);
                    Long luong = Long.parseLong(strLuong);

                    if (luong <= 0) {
                        Toast.makeText(context, "Lương phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    }

                    AddEmployeeToRestaurant(strSdt, strLuong);
                    dialog.dismiss();
                }
            }
        });
    }

    private void AddEmployeeToRestaurant(String employeeSdt, String luong) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference mDatabaseNhanVien = database.getReference("/restaurant/" + idRestaurent + "/NhanVien");
        mDatabaseNhanVien.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    if (postSnapShot.child("Sdt").getValue(String.class).equals(employeeSdt)) {
                        Toast.makeText(getActivity(), "Nhân viên đã tồn tại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                DatabaseReference dbRefUser = database.getReference("user");
                dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    boolean canAdd = false;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String idUser = "";
                        for (DataSnapshot postSnapshotUser : snapshot.getChildren()) {
                            if (postSnapshotUser.child("dienThoai").getValue(String.class).equals(employeeSdt)) {
                                idUser = postSnapshotUser.getKey();
                                canAdd = true;
                            }
                        }
                        if (canAdd == true) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("Sdt", employeeSdt);
                            map.put("Luong", luong);
                            map.put("ThoiGianLamViec", 0);
                            map.put("TrangThai", "KhongLamViec");

                            mDatabaseNhanVien.child(idUser).setValue(map);



                            //------------notify
                            database.getReference("user/" + idUser + "/hoTen").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String str = snapshot.getValue(String.class).toString();
                                    String label = "<b> Thêm nhân viên <b>";
                                    String content = "Chủ quán vừa thêm nhân viên:" + "<b>" + str + "</b> ";
                                    String img = "abcd";
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
                                    String currentDate = format.format(calendar.getTime());


                                    String _id = database.getReference("restaurant/" + idRestaurent).child("notification").push().getKey().toString();
                                    NotificationItem notificationItem = new NotificationItem(_id,img, label, content, currentDate);

                                    database.getReference("user/" + user + "/avatar").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String avt = snapshot.getValue(String.class).toString();
                                            notificationItem.setNoticeImg(avt);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    database.getReference("restaurant/" + idRestaurent).child("notification").child(_id).setValue(notificationItem);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            //--------
                            Toast.makeText(context, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, "Số điện thoại chưa được đăng kí tài khoản", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}

