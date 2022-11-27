package com.example.oderingfood;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.example.oderingfood.models.Food;

import java.util.ArrayList;
import java.util.List;

public class A2G7Activity extends Activity {
    ListView listView;
    Button btnFinish;
    List<Food> dataList = new ArrayList<>();
    FoodAdapter foodAdapter;
    int currentIndex = -1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2g7_activity_main);

        listView = findViewById(R.id.afo_listview);
        double TotalPrice;
        double total1 = 0;
        TextView allTotal = findViewById(R.id.afo_txtTotal);
//        TotalPrice = Double.parseDouble(txtPrice.getText().toString()) * Double.parseDouble(txtQuantity.getText().toString());
        TotalPrice = 2;
        dataList.add(new Food(R.drawable.food, "Pizza", 2, 5, TotalPrice));
        dataList.add(new Food(R.drawable.food, "Hamburger", 3, 4, TotalPrice));
        dataList.add(new Food(R.drawable.food, "Hotdog", 4, 5, TotalPrice));
        dataList.add(new Food(R.drawable.food, "Pizza", 2, 5, TotalPrice));
        dataList.add(new Food(R.drawable.food, "Hamburger", 3, 4, TotalPrice));
        dataList.add(new Food(R.drawable.food, "Hotdog", 4, 5, TotalPrice));


        foodAdapter = new FoodAdapter(this, dataList);

        listView.setAdapter(foodAdapter);

        registerForContextMenu(listView);
        for(int i=0;i<dataList.size();i++){
            total1 = total1 + dataList.get(i).getPrice() * dataList.get(i).getQuantity();
        }
//
        allTotal.setText("Tổng tiền tất cả: " + String.valueOf(total1) + " $");
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
