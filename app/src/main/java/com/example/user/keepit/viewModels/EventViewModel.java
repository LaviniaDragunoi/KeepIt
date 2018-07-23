package com.example.user.keepit.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.keepit.Repository;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventEntity;

import java.util.Date;
import java.util.List;

public class EventViewModel extends ViewModel{

    private Repository mRepository;
    private LiveData<List<EventEntity>> events;

    public EventViewModel(Repository repository){
       mRepository = repository;
       events = repository.getInitialEventsList();
    }

    public LiveData<List<EventEntity>> getEvents() {
        return events;
    }

    public LiveData<List<EventEntity>> getMeetingsList(){
        return mRepository.getMeetings();
    }

    public LiveData<List<EventEntity>> getEventsOfToday(String today) {

        return mRepository.getTodaysEvents(today);
    }
}
