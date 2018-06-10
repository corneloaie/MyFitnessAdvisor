package com.corneloaie.android.myfitnessadvisor.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.corneloaie.android.myfitnessadvisor.database.DateConverter;

import java.util.Date;

@Entity
public class Heartrate {

    @PrimaryKey
    @NonNull
    @TypeConverters({DateConverter.class})
    private Date heartRateDate;

    private int restingHeartRate;


    @NonNull
    public Date getHeartRateDate() {
        return heartRateDate;
    }

    public void setHeartRateDate(@NonNull Date heartRateDate) {
        this.heartRateDate = heartRateDate;
    }

    public int getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(int restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
    }

    @Override
    public String toString() {
        return "Heartrate{" +
                "heartRateDate=" + heartRateDate +
                ", restingHeartRate=" + restingHeartRate +
                '}';
    }
}
