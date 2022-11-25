package com.example.oderingfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.oderingfood.models.Table;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    TablesActivity tablesActivity;
    Context context;
    GridView gv;
    List<Table> tableList = new ArrayList<Table>();

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        try {
            context = getActivity();
            // tablesActivity = (TablesActivity) getActivity();

            tablesActivity = new TablesActivity();
        }catch (Exception e)
        {
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;

        mDatabase = database.getReference("/restaurant/xzxHmkiUMHVjqNu67Ewzsv2TQjr2/BanAn");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(getActivity(), "Hehhehe", Toast.LENGTH_SHORT).show();
                for(DataSnapshot postSnapShot: snapshot.getChildren())
                {
                    Table table = new Table();
                    table.TongTien(( postSnapShot.child("TongTien").getValue(float.class)));
                    table.State(postSnapShot.child("TrangThai").getValue(String.class));

                    tableList.add(table);

                    Log.i("Name",postSnapShot.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ReadTables()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout_page1 =(LinearLayout)inflater.inflate(R.layout.fragment_table_list_page1,null);

        gv = (GridView) layout_page1.findViewById(R.id.grid_view);
        ListTablesAdapter tablesAdapter = new ListTablesAdapter(context,R.layout.table_layout_item, tableList);
        gv.setAdapter(tablesAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(context,MonAnActivity.class);
                context.startActivity(intent);
            }
        });

        return layout_page1;
    }
}