package com.elses.myapplication;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

public class SlotBooking extends AppCompatActivity {
    private EditText slot1,slot2;
    private DatabaseReference proxi1,proxi2;
    private static final String GenericTag = "SlotBooking";
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i(GenericTag,"Loading parking ");
        setContentView(R.layout.activity_slot_booking);
        db = new DatabaseHelper();
        slot1 = findViewById(R.id.slot1);
        slot2 = findViewById(R.id.slot2);

        proxi1 = FirebaseDatabase.getInstance().getReference().child("CarSlot1");
        proxi2 = FirebaseDatabase.getInstance().getReference().child("CarSlot2");

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
