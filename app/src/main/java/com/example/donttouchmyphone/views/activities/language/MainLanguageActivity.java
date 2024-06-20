package com.example.donttouchmyphone.views.activities.language;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.views.activities.language.adapter.LanguageAdapter;
import com.example.donttouchmyphone.views.activities.intro.IntroActivity;
import com.example.donttouchmyphone.views.activities.main.MainActivity;
import com.example.donttouchmyphone.models.Language;
import com.example.donttouchmyphone.controll.DataLocalManager;
import com.example.donttouchmyphone.views.interfaces.IClickItemLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainLanguageActivity extends AppCompatActivity {
    private Button btnOk;
    private RecyclerView rlv;
    private LanguageAdapter adapter;
    private List<Language> list;
    Language language;

    private String languageCode ;
    private String previousActivityName;
    private static final String KEY_PREVIOUS_ACTIVITY ="KEY_PREVIOUS_ACTIVITY";
    IClickItemLanguage iClickItem = new IClickItemLanguage() {
        @Override
        public void getItem(Language languageItem) {
            language = languageItem;

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        anhXa();
        if (!DataLocalManager.getFirstInstalled()){
            DataLocalManager.setLanguage(list.get(0));
            setLanguageObject(list.get(0));
            setLanguage("en");
        }

        language = DataLocalManager.getLanguage();
        setLanguage(DataLocalManager.getLanguageCode());
        adapter = new LanguageAdapter(list,iClickItem);
        rlv.setLayoutManager(new GridLayoutManager(MainLanguageActivity.this,2));
        rlv.setAdapter(adapter);

        previousActivityName = getIntent().getStringExtra("activity");


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguageObject(language);
                DataLocalManager.setLanguage(language);
                Intent intent;
                if (KEY_PREVIOUS_ACTIVITY.equals(previousActivityName)){
                    intent = new Intent(MainLanguageActivity.this, MainActivity.class);
                }else {
                    intent = new Intent(MainLanguageActivity.this, IntroActivity.class);
                }
                startActivity(intent);

            }
        });

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
        adapter.notifyDataSetChanged();

    }


}
