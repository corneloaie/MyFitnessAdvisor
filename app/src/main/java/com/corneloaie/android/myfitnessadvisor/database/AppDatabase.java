package com.corneloaie.android.myfitnessadvisor.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.corneloaie.android.myfitnessadvisor.dao.SummaryDao;
import com.corneloaie.android.myfitnessadvisor.model.Summary;


@Database(entities = {Summary.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase APP_DATABASE = null;

    public static AppDatabase getInstance(Context context) {
        if (APP_DATABASE == null) {
            synchronized (AppDatabase.class) {
                if (APP_DATABASE == null) {
                    APP_DATABASE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database-name").allowMainThreadQueries().build();

                }
            }
        }
        return APP_DATABASE;
    }

    public abstract SummaryDao mSummaryDao();
}
