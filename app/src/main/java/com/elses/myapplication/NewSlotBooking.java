package com.elses.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elses.myapplication.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class NewSlotBooking extends Fragment {

    private EditText slot1, slot2;
    private TextView result;
    private DatabaseReference proxi1,proxi2;
    private static final String GenericTag = "SlotBooking";
    DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(GenericTag,"Car Slots available");
        final View root = inflater.inflate(R.layout.fragment_new_slot_booking, container, false);
        db = new DatabaseHelper();
        slot1 = root.findViewById(R.id.slot1);
        slot2 = root.findViewById(R.id.slot2);
        result = root.findViewById(R.id.text_result);

        proxi1 = db.getDbRootRef().child("CarSlot1");
        proxi2 = db.getDbRootRef().child("CarSlot2");

        ValueEventListener postListenerProxi1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).equals("0")){
                    GradientDrawable gradient1 = (GradientDrawable) slot1.getBackground().mutate();
                    gradient1.setColor(Color.LTGRAY);
                    if(db.isBeaconDetected("Beacon 1") && db.getCurrentSlot().equals("CarSlot1")){
                        db.setCurrentSlot("");
                        db.getDbRootRef().child(db.getUserId()).child("parkedAt").setValue("null");
                        Toast.makeText(getActivity(), "Thanks for parking here!", Toast.LENGTH_SHORT).show();
                        result.setText("");
                    }else{
                        Log.i(GenericTag,"Slot 1 status updated to vacant");
                    }
                }else  if(dataSnapshot.getValue(String.class).equals("1")){
                    if(db.isBeaconDetected("Beacon 1") && db.getCurrentSlot().equals("")){
                            db.setCurrentSlot("CarSlot1");
                        GradientDrawable gradient1 = (GradientDrawable) slot1.getBackground().mutate();
                        gradient1.setColor(Color.GREEN);
                            db.getDbRootRef().child(db.getUserId()).child("parkedAt").setValue("CarSlot1");
                            Toast.makeText(getActivity(), "You have parked at Slot 1", Toast.LENGTH_SHORT).show();
                            result.setText("You have parked at Slot 1");
                    }else{
                        GradientDrawable gradient1 = (GradientDrawable) slot1.getBackground().mutate();
                        gradient1.setColor(Color.RED);
                        Log.i(GenericTag,"Slot 1 status updated to occupied");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(GenericTag, "Value change listener failed for slot 1, ", databaseError.toException());
            }
        };
        proxi1.addValueEventListener(postListenerProxi1);


        ValueEventListener postListenerProxi2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class).equals("0")){
                        GradientDrawable gradient1 = (GradientDrawable) slot2.getBackground().mutate();
                        gradient1.setColor(Color.LTGRAY);
                        if(db.isBeaconDetected("Beacon 2") && db.getCurrentSlot().equals("CarSlot2")){
                            db.setCurrentSlot("");
                            db.getDbRootRef().child(db.getUserId()).child("parkedAt").setValue("null");
                            Toast.makeText(getActivity(), "Thanks for parking here!", Toast.LENGTH_SHORT).show();
                            result.setText("");
                        }else{
                            Log.i(GenericTag,"Slot 2 status updated to vacant");
                        }
                    }else  if(dataSnapshot.getValue(String.class).equals("1")){
                        if(db.isBeaconDetected("Beacon 2") && db.getCurrentSlot().equals("")){
                            db.setCurrentSlot("CarSlot2");
                            GradientDrawable gradient1 = (GradientDrawable) slot2.getBackground().mutate();
                            gradient1.setColor(Color.GREEN);
                            db.getDbRootRef().child(db.getUserId()).child("parkedAt").setValue("CarSlot2");
                            Toast.makeText(getActivity(), "You have parked at Slot 2", Toast.LENGTH_SHORT).show();
                            result.setText("You have parked at Slot 2");
                        }else{
                            GradientDrawable gradient1 = (GradientDrawable) slot1.getBackground().mutate();
                            gradient1.setColor(Color.RED);
                            Log.i(GenericTag,"Slot 2 status updated to occupied");
                        }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(GenericTag, "Value change listener failed for slot 2, ", databaseError.toException());
            }
        };
        proxi2.addValueEventListener(postListenerProxi2);
        refreshScreen();
        return root;
    }

    private void refreshScreen(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(GenericTag,"Refreshing slot booking fragment");
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("NewSlotBooking");
                if(fragment instanceof NewSlotBooking){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.detach(fragment);
                    transaction.attach(fragment);
                    transaction.commit();
                }

            }
        },1000);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
