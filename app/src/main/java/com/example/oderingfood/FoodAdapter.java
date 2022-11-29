package com.example.oderingfood;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.Food;

import java.util.List;

public class FoodAdapter extends BaseAdapter {
    Activity activity;
    List<Food> dataList;

    public FoodAdapter(Activity activity , List<Food> dataList){
        this.activity = activity;
        this.dataList = dataList;
    }



    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.item_food, null);

        ImageView thumbnail = view.findViewById(R.id.ifo_thumbnail);
        TextView name = view.findViewById(R.id.ifo_name);
        TextView price = view.findViewById(R.id.ifo_price);
        TextView quantity = view.findViewById(R.id.ifo_quantity);
        TextView total = view.findViewById(R.id.ifo_totalprice);

        Food food = dataList.get(position);
        Glide.with(activity.getBaseContext()).load(food.getUrlImage()).into(thumbnail);
        name.setText("Tên: " + food.getName());
        price.setText("Giá: " + String.valueOf(food.getPrice()) + " $");
        quantity.setText("Số lượng: " + String.valueOf(food.getQuantity()));
        total.setText("Tổng tiền: " + String.valueOf(food.getPrice() * food.getQuantity()) + " $");



        return view;
    }

}

