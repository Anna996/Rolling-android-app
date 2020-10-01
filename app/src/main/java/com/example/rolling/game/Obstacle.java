package com.example.rolling.game;

import android.content.res.Resources;

public class Obstacle extends GameCharacters {

    public int speed = 20;

    public Obstacle(Resources i_Res, int i_Width_To_Divide, int i_Height_to_divide, int i_ResIdImage) {
        super(i_Res, i_Width_To_Divide, i_Height_to_divide, i_ResIdImage);
        m_X = 2 * m_Width;
    }
}