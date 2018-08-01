package com.example.user.keepit.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

/**
 * Abstract class that contains methods to access the database
 */
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

    @Query("SELECT * FROM events WHERE eventType = :eventType")
    public abstract LiveData<List<EventEntity>> getEventsByEventType(String eventType);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateEvent(EventEntity event);

    @Query("SELECT * FROM events WHERE id = :eventId")
    public abstract LiveData<EventEntity> loadEventById(int eventId);

    @Query("SELECT * FROM events WHERE dateString = :date")
    public abstract LiveData<List<EventEntity>> getEventsByDate(String date);

    @Query("SELECT * FROM events WHERE dateString = :date")
    public abstract List<EventEntity> getEventsByDateList(String date);

    @Query("SELECT * FROM events WHERE id = :eventId")
    public abstract EventEntity loadEventEntityById(int eventId);

    @Delete
    public abstract void deleteEventsList(List<EventEntity> events);

    @Query("SELECT * FROM events WHERE eventType = :eventType")
    public abstract List<EventEntity> getEventsListByEventType(String eventType);
}

