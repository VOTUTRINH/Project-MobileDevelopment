package com.example.oderingfood.Restaurant.More;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.oderingfood.R;
import com.example.oderingfood.Restaurant.Bottomnavigation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class TuyChon_Fragment extends Fragment {

    Bottomnavigation bottomnavigation ;
    String user;
    String idRes;
    String role;
    CircleImageView avt_user;
    TextView nav_txt_name,nav_txt_sdt;
    Button btn_res_infor,btn_voucher,btn_out,menu;

    FirebaseDatabase database ;
    DatabaseReference myRef ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tuy_chon_, container, false);
        // Inflate the layout for this fragment
        bottomnavigation = (Bottomnavigation) getActivity();
        user= bottomnavigation.getUser();
        idRes = bottomnavigation.getIdRes();
        role = bottomnavigation.getRole();

        avt_user =(CircleImageView) view.findViewById(R.id.avt_user);
        nav_txt_name =(TextView) view.findViewById(R.id.nav_txt_name);
        nav_txt_sdt =(TextView) view.findViewById(R.id.nav_txt_sdt);
        btn_res_infor =(Button) view.findViewById(R.id.btn_res_infor);
        btn_voucher =(Button) view.findViewById(R.id.btn_voucher);
        btn_out = (Button)  view.findViewById(R.id.btn_out);
        menu = (Button)  view.findViewById(R.id.menu);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user/"+user);

        btn_res_infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Res_infor.class);
                Bundle bundle = new Bundle();
                bundle.putString("idRes", idRes);
                bundle.putString("idUser", user);
                bundle.putString("role",role);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btn_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Voucher.class);
                Bundle bundle = new Bundle();
                bundle.putString("idRes", idRes);
                bundle.putString("idUser", user);
                bundle.putString("role",role);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TuyChon_Menu.class);
                startActivity(intent);
            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Picasso.get().load(snapshot.child("avatar").getValue(String.class).toString()).into(avt_user);
                nav_txt_name.setText(snapshot.child("hoTen").getValue(String.class).toString());
                nav_txt_sdt.setText(snapshot.child("dienThoai").getValue(String.class).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setMessage("Bạn có thật sự muốn rời khỏi quán ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bottomnavigation.finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return view;
    }
}