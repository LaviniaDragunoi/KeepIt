package com.example.user.keepit.viewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.user.keepit.Repository;

/**
 * ViewModelFactory for EditEventViewModel
 */
public class EditEventModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final int mEventId;
    private Repository mRepository;

    public EditEventModelFactory(Repository repository, int eventId) {
        mRepository = repository;
        mEventId = eventId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new EditEventViewModel(mRepository, mEventId);
    }
}
