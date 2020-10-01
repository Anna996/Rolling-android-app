package com.example.rolling.game;

import android.content.res.Resources;

public class MainPlayer extends GameCharacters {

    MainPlayer(int i_ID, int i_Width_To_Divide, int i_Height_to_divide, int i_ScreenX, int i_ScreenY, Resources i_Res) {
        super(i_Res, i_Width_To_Divide, i_Height_to_divide, i_ID);

        m_X = i_ScreenX / 2 - m_Width / 2;
        m_Y = i_ScreenY - 2 * m_Height;
    }
}