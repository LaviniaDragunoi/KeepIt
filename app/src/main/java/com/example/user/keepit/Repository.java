package com.example.user.keepit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventDao;
import com.example.user.keepit.database.EventsEntity;
import com.example.user.keepit.database.MeetingsEntity;

import java.util.Date;
import java.util.List;

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

    //Clean up the database
    private void deleteFromDb() {
        mEventDao.deleteAll();
    }

    public LiveData<List<EventsEntity>> initializeListOfEvents(){
        final MediatorLiveData<List<EventsEntity>> mainEventsList = new MediatorLiveData<>();
        LiveData<List<EventsEntity>> eventsDB = mEventDao.loadAllEvents();
        mainEventsList.addSource(eventsDB, newEvents ->{
            if(mainEventsList.getValue() != newEvents){
                mainEventsList.setValue(newEvents);
            }
        });
        mainEventsList.observeForever(eventsEntityList -> {
            if(eventsEntityList != null && eventsEntityList.size() > 0){
                mAppExecutors.diskIO().execute(this::addToDatabaseNewEntry);
            }else mainEventsList.setValue(null);
        });
        return mainEventsList;
    }

    private void addToDatabaseNewEntry() {
        mEventDao.loadAllEvents();
    }

    public int getEventId(Date date){
       if(mEventDao.getEventEntityByDate(date) == null){
           //Add new eventEntity that has new id and a new Date
           EventsEntity newEvent = new EventsEntity(date);
           mEventDao.insertEvents(newEvent);
           return newEvent.getId();
       }else return mEventDao.getEventEntityByDate(date).getId();
    }

    public LiveData<MeetingsEntity>  getMeetingByDayId(int dayId){
        return mEventDao.getMeetingByIdOfDay(dayId);
    }

    public void initializeMeeting(MeetingsEntity entity){
        mEventDao.insertMeeting(entity);
    }

}
