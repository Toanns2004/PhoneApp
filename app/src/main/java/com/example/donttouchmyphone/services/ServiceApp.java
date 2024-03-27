package com.example.donttouchmyphone.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.donttouchmyphone.R;

import java.util.Date;

public class ServiceApp extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private ResultReceiver resultReceiver;
    private boolean changeDevice =false;
    private boolean senReceiver = false;
    public static final int RECEIVER_CODE =10;


    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (sensor!= null){
                sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        resultReceiver = intent.getParcelableExtra("resultReceiver");
        senNotification();

        return START_NOT_STICKY;
    }

    private void senNotification() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), getNotificationId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ApplicationApp.CHANNEL_ID)
                .setContentText(getString(R.string.notification))
                .setSmallIcon(R.drawable.disconnect_wifi)
                .setAutoCancel(true);
//                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(getNotificationId(), notification);
        }
    }


    private int getNotificationId(){
        return (int) new Date().getTime();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float a = 11f;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        Log.e("x",x+"");
        Log.e("y",y+"");
        Log.e("z",z+"");

        double total = Math.abs(x) + Math.abs(y) + Math.abs(z);

//        Log.e("total",total+"");
        if (total > a && !senReceiver) {
            changeDevice = true;
            Bundle bundle = new Bundle();
            bundle.putBoolean("changeDevice",changeDevice);
            resultReceiver.send(RECEIVER_CODE,bundle);
            senReceiver=true;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }



}
