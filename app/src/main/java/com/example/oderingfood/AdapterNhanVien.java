package com.example.oderingfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterNhanVien extends BaseAdapter {
    Context context;
    int layout;
    List<NhanVien> arrayList;

    public AdapterNhanVien(Context context, int layout, List<NhanVien> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private  class ViewHolder{
        TextView tvTen;
        ImageView imgAvt;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            viewHolder=new ViewHolder();
            viewHolder.tvTen=(TextView) view.findViewById(R.id.tvTen);
            viewHolder.imgAvt=(ImageView) view.findViewById(R.id.avt);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.tvTen.setText(arrayList.get(i).tenNhanVien);
        viewHolder.imgAvt.setImageResource(arrayList.get(i).avt);
        return view;
    }
}
