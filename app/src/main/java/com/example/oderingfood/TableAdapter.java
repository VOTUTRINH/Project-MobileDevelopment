package com.example.oderingfood;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TableAdapter extends ArrayAdapter<String>

{
    Context context;
    String[] froms;
    String[] tos;
    String[] names;
    String[] tables;
    String[] dates;
    Integer[] calenders;
    Integer[] locations;
    Integer[] users;
    public TableAdapter(Context context, int layout_custom, String[] froms, String[] tos, String[] names,String[] tables, String[] dates, Integer[] calenders, Integer[] locations, Integer[] users){
        super(context,R.layout.item_table,names);
        this.context=context;
        this.froms = froms;
        this.names=names;
        this.tos=tos;
        this.tables = tables;
        this.dates = dates;
        this.calenders = calenders;
        this.locations = locations;
        this.users = users;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.item_table, null);
        TextView name = (TextView) row.findViewById(R.id.txtName);
        TextView from = (TextView) row.findViewById(R.id.txtFrom);
        TextView to = (TextView) row.findViewById(R.id.txtTo);
        TextView table = (TextView) row.findViewById(R.id.txtTable);
        TextView date = (TextView) row.findViewById(R.id.txtDate);
        ImageView calender = (ImageView) row.findViewById(R.id.imgCalender);
        ImageView location = (ImageView) row.findViewById(R.id.imgLocation);
        ImageView user = (ImageView) row.findViewById(R.id.imgUser);
        name.setText(names[position]);
        from.setText(froms[position]);
        to.setText(tos[position]);
        table.setText(tables[position]);
        date.setText(dates[position]);
        calender.setImageResource(calenders[position]);
        location.setImageResource(locations[position]);
        user.setImageResource(users[position]);
        return (row);
    }


}
