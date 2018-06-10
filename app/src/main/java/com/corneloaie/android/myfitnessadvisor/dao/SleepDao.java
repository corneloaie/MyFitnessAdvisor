package com.corneloaie.android.myfitnessadvisor.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.Sleep;

@Dao
public interface SleepDao {

    @Query("SELECT * FROM sleep WHERE dateOfSleep LIKE :dateOfSleepSelected")
    Sleep getSleepBasicStats(int dateOfSleepSelected);

    @Insert
    void insert(Sleep sleep);

    @Delete
    void delete(Sleep sleep);
}
