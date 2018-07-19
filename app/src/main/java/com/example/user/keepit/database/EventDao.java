package com.example.user.keepit.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public abstract class EventDao {

    @Query("DELETE FROM events")
    public abstract void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertEvents(EventsEntity eventsEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertMeeting(MeetingsEntity meetingsEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertBirthday(BirthdayEntity birthdayEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertNote(NoteEntity noteEntity);

    @Query("SELECT * FROM events")
    public abstract LiveData<List<EventsEntity>> loadAllEvents();

    @Query("SELECT * FROM events")
    public abstract LiveData<List<MeetingsEntity>> loadMeetings();

    @Query("SELECT * FROM events WHERE id = :dayId")
    @Transaction
    public abstract EventsDetails getEventsByDate(int dayId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateMeeting(MeetingsEntity meetingsEntity);

    @Query("SELECT * FROM events WHERE sameDate = :date")
    @Transaction
    public abstract EventsEntity getEventEntityByDate(Date date);

    @Query("SELECT * FROM events WHERE id = :dayId")
    @Transaction
    public abstract LiveData<MeetingsEntity> getMeetingByIdOfDay(int dayId);

}
