package com.example.user.keepit.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.keepit.Repository;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventEntity;

import java.util.Date;
import java.util.List;

public class EditEventViewModel extends ViewModel{


    private Repository mRepository;
    private LiveData<EventEntity> event;



    public EditEventViewModel(Repository repository, int eventId){
        mRepository = repository;
        event = repository.loadEvent(eventId);
    }

    public LiveData<EventEntity> getEvent() {
        return event;
    }

    public void addEvent(EventEntity event){
        mRepository.insertNewEvent(event);
    }
    public void updateEvent(EventEntity event){
        mRepository.updateExistingEvent(event);
    }


    public void deleteEvent(int eventId) {
        mRepository.deleteEventById(eventId);
    }
}
