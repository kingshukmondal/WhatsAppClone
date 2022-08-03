package com.smartcheck.whatsappclone.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartcheck.whatsappclone.Adapter.chatAdapter;
import com.smartcheck.whatsappclone.Const;
import com.smartcheck.whatsappclone.Modalclass.GetChats;
import com.smartcheck.whatsappclone.Modalclass.ModalClassChat;
import com.smartcheck.whatsappclone.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ChatsFragment extends Fragment {
    RecyclerView chatsRecyclerView;
    chatAdapter adapter;
    List<ModalClassChat> list;
    LinearLayoutManager manager;
    DatabaseReference mDatabase;
    FirebaseDatabase mFirebaseInstance;
    ProgressDialog pd;
    ShimmerFrameLayout container;
    View root;

    String last_message = "";
    String lasttime = "";
    String unread = "";
    String str = "";

    public ChatsFragment() {

    }

    private void setData() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference("user");
        container = root.findViewById(R.id.shimmer_view_container);
        container.startShimmer();

        mDatabase.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModalClassChat user = dataSnapshot1.getValue(ModalClassChat.class);
                    user.setLastmessage(setLmessage(Const.phone_number, user.getPhonenumber()));
                    if (!user.getPhonenumber().equals(Const.phone_number))
                        list.add(user);

                }
                Collections.reverse(list);
                adapter = new chatAdapter(list, getContext());
                chatsRecyclerView.setAdapter(adapter);
                container.stopShimmer();
                container.setVisibility(View.GONE);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String setLmessage(String mynumber, String phonenumber) {


        FirebaseDatabase mmFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mmDatabase = mmFirebaseInstance.getReference("chats");
        mmDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String p = "";
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    GetChats user = dataSnapshot1.getValue(GetChats.class);
                    if ((user.getSender().equals(mynumber) && user.getReceiver().equals(phonenumber)) || (user.getSender().equals(phonenumber) && user.getReceiver().equals(mynumber))) {
                        p = user.getMessage();
                    }
                }
                last_message = p;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        return str;
    }

//    private String fun1(String phone_number) {
//
//        mFirebaseInstance = FirebaseDatabase.getInstance();
//        mDatabase = mFirebaseInstance.getReference("chats");
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//
//                    GetChats user = dataSnapshot1.getValue(GetChats.class);
//                    if ((user.getSender().equals(mynumber) && user.getReceiver().equals(receivernumber)) || (user.getSender().equals(receivernumber) && user.getReceiver().equals(mynumber))) {
//
//
//                    }
//                    if (list.size() > 0)
//                        displaychats.scrollToPosition(list.size() - 1);
//
//                }
//                adapter = new Chatpageadapter(list, getApplicationContext(), receivernumber, mynumber);
//                displaychats.setAdapter(adapter);
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//        return "";
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_chats, container, false);
        // Inflate the layout for this fragment
        chatsRecyclerView = root.findViewById(R.id.chatsRecyclerView);
        manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        chatsRecyclerView.setLayoutManager(manager);
        list = new ArrayList<>();


        setData();
        return root;
    }

}