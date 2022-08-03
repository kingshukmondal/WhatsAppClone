package com.smartcheck.whatsappclone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TermsAndConditions extends AppCompatActivity {

    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        AlphaAnimation click_animation = new AlphaAnimation(1f, 0.8f);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }


        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("name", "");
        String s2 = sh.getString("number", "");

        if (!s1.equals("")) {
            Intent i = new Intent(getApplicationContext(), HomeScreen.class);
            startActivity(i);
            FirebaseDatabase fc = FirebaseDatabase.getInstance();
            DatabaseReference ref = fc.getReference();
            ref.child("status").child(s2).setValue("1");
            Const.phone_number = s2;
        }
        ll = findViewById(R.id.terms);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(click_animation);
                Intent i = new Intent(getApplicationContext(), PhoneNumberRegistration.class);
                startActivity(i);
            }

        });
    }
}