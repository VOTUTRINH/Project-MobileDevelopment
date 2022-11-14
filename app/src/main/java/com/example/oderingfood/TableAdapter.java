package com.example.oderingfood;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder>

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
    public TableAdapter(Context context, String[] froms, String[] tos, String[] names,String[] tables, String[] dates, Integer[] calenders, Integer[] locations, Integer[] users){

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
        holder.name.setText((String) names[position]);
        holder.from.setText(froms[position]);
        holder.to.setText(tos[position]);
        holder.table.setText(tables[position]);
        holder.date.setText(dates[position]);
        holder.calender.setImageResource(calenders[position]);
        holder.location.setImageResource(locations[position]);
        holder.user.setImageResource(users[position]);
    }

    @Override
    public int getItemCount() {
        return tables.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,from,to,table,date;
        ImageView calender,location,user;
        public ViewHolder(View view) {
            super(view);
             name = (TextView) view.findViewById(R.id.txtName);
             from = (TextView) view.findViewById(R.id.txtFrom);
            to = (TextView) view.findViewById(R.id.txtTo);
            table = (TextView) view.findViewById(R.id.txtTable);
            date = (TextView) view.findViewById(R.id.txtDate);
            calender = (ImageView) view.findViewById(R.id.imgCalender);
            location = (ImageView) view.findViewById(R.id.imgLocation);
            user = (ImageView) view.findViewById(R.id.imgUser);
        }
    }
}
