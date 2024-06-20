package com.example.donttouchmyphone.views.activities.splashs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.controll.DataLocalManager;
import com.example.donttouchmyphone.views.activities.language.MainLanguageActivity;
import com.example.donttouchmyphone.views.activities.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (!DataLocalManager.getFirstInstalled()){
                     intent = new Intent(SplashActivity.this, MainLanguageActivity.class);

                }else {
                     intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },2000);
    }
}