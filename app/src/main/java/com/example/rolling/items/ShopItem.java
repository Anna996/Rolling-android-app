package com.example.rolling.items;

import com.example.rolling.GameInfo;

import java.util.HashSet;
import java.util.Set;

public class ShopItem {

    private int resIdFirst;
    private int resIdSecond;
    private int resIdThird;
    private int price;
    private Boolean isAvailable;
    private Boolean isAcquired;
    private Set<String> set;
    private int[] widthToDivideArray;
    private int[] heightToDivideArray;

    public ShopItem(int resIdFirst, int resIdSecond, int resIdThird, int price) {
        this.resIdFirst = resIdFirst;
        this.resIdSecond = resIdSecond;
        this.resIdThird = resIdThird;
        this.price = price;
        this.isAvailable = false;
        this.isAcquired = false;
        this.set = new HashSet<>();
    }

    public ShopItem(int[] resIds, int[] widthToDivideArray, int[] heightToDivideArray, int price) {
        this.resIdFirst = resIds[0];
        this.resIdSecond = resIds[1];
        this.resIdThird = resIds[2];
        this.widthToDivideArray = widthToDivideArray;
        this.heightToDivideArray = heightToDivideArray;
        this.price = price;
        this.isAvailable = false;
        this.isAcquired = false;
        this.set = new HashSet<>();
    }

    public int getResIdFirst() {
        return resIdFirst;
    }

    public void setResIdFirst(int resIdFirst) {
        this.resIdFirst = resIdFirst;
    }

    public int getResIdSecond() {
        return resIdSecond;
    }

    public void setResIdSecond(int resIdSecond) {
        this.resIdSecond = resIdSecond;
    }

    public int getResIdThird() {
        return resIdThird;
    }

    public void setResIdThird(int resIdThird) {
        this.resIdThird = resIdThird;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Boolean getAcquired() {
        return isAcquired;
    }

    public void setAcquired(Boolean acquired) {
        isAcquired = acquired;
    }

    public String getFullResIds() {
        return resIdFirst + ";" + resIdSecond + ";" + resIdThird + ";" +
                widthToDivideArray[0] + ";" + widthToDivideArray[1] + ";" + widthToDivideArray[2] + ";" +
                heightToDivideArray[0] + ";" + heightToDivideArray[1] + ";" + heightToDivideArray[2];
    }

    public void setSet(Set<String> set) {
        isAvailable = set.contains(GameInfo.SHOP_ITEM_AVAILABLE_TAG);
        isAcquired = set.contains(GameInfo.SHOP_ITEM_ACQUIRED_TAG);
    }

    public Set<String> getSet() {
        set.clear();
        set.add(getAvailable() ? GameInfo.SHOP_ITEM_AVAILABLE_TAG : GameInfo.SHOP_ITEM_NOT_AVAILABLE_TAG);
        set.add(getAcquired() ? GameInfo.SHOP_ITEM_ACQUIRED_TAG : GameInfo.SHOP_ITEM_NOT_ACQUIRED_TAG);
        return set;
    }
}