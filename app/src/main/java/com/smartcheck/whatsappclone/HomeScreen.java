package com.smartcheck.whatsappclone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.smartcheck.whatsappclone.Adapter.FragmentAdapter;

public class HomeScreen extends AppCompatActivity {

    TabLayout tab_layout;
    ViewPager viewPager;
    ConnectivityManager.NetworkCallback connectivityCallback;
    View check;
    boolean isConnected = true;
    ImageView searchicon;
    LinearLayout searchview;
    ImageView backtop;
    EditText searchforname;
    private boolean monitoringConnectivity = false;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.whatsapp1));
        }

        viewPager = findViewById(R.id.viewPager);
        tab_layout = findViewById(R.id.tab_layout);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        tab_layout.setupWithViewPager(viewPager);
        check = findViewById(R.id.netinclude);
        searchicon = findViewById(R.id.searchicon);
        searchforname = findViewById(R.id.searchforname);
        searchforname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        searchview = findViewById(R.id.searchview);
        searchview.setVisibility(View.GONE);
        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchPage.class);
                startActivity(i);

            }
        });

        backtop = findViewById(R.id.backtop);
        backtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchview.setVisibility(View.VISIBLE);
                searchview.setVisibility(View.GONE);

            }
        });


        connectivityCallback
                = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                isConnected = true;
                Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(i);
            }

            @Override
            public void onLost(Network network) {
                isConnected = false;
                check.setVisibility(View.VISIBLE);
                Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(i);
            }
        };


    }

    @Override
    public void onBackPressed() {
        Const.fun();
        finishAffinity();
        super.onBackPressed();

    }

   /* public boolean   permission()
    {
        ConnectivityManager connectivityManager = ((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }*/

    @Override
    protected void onDestroy() {
        Const.fun();
        finishAffinity();
        super.onDestroy();
    }

    // Method to check network connectivity in Main Activity
    private void checkConnectivity() {
        // here we are getting the connectivity service from connectivity manager
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);

        // Getting network Info
        // give Network Access Permission in Manifest
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // isConnected is a boolean variable
        // here we check if network is connected or is getting connected
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (!isConnected) {
            // SHOW ANY ACTION YOU WANT TO SHOW
            // WHEN WE ARE NOT CONNECTED TO INTERNET/NETWORK

            check.setVisibility(View.VISIBLE);
// if Network is not connected we will register a network callback to  monitor network
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(), connectivityCallback);
            monitoringConnectivity = true;
        }

    }

    @Override
    protected void onPause() {
        // if network is being moniterd then we will unregister the network callback
        if (monitoringConnectivity) {
            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(connectivityCallback);
            monitoringConnectivity = false;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnectivity();
    }


}