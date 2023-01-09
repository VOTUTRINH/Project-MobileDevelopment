package com.example.oderingfood;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMonAn extends RecyclerView.Adapter<AdapterMonAn.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<Food> menu;
    String idRes;
    String idUser;

    public AdapterMonAn(Context ctx, List<Food> menu, String idRes, String idUser){
        context = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.menu = menu;
        this.idUser = idUser;
        this.idRes = idRes;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_mon_an,parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;

        String pathR = "/restaurant/" + GlobalVariables.pathRestaurentID;
        mDatabase = database.getReference(pathR +"/Menu") ;


        Picasso.get().load(menu.get(position).getUrlImage()).into(holder.foodImageView);
        holder.foodNameView.setText(menu.get(position).getName());
        holder.foodPriceView.setText(GlobalVariables.displayCurrency(menu.get(position).getPrice()));
        holder.numOfFoods.setText(Integer.toString(menu.get(position).getQuantity()));
        if (menu.get(position).isAvailable() == false){
            holder.plus.setEnabled(false);
            holder.minus.setEnabled(false);
            holder.disableScreen.setVisibility(View.VISIBLE);
        }
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(inflater.getContext(), holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.edit_menu_mon_an);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final TextView disableScreen = holder.disableScreen;
                        final Button plus = holder.plus;
                        final Button minus = holder.minus;

                        switch (item.getItemId()) {
                            case R.id.item1_chinhsua:
                                //handle menu1 click
                                Intent intent = new Intent(context, AddFoodToMenu.class);
                                Bundle b = new Bundle();
                                b.putBoolean("edit", true); //Your id
                                b.putSerializable("food", menu.get(position));
                                b.putString("idUser", idUser );
                                b.putString("idRes", idRes);
                                intent.putExtras(b);
                                context.startActivity(intent);
                                return true;
                            case R.id.item2_sansang:
                                //handle menu2 click
                                plus.setEnabled(true);
                                minus.setEnabled(true);
                                disableScreen.setVisibility(View.GONE);
                                mDatabase.child(menu.get(position).getId()).child("available").setValue(true);
                                return true;
                            case R.id.item3_tamngung:
                                plus.setEnabled(false);
                                minus.setEnabled(false);
                                disableScreen.setVisibility(View.VISIBLE);
                                mDatabase.child(menu.get(position).getId()).child("available").setValue(false);

                                //handle menu3 click
                                return true;
                            case R.id.item4_xoa:
                                mDatabase.child(menu.get(position).getId()).setValue(null);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.get(position).plusQuantity();
                holder.numOfFoods.setText(Integer.toString(menu.get(position).getQuantity()));
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.get(position).minusQuantity();
                holder.numOfFoods.setText(Integer.toString(menu.get(position).getQuantity()));

            }
        });


    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView foodImageView;
        TextView foodNameView;
        TextView foodPriceView;
        TextView buttonViewOption;
        TextView disableScreen;
        TextView numOfFoods;
        Button plus;
        Button minus;
        Button order;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameView = itemView.findViewById(R.id.foodName);
            foodImageView = itemView.findViewById(R.id.foodImage);
            foodPriceView = itemView.findViewById(R.id.foodPrice);
            buttonViewOption = itemView.findViewById(R.id.btn_moremenu);
            disableScreen = itemView.findViewById(R.id.txt_disablescreen);
            plus = itemView.findViewById(R.id.btn_plus);
            minus = itemView.findViewById(R.id.btn_minus);
            numOfFoods = itemView.findViewById(R.id.thanhtoan_txt_tenban);
        }
    }

    public List<Food> getMenu() {
        return menu;
    }
}
