package com.example.oderingfood;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.oderingfood.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class TuyChon_Fragment extends Fragment {

    Bottomnavigation bottomnavigation ;
    String user;
    String idRes;
    String role;
    CircleImageView avt_user;
    TextView nav_txt_name,nav_txt_sdt;
    Button btn_res_infor,btn_voucher,btn_out;
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



        return view;
    }
}