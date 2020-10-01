package com.example.rolling.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.example.rolling.GameInfo;
import com.example.rolling.InfoChangedListener;
import com.example.rolling.game.GameView;
import com.example.rolling.items.LevelSettings;
import com.example.rolling.items.PurchasedItem;

public class GameActivity extends Activity {

    private int numOfLevel;
    private int currentScore;
    private int bestScore;
    private int amountOfCoins;
    private boolean levelFinished;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Intent resultIntent;
    private Point point;
    private GameView gameView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAttributes();
        setContentView(gameView);
    }

    public int getScreenX() {
        return point.x;
    }

    public int getScreenY() {
        return point.y;
    }

    private void initAttributes() {
        currentScore = 0;
        levelFinished = false;
        numOfLevel = getIntent().getIntExtra(GameInfo.numOfCurrentLevel_TAG, 0);
        sharedPreferences = getSharedPreferences(GameInfo.gameInfo_TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        bestScore = sharedPreferences.getInt(GameInfo.bestScore_TAG, 0);
        amountOfCoins = sharedPreferences.getInt(GameInfo.amountOfCoins_TAG, 0);
        resultIntent = new Intent();
        point = new Point();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindowManager().getDefaultDisplay().getRealSize(point);

        LevelSettings settings = getIntent().getParcelableExtra(GameInfo.levelSettings_TAG);
        if (!sharedPreferences.getBoolean(GameInfo.default_obstacles_TAG, true)) {
            String ids = sharedPreferences.getString(GameInfo.obstacles_images_TAG, "");
            if (!ids.isEmpty()) {
                int[][] info = PurchasedItem.getResIds(ids);
                settings = new LevelSettings(settings.getGameTime(), settings.getStartSpeed(), settings.getMaxSpeed(),
                        settings.getResIdFirstBackground(), settings.getResIdSecondBackground(),
                        info[0], info[1], info[2]);
            }
        }
        gameView = new GameView(this, settings, bestScore, amountOfCoins);
        gameView.setGameOverListener(new InfoChangedListener<Integer, Boolean>() {
            @Override
            public void infoChanged(Integer score, Integer coins, Boolean isFinished) {
                currentScore = score;
                amountOfCoins = coins;
                levelFinished = isFinished;
                gameOver();
            }
        });
    }

    private void gameOver() {
        Intent intent;
        int TAG = levelFinished ? GameInfo.LEVEL_FINISHED : GameInfo.LEVEL_NOT_FINISHED;
        resultIntent.putExtra(GameInfo.levelFinished_TAG, new int[]{TAG, numOfLevel});

        if (currentScore > bestScore) {
            editor.putInt(GameInfo.bestScore_TAG, currentScore);
        }
        if (levelFinished) {
            intent = new Intent(GameActivity.this, FinishedLevelActivity.class);
        } else {
            intent = new Intent(GameActivity.this, GameOverActivity.class);
        }
        intent.putExtra(GameInfo.currentScore_TAG, currentScore);
        intent.putExtra(GameInfo.bestScore_TAG, bestScore);
        startActivityForResult(intent, GameInfo.FINISHED_GAME_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int action;

        if (requestCode == GameInfo.FINISHED_GAME_REQUEST) {
            if (resultCode == RESULT_OK) {
                action = data.getIntExtra(GameInfo.nextAction_TAG, 0);
                resultIntent.putExtra(GameInfo.nextAction_TAG, action);
            }
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        editor.putInt(GameInfo.amountOfCoins_TAG, amountOfCoins);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
