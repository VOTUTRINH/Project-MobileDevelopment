package com.example.oderingfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookingFragment extends Fragment {
    String[]froms={"07:30","09:00","11:00","17:00","19:00"};
    String[]tos={"09:00","11:00","12:30","19:00","21:00"};
    String[]tables={"Bàn 1", "Bàn 2", "Bàn 3", "Bàn 4", "Bàn 5"};
    String[]dates={"28/10","29/10","30/10", "31/10","1/11"};
    String[]names={"Nguyen Van A","Le Thi B","Tran Van C","Phan Van C","Dinh Van D"};
    Integer[]calenders={R.drawable.calender,R.drawable.calender,R.drawable.calender,R.drawable.calender,R.drawable.calender};
    Integer[]locations={R.drawable.location,R.drawable.location,R.drawable.location,R.drawable.location,R.drawable.location};
    Integer[]users={R.drawable.user,R.drawable.user,R.drawable.user,R.drawable.user,R.drawable.user};

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
        TableAdapter adapter = new TableAdapter(getContext(),froms, tos, names,tables, dates, calenders, locations, users);

        // Setting Adapter to RecyclerView
        recyclerView1.setAdapter(adapter);

        return bookingFragment;

    }

}