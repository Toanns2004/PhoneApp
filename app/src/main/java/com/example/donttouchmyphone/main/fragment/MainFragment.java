package com.example.donttouchmyphone.main.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
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
    RelativeLayout rlt;
    TextView txtActive;
    View view;
    private ResultReceiver resultReceiver;
    ImageButton btnHow;
    boolean service=false;
    private boolean changeDevice;
    LottieAnimationView animationView;
    private MediaPlayer mediaPlayer;
    private int music = R.raw.dogbark;
    private int time = 1500;
    AudioManager audioManager;
    Vibrator vibrator;
    Boolean checkSound, checkVibration,checkFlash ;


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
                            music = bundle.getInt("music");
                            time = bundle.getInt("time");
                            checkSound = bundle.getBoolean("sound_alarm");
                            checkVibration = bundle.getBoolean("vibration_alarm");
                            checkFlash = bundle.getBoolean("flash_alarm");

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


        checkSound = DataLocalManager.getSound();
//
        checkVibration = DataLocalManager.getVibration();
//        time= DataLocalManager.getTimeValue();

        vibrator = (Vibrator) requireActivity().getSystemService(VIBRATOR_SERVICE);
        audioManager = (AudioManager) requireActivity().getSystemService(AUDIO_SERVICE);



        rlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!service){
                    clickServiceApp();
                }else {
                    clickStopServiceApp();
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        vibrator.cancel();
                    }
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
        list.add(new Sound(R.drawable.dog, R.string.dog,R.raw.dogbark));
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
                        if (checkVibration){
                            if (vibrator.hasVibrator()){
                                getVibration();
                            }
                        }
                        if (checkSound) {
                            if (mediaPlayer == null) {

                                mediaPlayer = MediaPlayer.create(requireActivity(), music);
                                mediaPlayer.start();
                                mediaPlayer.setLooping(true);

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

                            } else if (mediaPlayer.isPlaying()) { // Kiểm tra mediaPlayer đang phát nhạc hay không
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                        }

                    }
                }
            }
        };

    }

    private void getVibration(){
        // Tính thời gian của mỗi chu kỳ rung
        long vibrationDuration = time;
        long pauseDuration = 0;
        long[] pattern = {0, vibrationDuration, pauseDuration};
        VibrationEffect effect = VibrationEffect.createWaveform(pattern, -1); // -1 để lặp vô hạn
        vibrator.vibrate(effect);

    }


}