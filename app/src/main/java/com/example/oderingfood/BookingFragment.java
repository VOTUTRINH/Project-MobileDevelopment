package com.example.oderingfood;

import com.example.oderingfood.models.Booking;
import com.example.oderingfood.models.Food;
import com.example.oderingfood.models.GlobalVariables;
import com.example.oderingfood.models.Table;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.DateTime;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    Calendar timeStart;
//    String[]froms={"07:30","09:00","11:00","17:00","19:00"};
//    String[]tos={"09:00","11:00","12:30","19:00","21:00"};
//    String[]tables={"Bàn 1", "Bàn 2", "Bàn 3", "Bàn 4", "Bàn 5"};
//    String[]dates={"28/10","29/10","30/10", "31/10","1/11"};
//    String[]names={"Nguyen Van A","Le Thi B","Tran Van C","Phan Van C","Dinh Van D"};


    RecyclerView recyclerView1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View bookingFragment = inflater.inflate(R.layout.table_list, container, false);

        // Getting reference of recyclerView
        recyclerView1 = (RecyclerView) bookingFragment.findViewById(R.id.list_booking);
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
        adapter = new TableAdapter(dataList);

        // Setting Adapter to RecyclerView
        recyclerView1.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;

        mDatabase = database.getReference("/restaurant/xzxHmkiUMHVjqNu67Ewzsv2TQjr2/Booking");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dataList.clear();
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    // Get data
                    String StartTime = postSnapShot.child("StartTime").getValue(String.class);
                    String EndTime = postSnapShot.child("EndTime").getValue(String.class);
                    String ID = postSnapShot.child("ID").getValue(String.class);
                    String Name = postSnapShot.child("Name").getValue(String.class);
                    String Date = postSnapShot.child("Date").getValue(String.class);

                    Booking booking = new Booking(StartTime, EndTime, ID, Date, Name);

                    dataList.add(booking);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        registerForContextMenu(recyclerView1);
        return bookingFragment;

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_content, menu);
//
//        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int clickedItemPosition = item.getGroupId();

        if (item.getItemId() == R.id.menu_edit_item) {
            this.currentIndex = clickedItemPosition;
            showDialogAdd();
            return true;
        }
        if (item.getItemId() == R.id.menu_delete_item) {
            dataList.remove(clickedItemPosition);
            adapter.notifyDataSetChanged();

            return true;
        }

        return super.onContextItemSelected(item);
    }
    private void showDialogAdd() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_table, null);
//        Toast.makeText(getActivity(), "Số điện thoại chưa được đăng kí tài khoản",Toast.LENGTH_SHORT).show();
        final EditText edDate = view.findViewById(R.id.df_date);
        final EditText edTable = view.findViewById(R.id.df_table);
        final EditText edFrom = view.findViewById(R.id.df_from);
        final EditText edTo = view.findViewById(R.id.df_to);
        final EditText edGuest = view.findViewById(R.id.df_guest);
        final Button setDay = view.findViewById(R.id.btn_set_day);
        setDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDaytimeDialog();
            }
        });
        if(currentIndex >= 0) {
            edDate.setText(dataList.get(currentIndex).getDate());
            edTable.setText(dataList.get(currentIndex).getId());
            edFrom.setText(dataList.get(currentIndex).getStartTime());
            edTo.setText(dataList.get(currentIndex).getEndTime());
            edGuest.setText(dataList.get(currentIndex).getName());
        }

        builder.setView(view);
        builder.setTitle("Cập nhật/ Thêm đặt bàn")
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabase;

                        mDatabase = database.getReference("/restaurant/xzxHmkiUMHVjqNu67Ewzsv2TQjr2/Booking");
//                        Table newTable = new Table(tableName);


                        String from = edFrom.getText().toString();
                        String to = edTo.getText().toString();
                        String date = edDate.getText().toString();
                        String guest = edGuest.getText().toString();
                        String table = edTable.getText().toString();
                        int exist = 0;
                        for (int j = 0; j < dataList.size(); j++)
                        {
                            if (table.equals(dataList.get(j).getId()) ) {
                                exist = 1;
                            }
                        }
                        if (exist == 0) {
                            currentIndex = -1;
                        }
                        if(currentIndex >= 0) {
                            dataList.get(currentIndex).setName(guest);
                            dataList.get(currentIndex).setStartTime(from);
                            dataList.get(currentIndex).setEndTime(to);
                            dataList.get(currentIndex).setDate(date);
                            dataList.get(currentIndex).setId(table);
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    mDatabase.child(table).child("StartTime").setValue(from);
                                    mDatabase.child(table).child("EndTime").setValue(to);
                                    mDatabase.child(table).child("ID").setValue(table);
                                    mDatabase.child(table).child("Date").setValue(date);
                                    mDatabase.child(table).child("Name").setValue(guest);
                                    // Update count table

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            currentIndex = -1;
                        } else {
                            Booking booking = new Booking(from, to, table, date, guest);
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    mDatabase.child(table).child("StartTime").setValue(from);
                                    mDatabase.child(table).child("EndTime").setValue(to);
                                    mDatabase.child(table).child("ID").setValue(table);
                                    mDatabase.child(table).child("Date").setValue(date);
                                    mDatabase.child(table).child("Name").setValue(guest);
                                    // Update count table

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            dataList.add(booking);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        builder.show();
    }

    private void showDaytimeDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_pick_day_time, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        final Calendar time = null;
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

//                Toast.makeText(view.getContext(), Long.toString(calendar.getTimeInMillis()), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();

            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();

    }
}


