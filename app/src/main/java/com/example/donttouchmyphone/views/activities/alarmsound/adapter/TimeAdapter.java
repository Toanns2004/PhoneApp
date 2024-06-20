package com.example.donttouchmyphone.views.activities.alarmsound.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donttouchmyphone.R;
import com.example.donttouchmyphone.views.interfaces.IClickTime;
import com.example.donttouchmyphone.models.Time;
import com.example.donttouchmyphone.controll.DataLocalManager;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder>{

    List<Time> timeList;
    IClickTime iClickTime;






    public TimeAdapter(List<Time> timeList, IClickTime iClickTime) {
        this.timeList = timeList;
        this.iClickTime = iClickTime;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_duration,parent,false);

        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
       Time time = timeList.get(position);

       holder.textView.setText(time.getName());
        int timePrevious = DataLocalManager.getTimeValue();
        if (time.getTime() == timePrevious ){
            holder.relativeLayout.setBackgroundResource(R.drawable.custom_time_duration);
        }else {
            holder.relativeLayout.setBackgroundResource(R.drawable.custom_time_duration_uncheck);

        }
       holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               iClickTime.getTime(time);
               DataLocalManager.setTimeValue(time.getTime());
               notifyDataSetChanged();
           }
       });
    }

    @Override
    public int getItemCount() {
        if (timeList!=null){
            return timeList.size();
        }
        return 0;
    }

    class TimeViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        RelativeLayout relativeLayout;
        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_time);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_time_duration);
        }
    }
}
