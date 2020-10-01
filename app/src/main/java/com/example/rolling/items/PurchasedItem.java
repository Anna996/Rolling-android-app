package com.example.rolling.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PurchasedItem {

    private ShopItem shopItem;
    private Boolean isDefault;
    private Boolean isSelected;

    public PurchasedItem(String fullResIds) {
        if (!fullResIds.isEmpty()) {
            int[][] info = getResIds(fullResIds);

            shopItem = new ShopItem(info[0], info[1], info[2], 0);
            isDefault = false;
            isSelected = false;
        }
    }

    public PurchasedItem(int firstResId, int secondResId, int thirdResId) {
        shopItem = new ShopItem(firstResId, secondResId, thirdResId, 0);
        isDefault = false;
        isSelected = false;
    }

    public static int[][] getResIds(String fullResIds) {
        List<String> ids = new ArrayList<>(Arrays.asList(fullResIds.split(";")));
        int[] resIds = new int[3];
        int[] widthToDivideArray = new int[3];
        int[] heightToDivideArray = new int[3];

        resIds[0] = Integer.parseInt(ids.get(0));
        resIds[1] = Integer.parseInt(ids.get(1));
        resIds[2] = Integer.parseInt(ids.get(2));

        widthToDivideArray[0] = Integer.parseInt(ids.get(3));
        widthToDivideArray[1] = Integer.parseInt(ids.get(4));
        widthToDivideArray[2] = Integer.parseInt(ids.get(5));

        heightToDivideArray[0] = Integer.parseInt(ids.get(6));
        heightToDivideArray[1] = Integer.parseInt(ids.get(7));
        heightToDivideArray[2] = Integer.parseInt(ids.get(8));

        return new int[][]{resIds, widthToDivideArray, heightToDivideArray};
    }

    public int getResIdFirst() {
        return shopItem.getResIdFirst();
    }

    public int getResIdSecond() {
        return shopItem.getResIdSecond();
    }

    public int getResIdThird() {
        return shopItem.getResIdThird();
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getFullResId() {
        return shopItem.getFullResIds();
    }
}