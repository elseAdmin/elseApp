package com.elses.service;

import com.elses.myapplication.DatabaseHelper;

public class ParkService {
    DatabaseHelper db;

    public void setSlot1Proxi(Integer slot1Proxi) {
        this.slot1Proxi = slot1Proxi;
        db.getReading(1);
    }

    public void setSlot2Proxi(Integer slot2Proxi) {
        this.slot2Proxi = slot2Proxi;
        db.getReading(2);
    }

    public Integer slot1Proxi,slot2Proxi;

    public ParkService(DatabaseHelper db){
        this.db = db;
    }

}
