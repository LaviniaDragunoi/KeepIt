package com.example.user.keepit.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {EventEntity.class},
        version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppRoomDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    public static final String DATABASE_NAME = "events";
    private static Builder<AppRoomDatabase> sInstance;
    public abstract EventDao eventDao();


    public static AppRoomDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppRoomDatabase.class, AppRoomDatabase.DATABASE_NAME);
            }
        }
        return sInstance.build();
    }


}
