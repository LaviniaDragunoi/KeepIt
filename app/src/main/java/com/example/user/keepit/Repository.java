package com.example.user.keepit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventDao;
import com.example.user.keepit.database.EventEntity;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.fragment.BirthdayFragment.BIRTHDAY_TYPE;
import static com.example.user.keepit.fragment.MeetingFragment.MEETING_TYPE;
import static com.example.user.keepit.fragment.NoteFragment.NOTE_TYPE;

public class Repository {
    private static final Object LOCK = new Object();
    private static Repository sInstance;
    private final AppRoomDatabase mRoomDB;
    private final EventDao mEventDao;
    private final AppExecutors mAppExecutors;

    public Repository(AppExecutors appExecutors, AppRoomDatabase roomDB, EventDao eventDao){
        mAppExecutors = appExecutors;
        mRoomDB = roomDB;
        mEventDao = eventDao;
    }

    public synchronized static Repository getsInstance(AppExecutors appExecutors,
                                                       AppRoomDatabase roomDB, EventDao eventDao) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(appExecutors, roomDB, eventDao);
            }
        }
        return sInstance;
    }

    public LiveData<EventEntity> loadEvent(int id){
        return mEventDao.loadEventById(id);
    }

    public LiveData<List<EventEntity>> initializeEventsList(){
        final MediatorLiveData<List<EventEntity>> mainEventsList = new MediatorLiveData<>();
        LiveData<List<EventEntity>> eventsDb = mEventDao.loadAllEvents();
        eventsDb.observeForever(eventEntities -> {
            if(eventEntities != null && eventEntities.size() >0){
                mAppExecutors.diskIO().execute(()->{

                });
            }else {
                mainEventsList.setValue(null);
            }
        });
        return mainEventsList;
    }
    private void addToDatabase() {
        mEventDao.loadAllEvents();
    }


    //Clean up the database
    private void deleteFromDb() {
        mEventDao.deleteAll();
    }

    public void addEvent(EventEntity eventEntity){
        mAppExecutors.diskIO().execute(()->{
            mEventDao.insertEvent(eventEntity);

        });
    }

    public void insertNewEvent(EventEntity event) {
        mEventDao.insertEvent(event);
    }

    public void updateExistingEvent(EventEntity event) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mEventDao.updateEvent(event);
            }
        });

    }

    public LiveData<List<EventEntity>> getInitialEventsList() {
        return mEventDao.loadAllEvents();
    }

    public LiveData<List<EventEntity>> getMeetings() {

        return mEventDao.getEventsByEventType(MEETING_TYPE);
    }

    public LiveData<List<EventEntity>> getTodaysEvents(String dateString) {
        return mEventDao.getEventsByDate(dateString);
    }

    public List<EventEntity> getTodaysEventsList(String dateString) {
        return mEventDao.getEventsByDateList(dateString);
    }

    public LiveData<List<EventEntity>> getBirthdays() {
        return mEventDao.getEventsByEventType(BIRTHDAY_TYPE);
    }
    public List<EventEntity> getBirthdaysList() {
        return mEventDao.getEventsListByEventType(BIRTHDAY_TYPE);
    }

    public LiveData<List<EventEntity>> getNotes() {
        return mEventDao.getEventsByEventType(NOTE_TYPE);
    }

    public List<EventEntity> getNotesList() {
        return mEventDao.getEventsListByEventType(NOTE_TYPE);
    }
    public List<EventEntity> getMeetingsList() {

        return mEventDao.getEventsListByEventType(MEETING_TYPE);
    }
    public void deleteEventById(int eventId) {
        EventEntity event = mEventDao.loadEventEntityById(eventId);
        mEventDao.deleteEvent(event);
    }

    public void deleteEvents(List<EventEntity> events) {
        mEventDao.deleteEventsList(events);
    }
}