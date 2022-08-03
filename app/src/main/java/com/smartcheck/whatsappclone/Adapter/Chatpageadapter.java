package com.smartcheck.whatsappclone.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smartcheck.whatsappclone.Modalclass.GetChats;
import com.smartcheck.whatsappclone.R;

import java.util.List;


public class Chatpageadapter extends RecyclerView.Adapter<Chatpageadapter.viewholder> {
    public static final int MSG_TYPE_LEFT = 1;
    public static final int MSG_TYPE_RIGHT = 0;
    Context mCon;
    FirebaseUser fuser;
    String receivernumber, sender;
    private List<GetChats> list;

    public Chatpageadapter(List<GetChats> list, Context context, String receivernumber, String sender) {
        mCon = context;
        this.list = list;
        this.receivernumber = receivernumber;
        this.sender = sender;
    }

    @NonNull
    @Override
    public Chatpageadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender, parent, false);
        }
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Chatpageadapter.viewholder holder, int position) {

        String sender, receiver, message, time, vi;
        sender = list.get(position).getSender();
        receiver = list.get(position).getReceiver();
        message = list.get(position).getMessage();
        time = list.get(position).getTime();
        vi = list.get(position).getVisible();

        holder.setData(sender, receiver, message, time, vi);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getReceiver().equals(receivernumber)) {
            return MSG_TYPE_LEFT;
        } else {
            return MSG_TYPE_RIGHT;
        }


    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView chat, timer, lastmessagetime;
        ImageView seen;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            chat = itemView.findViewById(R.id.chat);
            timer = itemView.findViewById(R.id.time);
            seen = itemView.findViewById(R.id.seenstatus);
            lastmessagetime = itemView.findViewById(R.id.lastmessagetime);
        }

        @SuppressLint("NewApi")
        public void setData(String sender, String receiver, String message, String t, String vi) {
            chat.setText(message);

            timer.setText(t);
            if (vi.equals("t")) {
                seen.setBackgroundResource(R.drawable.btcheck2);
            } else {
                seen.setBackgroundResource(R.drawable.grerytick1);
            }


        }
    }
}



