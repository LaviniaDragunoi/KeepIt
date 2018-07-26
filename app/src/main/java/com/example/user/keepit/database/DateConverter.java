package com.example.user.keepit.database;
/**
 * Converter type
 * found at: https://github.com/googlecodelabs/android-build-an-app-architecture-components
 */

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp){
    return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date){
        return date == null ? null : date.getTime();
    }
}

