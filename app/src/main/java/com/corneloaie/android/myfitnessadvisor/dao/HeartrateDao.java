package com.corneloaie.android.myfitnessadvisor.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.Heartrate;

import java.util.Date;

@Dao
public interface HeartrateDao {

    @Query("SELECT * FROM heartrate WHERE heartRateDate LIKE :heartRateDateSelected")
    Heartrate getHeartrateStats(Date heartRateDateSelected);

    @Insert
    void insert(Heartrate heartrate);

    @Delete
    void delete(Heartrate heartrate);

}
