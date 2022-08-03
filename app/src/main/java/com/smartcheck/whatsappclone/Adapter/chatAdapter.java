package com.smartcheck.whatsappclone.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.smartcheck.whatsappclone.ChattingPage;
import com.smartcheck.whatsappclone.Modalclass.ModalClassChat;
import com.smartcheck.whatsappclone.R;

import java.util.List;


public class chatAdapter extends RecyclerView.Adapter<chatAdapter.viewholder> {

    Context mCon;
    private List<ModalClassChat> list;

    public chatAdapter(List<ModalClassChat> list, Context context) {
        mCon = context;
        this.list = list;
    }

    @NonNull
    @Override
    public chatAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatsrow, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull chatAdapter.viewholder holder, int position) {

        String name = list.get(position).getName();
        String number = list.get(position).getPhonenumber();
        String uri = list.get(position).getIv();
        String lastmessage = list.get(position).getLast_message();
        String lasttime = list.get(position).getLast_time();
        Long timestamp = list.get(position).getTimestamp();
        holder.setData(name, number, uri, lastmessage, lasttime, String.valueOf(timestamp));
        //   Toast.makeText(mCon, "lastmessage"+lastmessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {


        LinearLayout ll1;
        CardView unreadcircle;
        private TextView name, number, llmmtt;
        private ImageView profilepic;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            ll1 = itemView.findViewById(R.id.liner_user);
            profilepic = itemView.findViewById(R.id.profilepic);
            llmmtt = itemView.findViewById(R.id.lastmessagetime);
            unreadcircle = itemView.findViewById(R.id.unreadcircle);
        }

        public void setData(String nm, String nu, String uri, String lastmessage, String lastmessagetime, String timesstamp) {
            name.setText(nm);
            number.setText(lastmessage);
            if (lastmessagetime != null)


                llmmtt.setText(lastmessagetime);
            else
                llmmtt.setVisibility(View.INVISIBLE);
//            FirebaseDatabase.getInstance().getReference().child("chats").child(timesstamp).child("visible").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if (!task.isSuccessful()) {
//                        Log.e("firebase", "Error getting data", task.getException());
//                    }
//                    else {
//                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                        String k=String.valueOf(task.getResult().getValue());
//                        if(k.equals("t"))
//                            unreadcircle.setVisibility(View.INVISIBLE);
//                        else
//                            unreadcircle.setVisibility(View.VISIBLE);
//                        Toast.makeText(mCon, timesstamp+String.valueOf(task.getResult().getValue()), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });

            Glide.with(mCon)
                    .load(uri)
                    .error(R.drawable.whatsappprofilephoto)
                    .into(profilepic);
            ll1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mCon, ChattingPage.class);
                    i.putExtra("name", nm);
                    i.putExtra("number", nu);
                    i.putExtra("image", uri);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mCon.startActivity(i);
                }
            });
        }
    }
}



