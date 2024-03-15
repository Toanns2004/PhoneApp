package com.example.donttouchmyphone.alarmsound.iface;

import com.example.donttouchmyphone.model.Time;
import com.example.donttouchmyphone.model.Sound;

public interface IClickTime {
    void getTime(Time time);
    void getSound(Sound sound);
}
