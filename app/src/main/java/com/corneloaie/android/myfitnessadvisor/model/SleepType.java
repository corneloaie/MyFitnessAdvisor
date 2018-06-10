package com.corneloaie.android.myfitnessadvisor.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.corneloaie.android.myfitnessadvisor.database.DateConverter;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Sleep.class,
        parentColumns = "dateOfSleep",
        childColumns = "sleepDateFK",
        onDelete = CASCADE))
public class SleepType {

    @PrimaryKey(autoGenerate = true)
    private int idSleepType;

    private long duration;
    private int efficiency;
    private String startTime;
    private String endTime;
    private int timeInBed;
    @TypeConverters({DateConverter.class})
    private Date sleepDateFK;

    public int getIdSleepType() {
        return idSleepType;
    }

    public void setIdSleepType(int idSleepType) {
        this.idSleepType = idSleepType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTimeInBed() {
        return timeInBed;
    }

    public void setTimeInBed(int timeInBed) {
        this.timeInBed = timeInBed;
    }

    public Date getSleepDateFK() {
        return sleepDateFK;
    }

    public void setSleepDateFK(Date sleepDateFK) {
        this.sleepDateFK = sleepDateFK;
    }

    @Override
    public String toString() {
        return "SleepType{" +
                "idSleepType=" + idSleepType +
                ", duration=" + duration +
                ", efficiency=" + efficiency +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", timeInBed=" + timeInBed +
                ", sleepDateFK=" + sleepDateFK +
                '}';
    }
}
