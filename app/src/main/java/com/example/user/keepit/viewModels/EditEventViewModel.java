package com.example.user.keepit.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.user.keepit.Repository;
import com.example.user.keepit.database.entities.EventEntity;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import static com.example.user.keepit.utils.Constants.MEETING_TYPE;

/**
 * ViewModel for EditActivity, will take information from Repository and prepare data for displaying in UI
 */
public class EditEventViewModel extends ViewModel {
    private Repository mRepository;
    private LiveData<EventEntity> event;
    MutableLiveData<Boolean> permissionsUpdated;
    MutableLiveData<String> meetingLocation;
    private MutableLiveData<Boolean> showLocationOnMap;

    public EditEventViewModel(Repository repository, int eventId) {
        mRepository = repository;
        event = repository.loadEvent(eventId);
        permissionsUpdated = new MutableLiveData<>();
        meetingLocation = new MutableLiveData<>();
        showLocationOnMap = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getPermissionsUpdated() {
        return permissionsUpdated;
    }

    public void setPermissionsUpdate(boolean granted) {
        permissionsUpdated.setValue(granted);
    }

    public void setMeetingLocation(String meetingLocation) {

        this.meetingLocation.setValue(meetingLocation);
    }

    public MutableLiveData<String> getMeetingLocation() {
        return meetingLocation;
    }

    public LiveData<EventEntity> getEvent() {
        return event;
    }

    public void addEvent(EventEntity event) {
        mRepository.insertNewEvent(event);
    }

    public void updateEvent(EventEntity event) {
        mRepository.updateExistingEvent(event);
    }

    public void deleteEvent(int eventId) {
        mRepository.deleteEventById(eventId);
    }

    public void setShowLocation(boolean show) {
        showLocationOnMap.setValue(show);
    }

    public MutableLiveData<Boolean> getShowLocationOnMap() {
        return showLocationOnMap;
    }

}
