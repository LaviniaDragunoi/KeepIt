package com.example.user.keepit.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Abstract class for RoomDatabase
 * https://classroom.udacity.com/nanodegrees/nd801/
 */
@Database(entities = {EventEntity.class},
        version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppRoomDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "events";
    private static final Object LOCK = new Object();
    private static Builder<AppRoomDatabase> sInstance;

    public static AppRoomDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppRoomDatabase.class, AppRoomDatabase.DATABASE_NAME);
            }
        }
        return sInstance.build();
    }

    public abstract EventDao eventDao();
}
