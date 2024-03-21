package com.example.donttouchmyphone.sev;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.donttouchmyphone.model.Language;
import com.example.donttouchmyphone.model.Sound;
import com.example.donttouchmyphone.model.Time;
import com.google.gson.Gson;

public class DataLocalManager {
    private static final String CHECK_SOUND = "CHECK_SOUND";
    private static final String TIME_KEY = "TIME_KEY";
    private static final String TIME_KEY_VALUE = "TIME_KEY_VALUE";

    private static final String CHECK_VIBRATION = "CHECK_VIBRATION";
    private static final String CHECK_FLASH = "CHECK_FLASH";
    private static final String KEY_LANGUAGE ="language";
    private static final String CHECK_ALARM_SOUND = "CHECK_ALARM_SOUND";

    private static final String GROUP_RADIO_FLASHLIGHT = "GROUP_RADIO_FLASHLIGHT";
    private static final String GROUP_RADIO_VIBRATION = "GROUP_RADIO_VIBRATION";
    private static final String NAME_RADIO_FLASHLIGHT = "NAME_RADIO_FLASHLIGHT";
    private static final String STRING_RADIO_VIBRATION = "STRING_RADIO_VIBRATION";
    private static final String FIRST_INSTALLED = "FIRST_INSTALLED";

    private static DataLocalManager dataLocalManager;
    private SharedPreferencesApp preferencesApp;

    public static void init(Context context){
        dataLocalManager = new DataLocalManager();
        dataLocalManager.preferencesApp = new SharedPreferencesApp(context);
    }

    public static DataLocalManager getDataLocalManager(){
        if (dataLocalManager == null){
            dataLocalManager = new DataLocalManager();
        }
        return dataLocalManager;
    }

    public static void setFirstInstalled(boolean value){
        DataLocalManager.getDataLocalManager().preferencesApp.setBoolean(FIRST_INSTALLED,value);
    }

    public static boolean getFirstInstalled(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getBoolean(FIRST_INSTALLED);
    }

    public static void setSound(boolean sound){
        DataLocalManager.getDataLocalManager().preferencesApp.setBoolean(CHECK_SOUND,sound);
    }

    public static boolean getSound(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getBoolean(CHECK_SOUND);
    }

    public static void setTimeValue(int time){
        DataLocalManager.getDataLocalManager().preferencesApp.setInt(TIME_KEY_VALUE,time);
    }

    public static int getTimeValue(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getInt(TIME_KEY_VALUE);
    }

    public static void setMusic(int music){
        DataLocalManager.getDataLocalManager().preferencesApp.setInt(TIME_KEY,music);
    }

    public static int getMusic(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getInt(TIME_KEY);
    }



    public static void setVibration(boolean vibration){
        DataLocalManager.getDataLocalManager().preferencesApp.setBoolean(CHECK_VIBRATION,vibration);
    }

    public static boolean getVibration(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getBoolean(CHECK_VIBRATION);
    }

    public static void setFlashLight(boolean flash){
        DataLocalManager.getDataLocalManager().preferencesApp.setBoolean(CHECK_FLASH,flash);
    }

    public static boolean getFlashLight(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getBoolean(CHECK_FLASH);
    }

    public static String getLanguageCode(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getString(KEY_LANGUAGE);
    }

    public static void saveLanguageCode(String languageCode){
        DataLocalManager.getDataLocalManager().preferencesApp.setString(KEY_LANGUAGE,languageCode);
    }
    public static void setSoundAlarm(Sound sound){
        Gson gson = new Gson();
        String soundJson = gson.toJson(sound);
        DataLocalManager.getDataLocalManager().preferencesApp.setString(CHECK_ALARM_SOUND,soundJson);
    }

    public static Sound getSoundAlarm(){
        String soundJson = DataLocalManager.getDataLocalManager().preferencesApp.getString(CHECK_ALARM_SOUND);
        Gson gson = new Gson();
        Sound sound = gson.fromJson(soundJson,Sound.class);
        return sound;
    }


    public static void setIdGroupRadioFlash(int value){
        DataLocalManager.getDataLocalManager().preferencesApp.setInt(GROUP_RADIO_FLASHLIGHT,value);
    }

    public static int getIdGroupRadioFlash(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getInt(GROUP_RADIO_FLASHLIGHT);
    }

    public static void setRadioFlash(String value){
        DataLocalManager.getDataLocalManager().preferencesApp.setString(NAME_RADIO_FLASHLIGHT,value);
    }

    public static String getRadioFlash(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getString(NAME_RADIO_FLASHLIGHT);
    }



    public static void setIdGroupRadioVibration(int value){
        DataLocalManager.getDataLocalManager().preferencesApp.setInt(GROUP_RADIO_VIBRATION,value);
    }

    public static int getIdGroupRadioVibration(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getInt(GROUP_RADIO_VIBRATION);
    }
    public static void setRadioVibration(String value){
        DataLocalManager.getDataLocalManager().preferencesApp.setString(STRING_RADIO_VIBRATION,value);

    }

    public static String getRadioVibration(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getString(STRING_RADIO_VIBRATION);
    }


}
