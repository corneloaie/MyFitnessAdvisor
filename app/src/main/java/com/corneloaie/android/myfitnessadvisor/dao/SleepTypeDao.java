package com.corneloaie.android.myfitnessadvisor.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.SleepType;

import java.util.Date;
import java.util.List;

@Dao
public interface SleepTypeDao {

    @Query("SELECT * FROM sleeptype WHERE sleepDateFK LIKE :sleepDateSelected")
    List<SleepType> getSleepStages(Date sleepDateSelected);

    @Insert
    void insert(List<SleepType> sleepTypes);

    @Delete
    void delete(SleepType sleepType);
}
