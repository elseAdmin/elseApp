package com.elses.myapplication.ui;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.elses.myapplication.BeaconMessageReceiver;
import com.elses.myapplication.DatabaseHelper;
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


public class BeaconConnectivityActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private MessageListener mMessageListener;
    private OnFailureListener onFailureListener;
    private GoogleApiClient mGoogleApiClient;
    DatabaseHelper db;
    private static final String GenericTag = "Base Activity";
    private static final String BeaconTag = "Beacon";
    private static final int PERMISSIONS_REQUEST_CODE = 1111;
    private static final int BACKGROUND_PERMISSIONS_REQUEST_CODE = 911;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper();
        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(BeaconTag, "Discovered beacon with name: " + new String(message.getContent()));
                db.addBeacon(new String(message.getContent()));
            }

            @Override
            public void onLost(Message message) {
                db.removeBeacon(new String(message.getContent()));
                Log.i(BeaconTag, "User is now invisible to Beacon: " +new String(message.getContent()));
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
    }
    @Override
    protected void onResume () {
        super.onResume();
        if (havePermissions()) {
            buildGoogleApiClient();
        }
        Log.i(GenericTag, "GoogleApiClient services resumed");
    }

    // Subscribe to messages in the background.
    private void backgroundSubscribe() {
        Log.i(GenericTag, "Subscribing for background updates.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.getMessagesClient(this).subscribe(getPendingIntent(), options);
    }

    private PendingIntent getPendingIntent() {
        return PendingIntent.getBroadcast(this, BACKGROUND_PERMISSIONS_REQUEST_CODE, new Intent(this, BeaconMessageReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private synchronized void buildGoogleApiClient () {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Nearby.MESSAGES_API, new MessagesOptions.Builder()
                            .setPermissions(NearbyPermissions.BLE).build())
                    .addConnectionCallbacks(this)
                    .enableAutoManage(this, this)
                    .build();
        }
    }

    private boolean havePermissions () {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermissions () {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(GenericTag, "GoogleApiClient connected");
        subscribe();
        backgroundSubscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(GenericTag, "Connection suspended : GoogleApiClient with Error code: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(GenericTag,"Connection failed : GoogleApiClient with error message : "+connectionResult.getErrorMessage());
    }
    private void subscribe () {
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.DEFAULT)
                .build();

        Nearby.getMessagesClient(this).subscribe(mMessageListener).addOnFailureListener(onFailureListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient==null){
            buildGoogleApiClient();
        }
        mGoogleApiClient.connect();
        Log.i("suhail","start");
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        Log.i("suhail","stop");
    }
}
