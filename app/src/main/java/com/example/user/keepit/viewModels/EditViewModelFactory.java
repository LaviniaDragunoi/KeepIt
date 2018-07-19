package com.example.user.keepit.viewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.user.keepit.Repository;

public class EditViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Repository mRepository;
    private int mDayId;

    public EditViewModelFactory(Repository repository, int dayId){
        mRepository = repository;
        mDayId = dayId;
    }
    @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new EditMeetingViewModel(mRepository, mDayId);
        }

}
