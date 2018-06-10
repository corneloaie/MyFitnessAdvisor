package com.corneloaie.android.myfitnessadvisor.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.Lifetime;

@Dao
public interface LifetimeDao {

    @Query("SELECT * FROM lifetime ORDER BY id DESC LIMIT 1")
    Lifetime getLifetimeStats();

    @Insert
    void insert(Lifetime lifetime);

    @Delete
    void delete(Lifetime lifetime);
}
