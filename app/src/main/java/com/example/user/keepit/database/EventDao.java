package com.example.user.keepit.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public abstract class EventDao {

    @Query("DELETE FROM events")
    public abstract void deleteAll();

    @Delete
    public abstract void deleteEvent(EventEntity event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertEvent(EventEntity eventEntity);


    @Query("SELECT * FROM events")
    public abstract LiveData<List<EventEntity>> loadAllEvents();

    @Query("SELECT * FROM events WHERE eventType = :meeting")
    public abstract List<EventEntity> getMeetings(String meeting);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateEvent(EventEntity event);

}

