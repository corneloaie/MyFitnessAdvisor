package com.corneloaie.android.myfitnessadvisor.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.SleepData;

import java.util.List;

@Dao
public interface SleepDataDao {

    @Query("SELECT * FROM sleepdata WHERE idSleepTypeFK LIKE :idSleepTypeSelected")
    List<SleepData> getSleepData(int idSleepTypeSelected);

    @Insert
    void insert(List<SleepData> sleepDataList);

    @Delete
    void delete(SleepData sleepData);
}
