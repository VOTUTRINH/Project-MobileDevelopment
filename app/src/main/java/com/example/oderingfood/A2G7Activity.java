package com.example.oderingfood;

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
    Button btnFinish;
    List<Food> dataList = new ArrayList<>();
    FoodAdapter foodAdapter;
    Button thanhtoan;
    TextView tenban;
    DatabaseReference mDatabase;
    String tablePath;
    FirebaseDatabase database;
    double total1 = 0;
    int currentIndex = -1;




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

//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_content, menu);
//
//        super.onCreateContextMenu(menu, v, menuInfo);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int index = info.position;
//
//        if (item.getItemId() == R.id.menu_edit_item) {
//            this.currentIndex = index;
//            showDialogAdd();
//            return true;
//        }
//        if (item.getItemId() == R.id.menu_delete_item) {
//            dataList.remove(index);
//            foodAdapter.notifyDataSetChanged();
//
//            return true;
//        }
//
//        return super.onContextItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        if (item.getItemId() == R.id.menu_add_new_food) {
//
//            showDialogAdd();
//            return true;
//        }
//        if (item.getItemId() == R.id.menu_exit) {
//
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//
//    }
//
//
//    private void showDialogAdd() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.dialog_food, null);
//
//        final EditText edName = view.findViewById(R.id.df_name);
//        final EditText edPrice = view.findViewById(R.id.df_price);
//        final EditText edQuantity = view.findViewById(R.id.df_quantity);
//        if(currentIndex >= 0) {
//            edName.setText(dataList.get(currentIndex).getName());
//            edPrice.setText(String.valueOf(dataList.get(currentIndex).getPrice()));
//            edQuantity.setText(String.valueOf(dataList.get(currentIndex).getQuantity()));
//        }
//
//        builder.setView(view);
//        builder.setTitle("Add/Update Item")
//                .setPositiveButton("Save Item", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String name = edName.getText().toString();
//                        float price = Float.parseFloat(edPrice.getText().toString());
//                        int quantity = Integer.parseInt(edQuantity.getText().toString());
//
//                        if(currentIndex >= 0) {
//                            dataList.get(currentIndex).setName(name);
//                            dataList.get(currentIndex).setPrice(price);
//                            dataList.get(currentIndex).setQuantity(quantity);
//                            currentIndex = -1;
//                        } else {
//                            Food food = new Food(R.drawable.food01, name, price, quantity, 2);
//                            dataList.add(food);
//                        }
//
//                        foodAdapter.notifyDataSetChanged();
//                    }
//                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//
//        builder.show();
//    }
}
