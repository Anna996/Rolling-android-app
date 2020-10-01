package com.example.rolling.items;

import android.os.Parcel;
import android.os.Parcelable;

public class LevelSettings implements Parcelable {

    private int gameTime;  // seconds
    private int startSpeed;
    private int maxSpeed;
    private int resIdFirstBackground;
    private int resIdSecondBackground;
    private int[] resIdObstacles;
    private int[] widthToDivideArray;
    private int[] heightToDivideArray;

    public LevelSettings(int gameTime,
                         int startSpeed,
                         int maxSpeed,
                         int resIdFirstBackground,
                         int resIdSecondBackground,
                         int[] resIdObstacles,
                         int[] widthToDivideArray,
                         int[] heightToDivideArray) {
        this.gameTime = gameTime;
        this.startSpeed = startSpeed;
        this.maxSpeed = maxSpeed;
        this.resIdFirstBackground = resIdFirstBackground;
        this.resIdSecondBackground = resIdSecondBackground;
        this.resIdObstacles = resIdObstacles;
        this.widthToDivideArray = widthToDivideArray;
        this.heightToDivideArray = heightToDivideArray;
    }

    private LevelSettings(Parcel in) {
        gameTime = in.readInt();
        startSpeed = in.readInt();
        maxSpeed = in.readInt();
        resIdFirstBackground = in.readInt();
        resIdSecondBackground = in.readInt();
        resIdObstacles = in.createIntArray();
        widthToDivideArray = in.createIntArray();
        heightToDivideArray = in.createIntArray();
    }

    public static final Creator<LevelSettings> CREATOR = new Creator<LevelSettings>() {
        @Override
        public LevelSettings createFromParcel(Parcel in) {
            return new LevelSettings(in);
        }

        @Override
        public LevelSettings[] newArray(int size) {
            return new LevelSettings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(gameTime);
        dest.writeInt(startSpeed);
        dest.writeInt(maxSpeed);
        dest.writeInt(resIdFirstBackground);
        dest.writeInt(resIdSecondBackground);
        dest.writeIntArray(resIdObstacles);
        dest.writeIntArray(widthToDivideArray);
        dest.writeIntArray(heightToDivideArray);
    }

    public int getGameTime() {
        return gameTime;
    }

    public int getStartSpeed() {
        return startSpeed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getResIdFirstBackground() {
        return resIdFirstBackground;
    }

    public int getResIdSecondBackground() {
        return resIdSecondBackground;
    }

    public int[] getResIdObstacles() {
        return resIdObstacles;
    }

    public int[] getWidthToDivideArray() {
        return widthToDivideArray;
    }

    public int[] getHeightToDivideArray() {
        return heightToDivideArray;
    }
}