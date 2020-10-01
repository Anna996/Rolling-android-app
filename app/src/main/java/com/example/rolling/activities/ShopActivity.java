package com.example.rolling.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rolling.GameInfo;
import com.example.rolling.ItemClickedListener;
import com.example.rolling.R;
import com.example.rolling.adapters.ShopAdapter;
import com.example.rolling.items.ShopItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class ShopActivity extends Activity {

    private List<ShopItem> shopItems;
    private ShopAdapter shopAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView amountOfCoinsTextView;
    private int amountOfCoins;
    private int numOfPurchasedItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_layuot);

        initAttributes();
        updateItems();
    }

    public String getKey(int i) {
        return i + GameInfo._shopItem_TAG;
    }

    private void initAttributes() {
        RecyclerView recyclerView = findViewById(R.id.shop_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shopItems = new ArrayList<>();
        addItemsToShop();

        sharedPreferences = getSharedPreferences(GameInfo.gameInfo_TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean(GameInfo.shopActivityNotFirstTime_TAG, false)) {
            updateMemoryInfo();
        }

        amountOfCoinsTextView = findViewById(R.id.amountOfCoins_textView);
        amountOfCoins = sharedPreferences.getInt(GameInfo.amountOfCoins_TAG, 0);
        amountOfCoinsTextView.setText(String.format(Locale.getDefault(), "%d", amountOfCoins));
        numOfPurchasedItems = sharedPreferences.getInt(GameInfo.numOfPurchasedItems_TAG, 1);

        shopAdapter = new ShopAdapter(shopItems);
        shopAdapter.setListener(new ItemClickedListener() {
            @Override
            public void onItemClicked(int position, View view) {
                doWhenItemClicked(position);
            }
        });

        recyclerView.setAdapter(shopAdapter);
    }

    private void addItemsToShop() {
        int w1 = 14, w2 = 15, h = 7;
        int[] widthToDivideArray1 = {w1, w1, w1};
        int[] widthToDivideArray2 = {w2, w2, w2};
        int[] heightToDivideArray = {h, h, h};

        shopItems.add(new ShopItem(new int[]{R.drawable.obstacle_17, R.drawable.obstacle_18, R.drawable.obstacle_19},
                widthToDivideArray1, heightToDivideArray, 10));
        shopItems.add(new ShopItem(new int[]{R.drawable.obstacle_20, R.drawable.obstacle_21, R.drawable.obstacle_22},
                widthToDivideArray1, heightToDivideArray, 10));
        shopItems.add(new ShopItem(new int[]{R.drawable.obstacle_23, R.drawable.obstacle_24, R.drawable.obstacle_25},
                widthToDivideArray2, heightToDivideArray, 20));
        shopItems.add(new ShopItem(new int[]{R.drawable.obstacle_26, R.drawable.obstacle_27, R.drawable.obstacle_28},
                widthToDivideArray2, heightToDivideArray, 20));
    }

    private void updateMemoryInfo() {
        int i = 0;
        String key;

        for (ShopItem item : shopItems) {
            key = getKey(i++);
            item.setSet(sharedPreferences.getStringSet(key, new HashSet<String>()));
        }
    }

    private void updateItems() {
        int price;

        for (ShopItem item : shopItems) {
            if (!item.getAcquired()) {
                price = item.getPrice();
                item.setAvailable(amountOfCoins >= price);
            }
        }

        shopAdapter.notifyDataSetChanged();
    }

    private void doWhenItemClicked(int position) {
        ShopItem item = shopItems.get(position);
        int price;

        if (item.getAvailable() && !item.getAcquired()) {
            price = item.getPrice();
            item.setAcquired(true);
            shopAdapter.notifyItemChanged(position);
            amountOfCoins -= price;
            amountOfCoinsTextView.setText(String.format(Locale.getDefault(), "%d", amountOfCoins));
            savePurchasedItem(item);
            updateItems();
        }
    }

    private void savePurchasedItem(ShopItem item) {
        String key = PurchasedActivity.getKey(numOfPurchasedItems++);
        editor.putString(key, item.getFullResIds());
    }

    private void updateItemsSet() {
        int i = 0;
        String key;

        for (ShopItem item : shopItems) {
            key = getKey(i++);
            editor.putStringSet(key, item.getSet());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putBoolean(GameInfo.shopActivityNotFirstTime_TAG, true);
        editor.putInt(GameInfo.amountOfCoins_TAG, amountOfCoins);
        editor.putInt(GameInfo.numOfPurchasedItems_TAG, numOfPurchasedItems);
        updateItemsSet();
        editor.commit();
    }
}