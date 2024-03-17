package com.example.donttouchmyphone.sev;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesApp {

    private static final String SHARED_PREFERENCES_APP ="SHARED_PREFERENCES_APP";
    private Context context;

    public SharedPreferencesApp(Context context) {
        this.context = context;
    }

    public void setBoolean(String key, boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_APP,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }


    public boolean getBoolean(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_APP,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }

    public void setString(String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_APP,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public String getString(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_APP,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void setInt(String key, int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_APP,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public int getInt(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_APP,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,0);
    }

}
