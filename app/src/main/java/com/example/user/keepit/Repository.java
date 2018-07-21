package com.example.user.keepit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventDao;
import com.example.user.keepit.database.EventEntity;

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


    public LiveData<List<EventEntity>> initializeEventsList(){
        final MediatorLiveData<List<EventEntity>> mainEventsList = new MediatorLiveData<>();
        LiveData<List<EventEntity>> eventsDb = mEventDao.loadAllEvents();
        mainEventsList.addSource(eventsDb, newEvents ->{
            if(mainEventsList.getValue() != newEvents){
                mainEventsList.setValue(newEvents);
            }
        });
        eventsDb.observeForever(eventEntities -> {
            if(eventEntities != null && eventEntities.size() >0){
                mAppExecutors.diskIO().execute(()->{
//                    deleteFromDb();
//                    for(int i = 0; i< eventEntities.size(); i++){
//                        addEvent(eventEntities.get(i));
//                    }
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
}
