package com.example.user.keepit.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;

import android.content.Context;

import com.example.user.keepit.database.convertors.DateConverter;
import com.example.user.keepit.database.entities.EventEntity;

/**
 * Abstract class for RoomDatabase
 * https://classroom.udacity.com/nanodegrees/nd801/
 */
@Database(entities = {EventEntity.class},
        version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppRoomDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "events";
    private static final Object LOCK = new Object();
    private static Builder<AppRoomDatabase> sInstance;


    public static AppRoomDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppRoomDatabase.class, AppRoomDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration();
            }
        }
        return sInstance.build();
    }

    public abstract EventDao eventDao();
}
