package com.corneloaie.android.myfitnessadvisor.database;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @TypeConverter
    public static Date fromString(String value) {
        try {
            return value == null ? null : df.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static Long dateToString(Date date) {
        return date == null ? null : date.getTime();
    }

}
