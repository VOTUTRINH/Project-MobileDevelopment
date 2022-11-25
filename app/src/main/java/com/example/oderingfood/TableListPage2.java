package com.example.oderingfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableListPage2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableListPage2 extends Fragment {

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
    List<Table> listTable = new ArrayList<Table>();


    public TableListPage2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableListPage2.
     */
    // TODO: Rename and change types and number of parameters
    public static TableListPage2 newInstance(String param1, String param2) {
        TableListPage2 fragment = new TableListPage2();
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
            // tablesActivity = (TablesActivity)getActivity();
            tablesActivity = new TablesActivity();
        }catch (Exception e)
        {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout_page2 =(LinearLayout)inflater.inflate(R.layout.fragment_table_list_page2,null);

        gv = (GridView) layout_page2.findViewById(R.id.grid_view);
        ListTablesAdapter tablesAdapter = new ListTablesAdapter(context,R.layout.table_layout_item, listTable);
        gv.setAdapter(tablesAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(context,ChatActivity.class);
                context.startActivity(intent);
            }
        });
        return layout_page2;
    }
}