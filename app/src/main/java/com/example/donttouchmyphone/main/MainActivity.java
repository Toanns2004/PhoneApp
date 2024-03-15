package com.example.donttouchmyphone.main;


import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.alarmsound.AlarmSoundMain;
import com.example.donttouchmyphone.language.MainLanguage;
import com.example.donttouchmyphone.main.broadcast.BroadcastReceiverApp;
import com.example.donttouchmyphone.main.fragment.MainFragment;
import com.example.donttouchmyphone.main.fragment.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FragmentTransaction fragmentTransaction;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ImageButton imageButton,btnSetting,btnBack,btnAd,btnDel;
    TextView textNameApp;
    Dialog dialog;

    private int music;
    private int time;

    boolean wifi;
    static final String key_activity ="previous_activity";

    static final String ACTION_KEY = "MAIN_ACTION";

     BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent !=null && intent.getAction() != null){
                if (intent.getAction().equals(ACTION_KEY)){
                    wifi = intent.getBooleanExtra("broadcastTrue",false);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = findViewById(R.id.frame_layout_main);
        anhXa();
//        broadcastReceiver = new BroadcastReceiverApp();
//        registerNetworkBroadcast();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_main,new MainFragment());
        fragmentTransaction.commit();

        navigationView.setNavigationItemSelectedListener(this);

        //dialog
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.layout_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog));
        dialog.setCancelable(false);

        ImageButton btnDel = dialog.findViewById(R.id.icon_delete);
        RelativeLayout btnDialog = dialog.findViewById(R.id.button_dialog);

        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        //navigationLeft
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDrawer();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSettingsFragment();
                imageButton.setVisibility(View.INVISIBLE);
                btnSetting.setVisibility(View.INVISIBLE);
                btnAd.setVisibility(View.INVISIBLE);
                btnBack.setVisibility(View.VISIBLE);
                textNameApp.setText(R.string.settings);
            }
        });



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMainFragment();
                imageButton.setVisibility(View.VISIBLE);
                btnSetting.setVisibility(View.VISIBLE);
                btnAd.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.INVISIBLE);
                textNameApp.setText(R.string.app_name);
            }
        });

//        onBackPress();
    }

    private void registerNetworkBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void nextSettingsFragment(){
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();

        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction1.replace(R.id.frame_layout_main, settingsFragment);
        fragmentTransaction1.commit();
    }
    private void backMainFragment() {
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        MainFragment mainFragment = new MainFragment();
        fragmentTransaction1.replace(R.id.frame_layout_main, mainFragment);
        fragmentTransaction1.commit();
    }


    private void anhXa(){
        drawerLayout = findViewById(R.id.drawerLayout);
        imageButton = findViewById(R.id.frame_icon);
        navigationView = findViewById(R.id.navigation_view);
        btnSetting = findViewById(R.id.icon_setting);
        btnBack = findViewById(R.id.back_icon);
        btnAd = findViewById(R.id.icon_ad);
        textNameApp = findViewById(R.id.text_nameApp);
        btnDel =findViewById(R.id.icon_delete);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id== R.id.change_language){
            Intent intent1 = new Intent(MainActivity.this, MainLanguage.class);
            intent1.putExtra("activity",key_activity);
            startActivity(intent1);
            navigationView.getMenu().findItem(id).setCheckable(false);
        }else if (id== R.id.support_us) {
            if (!wifi){
                dialog.show();
            }
            navigationView.getMenu().findItem(id).setCheckable(false);
        } else if (id== R.id.share_for_friend) {
            if (!wifi){
                dialog.show();
            }
            navigationView.getMenu().findItem(id).setCheckable(false);
        }else  if (id == R.id.feedback){
            if (!wifi){
                dialog.show();
            }
            navigationView.getMenu().findItem(id).setCheckable(false);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void onBackPress(){
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onDestroy","onResume");
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("alarmSound_sendMain")) {
//            Bundle bundle = intent.getBundleExtra("alarmSound_sendMain");
//            if (bundle != null) {
//                music = bundle.getInt("music");
//                time = bundle.getInt("time");
//                Bundle data = new Bundle();
//                data.putInt("dataMusic", music);
//                data.putInt("dataTime", time);
//                // Tạo mới một instance của MainFragment
//                MainFragment mainFragment = new MainFragment();
//                mainFragment.setArguments(data);
//            }
//        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onDestroy","onStop");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onDestroy","onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
        Log.e("onDestroy","onDestroy");
    }
}