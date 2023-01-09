package com.example.oderingfood.Restaurant.Menu;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oderingfood.R;
import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class A2G7Activity extends Activity {
    ListView listView;
    List<Food> dataList = new ArrayList<>();
    FoodAdapter foodAdapter;
    Button thanhtoan;
    TextView tenban;
    DatabaseReference mDatabase;
    String tablePath;
    FirebaseDatabase database;
    double total1 = 0;
    double doanhThu = 0;
    boolean capNhapDoanhThu = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle b = getIntent().getExtras();
//        if(b != null) {
//            Serializable listTemp;
//            listTemp = b.getSerializable("Orders");
//            dataList = (List<Food>) listTemp;
//        }
        Bundle b = getIntent().getExtras();

        if(b != null) {
            tablePath = b.getString("key");
        }

        database = FirebaseDatabase.getInstance();

        String pathR = "/restaurant/" + GlobalVariables.pathRestaurentID;
        mDatabase = database.getReference(pathR+"/BanAn/"+ tablePath);
        setContentView(R.layout.a2g7_activity_main);

        listView = findViewById(R.id.afo_listview);
        thanhtoan = findViewById(R.id.thanhtoan_btn_thanhtoan);
        tenban = findViewById(R.id.thanhtoan_txt_tenban);

        double TotalPrice;

        TextView allTotal = findViewById(R.id.afo_txtTotal);
//        TotalPrice = Double.parseDouble(txtPrice.getText().toString()) * Double.parseDouble(txtQuantity.getText().toString());
        TotalPrice = 2;
        tenban.setText("Bàn " + tablePath);

        foodAdapter = new FoodAdapter(this, dataList);

        listView.setAdapter(foodAdapter);

        registerForContextMenu(listView);

//





        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (dataList.size() > 0) {
                    dataList.clear();
                }
                DataSnapshot menuSnapShot = snapshot.child("Order");
                for (DataSnapshot foodSnapShot : menuSnapShot.getChildren()) {
                    // Get data
                    String foodName = foodSnapShot.getKey();
                    Food food = foodSnapShot.getValue(Food.class);

                    // Add food ordered to table

                    dataList.add(food);
                }
                total1 = 0;
                for(int i=0;i<dataList.size();i++){
                    if(dataList.get(i).isServed()) {
                        total1 = total1 + dataList.get(i).getPrice() * dataList.get(i).getQuantity();
                    }
                }

                allTotal.setText("Tổng tiền tất cả: " + GlobalVariables.displayCurrency(total1));
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = true;
                for(int i=0; i<dataList.size(); i++){
                    check = check && dataList.get(i).isServed();
                }
                if (check==true){
                    thanhToan();
                }
                else{
                    ShowDialogConfirmCast();
                }
            }
        });

    }
    public void ShowDialogConfirmCast()
    {
        Dialog dialog = new Dialog(A2G7Activity.this);
        dialog.setContentView(R.layout.confirm_delete_table);
        dialog.show();

        TextView txt_confirm = (TextView) dialog.findViewById(R.id.txt_confirm);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);

        txt_confirm.setText("Các món được gọi chưa được phục vụ xong, bạn vẫn muốn thanh toán?");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                thanhToan();
                dialog.dismiss();
            }
        });

    }

    public void thanhToan(){
        String pathR = "/restaurant/" + GlobalVariables.pathRestaurentID;
        DatabaseReference resDatabase = database.getReference(pathR);
        DatabaseReference menuDatabase = database.getReference(pathR + "/Menu");
        DatabaseReference tableDatabase = database.getReference(pathR + "/BanAn");
        menuDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot foodSnapShot : snapshot.getChildren()) {
                    for (int i = 0; i<dataList.size(); i++){
                        Food food = foodSnapShot.getValue(Food.class);


                        if (food.getId().equals(dataList.get(i).getId()) && dataList.get(i).isServed()) {
                            int total = food.getTotal() +dataList.get(i).getQuantity();
                            foodSnapShot.child("total").getRef().setValue(total, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    if (postSnapShot.child("TrangThai").getValue(String.class).equals(getString(R.string.waiting_state))){
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

        resDatabase.addValueEventListener(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                               doanhThu = snapshot.child("DoanhThu").getValue(Double.class);
                                                               doanhThu = doanhThu + total1;
                                                               if(capNhapDoanhThu) {
                                                                   snapshot.getRef().child("DoanhThu").setValue(doanhThu);
                                                                   capNhapDoanhThu = false;
                                                               }
                                                       }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
                                                   });

        mDatabase.child("Priority").setValue(null);
        mDatabase.child("Order").setValue(null);
        mDatabase.child("TrangThai").setValue("Empty", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(A2G7Activity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


}
