package com.elses.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

public class BeaconMessageReceiver extends BroadcastReceiver {

    private static final String GenericTag = "Background beacon scanning";

    @Override
    public void onReceive(Context context, Intent intent) {
        Nearby.getMessagesClient(context).handleIntent(intent, new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(GenericTag, "Found message via PendingIntent: " + message);
            }

            @Override
            public void onLost(Message message) {
                Log.i(GenericTag, "Lost message via PendingIntent: " + message);
            }

            @Override
            public void onBleSignalChanged(Message message, BleSignal bleSignal) {
                super.onBleSignalChanged(message, bleSignal);
                Log.i(GenericTag, "Signal fluctuation for "+new String(message.getContent())+ " ,new RSSI: "+bleSignal.getRssi());
            }
        });
    }
}
