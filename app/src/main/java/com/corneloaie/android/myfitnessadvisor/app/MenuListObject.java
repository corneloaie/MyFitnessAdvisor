package com.corneloaie.android.myfitnessadvisor.app;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class MenuListObject {
    private static MenuListObject sMenuListObject;
    private List<String> mMenus;

    private MenuListObject(Context context) {
        mMenus = new ArrayList<>();
        mMenus.add("Summary");
        mMenus.add("Lifetime");
        mMenus.add("Heartrate");
        mMenus.add("Sleep");
        mMenus.add("Profile");
        mMenus.add("LifeCoach");
    }

    public static MenuListObject get(Context context) {
        if (sMenuListObject == null) {
            sMenuListObject = new MenuListObject(context);
        }
        return sMenuListObject;
    }

    public List<String> getMenus() {
        return mMenus;
    }


}
