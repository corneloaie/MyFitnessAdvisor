package com.corneloaie.android.myfitnessadvisor.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.corneloaie.android.myfitnessadvisor.database.DateConverter;

import java.util.Date;

@Entity
public class Lifetime {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private int lifetimeDistance;
    private int lifetimeFloors;
    private int lifetimeSteps;
    private int bestDistance;
    @TypeConverters({DateConverter.class})
    private Date bestDistanceDate;
    private int bestFloors;
    @TypeConverters({DateConverter.class})
    private Date bestFloorsDate;
    private int bestSteps;
    @TypeConverters({DateConverter.class})
    private Date bestStepsDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLifetimeDistance() {
        return lifetimeDistance;
    }

    public void setLifetimeDistance(int lifetimeDistance) {
        this.lifetimeDistance = lifetimeDistance;
    }

    public int getLifetimeFloors() {
        return lifetimeFloors;
    }

    public void setLifetimeFloors(int lifetimeFloors) {
        this.lifetimeFloors = lifetimeFloors;
    }

    public int getLifetimeSteps() {
        return lifetimeSteps;
    }

    public void setLifetimeSteps(int lifetimeSteps) {
        this.lifetimeSteps = lifetimeSteps;
    }

    public int getBestDistance() {
        return bestDistance;
    }

    public void setBestDistance(int bestDistance) {
        this.bestDistance = bestDistance;
    }

    public Date getBestDistanceDate() {
        return bestDistanceDate;
    }

    public void setBestDistanceDate(Date bestDistanceDate) {
        this.bestDistanceDate = bestDistanceDate;
    }

    public int getBestFloors() {
        return bestFloors;
    }

    public void setBestFloors(int bestFloors) {
        this.bestFloors = bestFloors;
    }

    public Date getBestFloorsDate() {
        return bestFloorsDate;
    }

    public void setBestFloorsDate(Date bestFloorsDate) {
        this.bestFloorsDate = bestFloorsDate;
    }

    public int getBestSteps() {
        return bestSteps;
    }

    public void setBestSteps(int bestSteps) {
        this.bestSteps = bestSteps;
    }

    public Date getBestStepsDate() {
        return bestStepsDate;
    }

    public void setBestStepsDate(Date bestStepsDate) {
        this.bestStepsDate = bestStepsDate;
    }

    @Override
    public String toString() {
        return "Lifetime{" +
                "id=" + id +
                ", lifetimeDistance=" + lifetimeDistance +
                ", lifetimeFloors=" + lifetimeFloors +
                ", lifetimeSteps=" + lifetimeSteps +
                ", bestDistance=" + bestDistance +
                ", bestDistanceDate=" + bestDistanceDate +
                ", bestFloors=" + bestFloors +
                ", bestFloorsDate=" + bestFloorsDate +
                ", bestSteps=" + bestSteps +
                ", bestStepsDate=" + bestStepsDate +
                '}';
    }
}
