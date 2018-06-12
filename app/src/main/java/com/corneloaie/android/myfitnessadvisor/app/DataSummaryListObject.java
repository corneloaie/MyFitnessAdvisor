package com.corneloaie.android.myfitnessadvisor.app;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DataSummaryListObject {
    private static DataSummaryListObject sDataSummaryListObject;
    private List<String> mDataSummaryList;

    private DataSummaryListObject(Context context) {
        mDataSummaryList = new ArrayList<>();
        mDataSummaryList.add("calories");
        mDataSummaryList.add("restingHR");
        mDataSummaryList.add("steps");
        mDataSummaryList.add("floors");
        mDataSummaryList.add("sedentaryMinutes");
        mDataSummaryList.add("lightlyActiveMinutes");
        mDataSummaryList.add("fairlyActiveMinutes");
        mDataSummaryList.add("veryActiveMinutes");
    }

    public static DataSummaryListObject get(Context context) {
        if (sDataSummaryListObject == null) {
            sDataSummaryListObject = new DataSummaryListObject(context);
        }
        return sDataSummaryListObject;
    }

    public List<String> getList() {
        return mDataSummaryList;
    }

}
