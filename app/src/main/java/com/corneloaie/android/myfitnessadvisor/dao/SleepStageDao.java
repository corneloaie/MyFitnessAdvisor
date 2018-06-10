package com.corneloaie.android.myfitnessadvisor.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.SleepStage;

import java.util.Date;
import java.util.List;

@Dao
public interface SleepStageDao {

    @Query("SELECT * FROM sleepstage WHERE sleepDateFK LIKE :sleepDateSelected")
    List<SleepStage> getSleepStages(Date sleepDateSelected);

    @Insert
    void insert(List<SleepStage> sleepStages);

    @Delete
    void delete(SleepStage sleepStage);
}
