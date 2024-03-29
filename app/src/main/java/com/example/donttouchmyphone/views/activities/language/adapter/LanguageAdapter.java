package com.example.donttouchmyphone.views.activities.language.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.services.DataLocalManager;
import com.example.donttouchmyphone.views.interfaces.IClickItemLanguage;
import com.example.donttouchmyphone.models.Language;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    List<Language> languageList ;
    IClickItemLanguage clickItem;

    private int textColorClick = Color.WHITE;
    private int textColorUnClick = Color.BLACK;
    public LanguageAdapter(List<Language> languageList, IClickItemLanguage item) {
        this.languageList = languageList;
        this.clickItem =item;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_language,parent,false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        Language language = languageList.get(position);
        Language languageLocal = DataLocalManager.getLanguage();

        holder.imageView.setImageResource(language.getUri());
        holder.textView.setText(language.getName());

        if (language.getName().equals(languageLocal.getName())){
            holder.relativeLayout.setBackgroundResource(R.drawable.custom_language_click);
            holder.textView.setTextColor(textColorClick);
            holder.icon.setVisibility(View.VISIBLE);
        }else {
            holder.relativeLayout.setBackgroundResource(R.drawable.custom_language);
            holder.textView.setTextColor(textColorUnClick);
            holder.icon.setVisibility(View.INVISIBLE);

        }


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItem.getItem(language);
                DataLocalManager.setLanguage(language);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (languageList!=null){
            return languageList.size();
        }
        return 0;
    }

    protected class LanguageViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        ImageView imageView,icon;
        TextView textView;
        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_language);
            imageView = itemView.findViewById(R.id.rectangle_english);
            textView = itemView.findViewById(R.id.language);
            icon = itemView.findViewById(R.id.mark_icon);
        }
    }
}
