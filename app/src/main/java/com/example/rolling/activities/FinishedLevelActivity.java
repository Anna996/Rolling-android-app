package com.example.rolling.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.rolling.GameInfo;
import com.example.rolling.R;

import java.util.Locale;

public class FinishedLevelActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finished_level_layout);

        TextView scoreTextView = findViewById(R.id.score_TextView);
        TextView bestScoreAnnouncement = findViewById(R.id.bestScoreAnnouncement_TV);
        int currentScore = getIntent().getIntExtra(GameInfo.currentScore_TAG, 0);
        int bestScore = getIntent().getIntExtra(GameInfo.bestScore_TAG, 0);
        scoreTextView.setText(String.format(Locale.getDefault(), "%d", currentScore));
        bestScoreAnnouncement.setVisibility(currentScore > bestScore ? View.VISIBLE : View.INVISIBLE);

        Button nextLevelBtn = findViewById(R.id.nextLevel_btn);
        nextLevelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(GameInfo.nextAction_TAG, GameInfo.NEXT_LEVEL);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button homeBtn = findViewById(R.id.home_btn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(GameInfo.nextAction_TAG, GameInfo.HOME);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
