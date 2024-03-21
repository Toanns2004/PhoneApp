package com.example.donttouchmyphone.language;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.adapter.LanguageAdapter;
import com.example.donttouchmyphone.intro.IntroActivity;
import com.example.donttouchmyphone.main.MainActivity;
import com.example.donttouchmyphone.model.Language;
import com.example.donttouchmyphone.sev.DataLocalManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainLanguage extends AppCompatActivity {
    Button btnOk;
    RecyclerView rlv;
    LanguageAdapter adapter;
    List<Language> list;

    String languageCode = "";
    String previousActivityName;
    static final String KEY_PREVIOUS_ACTIVITY ="KEY_PREVIOUS_ACTIVITY";
    IClickItem iClickItem = new IClickItem() {
        @Override
        public void getItem(Language language) {

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLanguageObject(language);
                    Intent intent;
                    if (KEY_PREVIOUS_ACTIVITY.equals(previousActivityName)){
                        intent = new Intent(MainLanguage.this, MainActivity.class);
                    }else {
                        intent = new Intent(MainLanguage.this, IntroActivity.class);
                    }
                    startActivity(intent);

                }
            });
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        anhXa();
        setLanguage(DataLocalManager.getLanguageCode());
        adapter = new LanguageAdapter(list,iClickItem);
        rlv.setLayoutManager(new GridLayoutManager(MainLanguage.this,2));
        rlv.setAdapter(adapter);

        previousActivityName = getIntent().getStringExtra("activity");




    }
    private void anhXa(){
        rlv = findViewById(R.id.recyclerView);
        btnOk = findViewById(R.id.button_language);

        list = new ArrayList<>();
        list.add(new Language(R.drawable.english,"English"));
        list.add(new Language(R.drawable.french,"French"));
        list.add(new Language(R.drawable.vietnames,"Vietnamese"));
        list.add(new Language(R.drawable.portusuese,"Portuguese"));
        list.add(new Language(R.drawable.german,"German"));

    }

    private void setLanguageObject(Language language) {

        switch (language.getName()) {
            case "English":
                languageCode = "en";
                break;
            case "French":
                languageCode = "fr";
                break;
            case "Vietnamese":
                languageCode = "vi";
                break;
            case "Portuguese":
                languageCode = "pt";
                break;
            case "German":
                languageCode = "de";
                break;

        }

        DataLocalManager.saveLanguageCode(languageCode);
        setLanguage(languageCode);
    }


    private void setLanguage(String languageCode) {
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        DataLocalManager.saveLanguageCode(languageCode);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        SharedPreferences sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("lang",languageCode);
//        editor.apply();

    }


}
