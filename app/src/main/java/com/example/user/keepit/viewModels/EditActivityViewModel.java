package com.example.user.keepit.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.keepit.Repository;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.BirthdayEntity;
import com.example.user.keepit.database.EventsEntity;
import com.example.user.keepit.database.MeetingsEntity;
import com.example.user.keepit.database.NoteEntity;

public class EditActivityViewModel extends ViewModel{

    private LiveData<BirthdayEntity> birthdayEntityLiveData;
    private LiveData<MeetingsEntity> meetingsEntityLiveData;
    private LiveData<NoteEntity> noteEntityLiveData;
    private LiveData<EventsEntity> eventsEntityLiveData;

    public EditActivityViewModel(Repository repository, int dayId, int id){

    }
}
