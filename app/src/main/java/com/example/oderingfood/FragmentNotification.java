package com.example.oderingfood;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.models.NotificationItem;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentNotification extends Fragment {

    RecyclerView recyclerView;
    Context context;
    ArrayList<NotificationItem> items;
    // Using ArrayList to store images data
//    ArrayList courseImg = new ArrayList<>(Arrays.asList(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_background));
//    ArrayList courseName = new ArrayList<>(Arrays.asList("Data Structure", "C++", "C#", "JavaScript", "Java",
//            "C-Language", "HTML 5", "CSS"));
//    ArrayList address = new ArrayList<>(Arrays.asList("Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh",
//            "Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh", "Thu duc, Thanh pho Ho chi Minh"));


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        items = new ArrayList<NotificationItem>();
        items.add(new NotificationItem(R.drawable.ic_person, "A Đã đặt bàn 10", "Danh sách món: A, B, C Danh sách món: A, B, C Danh sách món: A, B, C "));
        items.add(new NotificationItem(R.drawable.ic_person, "Bàn 2 Đã thanh toán", "Danh sách món: A, B, C Danh sách món: A, B, C Danh sách món: A, B, C "));
        items.add(new NotificationItem(R.drawable.ic_person, "Chủ quán vừa cập nhập menu mới", "Danh sách món: A, B, C Danh sách món: A, B, C Danh sách món: A, B, C "));
        items.add(new NotificationItem(R.drawable.ic_person, "B Đã đặt bàn 30", "Danh sách món: A, B, C Danh sách món: A, B, C Danh sách món: A, B, C "));
        items.add(new NotificationItem(R.drawable.ic_person, "Bàn 30 vừa gọi thêm món", "Danh sách món: A, B, C Danh sách món: A, B, C Danh sách món: A, B, C "));
        items.add(new NotificationItem(R.drawable.ic_person, "A Đã đặt bàn 10", "Danh sách món: A, B, C Danh sách món: A, B, C Danh sách món: A, B, C "));
        items.add(new NotificationItem(R.drawable.ic_person, "AXXXXXXXXX", "Danh sách món: A, B, C Danh sách món: A, B, C Danh sách món: A, B, C "));
        items.add(new NotificationItem(R.drawable.ic_person, "A Đã đặt bàn 10", "Danh sách món: A, B, C Danh sách món: A, B, C Danh sách món: A, B, C "));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout fragmentNtf = (LinearLayout) inflater.inflate(R.layout.fragment_tab1, container, false);

        // Getting reference of recyclerView
        recyclerView = (RecyclerView) fragmentNtf.findViewById(R.id.recyclerView);
        // Setting the layout as linear
        // layout for vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        // Sending reference and data to Adapter
        AdapterNotification adapter = new AdapterNotification(getContext(), items);
        // Setting Adapter to RecyclerView
        recyclerView.setAdapter(adapter);



        return fragmentNtf;
    }
}
