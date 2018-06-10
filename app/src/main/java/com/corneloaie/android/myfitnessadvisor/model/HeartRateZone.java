package com.corneloaie.android.myfitnessadvisor.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.corneloaie.android.myfitnessadvisor.database.DateConverter;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Heartrate.class,
        parentColumns = "heartRateDate",
        childColumns = "heartRateFK",
        onDelete = CASCADE))
public class HeartRateZone {

    @PrimaryKey(autoGenerate = true)
    private int idHeartRateZone;
    private double caloriesOut;
    private int max;
    private int min;
    private int minutes;
    private String name;

    @TypeConverters({DateConverter.class})
    private Date heartRateFK;


    public int getIdHeartRateZone() {
        return idHeartRateZone;
    }

    public void setIdHeartRateZone(int idHeartRateZone) {
        this.idHeartRateZone = idHeartRateZone;
    }

    public double getCaloriesOut() {
        return caloriesOut;
    }

    public void setCaloriesOut(double caloriesOut) {
        this.caloriesOut = caloriesOut;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getHeartRateFK() {
        return heartRateFK;
    }

    public void setHeartRateFK(Date heartRateFK) {
        this.heartRateFK = heartRateFK;
    }

    @Override
    public String toString() {
        return "HeartRateZone{" +
                "idHeartRateZone=" + idHeartRateZone +
                ", caloriesOut=" + caloriesOut +
                ", max=" + max +
                ", min=" + min +
                ", minutes=" + minutes +
                ", name='" + name + '\'' +
                ", heartRateFK=" + heartRateFK +
                '}';
    }
}
