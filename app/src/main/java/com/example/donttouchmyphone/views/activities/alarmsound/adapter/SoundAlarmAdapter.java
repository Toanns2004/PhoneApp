package com.example.donttouchmyphone.views.activities.alarmsound.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.views.interfaces.IClickTime;
import com.example.donttouchmyphone.models.Sound;
import com.example.donttouchmyphone.controll.DataLocalManager;

import java.util.List;

public class SoundAlarmAdapter extends RecyclerView.Adapter<SoundAlarmAdapter.SoundNameViewHolder>{
    List<Sound> soundNameList;
    IClickTime iClickTime;

    public SoundAlarmAdapter(List<Sound> soundNameList, IClickTime iClickTime) {
        this.soundNameList = soundNameList;
        this.iClickTime = iClickTime;
    }

    @NonNull
    @Override
    public SoundNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_circle_sound_name,parent,false);

        return new SoundNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundNameViewHolder holder, int position) {
        Sound soundName = soundNameList.get(position);

        holder.imageView.setImageResource(soundName.getImage());
        holder.textView.setText(soundName.getName());

        if (soundName.getImage() == DataLocalManager.getImages()){
            holder.rcl.setBackgroundResource(R.drawable.custom_circle_image_sound_name);

        }else {
            holder.rcl.setBackgroundResource(R.drawable.custom_circle_image_sound_name_unclick);

        }


        holder.ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickTime.getSound(soundName);
                DataLocalManager.setImages(soundName.getImage());
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (soundNameList!=null){
            return soundNameList.size();
        }
        return 0;
    }

    protected class SoundNameViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView ;
        RelativeLayout rcl;
        TextView textView;
        LinearLayout ln;
        public SoundNameViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_circle_sound_name);
            textView = itemView.findViewById(R.id.text_name_circle_sound_name);
            ln = itemView.findViewById(R.id.linearLayout_sound);
            rcl = itemView.findViewById(R.id.relativeLayout_circle);
        }
    }
}
