package com.elses.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NewSlotBooking extends Fragment {

    private EditText slot1,slot2;
    private TextView result;
    private DatabaseReference proxi1,proxi2;
    private static final String GenericTag = "Slot Booking";
    DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("Inside New Slot layout","Car SLots available");
        final View root = inflater.inflate(R.layout.fragment_new_slot_booking, container, false);
        db = new DatabaseHelper();
        final String androidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        slot1 = root.findViewById(R.id.slot1);
        slot2 = root.findViewById(R.id.slot2);
        result = root.findViewById(R.id.text_result);

        proxi1 = FirebaseDatabase.getInstance().getReference().child("CarSlot1");
        proxi2 = FirebaseDatabase.getInstance().getReference().child("CarSlot2");

        ValueEventListener postListenerProxi1 = new ValueEventListener() {
            Bundle bundle=new Bundle();
            Fragment resultFragment = new ResultFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Integer.class)==0){
                    GradientDrawable gradient1 = (GradientDrawable) slot2.getBackground().mutate();
                    db.setCurrentSlot(null);
                    gradient1.setColor(Color.GRAY);
                    if(db.isBeaconDetected("Beacon 1")){
                        Toast.makeText(getActivity(), "Thanks for parking here!", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.i(GenericTag,"updated location of Slot 1 to vacant");
                    }
                }else if(dataSnapshot.getValue(Integer.class)==1){
                    GradientDrawable gradient1 = (GradientDrawable) slot2.getBackground().mutate();
                    gradient1.setColor(Color.RED);
                    db.setCurrentSlot("Slot 1");
                    if(db.isBeaconDetected("Beacon 1")){
                        Toast.makeText(getActivity(), "You have parked at Slot 1", Toast.LENGTH_SHORT).show();
                        result.setText("You have parked at Slot 1");
                    }else{
                        Log.i(GenericTag,"updated location of Slot 1 to occupied");
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
            Bundle bundle=new Bundle();
            Fragment resultFragment = new ResultFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(Integer.class)==0){
                        GradientDrawable gradient1 = (GradientDrawable) slot2.getBackground().mutate();
                        db.setCurrentSlot(null);
                        gradient1.setColor(Color.GRAY);
                        if(db.isBeaconDetected("Beacon 2")){
                            Toast.makeText(getActivity(), "Thanks for parking here!", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.i(GenericTag,"updated location of Slot 2 to vacant");
                        }
                    }else if(dataSnapshot.getValue(Integer.class)==1){
                        GradientDrawable gradient1 = (GradientDrawable) slot2.getBackground().mutate();
                        gradient1.setColor(Color.RED);
                        db.setCurrentSlot("Slot 2");
                        if(db.isBeaconDetected("Beacon 2")){
                            Toast.makeText(getActivity(), "You have parked at Slot 2", Toast.LENGTH_SHORT).show();
                            result.setText("You have parked at Slot 2");
                        }else{
                            Log.i(GenericTag,"updated location of Slot 1 to occupied");
                        }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(GenericTag, "Value change listener failed for slot 2, ", databaseError.toException());
            }
        };
        proxi2.addValueEventListener(postListenerProxi2);
        return root;
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
