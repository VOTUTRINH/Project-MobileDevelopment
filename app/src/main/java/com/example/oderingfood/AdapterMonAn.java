package com.example.oderingfood;

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

import java.util.List;

public class AdapterMonAn extends RecyclerView.Adapter<AdapterMonAn.ViewHolder> {
    List<String> foodNames;
    List<Integer> foodImages;
    List<String> foodPrices;
    LayoutInflater inflater;


    public AdapterMonAn(Context ctx, List<String> names, List<Integer> images, List<String> prices){
        this.foodNames = names;
        this.foodImages = images;
        this.foodPrices = prices;
        this.inflater = LayoutInflater.from(ctx);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_mon_an,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.foodImageView.setImageResource(foodImages.get(position));
        holder.foodNameView.setText(foodNames.get(position));
        holder.foodPriceView.setText(foodPrices.get(position));
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
                        final Button order = holder.order;

                        switch (item.getItemId()) {
                            case R.id.item1_chinhsua:
                                //handle menu1 click
                                return true;
                            case R.id.item2_sansang:
                                //handle menu2 click
                                plus.setEnabled(true);
                                minus.setEnabled(true);
                                order.setEnabled(true);
                                disableScreen.setVisibility(View.GONE);
                                return true;
                            case R.id.item3_tamngung:
                                plus.setEnabled(false);
                                minus.setEnabled(false);
                                order.setEnabled(false);
                                disableScreen.setVisibility(View.VISIBLE);

                                //handle menu3 click
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
    }

    @Override
    public int getItemCount() {
        return foodNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView foodImageView;
        TextView foodNameView;
        TextView foodPriceView;
        TextView buttonViewOption;
        TextView disableScreen;
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
            order = itemView.findViewById(R.id.btn_order);

        }
    }
}
