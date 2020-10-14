package com.example.user.keepit.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.keepit.Repository;
import com.example.user.keepit.database.entities.EventEntity;

import java.util.List;

/**
 * ViewModel for EventList displayed in the UI
 */
public class EventViewModel extends ViewModel {

    private Repository mRepository;
    private LiveData<List<EventEntity>> events;

    public EventViewModel(Repository repository) {
        mRepository = repository;
        events = repository.getInitialEventsList();
    }

    public LiveData<List<EventEntity>> getEvents() {
        return events;
    }

    public LiveData<List<EventEntity>> getMeetingsList() {
        return mRepository.getMeetings();
    }

    public LiveData<List<EventEntity>> getEventsOfToday(String today) {

        return mRepository.getTodaysEvents(today);
    }

    public LiveData<List<EventEntity>> getBirthdaysList() {
        return mRepository.getBirthdays();
    }

    public LiveData<List<EventEntity>> getNotesList() {
        return mRepository.getNotes();
    }

    public void deleteEvent(EventEntity eventEntity) {
        mRepository.deleteEventById(eventEntity.getId());
    }

    public void deleteTodaysEvents(String todayDateString) {
        List<EventEntity> events = mRepository.getTodaysEventsList(todayDateString);
        mRepository.deleteEvents(events);
    }

    public void deleteAllBirthdays() {
        List<EventEntity> events = mRepository.getBirthdaysList();
        mRepository.deleteEvents(events);
    }

    public void deleteAllMeetings() {
        List<EventEntity> events = mRepository.getMeetingsList();
        mRepository.deleteEvents(events);
    }

    public void deleteAllNotes() {
        List<EventEntity> events = mRepository.getNotesList();
        mRepository.deleteEvents(events);
    }
}
