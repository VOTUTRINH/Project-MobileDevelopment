package com.example.oderingfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.ViewHolder> {
    ArrayList Img;
    Context context;

    // Constructor for initialization
    public AdapterImage(Context context, ArrayList Img) {
        this.context = context;
        this.Img =Img;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);

        // Passing view to ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TypeCast Object to int type
        int res = (int) Img.get(position);
        holder.images.setImageResource(res);
    }

    @Override
    public int getItemCount() {
        // Returns number of items
        // currently available in Adapter
        return Img.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;

        public ViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.img_item);
        }

    }
}
