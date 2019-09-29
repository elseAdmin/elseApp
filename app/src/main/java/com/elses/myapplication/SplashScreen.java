package com.elses.myapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

public class SplashScreen extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    Handler handler;
    private static final String GenericTag = "Splash Activity";
    private static final int PERMISSIONS_REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, NavigationScreen.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(GenericTag, "Splash Screen GoogleApiClient connected");
        backgroundSubscribe();
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
        return PendingIntent.getBroadcast(this, PERMISSIONS_REQUEST_CODE, new Intent(this, BeaconMessageReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(GenericTag, "Connection suspended : GoogleApiClient with Error code: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(GenericTag,"Connection failed : GoogleApiClient with error message : "+connectionResult.getErrorMessage());
    }
}
