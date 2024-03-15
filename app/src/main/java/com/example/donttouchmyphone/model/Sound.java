package com.example.donttouchmyphone.model;

import java.io.Serializable;

public class Sound implements Serializable {
    private int image;
    private int name;

    private int music;
    private int time;

    private boolean choose;
    public Sound(int image, int name, int music) {
        this.image = image;
        this.name = name;
        this.music = music;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getMusic() {
        return music;
    }

    public void setMusic(int music) {
        this.music = music;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
