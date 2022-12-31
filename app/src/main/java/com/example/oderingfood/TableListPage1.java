package com.example.oderingfood;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;
import com.example.oderingfood.models.Table;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableListPage1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableListPage1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Boolean isBooking = false;
    private String name = "";
    private String phone ="";
    private String date = "05-20-2003";
    private String timeS;
    private String timeE;
    boolean getdata = false;

    TablesActivity tablesActivity;
    Context context;
    GridView gv;
    ListTablesAdapter tablesAdapter;
    List<Table> tableList = new ArrayList<Table>();

    Bottomnavigation bottomnavigation ;
    String user;
    String idRes;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public TableListPage1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableListPage1.
     */
    // TODO: Rename and change types and number of parameters
    public static TableListPage1 newInstance(String param1, String param2) {
        TableListPage1 fragment = new TableListPage1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isBooking = getArguments().getBoolean("isBooking");
            name = getArguments().getString("ten");
            phone = getArguments().getString("phone");
            date = getArguments().getString("date");
            timeS = getArguments().getString("timeS");
            timeE = getArguments().getString("timeE");
            getdata = true;
        }

        try {
            context = getActivity();
            // tablesActivity = (TablesActivity) getActivity();

            tablesActivity = new TablesActivity();
        }catch (Exception e)
        {
        }

        bottomnavigation = (Bottomnavigation) getActivity();
        user= bottomnavigation.getUser();
        idRes = bottomnavigation.getIdRes();

        tablesAdapter = new ListTablesAdapter(context,R.layout.table_layout_item, tableList);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;

        mDatabase = database.getReference("/restaurant/"+idRes+"/BanAn");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(tableList.size() > 0)
                {
                    tableList.clear();
                }
                GlobalVariables.priority = 0;
                for(DataSnapshot postSnapShot: snapshot.getChildren())
                {
                    // Get data
                    String tenBan = postSnapShot.getKey();
                    int pri;
                    String trangThai = postSnapShot.child("TrangThai").getValue(String.class);
                    if (trangThai.equals("IsWaiting")) {
                        GlobalVariables.priority++;
                    }
                    try {
                        pri = postSnapShot.child("Priority").getValue(Integer.class);
                    }catch (Exception e){
                        pri = 1000;
                    }
                    // add table
                    Table table = new Table(tenBan, pri);
                    table.setState(trangThai);
                    if (trangThai.equals("Empty")) {
                        if (postSnapShot.hasChild("Bookings")) {
                            if (postSnapShot.child("Bookings").hasChild(date)) {
                                DataSnapshot bookingDate = postSnapShot.child("Bookings").child(date);
                                for (DataSnapshot BookingDetail : bookingDate.getChildren()) {
                                    String id = BookingDetail.getKey();
                                    String timeBookedStart = BookingDetail.child("timeS").getValue(String.class);
                                    String timeBookedEnd = BookingDetail.child("timeE").getValue(String.class);
                                    String idUser = BookingDetail.child("idUser").getValue(String.class);
                                    if (GlobalVariables.isDuplicateTime(timeS, timeE, timeBookedStart, timeBookedEnd)) {
                                        List<Food> foods = new ArrayList<>();
                                        for (DataSnapshot tableSnapShot : BookingDetail.child("orders").getChildren()) {

                                                Food food = tableSnapShot.getValue(Food.class);
                                                foods.add(food);


                                        }
                                        table.Book();
                                        table.setPriority(500);
                                        table.setBooking(timeBookedStart, timeBookedEnd, id, idUser, foods);
                                    }
                                }

                            }
                        }
                    }
                    // Get foods was ordered for table
                    DataSnapshot menuSnapShot = postSnapShot.child("Order");
                    for (DataSnapshot foodSnapShot: menuSnapShot.getChildren())
                    {
                        // Get data
                        String foodName = foodSnapShot.getKey();
                        Food food = foodSnapShot.getValue(Food.class);

                        // Add food ordered to table

                        table.AddFood(food);
                    }

                    tableList.add(table);
                }
                Collections.sort(tableList, new Comparator<Table>() {
                    @Override
                    public int compare(Table o1, Table o2) {
                        return o1.getPriority() - o2.getPriority();
                    }
                });

                tablesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout_page1 =(LinearLayout)inflater.inflate(R.layout.fragment_table_list_page1,null);

        gv = (GridView) layout_page1.findViewById(R.id.grid_view);
        gv.setAdapter(tablesAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                GlobalVariables.pathTable = tableList.get(i).getName();
                if(tableList.get(i).getState().equals("Empty")){
                    if(tableList.get(i).getIsbooked()){
                        ShowDialogConfirm(i);
                    }
                    else {
                        Intent intent = new Intent(context, MonAnActivity.class);
                        Bundle b = new Bundle();
                        b.putString("key", tableList.get(i).getName()); //Your id
                        b.putString("idRes", idRes);
                        b.putString("idUser", user);
                        intent.putExtras(b);
                        context.startActivity(intent);
                    }
                }else
                {
                    Intent intdn = new Intent(context, A2G7Activity.class); // Your nxt activity name instead of List_Activity

                    Bundle b = new Bundle();
                    b.putString("key", tableList.get(i).getName()); //Your id
                    intdn.putExtras(b); //Put your id to your next Intent
                    context.startActivity(intdn);
                }

            }
        });

        return layout_page1;
    }
    public void ShowDialogConfirm(int position)
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.confirm_delete_table);
        dialog.show();

        TextView txt_confirm = (TextView) dialog.findViewById(R.id.txt_confirm);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);

        txt_confirm.setText("Bàn ăn đã được đặt, bạn có muốn thực hiện đơn đã đặt.");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                GiaoBanAn(position);
                dialog.dismiss();
            }
        });

    }
    public void GiaoBanAn( int pos){
        DatabaseReference tableDatabase;
        String pathR = "/restaurant/" + idRes;
        tableDatabase = database.getReference(pathR + "/BanAn/"+ tableList.get(pos).getName());
        List<Food> temp = tableList.get(pos).getBooking().getTableBook().second;
        Map<String, Food> data = new HashMap<>();
        for(int i=0; i< temp.size(); i++){
            if (temp.get(i).getQuantity() != 0){
                data.put(temp.get(i).getId(), temp.get(i));
            }
        }
        //check co bao nhieu bang dang doi
        //set priority
        GlobalVariables.priority++;
        tableDatabase.child("Priority").setValue(GlobalVariables.priority);
        tableDatabase.child("TrangThai").setValue(getString(R.string.waiting_state));
        tableDatabase.child("Order").setValue(data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(TableListPage1.this.getContext(), "Bắt đầu phục vụ bàn " + tableList.get(pos).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        tableDatabase.child("Bookings").child(date).child(tableList.get(pos).getBooking().getId()).setValue(null);
        String pathU ="/user/" + tableList.get(pos).getBooking().getIdUser();
        DatabaseReference bookingDatabase = database.getReference(pathR + "/Bookings");
        bookingDatabase.child(date).child(tableList.get(pos).getBooking().getId()).setValue(null);
        DatabaseReference userBookingDatabase = database.getReference(pathU + "Bookings");
        userBookingDatabase.child(idRes).child(date).child(tableList.get(pos).getBooking().getId()).setValue(null);
    }
}