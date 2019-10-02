package com.elses.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.common.stats.LoggingConstants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DatabaseHelper {

    public DatabaseHelper() {
        if(beacons==null)
        beacons = new HashSet<>();
    }

    private static DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference();
    private static Set<String> beacons;
    private static String userId;
    private static String currentSlot = "";
    private static boolean isUserInPremise = false;

    public boolean isIsUserInPremise() {
        return isUserInPremise;
    }

    public void setIsUserInPremise(boolean isUserInPremise) {
        DatabaseHelper.isUserInPremise = isUserInPremise;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        DatabaseHelper.userId = userId;
    }

    public DatabaseReference getDbRootRef() {
        return dbRootRef;
    }

    public String getCurrentSlot() {
        return currentSlot;
    }

    public void setCurrentSlot(String currentSlot) {
        DatabaseHelper.currentSlot = currentSlot;
    }

    public void addBeacon (String name) {
       beacons.add(name);
    }
    public boolean isBeaconDetected(String beaconName){
        return beacons.contains(beaconName);
    }

    public void removeBeacon(String beaconName) {
      beacons.remove(beaconName);
    }

    public Set<String> getAllBeacons(){ return beacons; }
}
