package com.elses.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.elses.myapplication.DatabaseHelper;
import com.elses.myapplication.NavigationScreen;
import com.elses.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.gms.tasks.OnFailureListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BeaconBackgroundScanService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private MessageListener mMessageListener;
    private OnFailureListener onFailureListener;
    private GoogleApiClient mGoogleApiClient;
    DatabaseHelper db;
    private static final String BeaconTag = "BeaconBackground";
    private static final String GenericTag = "Service";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(GenericTag,"onStartCommand.");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, NavigationScreen.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle("Beacon Scanner")
                        .setContentText("is running to ensure else services")
                        .setSmallIcon(R.drawable.elselogo)
                        .setContentIntent(pendingIntent)
                        .setTicker("ticker text")
                        .build();

        startForeground(1, notification);
        if(mGoogleApiClient==null){
            buildGoogleApiClient();
        }
        mGoogleApiClient.connect();

        db = new DatabaseHelper();
        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(BeaconTag, "Discovered " + new String(message.getContent()));
                db.addBeacon(new String(message.getContent()));
            }

            @Override
            public void onLost(Message message) {
                db.removeBeacon(new String(message.getContent()));
                Log.i(BeaconTag, "User is now invisible to Beacon: " +new String(message.getContent()));
                Log.i(BeaconTag, "Total beacons in range : "+db.getAllBeacons().toString());
            }

            @Override
            public void onBleSignalChanged(Message message, BleSignal bleSignal) {
                super.onBleSignalChanged(message, bleSignal);
                //beaconRoot.child(new String(message.getContent())).push(androidId);
                db.addBeacon(new String(message.getContent()));
                Log.i(BeaconTag, "Signal fluctuation for "+new String(message.getContent())+ " ,new RSSI: "+bleSignal.getRssi());
                Log.i(BeaconTag, "Total beacons in range : "+db.getAllBeacons().toString());
            }
        };

        onFailureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(BeaconTag, "Beacon onFailure listener with error: "+e.getMessage());
            }
        };


        return START_STICKY;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(GenericTag, "GoogleApiClient connected");
        subscribe();
    }
    private void subscribe () {
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.DEFAULT)
                .build();

        Nearby.getMessagesClient(this).subscribe(mMessageListener).addOnFailureListener(onFailureListener);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(GenericTag, "Connection suspended : GoogleApiClient with Error code: " + i);
    }
    private synchronized void buildGoogleApiClient () {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Nearby.MESSAGES_API, new MessagesOptions.Builder()
                            .setPermissions(NearbyPermissions.BLE).build())
                    .addConnectionCallbacks(this)
                    .build();
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(GenericTag,"Connection failed : GoogleApiClient with error message : "+connectionResult.getErrorMessage());
    }
}
