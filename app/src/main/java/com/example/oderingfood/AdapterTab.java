package com.example.oderingfood;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.Restaurant;

import java.util.ArrayList;

public class AdapterTab extends RecyclerView.Adapter<AdapterTab.ViewHolder> {

    ArrayList<Restaurant> list;
    Context context;

    // Constructor for initialization
    public AdapterTab(Context context, ArrayList<Restaurant> list) {
        this.context = context;
        this.list = list;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        // Passing view to ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TypeCast Object to int type
        Glide.with(context).load(list.get(position).getUrlImage()).into(holder.images);
        holder.text.setText((String) list.get(position).getName());
        holder.txt_address.setText((String) list.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        // Returns number of items
        // currently available in Adapter
        return list.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView text, txt_address;

        public ViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.profile_image);
            text = (TextView) view.findViewById(R.id.txt_mame);
            txt_address = (TextView) view.findViewById(R.id.txt_address);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(context,Bottomnavigation.class);
                    context.startActivity(intent);
                }
            });
        }

    }

}