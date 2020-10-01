package com.example.rolling.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;


public class GameCharacters {
    protected int m_ID;
    protected int m_X, m_Y;
    protected int m_Width, m_Height, m_Width_To_Divide, m_Height_to_divide;
    protected float s_ScreenRatioX, s_ScreenRatioY;
    protected Bitmap m_player;
    protected Resources m_Res;


    public GameCharacters(Resources i_Res, int i_Width_To_Divide, int i_Height_to_divide, int i_ID) {
        m_Res = i_Res;
        m_ID = i_ID;
        s_ScreenRatioX = GameView.getS_ScreenRatioX();
        s_ScreenRatioY = GameView.getS_ScreenRatioY();
        m_Width_To_Divide = i_Width_To_Divide;
        m_Height_to_divide = i_Height_to_divide;
        m_player = BitmapFactory.decodeResource(m_Res, m_ID);
        setImage();
    }

    public void setImage() {
        m_Width = m_player.getWidth();
        m_Height = m_player.getHeight();

        m_Width /= m_Width_To_Divide;
        m_Height /= m_Height_to_divide;

        m_Width *= (int) (m_Width * s_ScreenRatioX);
        m_Height *= (int) (m_Height * s_ScreenRatioY);

        m_player = Bitmap.createScaledBitmap(m_player, m_Width, m_Height, false);
    }

    public Rect getCollisionShape() {
        return new Rect(m_X, m_Y, m_X + m_Width, m_Y + m_Height);
    }

    public Bitmap getM_player() {
        return m_player;
    }

    public int getM_ID() {
        return m_ID;
    }

    public int getM_Height() {
        return m_Height;
    }

    public int getM_Width() {
        return m_Width;
    }

    public int getM_X() {
        return m_X;
    }

    public int getM_Y() {
        return m_Y;
    }

    public void setM_X(int i_X) {
        this.m_X = i_X;
    }

    public void setM_Y(int m_Y) {
        this.m_Y = m_Y;
    }
}