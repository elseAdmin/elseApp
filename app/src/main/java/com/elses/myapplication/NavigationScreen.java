package com.elses.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.elses.service.ParkService;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class NavigationScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private MessageListener mMessageListener;
    private OnFailureListener onFailureListener;
    private GoogleApiClient mGoogleApiClient;
    private static final String BeaconTag = "Beacon";
    private static final String GenericcTag = "ElseApp";
    private static final int PERMISSIONS_REQUEST_CODE = 1111;
    DatabaseHelper db;
    ParkService parkService;
    private DatabaseReference proxi1,proxi2,root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_screen);
        root = FirebaseDatabase.getInstance().getReference();
        proxi1 = FirebaseDatabase.getInstance().getReference().child("parking").child("CarSlot1");
        proxi2 = FirebaseDatabase.getInstance().getReference().child("parking").child("CarSlot2");
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_parking, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i(GenericcTag,"User identified by android id :"+androidId);

        db = new DatabaseHelper(this);
        parkService = new ParkService(db);

        onFailureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(BeaconTag, "Beacon onFailure listener with error: "+e.getMessage());
            }
        };

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i("nameSpace : ", message.getNamespace());
                Log.i(BeaconTag, "Found message: " + new String(message.getContent()));
                Log.i(BeaconTag, "Namespace : " + message.getNamespace());
                Log.i(BeaconTag, "Content : " + new String(message.getContent()));
                Log.i(BeaconTag, "Type : " + message.getType());
                Log.i(BeaconTag, "Timestamp : " + message.hashCode());
            }

            @Override
            public void onLost(Message message) {
                Log.i(BeaconTag, "Lost sight of Beacon: " +new String(message.getContent()));
            }

            @Override
            public void onBleSignalChanged(Message message, BleSignal bleSignal) {
                super.onBleSignalChanged(message, bleSignal);
                db.recordReading(new String(message.getContent()), bleSignal.getRssi());
                Log.i(BeaconTag, "Signal fluctuation for Beacon: "+new String(message.getContent())+ " ,new RSSI: "+bleSignal.getRssi());
            }
        };

        ValueEventListener postListenerProxi1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                parkService.setSlot1Proxi(dataSnapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(GenericcTag, "Value change listener failed for slot 1, ", databaseError.toException());
            }
        };
        proxi1.addValueEventListener(postListenerProxi1);


        ValueEventListener postListenerProxi2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                parkService.setSlot2Proxi(dataSnapshot.getValue(Integer.class));
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(GenericcTag, "Value change listener failed for slot 2, ", databaseError.toException());
                // ...
            }
        };
        proxi2.addValueEventListener(postListenerProxi2);


        if (!havePermissions()) {
            Log.i(GenericcTag, "Requesting permissions needed for this app.");
            requestPermissions();
        }
    }

        @Override
        public void onConnected (@Nullable Bundle bundle){
            Log.i(GenericcTag, "GoogleApiClient connected");
            subscribe();
        }

        @Override
        protected void onResume () {
            super.onResume();
            if (havePermissions()) {
                buildGoogleApiClient();
            }
            Log.i(GenericcTag,"GoogleApiClient services resumed");
        }

        @Override
        protected void onPause () {
            super.onPause();
            Log.i(GenericcTag,"GoogleApiClient services paused");
        }

        @Override
        public void onConnectionFailed (@NonNull ConnectionResult connectionResult){
            Log.e(GenericcTag,"Connection failed : GoogleApiClient with error message : "+connectionResult.getErrorMessage());
        }

        @Override
        public void onConnectionSuspended ( int i){
            Log.w(GenericcTag, "Connection suspended : GoogleApiClient with Error code: " + i);
        }
        private void subscribe () {
            SubscribeOptions options = new SubscribeOptions.Builder()
                    .setStrategy(Strategy.DEFAULT)
                    .build();

            Nearby.getMessagesClient(this).subscribe(mMessageListener).addOnFailureListener(onFailureListener);
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
    }