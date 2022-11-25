package com.example.oderingfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
        name.setText("Bàn " + position);

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
                                intdn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                ((Activity)context).startActivity(intdn);
                                break;
                            case R.id.menu_xoabanan:
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
}
