package com.example.oderingfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.provider.ContactsContract;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.models.Booking;
import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder>

{
    List<Booking> dataList;
    String role;


    public TableAdapter(List<Booking> dataList, String r){
        role = r;
        this.dataList = dataList;
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
    public void onBindViewHolder(@NonNull TableAdapter.ViewHolder holder, int position) {
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

        }
        else{
            holder.confirm.setText("Trạng thái: Chờ xác nhận.");
            holder.confirm.setTextColor(Color.parseColor("#ff0000"));

        }
        List<Food> foods = dataList.get(position).getTableBook().second;
        String foodLabel = "Món: ";
        for(int i =0 ; i< foods.size(); i++){
            if (i==0) {
                foodLabel = foodLabel + foods.get(i).getName();

            }
            else { foodLabel = foodLabel + ", "+ foods.get(i).getName();}
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
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getPosition());
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView name,from,to,table,date ,phone, confirm, txtfood;
        ImageView calender,location,user;
        Button btnConfirm;
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
            view.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), R.id.menu_edit_item, 0, "Sửa/ Thêm");
            menu.add(getAdapterPosition(), R.id.menu_delete_item, 1, "Xóa");

        }


    }
}
