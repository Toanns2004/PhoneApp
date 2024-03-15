package com.example.donttouchmyphone.alarmsound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
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
import com.example.donttouchmyphone.adapter.SoundAlarmAdapter;
import com.example.donttouchmyphone.adapter.TimeAdapter;
import com.example.donttouchmyphone.alarmsound.iface.IClickTime;
import com.example.donttouchmyphone.main.MainActivity;
import com.example.donttouchmyphone.model.Sound;
import com.example.donttouchmyphone.model.Time;

import java.util.ArrayList;
import java.util.List;

public class AlarmSoundMain extends AppCompatActivity {

    RecyclerView rcl,rclTime;
    SoundAlarmAdapter adapter;
    TimeAdapter timeAdapter;
    List<Sound> list;
    List<Time> timeList;
    Button btnApply;
    ImageButton btnBack;
    ImageView IPlay;
    TextView txt;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    Vibrator vibrator;
    SwitchCompat swVibration,swFlash,swSound;
    Sound sound;
    int music;
    SharedPreferences sharedPreferences;

    int time;

    SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_sound_main);
        anhXa();


        adapter = new SoundAlarmAdapter(list,iClickTime);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        rcl.setLayoutManager(layoutManager);
        rcl.setAdapter(adapter);
        listTime();

        getDataFragmentMain();

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);





        //phát nhạc
        mediaPlayer = MediaPlayer.create(AlarmSoundMain.this,sound.getMusic());
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




        //Volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        final boolean[] savedStreamMuted = {false};
        int stream = AudioManager.STREAM_MUSIC;
        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        savedStreamMuted[0] = true;
                        audioManager.adjustStreamVolume(stream, AudioManager.ADJUST_MUTE, 0);
                    } else {
                        audioManager.setStreamMute(stream, true);
                    }
                }else {

                }
            }
        });

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);
        changeSeekBar();


    }




    //
    private void sendMainFragment() {
        Intent intent = new Intent(AlarmSoundMain.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("music",sound.getMusic());
        bundle.putInt("time",time);
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
        mediaPlayer.stop();
        mediaPlayer.release();
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
        mediaPlayer = MediaPlayer.create(AlarmSoundMain.this, sound.getMusic());
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

}