package com.corneloaie.android.myfitnessadvisor.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.corneloaie.android.myfitnessadvisor.dao.ActiveMinutesDao;
import com.corneloaie.android.myfitnessadvisor.dao.BadgeDao;
import com.corneloaie.android.myfitnessadvisor.dao.HeartRateIntradayDao;
import com.corneloaie.android.myfitnessadvisor.dao.HeartRateZoneDao;
import com.corneloaie.android.myfitnessadvisor.dao.HeartrateDao;
import com.corneloaie.android.myfitnessadvisor.dao.LifetimeDao;
import com.corneloaie.android.myfitnessadvisor.dao.ProfileDao;
import com.corneloaie.android.myfitnessadvisor.dao.SleepDao;
import com.corneloaie.android.myfitnessadvisor.dao.SleepDataDao;
import com.corneloaie.android.myfitnessadvisor.dao.SleepStageDao;
import com.corneloaie.android.myfitnessadvisor.dao.SummaryDao;
import com.corneloaie.android.myfitnessadvisor.model.ActiveMinutes;
import com.corneloaie.android.myfitnessadvisor.model.Badge;
import com.corneloaie.android.myfitnessadvisor.model.HeartRateIntraday;
import com.corneloaie.android.myfitnessadvisor.model.HeartRateZone;
import com.corneloaie.android.myfitnessadvisor.model.Heartrate;
import com.corneloaie.android.myfitnessadvisor.model.Lifetime;
import com.corneloaie.android.myfitnessadvisor.model.Profile;
import com.corneloaie.android.myfitnessadvisor.model.Sleep;
import com.corneloaie.android.myfitnessadvisor.model.SleepData;
import com.corneloaie.android.myfitnessadvisor.model.SleepStage;
import com.corneloaie.android.myfitnessadvisor.model.Summary;


@Database(entities = {Summary.class, ActiveMinutes.class, Lifetime.class, Profile.class,
        Badge.class, Heartrate.class, Sleep.class, HeartRateZone.class,
        HeartRateIntraday.class, SleepStage.class, SleepData.class}, version = 1)
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

    public abstract ActiveMinutesDao mActiveMinutesDao();

    public abstract LifetimeDao mLifetimeDao();

    public abstract ProfileDao mProfileDao();

    public abstract BadgeDao mBadgeDao();

    public abstract HeartrateDao mHeartrateDao();

    public abstract HeartRateZoneDao mHeartRateZoneDao();

    public abstract HeartRateIntradayDao mHeartRateIntradayDao();

    public abstract SleepDao mSleepDao();

    public abstract SleepStageDao mSleepStageDao();

    public abstract SleepDataDao mSleepDataDao();
}
