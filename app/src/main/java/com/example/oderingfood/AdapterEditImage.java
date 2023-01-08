package com.example.oderingfood;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oderingfood.models.Image;

import java.util.ArrayList;

public class AdapterEditImage  extends RecyclerView.Adapter<AdapterEditImage.ViewHolder> {
    ArrayList<Image> Img;
    Context context;
    String idRes;

    // Constructor for initialization
    public AdapterEditImage(Context context, ArrayList<Image> Img,String idRes) {
        this.context = context;
        this.Img = Img;
        this.idRes =idRes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);

        // Passing view to ViewHolder
        AdapterEditImage.ViewHolder viewHolder = new AdapterEditImage.ViewHolder(view);

        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull AdapterEditImage.ViewHolder holder, int position) {
        // TypeCast Object to int type
        String res = Img.get(position).getUrl().toString();
        Glide.with(context).load(res).into(holder.images);

    }

    @Override
    public int getItemCount() {
        // Returns number of items
        // currently available in Adapter
        return Img.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView images;
        public ViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.img_item);


            view.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.add(getAdapterPosition(), R.id.menu_delete_item, 1, "Xóa");
            menu.add(getAdapterPosition(), R.id.menu_edit_item, 2, "Xem");
        }

    }
}
