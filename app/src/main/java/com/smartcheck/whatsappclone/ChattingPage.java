package com.smartcheck.whatsappclone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartcheck.whatsappclone.Adapter.Chatpageadapter;
import com.smartcheck.whatsappclone.Modalclass.GetChats;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattingPage extends AppCompatActivity {

    View includedlayot;
    LinearLayout items;
    EditText msg;
    ImageView ivsend, camera, attachment, attachment2, profilepic;
    TextView tv_name;
    RecyclerView displaychats;
    String receivernumber;
    Chatpageadapter adapter;
    List<GetChats> list;
    Runnable run;
    Handler handler;
    LinearLayoutManager manager;
    DatabaseReference mDatabase;
    FirebaseDatabase mFirebaseInstance;
    DatabaseReference dref;
    ValueEventListener val;

    int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main_page);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.whatsapp1));
            Intent i = getIntent();
            String name = i.getStringExtra("name");
            String uri = i.getStringExtra("image");
            profilepic = findViewById(R.id.profilepic);
            Glide.with(getApplicationContext())
                    .load(uri)
                    .error(R.drawable.whatsappprofilephoto)
                    .into(profilepic);
            receivernumber = i.getStringExtra("number");
            tv_name = findViewById(R.id.chatname);
            tv_name.setText(name);

            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String sendernumber = sh.getString("number", "");
            seenmessage(sendernumber);

            handler = new Handler();
            TextView lastseen = findViewById(R.id.lastseen);
            run = new Runnable() {
                @Override
                public void run() {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("status").child(receivernumber);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {
                                if (snapshot.getValue().equals("1")) {
                                    // Toast.makeText(ChattingPage.this, receivernumber, Toast.LENGTH_SHORT).show();
                                    lastseen.setVisibility(View.VISIBLE);
                                } else {
                                    //Toast.makeText(ChattingPage.this, "not", Toast.LENGTH_SHORT).show();
                                    lastseen.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //Toast.makeText(ChattingPage.this, "unavailabel", Toast.LENGTH_SHORT).show();
                        }
                    });
                    handler.postDelayed(this, 1000);
                }
            };
            handler.post(run);

            includedlayot = findViewById(R.id.include_sendmsg);
            msg = includedlayot.findViewById(R.id.edt_message);
            items = includedlayot.findViewById(R.id.itemsvisible);
            ivsend = includedlayot.findViewById(R.id.iv_send);
            displaychats = findViewById(R.id.displaychats);
            attachment = includedlayot.findViewById(R.id.attachment);
            attachment2 = includedlayot.findViewById(R.id.attachment2);
            manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
            displaychats.smoothScrollToPosition(displaychats.getBottom());
            displaychats.setLayoutManager(manager);
            list = new ArrayList<>();
            msg.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    items.setVisibility(View.INVISIBLE);
                    attachment2.setVisibility(View.VISIBLE);
                    if (s.length() == 0) {
                        items.setVisibility(View.VISIBLE);
                        attachment2.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            showMessages();
            ivsend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String m = msg.getText().toString();
                    if (m.length() != 0) {
                        sendMessage(receivernumber, sendernumber, m);
                        msg.setText("");
                    } else {
                        msg.requestFocus();
                    }
                }
            });


        }
    }

    void seenmessage(String number) {
        dref = FirebaseDatabase.getInstance().getReference("chats");

        val = dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    GetChats user = dataSnapshot1.getValue(GetChats.class);
                    if ((user.getSender().equals(number) && user.getReceiver().equals(receivernumber)) || (user.getSender().equals(receivernumber) && user.getReceiver().equals(number))) {
                        if (user.getReceiver().equals(number)) {
                            DatabaseReference dref = mFirebaseInstance.getReference("chats").child(dataSnapshot1.getKey());
                            Map<String, Object> hashMap = new HashMap<>();
                            hashMap.put("visible", "t");
                            dref.updateChildren(hashMap);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showMessages() {

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference("chats");
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String mynumber = sh.getString("number", "");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    GetChats user = dataSnapshot1.getValue(GetChats.class);
                    if ((user.getSender().equals(mynumber) && user.getReceiver().equals(receivernumber)) || (user.getSender().equals(receivernumber) && user.getReceiver().equals(mynumber))) {
                        list.add(user);

                    }
                    if (list.size() > 0)
                        displaychats.scrollToPosition(list.size() - 1);

                }
                adapter = new Chatpageadapter(list, getApplicationContext(), receivernumber, mynumber);
                displaychats.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void sendMessage(String receivernumber, String userNumber, String message) {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("sender", userNumber);
        hash.put("receiver", receivernumber);
        hash.put("message", message);
        hash.put("visible", "f");
        Calendar c = Calendar.getInstance();
        Timestamp t1 = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String strDate = sdf.format(c.getTime());
        hash.put("time", strDate);
        DatabaseReference dref = mFirebaseInstance.getReference("user").child(userNumber);
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("last_message", message);
        hashMap.put("last_time", strDate);
        hashMap.put("timestamp", Long.valueOf(t1.getTime()));
        dref.updateChildren(hashMap);
        DatabaseReference dref1 = mFirebaseInstance.getReference("user").child(receivernumber);
        Map<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("last_time", strDate);
        hashMap1.put("last_message", message);
        hashMap1.put("timestamp", Long.valueOf(t1.getTime()));
        dref1.updateChildren(hashMap1);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("chats").child(String.valueOf(t1.getTime())).setValue(hash);
    }

    @Override
    protected void onDestroy() {
        dref.removeEventListener(val);
        handler.removeCallbacks(run);
        super.onDestroy();
    }

}
