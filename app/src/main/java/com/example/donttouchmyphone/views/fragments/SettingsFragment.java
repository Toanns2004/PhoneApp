package com.example.donttouchmyphone.views.fragments;

import static android.content.Context.VIBRATOR_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.services.DataLocalManager;

public class SettingsFragment extends Fragment {

    View view;
    SwitchCompat sw;
    RadioGroup groupFlash,groupVibration;
    SeekBar seekBar;
    Vibrator vibrator;
    CameraManager cameraManager;
    String mCameraId;
    boolean isFlashOn ;
    private Handler handler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_settings, container, false);
        // Inflate the layout for this fragment
        anhXa();



        changeSeekBar();

        sw.setChecked(DataLocalManager.getExtension());
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    DataLocalManager.setExtension(true);
                }else {
                    DataLocalManager.setExtension(false);
                }
            }
        });

        cameraManager = (CameraManager) requireActivity().getSystemService(Context.CAMERA_SERVICE);

        try {
            mCameraId = cameraManager.getCameraIdList()[0]; // Lấy ID của camera mặc định
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        vibrator = (Vibrator) requireActivity().getSystemService(VIBRATOR_SERVICE);


        if (!DataLocalManager.getSendSettingFist()) {
            DataLocalManager.setIdGroupRadioFlash(R.id.btn_default_flash);
            DataLocalManager.setIdGroupRadioVibration(R.id.btn_default_vibration);
            DataLocalManager.setSendSettingFist(true);
        }

        groupFlash.check(DataLocalManager.getIdGroupRadioFlash());



        groupFlash.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btn_default_flash) {
                    DataLocalManager.setIdGroupRadioFlash(R.id.btn_default_flash);
                    DataLocalManager.setRadioFlash("default");
                    if (isFlashOn){
                        stopFlashing();
                    }
                    startFlash(flashDefault);
                } else if (checkedId == R.id.btn_disco_flash) {
                    if (isFlashOn){
                        stopFlashing();
                    }
                    DataLocalManager.setIdGroupRadioFlash(R.id.btn_disco_flash);
                    DataLocalManager.setRadioFlash("disco");

                    startFlash(flashDisco);
                } else if(checkedId == R.id.btn_sos_flash){
                    if (isFlashOn){
                        stopFlashing();
                    }
                    DataLocalManager.setIdGroupRadioFlash(R.id.btn_sos_flash);
                    DataLocalManager.setRadioFlash("sos");

                    startFlash(flashSos);
                }
            }
        });

        groupVibration.check(DataLocalManager.getIdGroupRadioVibration());

        groupVibration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btn_default_vibration) {
                    DataLocalManager.setIdGroupRadioVibration(R.id.btn_default_vibration);
                    DataLocalManager.setRadioVibration("default");
                    vibrator.cancel();
                    getVibrationDefault();
                } else if (checkedId == R.id.btn_strong_vibration) {
                    DataLocalManager.setIdGroupRadioVibration(R.id.btn_strong_vibration);
                    DataLocalManager.setRadioVibration("strong");
                    vibrator.cancel();
                    getVibrationDisco();
                } else if(checkedId == R.id.btn_heartbeat_vibration){
                    DataLocalManager.setIdGroupRadioVibration(R.id.btn_heartbeat_vibration);
                    DataLocalManager.setRadioVibration("heart");
                    vibrator.cancel();
                    getVibrationSos();
                }
            }
        });
        return view;
    }

    private void changeSeekBar() {
        seekBar.setMax(100);
        seekBar.setProgress(DataLocalManager.getSeekbarSetting());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DataLocalManager.setSeekbarSetting(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void anhXa(){
        groupFlash = view.findViewById(R.id.radioGroup_flashLight);
        groupVibration = view.findViewById(R.id.radioGroup_vibration);
        seekBar = view.findViewById(R.id.seekbar_settings);
        sw = view.findViewById(R.id.switch_extension);
    }

    @SuppressLint("InlinedApi")
    private void getVibrationDefault(){
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.EFFECT_DOUBLE_CLICK));
            } else {
                vibrator.vibrate(2000);
            }
        }

    }

    private void getVibrationDisco(){
        if (vibrator.hasVibrator()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 200, 500, 200, 500, 200,500, 200, 500, 200, 500}, -1));
            } else {
                vibrator.vibrate(2000);
            }

        }

    }

    private void getVibrationSos(){
        if (vibrator.hasVibrator()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 400, 300, 500, 300, 600,300,1000}, -1));
            } else {
                vibrator.vibrate(2000);
            }

        }

    }



    private void OnlFlash() {
        try {
            cameraManager.setTorchMode(mCameraId, true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void OffFlash(){
        try {
            cameraManager.setTorchMode(mCameraId, false);
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

    private void startFlash(Runnable runnable){
        handler.post(runnable);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopFlashing();
            }
        },3000);
    }
    private void stopFlashing() {
        OffFlash();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        OffFlash();
        stopFlashing();
    }
}