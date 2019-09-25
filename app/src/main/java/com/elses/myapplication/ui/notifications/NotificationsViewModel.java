package com.elses.myapplication.ui.notifications;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.elses.myapplication.DatabaseHelper;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    DatabaseHelper db = new DatabaseHelper();
    public NotificationsViewModel() {
        if(db.getCurrentSlot()==null) {
            mText = new MutableLiveData<>();
            mText.setValue("No notifications as of now!");
        }else{
            mText = new MutableLiveData<>();
            mText.setValue("Your vehicles captured location is : "+db.getCurrentSlot()+".");
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}