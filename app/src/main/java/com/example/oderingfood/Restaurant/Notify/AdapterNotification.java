package com.example.oderingfood.Restaurant.Notify;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oderingfood.R;
import com.example.oderingfood.models.GlobalVariables;
import com.example.oderingfood.models.NotificationItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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

        Picasso.get().load(this.items.get(position).getNoticeImg()).into(holder.images);
        holder.txt_label.setText(Html.fromHtml(this.items.get(position).getNoticeLabel()));
        holder.txt_content.setText(Html.fromHtml(this.items.get(position).getNoticeContent()));
        holder.txt_time.setText(this.items.get(position).getTimeString());
        if(this.items.get(position).isRead == true){
            Seen(holder.CView, holder.txt_label, holder.status);
        }
        String id = this.items.get(position).getId();
        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database ;
                DatabaseReference myRef ;
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("restaurant/"+ GlobalVariables.pathRestaurentID);
                myRef.child("notification").child(id).child("isRead").setValue(true);

            }
        });

    }

    @Override
    public int getItemCount() {
        // Returns number of items
        // currently available in Adapter
        return items.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView  status;
        CircleImageView images;
        TextView txt_label, txt_content, txt_time;
        CardView CView;
        LinearLayout viewItem;
        public ViewHolder(View view) {
            super(view);
            CView = (CardView) view.findViewById(R.id.nt_card_notice);
            images = (CircleImageView) view.findViewById(R.id.avt_user);
            txt_label = (TextView) view.findViewById(R.id.nt_txt_label_notice);
            txt_content = (TextView) view.findViewById(R.id.nt_txt_content_notice);
            txt_time = (TextView) view.findViewById(R.id.nt_txt_time);
            status = (ImageView) view.findViewById(R.id.nt_img_not_read);
            viewItem = (LinearLayout) view.findViewById(R.id.view);
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

