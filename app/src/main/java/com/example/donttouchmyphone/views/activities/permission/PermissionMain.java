package com.example.donttouchmyphone.views.activities.permission;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.donttouchmyphone.views.activities.main.MainActivity;
import com.example.donttouchmyphone.R;

public class PermissionMain extends AppCompatActivity {
    private Button btnContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_main);

        btnContinue = findViewById(R.id.permission_btn_continue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PermissionMain.this, MainActivity.class);
//                startActivity(intent);
            }
        });
    }
}