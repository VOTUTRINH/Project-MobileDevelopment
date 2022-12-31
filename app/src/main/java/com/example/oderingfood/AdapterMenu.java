package com.example.oderingfood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;

import java.util.List;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.ViewHolder> {
        LayoutInflater inflater;
        Context context;
        List<Food> menu;

    public AdapterMenu(Context ctx, List<Food> menu){
            context = ctx;
            this.inflater = LayoutInflater.from(ctx);
            this.menu = menu;
        }
        @NonNull
        @Override
        public AdapterMenu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_menu,parent, false);

            return new AdapterMenu.ViewHolder(view);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(context).load(menu.get(position).getUrlImage()).into(holder.foodImageView);
            holder.foodNameView.setText(menu.get(position).getName());
            holder.foodPriceView.setText(GlobalVariables.displayCurrency(menu.get(position).getPrice()));
           // holder.numOfFoods.setText(Integer.toString(menu.get(position).getQuantity()));


        }

        @Override
        public int getItemCount() {
            return menu.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView foodImageView;
            TextView foodNameView;
            TextView foodPriceView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                foodNameView = itemView.findViewById(R.id.foodName);
                foodImageView = itemView.findViewById(R.id.foodImage);
                foodPriceView = itemView.findViewById(R.id.foodPrice1);
            }
        }

        public List<Food> getMenu() {
            return menu;
        }
    }

