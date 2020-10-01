package com.example.rolling.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x = 0, y = 0;
    Bitmap m_Background;

    Background(int i_ScreenX, int i_ScreenY, int i_background_id, Resources res) {
        m_Background = BitmapFactory.decodeResource(res, i_background_id);
        m_Background = Bitmap.createScaledBitmap(m_Background, i_ScreenX, i_ScreenY, false);
    }
}
