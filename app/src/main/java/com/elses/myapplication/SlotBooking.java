package com.elses.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elses.myapplication.ui.BeaconConnectivityActivity;
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

public class SlotBooking extends BeaconConnectivityActivity{
    private EditText slot1,slot2;
    private DatabaseReference proxi1,proxi2;
    private static final String GenericTag = "Slot Booking";
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("Inside Slot layout","Car SLots available");
        setContentView(R.layout.activity_slot_booking);
        db = new DatabaseHelper();
        final String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        slot1 = findViewById(R.id.slot1);
        slot2 = findViewById(R.id.slot2);

        proxi1 = FirebaseDatabase.getInstance().getReference().child("parking").child("CarSlot1");
        proxi2 = FirebaseDatabase.getInstance().getReference().child("parking").child("CarSlot2");

        ValueEventListener postListenerProxi1 = new ValueEventListener() {
            int temp = -1;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(temp==-1){
                    temp = dataSnapshot.getValue(Integer.class);
                }else {
                    if(dataSnapshot.getValue(Integer.class)==0 && db.isBeaconDetected("Beacon 1")){
                        GradientDrawable gradient1 = (GradientDrawable) slot1.getBackground().mutate();
                        gradient1.setColor(Color.GRAY);
                        Toast.makeText(SlotBooking.this, "Thanks for parking here!", Toast.LENGTH_SHORT).show();
                    }else if(dataSnapshot.getValue(Integer.class)==1 && db.isBeaconDetected("Beacon 1")){
                        GradientDrawable gradient1 = (GradientDrawable) slot1.getBackground().mutate();
                        gradient1.setColor(Color.RED);
                        Toast.makeText(SlotBooking.this, "You have parked at Slot 1", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(GenericTag, "Value change listener failed for slot 1, ", databaseError.toException());
            }
        };
        proxi1.addValueEventListener(postListenerProxi1);


        ValueEventListener postListenerProxi2 = new ValueEventListener() {
            int temp = -1;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(temp==-1){
                    temp = dataSnapshot.getValue(Integer.class);
                }else {
                    if(dataSnapshot.getValue(Integer.class)==0 && db.isBeaconDetected("Beacon 2")){
                        GradientDrawable gradient1 = (GradientDrawable) slot2.getBackground().mutate();
                        gradient1.setColor(Color.GRAY);
                        Toast.makeText(SlotBooking.this, "Thanks for parking here!", Toast.LENGTH_SHORT).show();
                    }else if(dataSnapshot.getValue(Integer.class)==1 && db.isBeaconDetected("Beacon 2")){
                        GradientDrawable gradient1 = (GradientDrawable) slot2.getBackground().mutate();
                        gradient1.setColor(Color.RED);
                        Toast.makeText(SlotBooking.this, "You have parked at Slot 2", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(GenericTag, "Value change listener failed for slot 2, ", databaseError.toException());
                // ...
            }
        };
        proxi2.addValueEventListener(postListenerProxi2);
    }




}
