package com.example.rolling.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolling.GameInfo;
import com.example.rolling.ItemClickedListener;
import com.example.rolling.R;
import com.example.rolling.adapters.PurchasedAdapter;
import com.example.rolling.items.PurchasedItem;

import java.util.ArrayList;
import java.util.List;

public class PurchasedActivity extends Activity {

    private List<PurchasedItem> purchasedItems;
    private static int numOfPurchasedItems;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private PurchasedAdapter purchasedAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchased_layout);

        initAttributes();
    }

    public static String getKey(int i) {
        return i + GameInfo._purchasedItem_TAG;
    }

    public String getItemSelectedKey(int i) {
        return i + GameInfo._purchasedItemSelected_TAG;
    }

    private void initAttributes() {
        RecyclerView recyclerView = findViewById(R.id.purchased_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        sharedPreferences = getSharedPreferences(GameInfo.gameInfo_TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        purchasedItems = new ArrayList<>();
        purchasedItems.add(new PurchasedItem(R.drawable.obstacle_4, R.drawable.obstacle_5, R.drawable.obstacle_6));
        purchasedItems.get(0).setDefault(true);
        purchasedItems.get(0).setSelected(true);
        numOfPurchasedItems = sharedPreferences.getInt(GameInfo.numOfPurchasedItems_TAG, 0);
        if (numOfPurchasedItems == 0) {
            editor.putInt(GameInfo.numOfPurchasedItems_TAG, 1);
        }
        for (int i = 1; i < numOfPurchasedItems; i++) {
            String key = getKey(i);
            String fullResIds = sharedPreferences.getString(key, "");
            purchasedItems.add(new PurchasedItem(fullResIds));
        }

        if (sharedPreferences.getBoolean(GameInfo.purchasedActivityNotFirstTime_TAG, false)) {
            updateMemoryInfo();
        }

        purchasedAdapter = new PurchasedAdapter(purchasedItems);
        purchasedAdapter.setListener(new ItemClickedListener() {
            @Override
            public void onItemClicked(int position, View view) {
                doWhenItemClicked(position);
            }
        });

        recyclerView.setAdapter(purchasedAdapter);
    }

    private void updateMemoryInfo() {
        int i = 0;
        String key;

        for (PurchasedItem item : purchasedItems) {
            key = getItemSelectedKey(i++);
            item.setSelected(sharedPreferences.getBoolean(key, false));
        }
    }

    private void doWhenItemClicked(int position) {
        for (PurchasedItem item : purchasedItems) {
            item.setSelected(false);
        }
        PurchasedItem item = purchasedItems.get(position);
        item.setSelected(true);
        purchasedAdapter.notifyDataSetChanged();

        Boolean isDefault = item.isDefault();
        editor.putBoolean(GameInfo.default_obstacles_TAG, isDefault);
        if (!isDefault) {
            editor.putString(GameInfo.obstacles_images_TAG, item.getFullResId());
        }
    }

    private void saveItemsInfo() {
        int i = 0;
        String key;

        for (PurchasedItem item : purchasedItems) {
            key = getItemSelectedKey(i++);
            editor.putBoolean(key, item.isSelected());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putBoolean(GameInfo.purchasedActivityNotFirstTime_TAG, true);
        saveItemsInfo();
        editor.commit();
    }
}