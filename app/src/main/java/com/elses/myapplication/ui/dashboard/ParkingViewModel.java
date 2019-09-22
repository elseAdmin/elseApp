package com.elses.myapplication.ui.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ParkingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ParkingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is parking fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}