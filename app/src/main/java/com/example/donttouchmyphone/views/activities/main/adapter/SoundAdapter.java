package com.example.donttouchmyphone.views.activities.main.adapter;

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
import com.example.donttouchmyphone.views.interfaces.IClickItemSound;
import com.example.donttouchmyphone.models.Sound;
import com.example.donttouchmyphone.services.DataLocalManager;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder>{
    List<Sound> soundList;
    IClickItemSound iClickItem;

    private int textColorClick = Color.WHITE;
    private int textColorUnClick = Color.BLACK;
    public SoundAdapter(List<Sound> soundList, IClickItemSound iClickItem) {
        this.soundList = soundList;
        this.iClickItem =iClickItem;
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_alarm_sound,parent,false);

        return new SoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundViewHolder holder, int position) {

        Sound sound = soundList.get(position);

        holder.imageView.setImageResource(sound.getImage());
        holder.textView.setText(sound.getName());
        int music = DataLocalManager.getMusic();
         if (sound.getMusic() == music){
            holder.rcl.setBackgroundResource(R.drawable.custom_alarm_sound_click);
            holder.textView.setTextColor(textColorClick);
        }else {
            holder.rcl.setBackgroundResource(R.drawable.custom_alarm_sound);
             holder.textView.setTextColor(textColorUnClick);
        }
        holder.rcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.setChoose(true);
                iClickItem.getItem(sound);
            }
        });



    }

    @Override
    public int getItemCount() {
        if (soundList!=null){
            return soundList.size();
        }
        return 0;
    }

    protected class SoundViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rcl;
        ImageView imageView;
        TextView textView;
        public SoundViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_sound);
            textView = itemView.findViewById(R.id.text_sound_name);
            rcl = itemView.findViewById(R.id.relativeLayout_sound);
        }
    }
}
