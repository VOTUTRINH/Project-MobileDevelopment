package com.example.oderingfood;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.models.Booking;
import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;
import com.example.oderingfood.models.NotificationItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder>

{
    List<Booking> dataList;
    String role;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String idRes;
    String user;


    public TableAdapter(Context ct,List<Booking> dataList, String r, String idRes, String user){
        this.role = r;
        this.dataList = dataList;
        this.context = ct;
        this.idRes = idRes;
        this.user = user;
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @NonNull
    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table, parent, false);

        // Passing view to ViewHolder
        TableAdapter.ViewHolder viewHolder = new TableAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        setPosition(position);
        holder.name.setText("Tên: " + dataList.get(position).getName());
        holder.from.setText(dataList.get(position).getTimeStart());
        holder.to.setText(dataList.get(position).getTimeEnd());
        holder.table.setText("Bàn: " + dataList.get(position).getTableBook().first);
        holder.date.setText("Ngày: " + dataList.get(position).getDate());
        holder.phone.setText("SĐT: " + dataList.get(position).getPhone());
        if(dataList.get(position).isConfirm()){
            holder.confirm.setText("Trạng thái: Đã nhận đặt bàn");
            holder.confirm.setTextColor(Color.parseColor("#04b711"));
            holder.btnConfirm.setText("Đã duyệt");
            holder.btnConfirm.setBackgroundColor(Color.parseColor("#af0076"));
            holder.btnConfirm.setClickable(false);

        }
        else{
            holder.confirm.setText("Trạng thái: Chờ xác nhận.");
            holder.confirm.setTextColor(Color.parseColor("#ff0000"));

        }
        List<Food> foods = dataList.get(position).getTableBook().second;
        String foodLabel = "Món: ";
        for(int i =0 ; i< foods.size(); i++){
            if (i==0) {
                foodLabel = foodLabel + foods.get(i).getName()+ " x" +foods.get(i).getQuantity();

            }
            else { foodLabel = foodLabel + ", "+ foods.get(i).getName() + " x" +foods.get(i).getQuantity();}
        }
        holder.txtfood.setText(foodLabel);
        holder.calender.setImageResource(R.drawable.calender);
        holder.location.setImageResource(R.drawable.location);
        holder.user.setImageResource(R.drawable.user);
        if(role.equals("KhachHang")){
            holder.btnConfirm.setVisibility(View.GONE);
        }

        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pathR = "/restaurant/" + GlobalVariables.pathRestaurentID;
                String pathU = "/user/" + dataList.get(position).getIdUser();
                String table = dataList.get(position).getTableBook().first;
                String date = dataList.get(position).getDate();
                String id = dataList.get(position).getId();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference resDatabase = database.getReference().child(pathR);

                DatabaseReference tableDatabase = resDatabase.child("BanAn").child(table).child("Bookings");
                Map<String, String> temp = new HashMap<>();
                temp.put("timeS", dataList.get(position).getTimeStart());
                temp.put("timeE", dataList.get(position).getTimeEnd());
                temp.put("id", id);
                temp.put("idUser", dataList.get(position).getIdUser());

                tableDatabase.child(date).child(id).setValue(temp);


                Map<String, Food> foodOrder = new HashMap<>();
                List<Food> tempFoods = dataList.get(position).getTableBook().second;
                for(int i=0; i< tempFoods.size(); i++){
                    foodOrder.put(tempFoods.get(i).getId(), tempFoods.get(i));
                }
                tableDatabase.child(date).child(id).child("orders").setValue(tempFoods);

                DatabaseReference bookingResDatabase = resDatabase.child("Bookings").child(date).child(id);
                bookingResDatabase.child("isConfirmed").setValue(true);
                DatabaseReference bookingUserDatabase = database.getReference().child(pathU).child("Bookings").child(GlobalVariables.pathRestaurentID).child(date).child(id);
                bookingUserDatabase.child("isConfirmed").setValue(true);
                holder.btnConfirm.setText("Đã duyệt");
                holder.btnConfirm.setBackgroundColor(Color.parseColor("#af0076"));
                holder.btnConfirm.setClickable(false);
                // thong bao chu quan da duyet booking abc ...

                //todo
                FirebaseDatabase noti = FirebaseDatabase.getInstance();


                noti.getReference("restaurant/" + idRes ).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String owner = snapshot.child("ChuQuan").getValue(String.class).toString();
                        noti.getReference("user/"+owner).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String avt = snapshot.child("avatar").getValue(String.class).toString();
                                String ad = snapshot.child("hoTen").getValue(String.class).toString();
                                String label = "<b> Xác nhận đặt bàn <b>";
                                String content = "Chủ quán đã xác nhận đơn đặt bàn "+table;
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



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });

                //
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogConfirm(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, from, to, table, date, phone, confirm, txtfood;
        ImageView calender, location, user;
        Button btnConfirm, btnCancel;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txtName);
            from = (TextView) view.findViewById(R.id.txtFrom);
            to = (TextView) view.findViewById(R.id.txtTo);
            table = (TextView) view.findViewById(R.id.txtTable);
            date = (TextView) view.findViewById(R.id.txtDate);
            phone = (TextView) view.findViewById(R.id.txtPhone);
            confirm = (TextView) view.findViewById(R.id.txt_confirm);
            txtfood = (TextView) view.findViewById(R.id.txt_food);

            calender = (ImageView) view.findViewById(R.id.imgCalender);
            location = (ImageView) view.findViewById(R.id.imgLocation);
            user = (ImageView) view.findViewById(R.id.imgUser);
            btnConfirm = (Button) view.findViewById(R.id.booking_btn_confirm);
            btnCancel = (Button) view.findViewById(R.id.booking_btn_cancel);


        }


    }

    public void ShowDialogConfirm(int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.confirm_delete_booking);
        dialog.show();

        TextView txt_confirm = (TextView) dialog.findViewById(R.id.txt_confirm);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);
        EditText reason = (EditText) dialog.findViewById(R.id.booking_txt_reason_cancel);


        if (dataList.get(position).isConfirm()){
            txt_confirm.setText("Đơn đặt này đã được nhà hàng xác nhận, bạn có muốn hủy đơn hàng.");
        }
        else{
            txt_confirm.setVisibility(View.GONE);
        }
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String re = reason.getText().toString();
                if(re.matches("") ){
                    Toast.makeText(TableAdapter.this.getContext(), "Không để trống lý do.", Toast.LENGTH_SHORT).show();
                }
                else {
                    deleteBooking(position, re);
                    dialog.dismiss();
                }
            }
        });

    }
    public void deleteBooking(int pos, String reason){
        String pathR = "/restaurant/" + idRes;
        // bien user la nguoi thuc hien xoa, co the la khach hang hoac chu
        String table = dataList.get(pos).getTableBook().first;
        String date = dataList.get(pos).getDate();
        String ID = dataList.get(pos).getId();
        String idUser = dataList.get(pos).getIdUser();
        DatabaseReference res = database.getReference(pathR);
        String pathU ="/user/" + idUser;

        DatabaseReference bookingDatabase = database.getReference(pathR + "/Bookings");
        bookingDatabase.child(date).child(ID).setValue(null);
        DatabaseReference tableDatabase = database.getReference(pathR + "/BanAn");
        tableDatabase.child(table).child("Bookings").child(date).child(ID).setValue(null);
        DatabaseReference userBookingDatabase = database.getReference(pathU + "Bookings");
        userBookingDatabase.child(idRes).child(date).child(ID).setValue(null);
        // thong bao ly do. User (hoac chu quan) da huy booking vi ly do .....
        //todo
        FirebaseDatabase noti = FirebaseDatabase.getInstance();


        noti.getReference("user/" + user ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String avt = snapshot.child("avatar").getValue(String.class).toString();
                String ad = snapshot.child("hoTen").getValue(String.class).toString();
                String label = "<b> Hủy đặt bàn  <b>";
                String content = ad +" đã hủy đặt bàn vì lý do:  "+ reason;
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
        //


    }

    public Context getContext() {
        return context;
    }
}
