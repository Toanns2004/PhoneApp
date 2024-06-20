package com.example.donttouchmyphone.controll;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.models.Sound;
import com.example.donttouchmyphone.views.activities.main.MainActivity;

import java.util.Date;

public class ServiceApp extends Service implements SensorEventListener {
    public static final int RECEIVER_CODE = 10;
    public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    private SensorManager sensorManager;
    private Sensor sensor;
    private Sound sound;
    private int time;

    private Bundle bundle;
    private boolean senReceiver = false;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private Handler handler = new Handler();
    private boolean isFlashOn ;

    private int loopVibration;

    private boolean changeDevice=false;

    private ResultReceiver resultReceiver;



    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            if (sensor!= null){
                sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
        vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("NotificationTrampoline")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bundle = intent.getBundleExtra("senServiceData");
        if (bundle!=null){
            resultReceiver = bundle.getParcelable("resultReceiver");
//            Log.e("ServiceApp",resultReceiver+"");
            sound = (Sound) bundle.getSerializable("sendServiceSound");
            time = bundle.getInt("sendServiceTime");
        }
        if (intent.getAction() != null && intent.getAction().equals(ACTION_STOP_SERVICE)) {
            stopSelf();
            DataLocalManager.setService(false);
//            Intent broadcastIntent = new Intent("CHECK_SERVICE");
//            broadcastIntent.putExtra("data", true);
//            sendBroadcast(broadcastIntent);

            return START_NOT_STICKY;
        }



        switch (time) {
            case 15000:
                loopVibration = -1;
                break;
            case 30000:
                loopVibration = 1;
                break;
            case 60000:
                loopVibration = 3;
                break;
            case 120000:
                loopVibration = 7;
                break;

        }

        senNotification();
        return START_NOT_STICKY;
    }

    private void senNotification() {

//        Intent intent = new Intent(this, ServiceApp.class);
//        intent.setAction(ACTION_STOP_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getService(this, getNotificationId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent2 = new Intent(this, MainActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.setAction(ACTION_STOP_SERVICE);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, getNotificationId(), intent2, PendingIntent.FLAG_UPDATE_CURRENT);

       Notification notification = new NotificationCompat.Builder(this, ApplicationApp.CHANNEL_ID)
                .setContentText(getString(R.string.notification))
                .setSmallIcon(R.drawable.image_drawer)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent2)
               .build();

        startForeground(getNotificationId(),notification);
    }


    private int getNotificationId(){
        return (int) new Date().getTime();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float a = 3f;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        Log.e("x",x+"");
        Log.e("y",y+"");
        Log.e("z",z+"");

        double total = Math.abs(x) + Math.abs(y) + Math.abs(z);

        Log.e("total",total+"");
        if (total > a && !senReceiver) {
            changeDevice = true;
            Bundle bundle = new Bundle();
            bundle.putBoolean("changeDevice",changeDevice);
            resultReceiver.send(RECEIVER_CODE, bundle);
            isPlaySound(sound,time);
            isPlayVibration(loopVibration);
            isFlashLightActive(time);
            senReceiver=true;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    private void isPlaySound(Sound sound,int time){
        if (DataLocalManager.getSound()) {
            DataLocalManager.setSound(true);

            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), sound.getMusic());
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                    }
                }, time);

            } else if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

        private void isPlayVibration(int loopVibration){
        if (DataLocalManager.getVibration()){
            DataLocalManager.setVibration(true);
            if (vibrator.hasVibrator()){
                String vibrationType = DataLocalManager.getRadioVibration();
                if (vibrationType!=null){
                    if (vibrationType.equals("default")){
                        getVibrationDefault(loopVibration);
                    } else if (vibrationType.equals("strong")) {
                        getVibrationStrong(loopVibration);
                    }else if (vibrationType.equals("heart")){
                        getVibrationHeart(loopVibration);
                    }else {
                        getVibrationDefault(loopVibration);
                    }

                }

            }
        }

    }

        private void getVibrationDefault(int loopVibration){
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                long[] pattern = {0,3000,0,3000,0,3000,0,3000,0,3000};
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, loopVibration));
            } else {
                vibrator.vibrate(2000);
            }
        }

    }

    private void getVibrationStrong(int loopVibration){
        if (vibrator.hasVibrator()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                long[] pattern = {0, 200, 500, 200, 500, 200,500, 200, 500,200, 200, 500, 200, 500, 200,500, 200, 500, 200, 500, 200, 500, 200,500, 200, 500, 200, 500, 200, 500, 200,500, 200, 500, 200, 500, 200, 500, 200,500, 200, 500};
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, loopVibration));
            } else {
                vibrator.vibrate(2000);
            }

        }

    }

    private void getVibrationHeart(int loopVibration){
        if (vibrator.hasVibrator()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                long[] pattern = {0, 400, 200, 400, 300, 400,300,1000, 400, 200, 400, 300, 400,300,1000, 400, 200, 400, 300, 400,300,1000, 400, 200, 400, 300, 400,300,1000, 400, 200, 400, 300, 400,300,1000, 400, 200, 400, 300, 400,300,1000} ;
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, loopVibration));
            } else {
                vibrator.vibrate(2000);
            }

        }

    }


    private Runnable flashDefault = new Runnable() {
        @Override
        public void run() {
            if (!isFlashOn){
                OnlFlash();
                isFlashOn = true;
            }else {
                OffFlash();
                isFlashOn = false;
            }
            handler.postDelayed(this, 1000);
        }
    };

    private Runnable flashDisco = new Runnable() {
        @Override
        public void run() {
            if (!isFlashOn){
                OnlFlash();
                isFlashOn = true;
            }else {
                OffFlash();
                isFlashOn = false;
            }
            handler.postDelayed(this, 600);
        }
    };

    public Runnable flashSos = new Runnable() {
        @Override
        public void run() {
            if (!isFlashOn){
                OnlFlash();
                isFlashOn = true;
            }else {
                OffFlash();
                isFlashOn = false;
            }
            handler.postDelayed(this, 300);
        }
    };


    private void OnlFlash(){
        try {
            CameraManager cameraManager = (CameraManager) getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            isFlashOn = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void OffFlash(){
        try {
            CameraManager cameraManager = (CameraManager) getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            isFlashOn = false;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void stopFlashing() {
        handler.removeCallbacksAndMessages(null);
    }

    private void isFlashLightActive(int time){
        if (DataLocalManager.getFlashLight()){
            DataLocalManager.setFlashLight(true);
            String flashType = DataLocalManager.getRadioFlash();
            if (flashType !=null) {
                if (flashType.equals("default")) {
                    handler.post(flashDefault);
                } else if (flashType.equals("disco")) {
                    handler.post(flashDisco);
                } else if (flashType.equals("sos")) {
                    handler.post(flashSos);
                } else {
                    handler.post(flashDefault);
                }

            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopFlashing();
                    OffFlash();
                }
            },time);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.release();
            mediaPlayer=null;
        }
        sensorManager.unregisterListener(this);
        vibrator.cancel();
        OffFlash();
        stopFlashing();
    }



}
