package com.example.donttouchmyphone.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.main.iface.IClickItem;
import com.example.donttouchmyphone.model.Sound;
import com.example.donttouchmyphone.sev.DataLocalManager;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder>{
    List<Sound> soundList;
    IClickItem iClickItem;

    public SoundAdapter(List<Sound> soundList,IClickItem iClickItem) {
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
        Log.e("sound",""+DataLocalManager.getSoundAlarm());
        Log.e("sound",sound+"");
        if (sound == DataLocalManager.getSoundAlarm()){

            holder.rcl.setBackgroundResource(R.drawable.custom_alarm_sound_click);
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
