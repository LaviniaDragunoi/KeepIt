package com.example.user.keepit.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.keepit.Repository;
import com.example.user.keepit.database.EventEntity;

import java.util.Date;
import java.util.List;

public class EditEventViewModel extends ViewModel{

    public String eventType;
    public String title;
    public Date date;
    public String dateString;
    public String time;
    public String personName;
    public String location;
    public String note;

    private Repository mRepository;



    public EditEventViewModel(Repository repository){
        mRepository = repository;
    }

    public void addMeeting(EventEntity meeting){
        mRepository.addEvent(meeting);
    }

}
