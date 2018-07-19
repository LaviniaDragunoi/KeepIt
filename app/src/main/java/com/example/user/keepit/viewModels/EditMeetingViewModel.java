package com.example.user.keepit.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.BirthdayEntity;
import com.example.user.keepit.database.EventDao;
import com.example.user.keepit.database.EventsEntity;
import com.example.user.keepit.database.MeetingsEntity;
import com.example.user.keepit.database.NoteEntity;

import java.util.Date;
import java.util.List;

public class EditMeetingViewModel extends ViewModel{

    public String meetingTitle;
    public String meetingPersonName;
    public String location;
    public Date date;
    public String time;
    private Repository mRepository;
    private int mDayId;
    private LiveData<MeetingsEntity> meetingEntity;

    public EditMeetingViewModel(Repository repository, int dayId){
        mRepository = repository;
        mDayId = dayId;
        meetingEntity = repository.getMeetingByDayId(dayId);
    }


}
