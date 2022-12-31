package com.example.oderingfood;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.oderingfood.models.Booking;
import com.example.oderingfood.models.GlobalVariables;
import com.example.oderingfood.models.NotificationItem;
import com.example.oderingfood.models.Table;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ListTablesAdapter extends ArrayAdapter<Table> {
    private  Context context;
    private List<Table> listTables;

    String user;
    String idRes;

    public ListTablesAdapter(Context context, int resource, List<Table> listTables) {
        super(context,R.layout.table_layout_item,listTables);
        this.context = context;
        this.listTables = listTables;


        user= GlobalVariables.IDUser;
        idRes = GlobalVariables.pathRestaurentID;
    }

    public void setItem(List<Table> ltb){
        this.listTables.clear();
        this.listTables.addAll(ltb);
        this.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.table_layout_item, null);
        TextView name = (TextView) row.findViewById(R.id.table_item_text);
        name.setText("Bàn " + listTables.get(position).getName());
        LinearLayout table = row.findViewById(R.id.table_linear_layout);

        TextView txtState = (TextView) row.findViewById(R.id.table_state);
        Table tb = listTables.get(position);
        String state;
        Booking booking;
        if(tb.getState().equals("IsUsing")){
            state = "Đang sử dụng";
        }
        else if (tb.getState().equals("IsWaiting")){
            state = "Đang đợi món ăn, độ ưu tiên: " +  Integer.toString(tb.getPriority());
        }
        else{
            state = "Còn trống";
        }
        if(listTables.get(position).getIsbooked()){
            booking = listTables.get(position).getBooking();

            state = "Đã đặt từ "+booking.getTimeStart()+" đến " + booking.getTimeEnd();
        }

        txtState.setText(state);
        if(tb.getState().equals("IsUsing")) {
            table.setBackgroundResource(R.drawable.table_using_not_wait_bg);
        }
        else if(tb.getState().equals("IsWaiting")){
            table.setBackgroundResource(R.drawable.table_wait_food_bg);
        }
        else if(tb.getIsbooked()){
            table.setBackgroundResource(R.drawable.table_booked);
        }

        TextView btnMoreAction = (TextView) row.findViewById(R.id.btn_moremenu);

        btnMoreAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu((Activity)context,btnMoreAction);
                popupMenu.getMenuInflater().inflate(R.menu.menu_table_action, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override

                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.menu_thanhtoan:
                                if(listTables.get(position).getState().equals("Empty")){
                                    Toast.makeText(context,"Bàn ăn trống", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Intent intdn = new Intent(context, A2G7Activity.class); // Your nxt activity name instead of List_Activity

                                    Bundle b = new Bundle();
                                    b.putString("key", listTables.get(position).getName()); //Your id
                                    intdn.putExtras(b); //Put your id to your next Intent
                                    context.startActivity(intdn);
                                }
                                break;
                            case R.id.menu_xoabanan:
                                ShowDialogAddTable(listTables.get(position).getName());
                                break;
                            default:

                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        return (row);
    }

    private void ShowDialogAddTable(String name)
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.confirm_delete_table);
        dialog.show();

        TextView txt_confirm = (TextView) dialog.findViewById(R.id.txt_confirm);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);

        txt_confirm.setText("Bạn muốn xóa bàn " + name);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mDatabase;

                mDatabase = database.getReference("/restaurant/"+idRes+"/BanAn");

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapShot: snapshot.getChildren())
                        {
                            if(postSnapShot.getKey().equals(name)) {
                                if (postSnapShot.child("TrangThai").getValue(String.class).equals("IsUsing")) {
                                    Toast.makeText(context, "Không thể xóa bàn đang hoạt động", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                mDatabase.child(name).removeValue();

                                //------------notify

                                database.getReference("user/" + user ).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        String avt = snapshot.child("avatar").getValue(String.class).toString();
                                        String ad = snapshot.child("hoTen").getValue(String.class).toString();
                                        String label = "<b> Xoá bàn <b>";
                                        String content = ad + " vừa xóa bàn "+ name;
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
                                        String currentDate = format.format(calendar.getTime());

                                        String _id =database.getReference("restaurant/" + idRes).child("notification").push().getKey().toString();
                                        NotificationItem notificationItem = new NotificationItem(_id,avt, label, content, currentDate);

                                        database.getReference("restaurant/" + idRes).child("notification").child(_id).setValue(notificationItem);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {


                                    }
                                });



                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                dialog.dismiss();
            }
        });

    }

    public void updateAdapter(List<Table> mDataList) {
        this.listTables.clear();

        this.listTables.addAll(mDataList);

        notifyDataSetChanged();
    }
}
