package com.corneloaie.android.myfitnessadvisor.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.Summary;

@Dao
public interface SummaryDao {

    @Query("SELECT * FROM summary WHERE summaryDate LIKE :summaryDateSelected")
    Summary getSummaryFromDate(String summaryDateSelected);

    @Insert
    void insert(Summary summary);

    @Delete
    void delete(Summary summary);
}
