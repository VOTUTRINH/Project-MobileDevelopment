package com.example.oderingfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView1,recyclerView2;
    TextView txt_home_tongban,txt_home_bantrong,txt_home_diachi,txt_home_tenquan;

    Bottomnavigation bottomnavigation ;
    String user;
    String idRes;
    String role;

    FirebaseDatabase database ;
    DatabaseReference myRef ;

    ArrayList Img =new ArrayList<>();
    ArrayList Img2 =new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bottomnavigation = (Bottomnavigation) getActivity();
        user= bottomnavigation.getUser();
        idRes = bottomnavigation.getIdRes();
        role = bottomnavigation.getRole();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("restaurant/"+idRes);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeFragment = inflater.inflate(R.layout.fragment_home, container, false);

        // Getting reference of recyclerView
        recyclerView1 = (RecyclerView) homeFragment.findViewById(R.id.recycler_list_image_1);
        recyclerView2 = (RecyclerView) homeFragment.findViewById(R.id.recycler_list_image_2);

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

        txt_home_diachi= (TextView) homeFragment.findViewById(R.id.txt_home_diachi);
        txt_home_tenquan=(TextView) homeFragment.findViewById(R.id.txt_home_tenquan);
        txt_home_tongban = (TextView) homeFragment.findViewById(R.id.txt_home_tongban);
        txt_home_bantrong =(TextView) homeFragment.findViewById(R.id.txt_home_bantrong);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                txt_home_tenquan.setText(snapshot.child("TenQuan").getValue().toString());
                txt_home_diachi.setText(snapshot.child("DiaChi").getValue().toString());

                Integer count =0,count2=0;
                try{

                    for (DataSnapshot postSnapshot: snapshot.child("BanAn").getChildren()) {
                        count2++;
                        String status = postSnapshot.child("TrangThai").getValue().toString();
                        if(status.equals("Empty")){
                            count ++;
                        }
                    }
                    txt_home_tongban.setText(String.valueOf(count2));
                    txt_home_bantrong.setText(String.valueOf(count));

                }catch(Exception e)
                {
                    txt_home_bantrong.setText("0");
                    txt_home_tongban.setText("0");
                }
                //load images restaurant

                for(DataSnapshot postSnapshot: snapshot.child("HinhAnh").getChildren()){
                    try{
                    String urlImage = postSnapshot.getValue().toString();
                    Img2.add(urlImage);
                    }catch(Exception e){
                        //not have images
                    }
                }
                    adapter1.notifyDataSetChanged();

                //load menu image

                    for(DataSnapshot postSnapshot: snapshot.child("Menu").getChildren()){
                        try{
                        String link =  postSnapshot.child("urlImage").getValue().toString();
                        Img.add(link);
                        }catch (Exception e){
                            //not have images
                        }

                    }
                    adapter2.notifyDataSetChanged();

                if(Img2.isEmpty()){
                    TextView txt_images_restaurant = (TextView) homeFragment.findViewById(R.id.txt_images_restaurant);
                    txt_images_restaurant.setVisibility(View.INVISIBLE);
                }

                if(Img.isEmpty()){
                    LinearLayout layout_txt_menu = (LinearLayout) homeFragment.findViewById(R.id.layout_txt_menu);
                    layout_txt_menu.setVisibility(View.INVISIBLE);
                }
                //set voucher
                try{
                    TextView txt_voucher = (TextView) homeFragment.findViewById(R.id.txt_voucher);
                    txt_voucher.setText(snapshot.child("Voucher").getValue().toString());
                }catch(Exception e){
                    LinearLayout layout_voucher= (LinearLayout) homeFragment.findViewById(R.id.layout_voucher);
                    layout_voucher.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return homeFragment;
    }


}