package com.example.donttouchmyphone.main.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.alarmsound.AlarmSoundMain;
import com.example.donttouchmyphone.how.HowToActivity;
import com.example.donttouchmyphone.model.Sound;
import com.example.donttouchmyphone.adapter.SoundAdapter;
import com.example.donttouchmyphone.main.iface.IClickItem;
import com.example.donttouchmyphone.main.ser.ServiceApp;

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

    private MediaPlayer mediaPlayer;
    private int music = R.raw.dogbark;
    private int time = 15000;

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

        Log.e("music",music+"");
        Log.e("music",time+"");

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
        service = false;
        txtActive.setText(R.string.tap_to_active);
        int color = getResources().getColor(R.color.blue);
        txtActive.setTextColor(color);
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
    }

    private void sendAlarmSound(Sound sound){
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
                        if (mediaPlayer == null) { // Kiểm tra mediaPlayer có null hay không
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
        };

    }


}