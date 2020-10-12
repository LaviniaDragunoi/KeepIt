package com.example.user.keepit.viewModels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
