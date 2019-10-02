package com.elses.myapplication.ui.notifications;


import com.elses.myapplication.DatabaseHelper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    DatabaseHelper db = new DatabaseHelper();
    public NotificationsViewModel() {
        if(db.getCurrentSlot().equals("")) {
            mText = new MutableLiveData<>();
            mText.setValue("No notifications as of now!");
        }else{
            mText = new MutableLiveData<>();
            mText.setValue("Your vehicle's captured location is : "+db.getCurrentSlot()+".");
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}