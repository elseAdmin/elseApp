package com.elses.service;

import android.util.Log;

import com.elses.myapplication.Constants;
import com.elses.respository.BeaconMetrics;
import com.elses.respository.MetaData;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Message;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;

public class MetricsService {
    BeaconMetrics metrics;
    public void pushMetrics(DatabaseReference ref, Message message, BleSignal bleSignal){
        Log.i("else", "Message " + new String(message.getContent()) + "  has new BLE signal information: " + bleSignal);
    if(Constants.recording) {
        switch (new String(message.getContent())) {
            case "Beacon 1":
                metrics = new BeaconMetrics(new String(message.getContent()), bleSignal.getRssi(), bleSignal.getTxPower(), Constants.distance1);
                break;
            case "Beacon 2":
                metrics = new BeaconMetrics(new String(message.getContent()), bleSignal.getRssi(), bleSignal.getTxPower(), Constants.distance2);
                break;
            case "Beacon 3":
                metrics = new BeaconMetrics(new String(message.getContent()), bleSignal.getRssi(), bleSignal.getTxPower(), Constants.distance3);
                break;
            case "Beacon 4":
                metrics = new BeaconMetrics(new String(message.getContent()), bleSignal.getRssi(), bleSignal.getTxPower(), Constants.distance4);
                break;
        }
        ref.child(Constants.testCase).child(metrics.name).child(metrics.timestamp.toString()).setValue(metrics);
    }
    }

    public void pushMetaData(DatabaseReference myRef, Date startedAt, Date stoppedAt) {
        MetaData meta = new MetaData();
        meta.setRecordingStoppedAt(stoppedAt.toString());
meta.setRecordingStartedAt(startedAt.toString());
        myRef.child(Constants.testCase).child("metaData").setValue(meta);
    }
}
