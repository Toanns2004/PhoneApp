package com.example.donttouchmyphone.views.activities.main;


import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.MutableContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
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
import com.example.donttouchmyphone.views.activities.language.MainLanguageActivity;
import com.example.donttouchmyphone.services.CheckInternet;
import com.example.donttouchmyphone.views.fragments.MainFragment;
import com.example.donttouchmyphone.views.fragments.SettingsFragment;
import com.example.donttouchmyphone.services.DataLocalManager;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String MY_ACTION = "MY_ACTION";
    private FragmentTransaction fragmentTransaction;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ImageButton imageButton,btnSetting,btnBack,btnAd,btnDel;
    private TextView textNameApp;
    private Dialog dialog;
    private boolean wifi;
    private static final String KEY_PREVIOUS_ACTIVITY ="KEY_PREVIOUS_ACTIVITY";

    CheckInternet broadcastReceiverApp;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FrameLayout frameLayout = findViewById(R.id.frame_layout_main);
        anhXa();
        broadcastReceiverApp = new CheckInternet();
        registerNetworkBroadcast();

        Intent intent = getIntent();
        wifi = intent.getBooleanExtra("WIFI",false);
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

        onBackPress();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        MutableContextWrapper contextWrapper = new MutableContextWrapper(newBase);
        updateLanguage(contextWrapper);

        super.attachBaseContext(contextWrapper);
    }

    private void updateLanguage(ContextWrapper contextWrapper) {
        String languageCode = DataLocalManager.getLanguageCode(); // Lấy mã ngôn ngữ từ SharedPreferences hoặc nơi lưu trữ khác
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        Resources resources = contextWrapper.getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void registerNetworkBroadcast() {
        IntentFilter intentFilter= new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiverApp,intentFilter);
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
            Intent intent1 = new Intent(MainActivity.this, MainLanguageActivity.class);
            intent1.putExtra("activity",KEY_PREVIOUS_ACTIVITY);
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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onDestroy","onResume");
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
        unregisterReceiver(broadcastReceiverApp);
        Log.e("onDestroy","onDestroy");
    }
}