package com.example.donttouchmyphone.main.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;


import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.ResultReceiver;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.alarmsound.AlarmSoundMain;
import com.example.donttouchmyphone.how.HowToActivity;
import com.example.donttouchmyphone.model.Sound;
import com.example.donttouchmyphone.adapter.SoundAdapter;
import com.example.donttouchmyphone.main.iface.IClickItem;
import com.example.donttouchmyphone.sev.DataLocalManager;
import com.example.donttouchmyphone.sev.ServiceApp;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment  {
    RecyclerView rcl;
    SoundAdapter adapter;
    List<Sound> list;
    Sound sound ;
    RelativeLayout rlt;
    TextView txtActive;
    View view;
    private ResultReceiver resultReceiver;
    ImageButton btnHow;
    boolean service=false;
    private boolean changeDevice;
    LottieAnimationView animationView;
    private MediaPlayer mediaPlayer;
    private int time ;
    AudioManager audioManager;
    Vibrator vibrator;
    boolean checkSound, checkVibration,checkFlash ;
    boolean isFlashOn ;
    private Handler handler = new Handler();

    IClickItem iClickItem = new IClickItem() {
        @Override
        public void getItem(Sound sound) {
            sendAlarmSound(sound);

        }
    };

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK){
                        Intent intent = o.getData();
                        Bundle bundle = null;
                        if (intent != null) {
                            bundle = intent.getBundleExtra("sendMainFragment");
                        }
                        if (bundle!=null){
                            sound = (Sound) bundle.getSerializable("SOUND");
                            time = bundle.getInt("time");
                            checkSound = bundle.getBoolean("sound_alarm");
                            checkVibration = bundle.getBoolean("vibration_alarm");
                            checkFlash = bundle.getBoolean("flash_alarm");
                            DataLocalManager.setSoundAlarm(sound);
                            DataLocalManager.setTimeValue(time);
                            DataLocalManager.setSound(checkSound);
                            DataLocalManager.setVibration(checkVibration);
                            DataLocalManager.setFlashLight(checkFlash);
                        }
                    }
                }
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main,container,false);
        // Inflate the layout for this fragment
        anhXa();



        adapter = new SoundAdapter(list,iClickItem);
        rcl.setLayoutManager(new GridLayoutManager(requireActivity(),2));
        rcl.setAdapter(adapter);

        if (!DataLocalManager.getFirstInstalled()){
            sound = list.get(0);
            time = 15000;
            checkSound =true;
            DataLocalManager.setTimeValue(time);
            DataLocalManager.setFirstInstalled(true);
        }else {
            sound =  DataLocalManager.getSoundAlarm();
            checkSound = DataLocalManager.getSound();
            checkVibration = DataLocalManager.getVibration();
            time= DataLocalManager.getTimeValue();
            checkFlash = DataLocalManager.getFlashLight();
        }



        vibrator = (Vibrator) requireActivity().getSystemService(VIBRATOR_SERVICE);
        audioManager = (AudioManager) requireActivity().getSystemService(AUDIO_SERVICE);



        rlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!service){
                    clickServiceApp();
                }else {
                    clickStopServiceApp();

                }

            }
        });


        startMusic();


        btnHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(requireActivity(), HowToActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    private void clickStopServiceApp() {
        Intent intent = new Intent(requireActivity(), ServiceApp.class);
        requireActivity().stopService(intent);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        vibrator.cancel();
        stopFlashing();
        OffFlash();
        service = false;
        txtActive.setText(R.string.tap_to_active);
        int color = getResources().getColor(R.color.blue);
        txtActive.setTextColor(color);
        animationView.cancelAnimation();
    }

    private void clickServiceApp() {
        Intent intent = new Intent(requireActivity(), ServiceApp.class);
        intent.putExtra("resultReceiver",resultReceiver);
        requireActivity().startService(intent);
        service = true;
        txtActive.setText(R.string.text_active);
        int color = getResources().getColor(R.color.time);
        txtActive.setTextColor(color);
    }

    private void anhXa(){
        rcl = view.findViewById(R.id.recyclerView_alarm_sound);
        list = new ArrayList<>();
        list.add(new Sound(R.drawable.dog, R.string.dog,R.raw.dogbarkwa));
        list.add(new Sound(R.drawable.cat, R.string.cat,R.raw.catangry));
        list.add(new Sound(R.drawable.police, R.string.police,R.raw.police));
        list.add(new Sound(R.drawable.doorbell, R.string.doorbell,R.raw.doorbell));
        list.add(new Sound(R.drawable.hello, R.string.hello,R.raw.hello));
        list.add(new Sound(R.drawable.harp, R.string.harp,R.raw.harp));
        list.add(new Sound(R.drawable.laughing, R.string.laughing,R.raw.laugh));
        list.add(new Sound(R.drawable.clock, R.string.clock,R.raw.alarmclock));
        list.add(new Sound(R.drawable.rooster, R.string.rooster,R.raw.farm_rooster));
        list.add(new Sound(R.drawable.piano, R.string.piano,R.raw.piano));
        list.add(new Sound(R.drawable.sneeze, R.string.sneeze,R.raw.sneeze));
        list.add(new Sound(R.drawable.train, R.string.train,R.raw.train_1_99265));
        list.add(new Sound(R.drawable.wind, R.string.wind,R.raw.wind_chimes));
        list.add(new Sound(R.drawable.whistle, R.string.whistle,R.raw.whistle));

        rlt = view.findViewById(R.id.tap_to_active);
        btnHow = view.findViewById(R.id.icon_next_how_to_use);
        txtActive = view.findViewById(R.id.text_to_active);
        animationView = view.findViewById(R.id.animation_active);
    }

    private void sendAlarmSound(Sound sound){
        clickStopServiceApp();
        Intent intent = new Intent(requireActivity(), AlarmSoundMain.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sound_main",sound);
        intent.putExtra("intent_sound",bundle);
        activityResultLauncher.launch(intent);
    }

    private void startMusic(){
        resultReceiver = new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultCode == 10 && resultData != null){
                    changeDevice = resultData.getBoolean("changeDevice");

                    if (changeDevice){

                        animationView.setRepeatCount(LottieDrawable.INFINITE);
                        animationView.playAnimation();

                        isVibrationActive();

                        isFlashLightActive();

                        isSoundActive();
                    }
                }
            }
        };

    }

    private void getVibrationDefault(){
        if (vibrator.hasVibrator()) {
            VibrationEffect effect = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                effect = VibrationEffect.createOneShot(time, VibrationEffect.EFFECT_DOUBLE_CLICK );
            }
            vibrator.vibrate(effect);
        }

    }

    private void getVibrationStrong(){
        if (vibrator.hasVibrator()) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.CONTENTS_FILE_DESCRIPTOR ));
            }else {
                long[] pattern = {0, 200,10,500};
                vibrator.vibrate(pattern, -1);
            }

        }

    }

    private void getVibrationHeart(){
        if (vibrator.hasVibrator()) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.CONTENTS_FILE_DESCRIPTOR ));
            }else {
                long[] pattern = {0, 200,300,500};
                vibrator.vibrate(pattern, -1);
            }

        }

    }


    private void OnlFlash(){
        try {
            CameraManager cameraManager = (CameraManager) requireActivity().getSystemService(Context.CAMERA_SERVICE);
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            isFlashOn = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void OffFlash(){
        try {
            CameraManager cameraManager = (CameraManager) requireActivity().getSystemService(Context.CAMERA_SERVICE);
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            isFlashOn = false;
        } catch (CameraAccessException e) {
            e.printStackTrace();
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


    private void stopFlashing() {
        handler.removeCallbacksAndMessages(null);
    }

    private void isFlashLightActive(){
        if (checkFlash){
            DataLocalManager.setFlashLight(true);
            String flashType = DataLocalManager.getRadioFlash();
            if (flashType !=null){
                if (flashType.equals("default")){
                    handler.post(flashDefault);
                }else if (flashType.equals("disco")){
                    handler.post(flashDisco);
                } else if (flashType.equals("sos")) {
                    handler.post(flashSos);
                }
            }else {
                handler.post(flashDefault);
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

    private void isVibrationActive(){
        if (checkVibration){
            DataLocalManager.setVibration(true);
            if (vibrator.hasVibrator()){
                String vibrationType = DataLocalManager.getRadioVibration();
                if (vibrationType!=null){
                    if (vibrationType.equals("default")){
                        getVibrationDefault();
                    } else if (vibrationType.equals("strong")) {
                        getVibrationStrong();
                    }else if (vibrationType.equals("heart")){
                        getVibrationHeart();
                    }
                }else {
                    getVibrationDefault();
                }

            }
        }

    }

    private void isSoundActive(){
        if (checkSound) {
            DataLocalManager.setSound(true);

            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(requireActivity(), sound.getMusic());
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    DataLocalManager.setSoundAlarm(sound);

                } else {
                    mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.dogbarkwa);
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
//                    Toast.makeText(requireActivity(), "Can not play music", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onPause() {
        super.onPause();
        clickStopServiceApp();
    }
}