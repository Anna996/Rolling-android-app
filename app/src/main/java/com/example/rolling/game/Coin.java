package com.example.rolling.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.rolling.R;

public class Coin extends GameCharacters {

    private int m_AmountOfCoins;
    private Resources m_Res;

    public Coin(Resources i_Res, int i_AmountOfCoins) {
        super(i_Res, 12, 6, R.drawable.money);

        m_AmountOfCoins = i_AmountOfCoins;
        m_Res = i_Res;
        m_Y = -m_Height;
    }

    public void DoWhenCollected() {
        m_AmountOfCoins++;
    }

    public Bitmap GetSmallPicture() {
        Bitmap bitmap = BitmapFactory.decodeResource(m_Res, this.m_ID);
        int width = bitmap.getWidth() / 14;
        int height = bitmap.getHeight() / 7;

        width *= (int) (width * s_ScreenRatioX);
        height *= (int) (height * s_ScreenRatioY);

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        return bitmap;
    }

    public int getM_AmountOfCoins() {
        return m_AmountOfCoins;
    }
}