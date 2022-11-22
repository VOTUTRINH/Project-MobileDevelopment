package com.example.oderingfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

public class ListTablesAdapter extends ArrayAdapter<String> {
    private  Context context;
    private  String[] listTables;


    public ListTablesAdapter(Context context, int resource, String[] listTables) {
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
        name.setText("Bàn " + listTables[position]);

        TextView btnMoreAction = (TextView) row.findViewById(R.id.btn_moremenu);

        btnMoreAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu((Activity)context,btnMoreAction);
                popupMenu.getMenuInflater().inflate(R.menu.menu_table_action, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_thanhtoan:
                                //handle menu1 click
                                return true;
                            case R.id.menu_xoabanan:
                                //handle menu2 click
                                Intent intent = new Intent(context, A2G7Activity.class);
                                context.startActivity(intent);
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        return (row);
    }
}
