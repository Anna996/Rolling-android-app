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

public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_layout);

        TextView scoreTextView = findViewById(R.id.score_TV);
        TextView bestScoreAnnouncement = findViewById(R.id.bestScoreAnnouncement_textView);
        TextView bestScoreTextView = findViewById(R.id.bestScore_textView);
        int currentScore = getIntent().getIntExtra(GameInfo.currentScore_TAG, 0);
        int bestScore = getIntent().getIntExtra(GameInfo.bestScore_TAG, 0);

        scoreTextView.setText(String.format(Locale.getDefault(), "%d", currentScore));
        if (bestScore > currentScore) {
            bestScoreAnnouncement.setText(R.string.bestScore_text);
            bestScoreTextView.setText(String.format(Locale.getDefault(), "%d", bestScore));
        } else {
            bestScoreAnnouncement.setText(R.string.bestScoreAnnouncement);
            bestScoreTextView.setText("");
        }


        Button tryAgainBtn = findViewById(R.id.tryAgain_btn);
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(GameInfo.nextAction_TAG, GameInfo.TRY_AGAIN);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button homeBtn = findViewById(R.id.home2_btn);
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
