package com.example.donttouchmyphone.views.activities.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.views.activities.intro.adapter.IntroAdapter;
import com.example.donttouchmyphone.views.activities.main.MainActivity;
import com.example.donttouchmyphone.views.activities.permission.PermissionMain;

import me.relex.circleindicator.CircleIndicator3;

public class IntroActivity extends AppCompatActivity {

    private Button btnContinue;
    private ViewPager2 viewPager2;
    private CircleIndicator3 circleIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        viewPager2 = findViewById(R.id.viewPager);
        btnContinue = findViewById(R.id.button_continue_intro);
        circleIndicator = findViewById(R.id.indicator_intro);


        IntroAdapter introAdapter = new IntroAdapter(this);
        viewPager2.setAdapter(introAdapter);
        circleIndicator.setViewPager(viewPager2);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id_Fragment = viewPager2.getCurrentItem();
                if (id_Fragment == 0){
                    viewPager2.setCurrentItem(1);
                } else if (id_Fragment ==1) {
                    viewPager2.setCurrentItem(2);
                }else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}