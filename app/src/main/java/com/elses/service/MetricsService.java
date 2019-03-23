package com.elses.service;

import android.util.Log;

import com.elses.respository.BeaconMetrics;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Message;
import com.google.firebase.database.DatabaseReference;

public class MetricsService {
    BeaconMetrics metrics;
    public void pushMetrics(DatabaseReference ref, Message message, BleSignal bleSignal){
        Log.i("else", "Message " + new String(message.getContent()) + "  has new BLE signal information: " + bleSignal);

        metrics = new BeaconMetrics(new String(message.getContent()),bleSignal.getRssi(),bleSignal.getTxPower(),1);

        ref.child(metrics.name).child(metrics.timestamp.toString()).setValue(metrics);
    }
}
