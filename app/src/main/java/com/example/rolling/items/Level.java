package com.example.rolling.items;

public class Level {

    private String name;
    private Boolean isAvailable;
    private LevelSettings settings;
    private static int resIdAvailable;
    private static int resIdNotAvailable;

    public Level(String name, LevelSettings settings) {
        this.name = name;
        this.isAvailable = false;
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public static int getResIdAvailable() {
        return resIdAvailable;
    }

    public static void setResIdAvailable(int resIdAvailable) {
        Level.resIdAvailable = resIdAvailable;
    }

    public static int getResIdNotAvailable() {
        return resIdNotAvailable;
    }

    public static void setResIdNotAvailable(int resIdNotAvailable) {
        Level.resIdNotAvailable = resIdNotAvailable;
    }

    public int getResId() {
        return isAvailable ? resIdAvailable : resIdNotAvailable;
    }

    public LevelSettings getSettings() {
        return settings;
    }

    public void setSettings(LevelSettings settings) {
        this.settings = settings;
    }
}