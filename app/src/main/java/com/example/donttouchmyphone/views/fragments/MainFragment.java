package com.example.donttouchmyphone.views.fragments;

import static android.app.Activity.RESULT_OK;


import static com.example.donttouchmyphone.controll.ServiceApp.RECEIVER_CODE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.views.activities.alarmsound.AlarmSoundActivity;
import com.example.donttouchmyphone.views.activities.how.HowToActivity;
import com.example.donttouchmyphone.models.Sound;
import com.example.donttouchmyphone.views.activities.main.adapter.SoundAdapter;
import com.example.donttouchmyphone.views.interfaces.IClickItemSound;
import com.example.donttouchmyphone.controll.DataLocalManager;
import com.example.donttouchmyphone.controll.ServiceApp;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment  {
    private RecyclerView rcl;
    private SoundAdapter adapter;
    private List<Sound> list;
    private Sound sound ;
    private RelativeLayout rlt;
    private TextView txtActive;
    private View view;
    private ImageButton btnHow;
    private LottieAnimationView animationView;
    private int time ;
    boolean checkSound, checkVibration,checkFlash ;

    private ResultReceiver resultReceiver;

    private boolean checkService;

    IClickItemSound iClickItem = new IClickItemSound() {
        @Override
        public void getItem(Sound sound) {
            sendAlarmSound(sound);

        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkService = intent.getBooleanExtra("data",false);
            if (checkService){
                txtActive.setText(R.string.tap_to_active);
                int color = getResources().getColor(R.color.blue);
                txtActive.setTextColor(color);
                animationView.cancelAnimation();


            }
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
            DataLocalManager.setSound(true);
            DataLocalManager.setSoundAlarm(sound);
            DataLocalManager.setMusic(sound.getMusic());
            DataLocalManager.setTimeValue(15000);
            DataLocalManager.setFlashLight(false);
            DataLocalManager.setVibration(false);
            DataLocalManager.setService(false);
            DataLocalManager.setFirstInstalled(true);
        }
            sound =  DataLocalManager.getSoundAlarm();
            checkSound = DataLocalManager.getSound();
            checkVibration = DataLocalManager.getVibration();
            time= DataLocalManager.getTimeValue();
            checkFlash = DataLocalManager.getFlashLight();

        if (!DataLocalManager.getService()){
            txtActive.setText(R.string.tap_to_active);
            int color = getResources().getColor(R.color.blue);
            txtActive.setTextColor(color);
            animationView.cancelAnimation();

        }else {
            txtActive.setText(R.string.text_active);
            int color = getResources().getColor(R.color.time);
            txtActive.setTextColor(color);
        }


        rlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DataLocalManager.getService()){
                    clickServiceApp();
                }else {
                    clickStopServiceApp();

                }

            }
        });
        startAnimation();





        btnHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(requireActivity(), HowToActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    public void clickStopServiceApp() {
        Intent intent = new Intent(requireActivity(), ServiceApp.class);
        requireActivity().stopService(intent);
        DataLocalManager.setService(false);
        txtActive.setText(R.string.tap_to_active);
        int color = getResources().getColor(R.color.blue);
        txtActive.setTextColor(color);
        animationView.cancelAnimation();
    }

    private void clickServiceApp() {
        Intent intent = new Intent(requireActivity(), ServiceApp.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("resultReceiver",resultReceiver);
        bundle.putSerializable("sendServiceSound",sound);
        bundle.putInt("sendServiceTime",time);
        intent.putExtra("senServiceData",bundle);
        requireActivity().startService(intent);
        DataLocalManager.setService(true);
        txtActive.setText(R.string.text_active);
        int color = getResources().getColor(R.color.time);
        txtActive.setTextColor(color);
        animationView.cancelAnimation();
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
        DataLocalManager.setImages(sound.getImage());
        clickStopServiceApp();
        Intent intent = new Intent(requireActivity(), AlarmSoundActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("sound_main",sound);
        intent.putExtra("intent_sound",bundle);
        activityResultLauncher.launch(intent);
    }

    private void startAnimation(){
        resultReceiver = new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultCode== RECEIVER_CODE &&  resultData!=null){
                    boolean changeDevice = resultData.getBoolean("changeDevice");

                    if (changeDevice){

                        animationView.setRepeatCount(LottieDrawable.INFINITE);
                        animationView.playAnimation();

                    }
                }
            }
        };
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        IntentFilter filter = new IntentFilter("CHECK_SERVICE");
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

