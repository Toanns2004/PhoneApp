package com.example.donttouchmyphone.sev;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.donttouchmyphone.model.Language;
import com.example.donttouchmyphone.model.Time;
import com.google.gson.Gson;

public class DataLocalManager {
    private static final String CHECK_SOUND = "CHECK_SOUND";
    private static final String TIME_KEY = "TIME_KEY";
    private static final String TIME_KEY_VALUE = "TIME_KEY_VALUE";

    private static final String CHECK_VIBRATION = "CHECK_VIBRATION";
    private static final String CHECK_FLASH = "CHECK_FLASH";
    private static final String KEY_LANGUAGE ="language";
    private static final String CHECK_LANGUAGE = "CHECK_LANGUAGE";
    private static final String CHECK_INTERNET = "CHECK_INTERNET";

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


    public static void setSound(boolean sound){
        DataLocalManager.getDataLocalManager().preferencesApp.setBoolean(CHECK_SOUND,sound);
    }

    public static boolean getSound(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getBoolean(CHECK_SOUND);
    }

    public static void setTimeValue(Time time){
        Gson gson =  new Gson();
        String timJson  = gson.toJson(time);
        DataLocalManager.getDataLocalManager().preferencesApp.setString(TIME_KEY_VALUE,timJson);
    }

    public static Time getTimeValue(){
        String timeJson = DataLocalManager.getDataLocalManager().preferencesApp.getString(TIME_KEY_VALUE);
        Gson gson = new Gson();
        Time time = gson.fromJson(timeJson, Time.class);
        return time;
    }

    public static void setTime(boolean time){
        DataLocalManager.getDataLocalManager().preferencesApp.setBoolean(TIME_KEY,time);
    }

    public static boolean getTime(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getBoolean(TIME_KEY);
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
    public static void setLanguage(Language language){
        Gson gson = new Gson();
        String languageJson = gson.toJson(language);
        DataLocalManager.getDataLocalManager().preferencesApp.setString(CHECK_LANGUAGE,languageJson);
    }

    public static String getLanguage(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getString(CHECK_LANGUAGE);
    }


    public static void setCheckInternet(boolean internet){
        DataLocalManager.getDataLocalManager().preferencesApp.setBoolean(CHECK_INTERNET,internet);
    }

    public static boolean getCheckInternet(){
        return DataLocalManager.getDataLocalManager().preferencesApp.getBoolean(CHECK_INTERNET);
    }

}
