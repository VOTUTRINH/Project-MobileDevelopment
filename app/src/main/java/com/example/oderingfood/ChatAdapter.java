package com.example.oderingfood;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Wrapper;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Message> listMessagesObject;
    private String idRes;
    public static final int FROM_OTHER = 0;
    public static final int TO_OTHER = 1;

    public ChatAdapter(Context context, List<Message> objects, String idRes) {
        mContext = context;
        listMessagesObject = objects;
        this.idRes = idRes;
    }

    @Override
    public int getItemViewType(int position) {
        if (listMessagesObject.get(position) instanceof messageFromOther)
            return FROM_OTHER;
        else if (listMessagesObject.get(position) instanceof messageToOther)
            return TO_OTHER;
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(mContext);
        switch (viewType) {
            case FROM_OTHER:
                View itemView0 = li.inflate(R.layout.chat_from_other, parent, false);
                return new FromOtherViewHolder(itemView0);
            case TO_OTHER:
                View itemView1 = li.inflate(R.layout.chat_to_other, parent, false);
                return new ToOtherViewHolder(itemView1);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case FROM_OTHER:
                messageFromOther user = (messageFromOther) listMessagesObject.get(position);
                FromOtherViewHolder fromOtherViewHolder = (FromOtherViewHolder) holder;
                fromOtherViewHolder.message.setText(user.getMessage());
                Glide.with(mContext).load(((messageFromOther) listMessagesObject.get(position)).getImg()).into(fromOtherViewHolder.img);
                fromOtherViewHolder.name.setText(user.name);
                fromOtherViewHolder.time.setText(user.getDateTime());

                fromOtherViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(fromOtherViewHolder.time.getVisibility() == View.VISIBLE)
                        {
                            fromOtherViewHolder.time.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            fromOtherViewHolder.time.setVisibility(View.VISIBLE);
                        }
                    }
                });
                break;
            case TO_OTHER:
                messageToOther you = (messageToOther) listMessagesObject.get(position);
                ToOtherViewHolder toOtherViewHolder= (ToOtherViewHolder) holder;
                toOtherViewHolder.message.setText(you.getMessage());
                Glide.with(mContext).load(((messageToOther) listMessagesObject.get(position)).getImg()).into(toOtherViewHolder.img);
                toOtherViewHolder.time.setText(you.getDateTime());
                toOtherViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        String id = ((messageToOther) listMessagesObject.get(toOtherViewHolder.getAbsoluteAdapterPosition())).id;
                        ShowDialogDeleteMessage(id);
                        return true;
                    }
                });

                toOtherViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(toOtherViewHolder.time.getVisibility() == View.VISIBLE)
                        {
                            toOtherViewHolder.time.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            toOtherViewHolder.time.setVisibility(View.VISIBLE);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listMessagesObject.size();
    }

    public class FromOtherViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private CircleImageView img;
        private TextView name;
        private TextView time;

        public FromOtherViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.ac_txt_from_other);
            img = (CircleImageView) itemView.findViewById(R.id.ac_img_from_other);
            name = (TextView) itemView.findViewById(R.id.ac_txt_name);
            time = (TextView) itemView.findViewById(R.id.time_msg);
        }
    }

    public class ToOtherViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private CircleImageView img;
        private TextView time;

        public ToOtherViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.ac_txt_to_other);
            img = (CircleImageView) itemView.findViewById(R.id.ac_img_to_other);
            time = (TextView) itemView.findViewById(R.id.time_msg);
        }
    }

    private void ShowDialogDeleteMessage(String idMsg)
    {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.confirm_delete_message);
        dialog.show();

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_accept = (Button) dialog.findViewById(R.id.btn_accept);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMessage(idMsg);
                dialog.dismiss();
            }
        });
    }

    private void deleteMessage(String idMsg)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefListMessage = database.getReference("message/" + idRes + "/" + idMsg);
        if(dbRefListMessage == null) {
            return;
        }



        dbRefListMessage.setValue(null);
    }
}
class Message{
    protected String id;
    protected String message;
    protected String img;
    protected String time;
    protected String date;

    public String getId(){return id;}
    public String getDateTime()
    {
        return date + " " + time;
    }
    public String getDate(){return date;}
    public String getTime() {return time;}
    public String getMessage()
    {
        return message;
    }
    public String getImg()
    {
        return img;
    }
}

class messageFromOther extends  Message{
    public String name;
    messageFromOther(String id, String name, String msg, String img,String date, String time){
        this.name = name;
        this.message = msg;
        this.img = img;
        this.id = id;
        this.date =date;
        this.time = time;
    }
}

class messageToOther extends  Message{
    messageToOther(String id, String msg, String img,String date, String time){
        this.message = msg;
        this.img = img;
        this.id = id;
        this.date = date;
        this.time = time;
    }
}