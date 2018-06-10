package com.corneloaie.android.myfitnessadvisor.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.corneloaie.android.myfitnessadvisor.model.Badge;

import java.util.List;

@Dao
public interface BadgeDao {

    @Query("SELECT * FROM badge")
    List<Badge> getAllBadges();

    @Insert
    void insert(List<Badge> badgeList);

    @Delete
    void delete(Badge badge);
}
