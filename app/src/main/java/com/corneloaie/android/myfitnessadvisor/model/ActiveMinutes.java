package com.corneloaie.android.myfitnessadvisor.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.corneloaie.android.myfitnessadvisor.database.DateConverter;

import java.io.Serializable;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Summary.class,
        parentColumns = "summaryDate",
        childColumns = "activeMinutesDate",
        onDelete = CASCADE))
public class ActiveMinutes implements Serializable {

    @PrimaryKey
    @NonNull
    @TypeConverters({DateConverter.class})
    private Date activeMinutesDate;

    private int fairlyActiveMinutes;
    private int lightlyActiveMinutes;
    private int sedentaryMinutes;
    private int veryActiveMinutes;

    public ActiveMinutes(Date activeMinutesDate, int fairlyActiveMinutes, int lightlyActiveMinutes, int sedentaryMinutes, int veryActiveMinutes) {
        this.activeMinutesDate = activeMinutesDate;
        this.fairlyActiveMinutes = fairlyActiveMinutes;
        this.lightlyActiveMinutes = lightlyActiveMinutes;
        this.sedentaryMinutes = sedentaryMinutes;
        this.veryActiveMinutes = veryActiveMinutes;
    }

    public Date getActiveMinutesDate() {
        return activeMinutesDate;
    }

    public void setActiveMinutesDate(Date activeMinutesDate) {
        this.activeMinutesDate = activeMinutesDate;
    }

    public int getFairlyActiveMinutes() {
        return fairlyActiveMinutes;
    }

    public void setFairlyActiveMinutes(int fairlyActiveMinutes) {
        this.fairlyActiveMinutes = fairlyActiveMinutes;
    }

    public int getLightlyActiveMinutes() {
        return lightlyActiveMinutes;
    }

    public void setLightlyActiveMinutes(int lightlyActiveMinutes) {
        this.lightlyActiveMinutes = lightlyActiveMinutes;
    }

    public int getSedentaryMinutes() {
        return sedentaryMinutes;
    }

    public void setSedentaryMinutes(int sedentaryMinutes) {
        this.sedentaryMinutes = sedentaryMinutes;
    }

    public int getVeryActiveMinutes() {
        return veryActiveMinutes;
    }

    public void setVeryActiveMinutes(int veryActiveMinutes) {
        this.veryActiveMinutes = veryActiveMinutes;
    }

    @Override
    public String toString() {
        return "ActiveMinutes{" +
                "activeMinutesDate=" + activeMinutesDate +
                ", fairlyActiveMinutes=" + fairlyActiveMinutes +
                ", lightlyActiveMinutes=" + lightlyActiveMinutes +
                ", sedentaryMinutes=" + sedentaryMinutes +
                ", veryActiveMinutes=" + veryActiveMinutes +
                '}';
    }
}
