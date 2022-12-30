package com.example.oderingfood;

import com.example.oderingfood.models.Booking;
import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;
import com.example.oderingfood.models.Table;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.DateTime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BookingFragment extends Fragment {
    int currentIndex = -1;
    List<Booking> dataList = new ArrayList<>();
    TableAdapter adapter;
    Calendar timeStart = null;
    Calendar timeEnd = null;
    String user;
    String IDres;
    String role;
    Bundle b;
//    String[]froms={"07:30","09:00","11:00","17:00","19:00"};
//    String[]tos={"09:00","11:00","12:30","19:00","21:00"};
//    String[]tables={"Bàn 1", "Bàn 2", "Bàn 3", "Bàn 4", "Bàn 5"};
//    String[]dates={"28/10","29/10","30/10", "31/10","1/11"};
//    String[]names={"Nguyen Van A","Le Thi B","Tran Van C","Phan Van C","Dinh Van D"};


    RecyclerView recyclerView1;
    FloatingActionButton addBooking;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b=this.getArguments();
        if (b!=null){
            user = b.getString("user");
            IDres = b.getString("restaurant");
            role = b.getString("role");
        }
        View bookingFragment = inflater.inflate(R.layout.fragment_tab2, container, false);

        // Getting reference of recyclerView
        recyclerView1 = (RecyclerView) bookingFragment.findViewById(R.id.recyclerView2);
        addBooking = (FloatingActionButton) bookingFragment.findViewById(R.id.floating_action_button);
        // Setting the layout as linear

        // layout for vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);

        recyclerView1.setLayoutManager(linearLayoutManager);
        // Sending reference and data to Adapter
//        for (int i = 0; i < froms.length; i++)
//        {
//            Booking booking = new Booking(froms[i], tos[i], tables[i], dates[i], names[i]);
//            dataList.add(booking);
//        }
        adapter = new TableAdapter(getContext(), dataList, role, IDres, user);

        // Setting Adapter to RecyclerView
        recyclerView1.setAdapter(adapter);
        String pathR = "/restaurant/" + GlobalVariables.pathRestaurentID;
        String pathU = "/user/" + user;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookingDatabase = null;
        switch (role)
        {
            case "KhachHang":
                bookingDatabase = database.getReference(pathU + "/Bookings/" + IDres + "/");
                break;
            default:
                bookingDatabase = database.getReference(pathR + "/Bookings/");
                break;
        }

        bookingDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataList.clear();
                for (DataSnapshot dateSnapShot : snapshot.getChildren()) {
                    for (DataSnapshot postSnapShot : dateSnapShot.getChildren()) {
                        // Get data

                        String StartTime = postSnapShot.child("TimeS").getValue(String.class);
                        String EndTime = postSnapShot.child("TimeE").getValue(String.class);
                        String ID = postSnapShot.child("id").getValue(String.class);
                        String Name = postSnapShot.child("Ten").getValue(String.class);
                        String Date = postSnapShot.child("Date").getValue(String.class);
                        String IdUser = postSnapShot.child("IdUser").getValue(String.class);
                        String Phone = postSnapShot.child("Phone").getValue(String.class);
                        boolean isConfirm = false;
                        if(postSnapShot.hasChild("isConfirmed")) {
                            isConfirm = postSnapShot.child("isConfirmed").getValue(Boolean.class);
                        }
                        String table ="";
                        Booking booking = new Booking(StartTime, EndTime, ID, Date, Name, Phone, IdUser, isConfirm);

                        for (DataSnapshot tableSnapShot : postSnapShot.child("order").getChildren()) {

                            table = tableSnapShot.getKey();
                            List<Food> foods = new ArrayList<>();
                            for (DataSnapshot foodSnapShot : tableSnapShot.getChildren()) {
                                Food food = foodSnapShot.getValue(Food.class);
                                foods.add(food);
                            }
                            booking.addTableBook(table, foods);
                        }



                        dataList.add(booking);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        registerForContextMenu(recyclerView1);
        addBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),addBookingActivity.class);
                getContext().startActivity(intent);
            }
        });
        return bookingFragment;

    }

}
