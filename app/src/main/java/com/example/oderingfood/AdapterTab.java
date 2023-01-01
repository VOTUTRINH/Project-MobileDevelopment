package com.example.oderingfood;


import static com.example.oderingfood.models.GlobalVariables.pathRestaurentID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.GlobalVariables;
import com.example.oderingfood.models.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterTab extends RecyclerView.Adapter<AdapterTab.ViewHolder> {

    ArrayList<Restaurant> list;
    Context context; String idUser;

    // Constructor for initialization
    public AdapterTab(Context context, ArrayList<Restaurant> list,String idUser) {
        this.context = context;
        this.list = list;
        this.idUser = idUser;
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
        Restaurant restaurant = list.get(position);
        try{
            Picasso.get().load(restaurant.getUrlImage()).into(holder.images);
        }catch(Exception e){
            holder.images.setImageResource(R.drawable.abc);
        }

        holder.text.setText((String)restaurant.getName());
        holder.txt_address.setText((String) restaurant.getAddress());

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get id to acess database
                pathRestaurentID = restaurant.getId();

                Intent intent=new Intent(context,StartResActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("restaurant",restaurant.getId());
                bundle.putString("user",idUser);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
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
        LinearLayout layoutItem;

        public ViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.profile_image);
            text = (TextView) view.findViewById(R.id.txt_mame);
            txt_address = (TextView) view.findViewById(R.id.txt_address);
            layoutItem =(LinearLayout) view.findViewById(R.id.layout_item);
        }

    }


}