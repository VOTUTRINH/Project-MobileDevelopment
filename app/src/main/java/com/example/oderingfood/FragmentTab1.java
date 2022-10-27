package com.example.oderingfood;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentTab1 extends Fragment {

    RecyclerView recyclerView;
    Context context;
    // Using ArrayList to store images data
    ArrayList courseImg = new ArrayList<>(Arrays.asList(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background));
    ArrayList courseName = new ArrayList<>(Arrays.asList("Data Structure", "C++", "C#", "JavaScript", "Java",
            "C-Language", "HTML 5", "CSS"));
    ArrayList address = new ArrayList<>(Arrays.asList("Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh",
            "Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh"));


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       LinearLayout fragmentTab1 = (LinearLayout) inflater.inflate(R.layout.fragment_tab1, container, false);

        // Getting reference of recyclerView
        recyclerView = (RecyclerView) fragmentTab1.findViewById(R.id.recyclerView);
        // Setting the layout as linear
        // layout for vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        // Sending reference and data to Adapter
        AdapterTab adapter = new AdapterTab(getContext(), courseImg, courseName, address);
        // Setting Adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        return fragmentTab1;
    }


}