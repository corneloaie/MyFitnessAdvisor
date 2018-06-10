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
public class HeartRateIntraday {

    @PrimaryKey(autoGenerate = true)
    private int idHeartRateIntraday;
    private String time;
    private int value;
    @TypeConverters({DateConverter.class})
    private Date heartRateFK;


    public int getIdHeartRateIntraday() {
        return idHeartRateIntraday;
    }

    public void setIdHeartRateIntraday(int idHeartRateIntraday) {
        this.idHeartRateIntraday = idHeartRateIntraday;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getHeartRateFK() {
        return heartRateFK;
    }

    public void setHeartRateFK(Date heartRateFK) {
        this.heartRateFK = heartRateFK;
    }

    @Override
    public String toString() {
        return "HeartRateIntraday{" +
                "idHeartRateIntraday=" + idHeartRateIntraday +
                ", time='" + time + '\'' +
                ", value=" + value +
                ", heartRateFK=" + heartRateFK +
                '}';
    }
}
