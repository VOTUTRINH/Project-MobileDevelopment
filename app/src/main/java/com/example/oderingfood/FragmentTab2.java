package com.example.oderingfood;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentTab2 extends Fragment {


    RecyclerView recyclerView2;
    Context context; FloatingActionButton fab;
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
        context=getActivity();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentTab2 = inflater.inflate(R.layout.fragment_tab2, container, false);
        // Getting reference of recyclerView
        recyclerView2 = (RecyclerView) fragmentTab2.findViewById(R.id.recyclerView2);

        // Setting the layout as linear
        // layout for vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView2.setLayoutManager(linearLayoutManager);

        // Sending reference and data to dapter
        AdapterTab adapter = new AdapterTab(getContext(), courseImg, courseName, address);

        // Setting Adapter to RecyclerView
        recyclerView2.setAdapter(adapter);
        fab = (FloatingActionButton) fragmentTab2.findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return fragmentTab2;
    }


}