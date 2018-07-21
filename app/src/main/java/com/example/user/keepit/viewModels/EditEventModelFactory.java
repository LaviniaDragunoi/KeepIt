package com.example.user.keepit.viewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.user.keepit.Repository;

public class EditEventModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Repository mRepository;


    public EditEventModelFactory(Repository repository){
        mRepository = repository;

    }
    @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new EditEventViewModel(mRepository);
        }

}
