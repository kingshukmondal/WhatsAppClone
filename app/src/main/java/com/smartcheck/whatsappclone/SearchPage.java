package com.smartcheck.whatsappclone;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smartcheck.whatsappclone.Adapter.chatAdapter;
import com.smartcheck.whatsappclone.Modalclass.ModalClassChat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchPage extends AppCompatActivity {


    RecyclerView searcheditems;
    chatAdapter adapter;
    List<ModalClassChat> list;
    LinearLayoutManager manager;
    DatabaseReference mDatabase;
    FirebaseDatabase mFirebaseInstance;
    ProgressDialog pd;
    ShimmerFrameLayout container;
    View root;
    ImageView backtop;
    TextView nodata;
    EditText searchforname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.whatsapp1));
        }

        setupWindowAnimations();


        backtop = findViewById(R.id.backtop);
        searcheditems = findViewById(R.id.searcheditems);
        manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        searcheditems.setLayoutManager(manager);
        list = new ArrayList<>();
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        searchforname = findViewById(R.id.searchforname);
        setData();
        searchforname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String k = searchforname.getText().toString();
                if (k == "") {
                    setData();
                } else {
                    setData(k);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        backtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setData(String k) {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference("user");

        mDatabase.orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //   Toast.makeText(getContext(), Const.phone_number, Toast.LENGTH_SHORT).show();
                    ModalClassChat user = dataSnapshot1.getValue(ModalClassChat.class);
                    {
                        // Toast.makeText(getContext(), user.getPhonenumber(), Toast.LENGTH_SHORT).show();
                        if (!user.getPhonenumber().equals(Const.phone_number) && user.getName().toLowerCase().contains(k.toLowerCase(Locale.ROOT)))
                            list.add(user);
                    }
                }
                if (list.isEmpty())
                    nodata.setVisibility(View.VISIBLE);
                else
                    nodata.setVisibility(View.GONE);
                adapter = new chatAdapter(list, getApplicationContext());
                searcheditems.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setData() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference("user");

        mDatabase.orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //   Toast.makeText(getContext(), Const.phone_number, Toast.LENGTH_SHORT).show();
                    ModalClassChat user = dataSnapshot1.getValue(ModalClassChat.class);
                    {
                        // Toast.makeText(getContext(), user.getPhonenumber(), Toast.LENGTH_SHORT).show();
                        if (!user.getPhonenumber().equals(Const.phone_number))
                            list.add(user);
                    }
                }
                adapter = new chatAdapter(list, getApplicationContext());
                searcheditems.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

}