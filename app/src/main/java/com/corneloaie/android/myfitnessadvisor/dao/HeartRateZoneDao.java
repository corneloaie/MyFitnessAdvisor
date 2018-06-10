package com.corneloaie.android.myfitnessadvisor.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.HeartRateZone;

import java.util.Date;
import java.util.List;

@Dao
public interface HeartRateZoneDao {

    @Query("SELECT * FROM heartratezone WHERE heartRateFK LIKE :dateSelected")
    List<HeartRateZone> getHeartRateZones(Date dateSelected);

    @Insert()
    void insert(List<HeartRateZone> heartRateZoneList);

    @Delete
    void delete(HeartRateZone heartRateZone);
}
