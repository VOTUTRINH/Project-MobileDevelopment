package com.example.oderingfood;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        Button giaoMon = view.findViewById(R.id.btn_giaomon);
        TextView isServed = view.findViewById(R.id.txt_servedscreen);

        String pathR = "/restaurant/" + GlobalVariables.pathRestaurentID;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tableDatabase = database.getReference(pathR + "/BanAn");

        Food food = dataList.get(position);
        Glide.with(activity.getBaseContext()).load(food.getUrlImage()).into(thumbnail);
        name.setText("Tên: " + food.getName());
        price.setText("Giá: " + String.valueOf(food.getPrice()) + " $");
        quantity.setText("Số lượng: " + String.valueOf(food.getQuantity()));
        total.setText("Tổng tiền: " + String.valueOf(food.getPrice() * food.getQuantity()) + " $");
        if (food.isServed() == true){
            giaoMon.setText("Đã giao");
            giaoMon.setClickable(false);
            isServed.setVisibility(View.VISIBLE);
        }

        giaoMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giaoMon.setText("Đã giao");
                giaoMon.setClickable(false);
                isServed.setVisibility(View.VISIBLE);
                DatabaseReference yourtable = tableDatabase.child(GlobalVariables.pathTable);
                //read priority to countWaiting[1]
                yourtable.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        GlobalVariables.tablePriority = snapshot.child("Priority").getValue(Integer.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                tableDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                                if (postSnapShot.child("TrangThai").getValue(String.class).equals(activity.getString(R.string.waiting_state))){
                                    int pri = postSnapShot.child("Priority").getValue(Integer.class);
                                    if ( pri > GlobalVariables.tablePriority){
                                        pri --;
                                        postSnapShot.child("Priority").getRef().setValue(pri);
                                    }
                                }
                            }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                dataList.get(position).setServed(true);
                yourtable.child("Order").child(food.getId()).child("served").setValue(true);
                boolean check = true;
                for(int i =0 ; i<dataList.size(); i++){
                    check = check && dataList.get(i).isServed();
                }
                if (check){
                    yourtable.child("TrangThai").setValue("IsUsing");
                    GlobalVariables.priority--;
                }
                else {
                    yourtable.child("Priority").setValue(GlobalVariables.priority);
                }
            }
        });



        return view;
    }

}

