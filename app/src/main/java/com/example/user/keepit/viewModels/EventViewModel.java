package com.example.user.keepit.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.user.keepit.Repository;
import com.example.user.keepit.database.EventEntity;

import java.util.List;

public class EventViewModel extends ViewModel{
    private Repository mRepository;
    private LiveData<List<EventEntity>> eventsList;
    private LiveData<EventEntity> eventEntity;
    public MutableLiveData<Integer> mId = new MutableLiveData<>();

    public EventViewModel(Repository repository, Integer id){
        mRepository = repository;
        mId.setValue(id);
    }
}
