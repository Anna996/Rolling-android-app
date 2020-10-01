package com.example.rolling.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolling.GameInfo;
import com.example.rolling.items.LevelSettings;
import com.example.rolling.items.Level;
import com.example.rolling.ItemClickedListener;
import com.example.rolling.adapters.LevelsAdapter;
import com.example.rolling.R;

import java.util.ArrayList;
import java.util.List;

public class LevelsActivity extends Activity {

    private List<Level> levelList;
    private LevelsAdapter levelsAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levels_layout);

        initAttributes();
        updateMemoryInfo();
    }

    public void LevelFinished(int position) {
        if (position == levelList.size() - 1) {
            //FINISHED ALL LEVELS
        } else {
            int current = sharedPreferences.getInt(GameInfo.numOfLastAvailableLevel_TAG, 0);
            if (position == current) {
                levelList.get(position + 1).setAvailable(true);
                editor.putInt(GameInfo.numOfLastAvailableLevel_TAG, position + 1);
                levelsAdapter.notifyItemChanged(position + 1);
            }
        }
    }

    private void initAttributes() {
        RecyclerView recyclerView = findViewById(R.id.levels_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        levelList = new ArrayList<>();
        Level.setResIdAvailable(R.drawable.opend_image);
        Level.setResIdNotAvailable(R.drawable.locked_image);
        addLevelsToList();

        sharedPreferences = getSharedPreferences(GameInfo.gameInfo_TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        levelsAdapter = new LevelsAdapter(levelList);
        levelsAdapter.setListener(new ItemClickedListener() {
            @Override
            public void onItemClicked(int position, View view) {
                openLevel(position);
            }
        });

        recyclerView.setAdapter(levelsAdapter);
    }

    /**
     * If activity is used for the fist time, only first level is available.
     * If not, then update all the levels that are already available.
     */
    private void updateMemoryInfo() {
        if (sharedPreferences.getBoolean(GameInfo.levelsActivityFirstTime_TAG, true)) {
            editor.putInt(GameInfo.numOfLastAvailableLevel_TAG, 0);
        } else {
            int numOfLevelsToUpdate = sharedPreferences.getInt(GameInfo.numOfLastAvailableLevel_TAG, 0);
            if (numOfLevelsToUpdate > 0) {
                for (int i = 1; i <= numOfLevelsToUpdate; i++) {
                    levelList.get(i).setAvailable(true);
                }
            }
        }
    }

    private void addLevelsToList() {
        List<LevelSettings> settingsList = updateSettings();
        String key;
        int id;

        for (int i = 0; i < settingsList.size(); i++) {
            key = getKey(i + 1);
            id = getResources().getIdentifier(key, "string", getPackageName());
            levelList.add(new Level(getString(id), settingsList.get(i)));
        }

        levelList.get(0).setAvailable(true);
    }

    private List<LevelSettings> updateSettings() {

        int w = 10, h = 5;
        int[] widthToDivideArray = {w, w, w};
        int[] heightToDivideArray = {h, h, h};
        List<LevelSettings> settingsList = new ArrayList<>();

        settingsList.add(new LevelSettings(20, 20,30,
                R.drawable.game_background_1, R.drawable.game_background_2,
                new int[]{R.drawable.beach, R.drawable.sports32, R.drawable.golf},
                widthToDivideArray, heightToDivideArray));

        settingsList.add(new LevelSettings(30, 23,33,
                R.drawable.farm_background, R.drawable.farm_background2,
                new int[]{R.drawable.cow, R.drawable.chick, R.drawable.pig},
                widthToDivideArray, heightToDivideArray));

        settingsList.add(new LevelSettings(40, 26,36,
                R.drawable.game_background_4, R.drawable.game_background_4,
                new int[]{R.drawable.planets, R.drawable.planet_yellow, R.drawable.planet_colorful},
                widthToDivideArray, heightToDivideArray));

        settingsList.add(new LevelSettings(50, 29,40,
                R.drawable.road_background, R.drawable.road_background,
                new int[]{R.drawable.wheels, R.drawable.car, R.drawable.police_car},
                widthToDivideArray, heightToDivideArray));

        settingsList.add(new LevelSettings(60, 32,43,
                R.drawable.flowerfield_background, R.drawable.flowerfield_background2,
                new int[]{R.drawable.flower, R.drawable.flower2, R.drawable.flower3},
                widthToDivideArray, heightToDivideArray));

        settingsList.add(new LevelSettings(70,35, 46,
                R.drawable.sea_background, R.drawable.sea_background2,
                new int[]{R.drawable.mollusc, R.drawable.crab, R.drawable.dolphin},
                widthToDivideArray, heightToDivideArray));

        settingsList.add(new LevelSettings(80, 38,50,
                R.drawable.game_background_3, R.drawable.game_background_3,
                new int[]{R.drawable.moon, R.drawable.planet_pink, R.drawable.planet_green},
                widthToDivideArray, heightToDivideArray));

        settingsList.add(new LevelSettings(90, 41,55,
                R.drawable.drink_background, R.drawable.drink_background2,
                new int[]{R.drawable.cocktail, R.drawable.cocktail2, R.drawable.coconut},
                widthToDivideArray, heightToDivideArray));

        settingsList.add(new LevelSettings(100, 45,60,
                R.drawable.jungle_background, R.drawable.jungle_background2,
                new int[]{R.drawable.tiger, R.drawable.elephant, R.drawable.monkey},
                widthToDivideArray, heightToDivideArray));

        settingsList.add(new LevelSettings(110, 50,70,
                R.drawable.game_background_7, R.drawable.game_background_8,
                new int[]{R.drawable.universe_blue, R.drawable.universe, R.drawable.planet_yellow},
                widthToDivideArray, heightToDivideArray));

        return settingsList;
    }

    private String getKey(int i) {
        return "level" + i + "_text";
    }

    private void openLevel(int position) {
        if (position < levelList.size()) {
            // Check if the level is available or not:
            Level level = levelList.get(position);
            if (level.getAvailable()) {
                Intent intent = new Intent(LevelsActivity.this, GameActivity.class);
                intent.putExtra(GameInfo.numOfCurrentLevel_TAG, position);
                intent.putExtra(GameInfo.levelSettings_TAG, level.getSettings());
                startActivityForResult(intent, GameInfo.LEVEL_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GameInfo.LEVEL_REQUEST) {
            if (resultCode == RESULT_OK) {
                int[] levelFinished = data.getIntArrayExtra(GameInfo.levelFinished_TAG);
                int level = levelFinished[1];

                if (levelFinished[0] == GameInfo.LEVEL_FINISHED) {
                    LevelFinished(level);
                }

                int action = data.getIntExtra(GameInfo.nextAction_TAG, 0);
                if (action == GameInfo.TRY_AGAIN) {
                    openLevel(level);
                } else if (action == GameInfo.NEXT_LEVEL) {
                    openLevel(level + 1);
                } else if (action == GameInfo.HOME) {
                    onPause();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putBoolean(GameInfo.levelsActivityFirstTime_TAG, false);
        editor.commit();
    }
}