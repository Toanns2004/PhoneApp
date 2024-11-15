package com.example.donttouchmyphone.views.activities.alarmsound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.views.activities.alarmsound.adapter.SoundAlarmAdapter;
import com.example.donttouchmyphone.views.activities.alarmsound.adapter.TimeAdapter;
import com.example.donttouchmyphone.views.interfaces.IClickTime;
import com.example.donttouchmyphone.views.activities.main.MainActivity;
import com.example.donttouchmyphone.models.Sound;
import com.example.donttouchmyphone.models.Time;
import com.example.donttouchmyphone.controll.DataLocalManager;

import java.util.ArrayList;
import java.util.List;

public class AlarmSoundActivity extends AppCompatActivity {

    private RecyclerView rcl,rclTime;
    private SoundAlarmAdapter adapter;
    private TimeAdapter timeAdapter;
    private List<Sound> list;
    private List<Time> timeList;
    private Button btnApply;
    private ImageButton btnBack;
    private ImageView IPlay;
    private TextView txt;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Vibrator vibrator;
    private SwitchCompat swVibration,swFlash,swSound;
    private Sound sound;
    private int time;
    private SeekBar seekBar;
    private Boolean checkSound, checkVibration,checkFlash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_sound_main);
        anhXa();

        getDataFragmentMain();
        adapter = new SoundAlarmAdapter(list,iClickTime);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        rcl.setLayoutManager(layoutManager);
        rcl.setAdapter(adapter);
        listTime();





        time = DataLocalManager.getTimeValue();
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(AlarmSoundActivity.this,sound.getMusic());
        }
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


        //phát nhạc

        IPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMainFragment();
//                finish();
            }
        });


       checkSound = DataLocalManager.getSound();
       if (!checkSound){
           swSound.setChecked(false);
           seekBar.setEnabled(false);
           audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
       }else {
           swSound.setChecked(true);
           seekBar.setEnabled(true);
           audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
       }

        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkSound = true;
                    seekBar.setEnabled(true);
//                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamVolume(currentVolume),0);


                } else {
                    checkSound = false;
                    seekBar.setEnabled(false);
//                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);

                }
            }
        });

        checkVibration = DataLocalManager.getVibration();
        if (!checkVibration){
            swVibration.setChecked(false);
        }else {
            swVibration.setChecked(true);
        }
        swVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkVibration = true;

                }else {
                    checkVibration = false;

                }
            }
        });

        checkFlash = DataLocalManager.getFlashLight();
        if (!checkFlash){
            swFlash.setChecked(false);
        }else {
            swFlash.setChecked(true);
        }
        swFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkFlash = true;

                }else {
                    checkFlash = false;
                }
            }
        });

        //Volume
        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);
        changeSeekBar();
    }


    private void sendMainFragment() {
        DataLocalManager.setMusic(sound.getMusic());
        Intent intent = new Intent(AlarmSoundActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("SOUND",sound);
        bundle.putInt("music",sound.getMusic());
        bundle.putInt("time",time);
        bundle.putBoolean("sound_alarm",checkSound);
        bundle.putBoolean("vibration_alarm",checkVibration);
        bundle.putBoolean("flash_alarm",checkFlash);
        intent.putExtra("sendMainFragment",bundle);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    private void getDataFragmentMain() {

        Intent intent= getIntent();
        Bundle bundle = intent.getBundleExtra("intent_sound");
        if (bundle!=null){
            sound = (Sound) bundle.getSerializable("sound_main");
            txt.setText(sound.getName());
        }
    }


    private void playSound() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            IPlay.setImageResource(R.drawable.polygon);
        }else {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            IPlay.setImageResource(R.drawable.baseline_pause_24);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void anhXa(){
        rcl = findViewById(R.id.recyclerView_sound_name);
        rclTime = findViewById(R.id.recyclerView_time_duration);
        btnBack = findViewById(R.id.image_button_back_alarmSound);
        txt = findViewById(R.id.text_soundName_Alarm);
        IPlay = findViewById(R.id.icon_play);
        swVibration = findViewById(R.id.switch_vibration);
        swFlash = findViewById(R.id.switch_flashlight);
        swSound = findViewById(R.id.switch_sound);
        btnApply = findViewById(R.id.button_apply_alarmSound);
        seekBar = findViewById(R.id.seekbarProgress_alarmSound);
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


        timeList = new ArrayList<>();
        timeList.add(new Time(15000,"15s"));
        timeList.add(new Time(30000,"30s"));
        timeList.add(new Time(60000,"1m"));
        timeList.add(new Time(120000,"2m"));

    }

    IClickTime iClickTime = new IClickTime() {
        @Override
        public void getTime(Time t) {
            time = t.getTime();
        }

        @Override
        public void getSound(Sound sound1) {
            sound =sound1;
            changeSound(sound1);
        }
    };
    private void listTime(){
        timeAdapter = new TimeAdapter(timeList,iClickTime);
        rclTime.setLayoutManager(new GridLayoutManager(this,4));
        rclTime.setAdapter(timeAdapter);
    }


    private void changeSound(Sound sound) {
        txt.setText(sound.getName());
        IPlay.setImageResource(R.drawable.baseline_pause_24);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();

            }
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(AlarmSoundActivity.this, sound.getMusic());
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void changeSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}