package com.example.donttouchmyphone.controll;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationApp extends Application {
    public static final String CHANNEL_ID = "NOTIFICATION_CODE";
    @Override
    public void onCreate() {
        DataLocalManager.init(getApplicationContext());
        createNotification();

        super.onCreate();

    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"notification",importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
