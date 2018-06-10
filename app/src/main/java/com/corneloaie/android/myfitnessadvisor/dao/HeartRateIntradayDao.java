package com.corneloaie.android.myfitnessadvisor.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.HeartRateIntraday;

import java.util.Date;
import java.util.List;

@Dao
public interface HeartRateIntradayDao {


    @Query("SELECT * FROM heartrateintraday WHERE heartRateFK LIKE :dateSelected")
    List<HeartRateIntraday> getHeartRateZonesIntraday(Date dateSelected);

    @Insert()
    void insert(List<HeartRateIntraday> heartRateIntradayList);

    @Delete
    void delete(HeartRateIntraday heartRateIntraday);
}
