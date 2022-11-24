package com.example.oderingfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView1,recyclerView2;
    ArrayList Img =new ArrayList<>(Arrays.asList(R.drawable.img_1,R.drawable.img_2,R.drawable.img_3,R.drawable.img_4));

    ArrayList Img2 =new ArrayList<>(Arrays.asList(R.drawable.res_1,R.drawable.res_2,R.drawable.res_3,R.drawable.res_4));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeFragment = inflater.inflate(R.layout.fragment_home, container, false);

        // Getting reference of recyclerView
        recyclerView1 = (RecyclerView) homeFragment.findViewById(R.id.recycler_list_image_1);
        recyclerView2 = (RecyclerView) homeFragment.findViewById(R.id.recycler_list_image_2);
        // Setting the layout as linear


        // layout for vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        // Sending reference and data to Adapter
        AdapterImage adapter1 = new AdapterImage(getContext(), Img2);
        AdapterImage adapter2 = new AdapterImage(getContext(), Img);
        // Setting Adapter to RecyclerView
        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);
        return homeFragment;
    }
}