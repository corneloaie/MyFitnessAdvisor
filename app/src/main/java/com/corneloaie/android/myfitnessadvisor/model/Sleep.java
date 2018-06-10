package com.corneloaie.android.myfitnessadvisor.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.corneloaie.android.myfitnessadvisor.database.DateConverter;

import java.util.Date;

@Entity
public class Sleep {

    @PrimaryKey
    @NonNull
    @TypeConverters({DateConverter.class})
    private Date dateOfSleep;

    private int totalMinutesAsleep;
    private int totalTimeInBed;
    private int totalSleepRecords;
    private int deep;
    private int light;
    private int rem;
    private int wake;

    @NonNull
    public Date getDateOfSleep() {
        return dateOfSleep;
    }

    public void setDateOfSleep(@NonNull Date dateOfSleep) {
        this.dateOfSleep = dateOfSleep;
    }

    public int getTotalMinutesAsleep() {
        return totalMinutesAsleep;
    }

    public void setTotalMinutesAsleep(int totalMinutesAsleep) {
        this.totalMinutesAsleep = totalMinutesAsleep;
    }

    public int getTotalTimeInBed() {
        return totalTimeInBed;
    }

    public void setTotalTimeInBed(int totalTimeInBed) {
        this.totalTimeInBed = totalTimeInBed;
    }

    public int getTotalSleepRecords() {
        return totalSleepRecords;
    }

    public void setTotalSleepRecords(int totalSleepRecords) {
        this.totalSleepRecords = totalSleepRecords;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getRem() {
        return rem;
    }

    public void setRem(int rem) {
        this.rem = rem;
    }

    public int getWake() {
        return wake;
    }

    public void setWake(int wake) {
        this.wake = wake;
    }

    @Override
    public String toString() {
        return "Sleep{" +
                "dateOfSleep=" + dateOfSleep +
                ", totalMinutesAsleep=" + totalMinutesAsleep +
                ", totalTimeInBed=" + totalTimeInBed +
                ", totalSleepRecords=" + totalSleepRecords +
                ", deep=" + deep +
                ", light=" + light +
                ", rem=" + rem +
                ", wake=" + wake +
                '}';
    }
}
