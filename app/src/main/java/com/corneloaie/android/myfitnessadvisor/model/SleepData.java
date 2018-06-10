package com.corneloaie.android.myfitnessadvisor.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = SleepStage.class,
        parentColumns = "idSleepStage",
        childColumns = "idSleepStageFK",
        onDelete = CASCADE))
public class SleepData {

    String dateTime;
    String level;
    int seconds;
    int idSleepStageFK;
    @PrimaryKey(autoGenerate = true)
    private int idSleepData;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getIdSleepStageFK() {
        return idSleepStageFK;
    }

    public void setIdSleepStageFK(int idSleepStageFK) {
        this.idSleepStageFK = idSleepStageFK;
    }

    public int getIdSleepData() {
        return idSleepData;
    }

    public void setIdSleepData(int idSleepData) {
        this.idSleepData = idSleepData;
    }

    @Override
    public String toString() {
        return "SleepData{" +
                "dateTime='" + dateTime + '\'' +
                ", level='" + level + '\'' +
                ", seconds=" + seconds +
                ", idSleepStageFK=" + idSleepStageFK +
                ", idSleepData=" + idSleepData +
                '}';
    }
}
