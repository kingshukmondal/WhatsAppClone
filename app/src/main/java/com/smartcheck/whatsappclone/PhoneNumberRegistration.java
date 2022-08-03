package com.smartcheck.whatsappclone;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PhoneNumberRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    TextView tv, phonenumber;
    LinearLayout next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        AlphaAnimation click_animation = new AlphaAnimation(1f, 0.8f);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        phonenumber = findViewById(R.id.phonenumber);
        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Const.phone_number = "+91" + phonenumber.getText().toString();
                ConfirmBox1 com = new ConfirmBox1();
                com.show(getSupportFragmentManager(), "confirmation_box1");
            }
        });
        tv = findViewById(R.id.tv_country);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.arrayitems, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        tv.setText(text);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}