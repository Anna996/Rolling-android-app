package com.example.rolling.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.rolling.GameInfo;
import com.example.rolling.R;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAttributes();

        final ImageView imageView = findViewById(R.id.main_imageView);
        imageView.animate().scaleX(3).scaleY(3).setDuration(500).withEndAction(new Runnable() {
            @Override
            public void run() {
                imageView.animate().scaleX(1).scaleY(1).setDuration(1000);
            }
        }).start();

        Button playBtn = findViewById(R.id.play_btn);
        Button shopBtn = findViewById(R.id.shop_btn);
        Button recordsBtn = findViewById(R.id.records_btn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LevelsActivity.class);
                startActivity(intent);
            }
        });

        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });

        recordsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PurchasedActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initAttributes() {
        sharedPreferences = getSharedPreferences(GameInfo.gameInfo_TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean(GameInfo.mainActivityFirstTime_TAG, true)) {
            editor.putInt(GameInfo.bestScore_TAG, 0);
            editor.putInt(GameInfo.amountOfCoins_TAG, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putBoolean(GameInfo.mainActivityFirstTime_TAG, false);
        editor.commit();
    }
}
