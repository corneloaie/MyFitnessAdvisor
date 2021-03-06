package com.corneloaie.android.myfitnessadvisor.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.corneloaie.android.myfitnessadvisor.database.DateConverter;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Summary implements Serializable {

    private int activityCalories;
    private int caloriesBMR;
    private int caloriesOut;
    private int floors;
    private int restingHeartRate;
    private int steps;

    @PrimaryKey
    @NonNull
    @TypeConverters({DateConverter.class})
    private Date summaryDate;

    public Summary(int activityCalories, int caloriesBMR, int caloriesOut, int floors, int restingHeartRate, int steps, Date summaryDate) {
        this.activityCalories = activityCalories;
        this.caloriesBMR = caloriesBMR;
        this.caloriesOut = caloriesOut;
        this.floors = floors;
        this.restingHeartRate = restingHeartRate;
        this.steps = steps;
        this.summaryDate = summaryDate;
    }

    public int getActivityCalories() {
        return activityCalories;
    }

    public void setActivityCalories(int activityCalories) {
        this.activityCalories = activityCalories;
    }

    public int getCaloriesBMR() {
        return caloriesBMR;
    }

    public void setCaloriesBMR(int caloriesBMR) {
        this.caloriesBMR = caloriesBMR;
    }

    public int getCaloriesOut() {
        return caloriesOut;
    }

    public void setCaloriesOut(int caloriesOut) {
        this.caloriesOut = caloriesOut;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public int getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(int restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public Date getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(Date summaryDate) {
        this.summaryDate = summaryDate;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "activityCalories=" + activityCalories +
                ", caloriesBMR=" + caloriesBMR +
                ", caloriesOut=" + caloriesOut +
                ", floors=" + floors +
                ", restingHeartRate=" + restingHeartRate +
                ", steps=" + steps +
                ", summaryDate=" + summaryDate +
                '}';
    }
}
