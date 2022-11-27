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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.oderingfood.models.Table;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListTablesAdapter extends ArrayAdapter<Table> {
    private  Context context;
    private List<Table> listTables;


    public ListTablesAdapter(Context context, int resource, List<Table> listTables) {
        super(context,R.layout.table_layout_item,listTables);
        this.context = context;
        this.listTables = listTables;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.table_layout_item, null);
        TextView name = (TextView) row.findViewById(R.id.table_item_text);
        name.setText("Bàn " + listTables.get(position).getName());

        TextView txtState = (TextView) row.findViewById(R.id.table_state);
        String state = (listTables.get(position).getState().equals("IsUsing")?"Đang sử dụng":"Còn trống");
        txtState.setText(state);

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

                                Intent intdn = new Intent((Activity)context,A2G7Activity.class); // Your nxt activity name instead of List_Activity
                                Bundle bundle = new Bundle();
//                                intdn.putParcelableArrayListExtra("order",listTables.get(position).getMenu());

                                intdn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                ((Activity)context).startActivity(intdn);
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

                mDatabase = database.getReference("/restaurant/xzxHmkiUMHVjqNu67Ewzsv2TQjr2/BanAn");

                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapShot: snapshot.getChildren())
                        {
                            if(postSnapShot.getKey().equals(name))
                            {
                                if(postSnapShot.child("TrangThai").getValue(String.class).equals("IsUsing"))
                                {
                                    Toast.makeText(context,"Không thể xóa bàn đang hoạt động",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                mDatabase.child(name).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                mDatabase.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Toast.makeText(context, (int) snapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();
//                        for(DataSnapshot snapshotChild : snapshot.getChildren())
//                        {
//                            Toast.makeText(context, snapshotChild.getKey(),Toast.LENGTH_SHORT).show();
//
////                            if(snapshotChild.getKey().equals(name))
////                            {
////                                String state = snapshot.child("TrangThai").getValue(String.class);
////                                if(state.equals("IsUsing"))
////                                {
////                                    Toast.makeText(context, "Bàn ăn đang được sử dụng, không thể xóa", Toast.LENGTH_SHORT).show();
////                                    return;
////                                }
////                                snapshotChild.getRef().removeValue();
////                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                dialog.dismiss();
            }
        });

    }

}
