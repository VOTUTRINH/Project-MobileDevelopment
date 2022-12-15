package com.example.oderingfood;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oderingfood.models.NotificationItem;

import java.sql.Time;
import java.util.ArrayList;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {
    ArrayList<NotificationItem> items;
    Context context;

    // Constructor for initialization
    public AdapterNotification(Context context, ArrayList arrayItem) {
        this.context = context;
        this.items = arrayItem;
    }
    @NonNull
    @Override
    public AdapterNotification.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);

        // Passing view to ViewHolder
        AdapterNotification.ViewHolder viewHolder = new AdapterNotification.ViewHolder(view);

        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull AdapterNotification.ViewHolder holder, int position) {
        // TypeCast Object to int type
        //holder.images.setImageResource(this.items.get(position).getNoticeImg());
        holder.txt_label.setText(this.items.get(position).getNoticeLabel());
        holder.txt_content.setText(this.items.get(position).getNoticeContent());
        holder.txt_time.setText(this.items.get(position).getTimeString());
        if(this.items.get(position).isRead == true){
            Seen(holder.CView, holder.txt_label, holder.status);
        }

    }

    @Override
    public int getItemCount() {
        // Returns number of items
        // currently available in Adapter
        return items.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images, status;
        TextView txt_label, txt_content, txt_time;
        CardView CView;
        public ViewHolder(View view) {
            super(view);
            CView = (CardView) view.findViewById(R.id.nt_card_notice);
            images = (ImageView) view.findViewById(R.id.nt_img_notice);
            txt_label = (TextView) view.findViewById(R.id.nt_txt_label_notice);
            txt_content = (TextView) view.findViewById(R.id.nt_txt_content_notice);
            txt_time = (TextView) view.findViewById(R.id.nt_txt_time);
            status = (ImageView) view.findViewById(R.id.nt_img_not_read);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Seen(CView,txt_label,status);
                }
            });
        }

    }
    void Seen(CardView c, TextView label, ImageView status){
        c.setBackgroundColor(Color.argb(1,240,240,240));
        label.setTypeface(null, Typeface.NORMAL);
        status.setVisibility(View.GONE);
    }
}

