package com.corneloaie.android.myfitnessadvisor.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.ActiveMinutes;

public interface ActiveMinutesDao {
    @Query("SELECT * FROM activeminutes WHERE activeMinutesDate LIKE :activeMinutesDateSelected")
    ActiveMinutes getActiveMinutesFromDate(String activeMinutesDateSelected);

    @Insert
    void insert(ActiveMinutes summary);

    @Delete
    void delete(ActiveMinutes summary);
}
