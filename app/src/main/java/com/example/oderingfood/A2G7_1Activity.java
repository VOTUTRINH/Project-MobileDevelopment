package com.example.oderingfood;

import android.app.ListActivity;
import android.os.Bundle;

public class A2G7_1Activity extends ListActivity {
    String[]froms={"07:30","09:00","11:00","17:00","19:00"};
    String[]tos={"09:00","11:00","12:30","19:00","21:00"};
    String[]tables={"Bàn 1", "Bàn 2", "Bàn 3", "Bàn 4", "Bàn 5"};
    String[]dates={"28/10","29/10","30/10", "31/10","1/11"};
    String[]names={"Nguyen Van A","Le Thi B","Tran Van C","Phan Van C","Dinh Van D"};
    Integer[]calenders={R.drawable.calender,R.drawable.calender,R.drawable.calender,R.drawable.calender,R.drawable.calender};
    Integer[]locations={R.drawable.location,R.drawable.location,R.drawable.location,R.drawable.location,R.drawable.location};
    Integer[]users={R.drawable.user,R.drawable.user,R.drawable.user,R.drawable.user,R.drawable.user};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.table_list);

        TableAdapter adapter =new TableAdapter(this,R.layout.item_table,froms, tos, names,tables, dates, calenders, locations, users);
        setListAdapter(adapter);

    }

}