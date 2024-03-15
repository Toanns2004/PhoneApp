package com.example.donttouchmyphone.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LanguagePreferenceHelper {
    private static final String KEY_LANGUAGE ="language";
    private SharedPreferences sharedPreferences;

    public LanguagePreferenceHelper(Context context){
        sharedPreferences = context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
    }

    public String getLanguage(){
        return sharedPreferences.getString(KEY_LANGUAGE,"");
    }

    public void saveLanguage(String languageCode){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LANGUAGE,languageCode);
        editor.apply();
    }
}
