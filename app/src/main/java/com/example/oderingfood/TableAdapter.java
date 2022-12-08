package com.example.oderingfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.models.Booking;

import java.util.List;


public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder>

{
    List<Booking> dataList;

    public TableAdapter(List<Booking> dataList){

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

        holder.name.setText(dataList.get(position).getName());
        holder.from.setText(dataList.get(position).getStartTime());
        holder.to.setText(dataList.get(position).getEndTime());
        holder.table.setText(dataList.get(position).getId());
        holder.date.setText(dataList.get(position).getDate());
        holder.calender.setImageResource(R.drawable.calender);
        holder.location.setImageResource(R.drawable.location);
        holder.user.setImageResource(R.drawable.user);
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
            view.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), R.id.menu_edit_item, 0, "Sửa/ Thêm");
            menu.add(getAdapterPosition(), R.id.menu_delete_item, 1, "Xóa");

        }


    }
}
