package com.corneloaie.android.myfitnessadvisor.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.Profile;

@Dao
public interface ProfileDao {

    @Query("SELECT * FROM profile ORDER BY idProfile DESC LIMIT 1")
    Profile getProfile();

    @Insert
    void insert(Profile profile);

    @Delete
    void delete(Profile profile);


}
