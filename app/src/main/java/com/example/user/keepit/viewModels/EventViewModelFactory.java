package com.example.user.keepit.viewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.user.keepit.Repository;

/**
 * ViewModelFactory for EventsList
 */
public class EventViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Repository mRepository;

    public EventViewModelFactory(Repository repository) {
        mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new EventViewModel(mRepository);
    }
}
