package com.example.donttouchmyphone.main.fragment;

import static android.content.Context.VIBRATOR_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.sev.DataLocalManager;
import com.google.android.material.radiobutton.MaterialRadioButton;

public class SettingsFragment extends Fragment {

    View view;
    RadioGroup groupFlash,groupVibration;
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

        cameraManager = (CameraManager) requireActivity().getSystemService(Context.CAMERA_SERVICE);

        try {
            mCameraId = cameraManager.getCameraIdList()[0]; // Lấy ID của camera mặc định
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        vibrator = (Vibrator) requireActivity().getSystemService(VIBRATOR_SERVICE);

        int savedCheckedIdFlash = DataLocalManager.getIdGroupRadioFlash();
        if (savedCheckedIdFlash != -1) {
            groupFlash.check(R.id.btn_default_flash);
        }

        groupFlash.check(savedCheckedIdFlash);


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

        int savedCheckedIdVibration = DataLocalManager.getIdGroupRadioVibration();
        if (savedCheckedIdVibration != -1) {
            groupVibration.check(R.id.btn_default_vibration);
        }
        groupVibration.check(savedCheckedIdVibration);

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

    private void anhXa(){
        groupFlash = view.findViewById(R.id.radioGroup_flashLight);
        groupVibration = view.findViewById(R.id.radioGroup_vibration);

    }

    private void getVibrationDefault(){
        if (vibrator.hasVibrator()) {
            VibrationEffect effect = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                effect = VibrationEffect.createOneShot(2000, VibrationEffect.EFFECT_DOUBLE_CLICK );
            }
            vibrator.vibrate(effect);
        }

    }

    private void getVibrationDisco(){
        if (vibrator.hasVibrator()) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.CONTENTS_FILE_DESCRIPTOR ));
            }else {
                long[] pattern = {0, 200,10,500};
                vibrator.vibrate(pattern, -1);
            }

        }

    }

    private void getVibrationSos(){
        if (vibrator.hasVibrator()) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.CONTENTS_FILE_DESCRIPTOR ));
            }else {
                long[] pattern = {0, 200,300,500};
                vibrator.vibrate(pattern, -1);
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