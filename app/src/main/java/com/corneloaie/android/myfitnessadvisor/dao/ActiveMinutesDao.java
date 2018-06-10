package com.corneloaie.android.myfitnessadvisor.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.ActiveMinutes;

import java.util.Date;

@Dao
public interface ActiveMinutesDao {
    @Query("SELECT * FROM activeminutes WHERE activeMinutesDate LIKE :activeMinutesDateSelected")
    ActiveMinutes getActiveMinutesFromDate(Date activeMinutesDateSelected);

    @Insert
    void insert(ActiveMinutes activeMinutes);

    @Delete
    void delete(ActiveMinutes activeMinutes);
}
