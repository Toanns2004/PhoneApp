package com.example.donttouchmyphone.models;

public class Language {
    private int uri;
    private String name;

    private boolean chose;

    public Language(int uri, String name) {
        this.uri = uri;
        this.name = name;

    }

    public int getUri() {
        return uri;
    }

    public void setUri(int uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChose() {
        return chose;
    }

    public void setChose(boolean chose) {
        this.chose = chose;
    }
}
