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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NewSlotBooking extends Fragment {

    private EditText slot1,slot2;
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

        proxi1 = FirebaseDatabase.getInstance().getReference().child("parking").child("CarSlot1");
        proxi2 = FirebaseDatabase.getInstance().getReference().child("parking").child("CarSlot2");

        ValueEventListener postListenerProxi1 = new ValueEventListener() {
            int temp = -1;
            Bundle bundle=new Bundle();
            Fragment resultFragment = new ResultFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(temp==-1){
                    temp = dataSnapshot.getValue(Integer.class);
                }else {
                    if(dataSnapshot.getValue(Integer.class)==0 && db.isBeaconDetected("Beacon 1")){
                        GradientDrawable gradient1 = (GradientDrawable) slot1.getBackground().mutate();
                        gradient1.setColor(Color.GRAY);
                        Toast.makeText(getActivity(), "Thanks for parking here!", Toast.LENGTH_SHORT).show();
                        bundle.putString("message", "Thanks for parking here!");
                        resultFragment.setArguments(bundle);
                        transaction.replace(R.id.nav_host_fragment, resultFragment);
                        transaction.commit();
                    }else if(dataSnapshot.getValue(Integer.class)==1 && db.isBeaconDetected("Beacon 1")){
                        GradientDrawable gradient1 = (GradientDrawable) slot1.getBackground().mutate();
                        gradient1.setColor(Color.RED);
                        Toast.makeText(getActivity(), "You have parked at Slot 1", Toast.LENGTH_SHORT).show();
                        bundle.putString("message", "You have parked at Slot 1");
                        resultFragment.setArguments(bundle);
                        transaction.replace(R.id.nav_host_fragment, resultFragment);
                        transaction.commit();
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
            Bundle bundle=new Bundle();
            Fragment resultFragment = new ResultFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(temp==-1){
                    temp = dataSnapshot.getValue(Integer.class);
                }else {
                    if(dataSnapshot.getValue(Integer.class)==0 && db.isBeaconDetected("Beacon 2")){
                        GradientDrawable gradient1 = (GradientDrawable) slot2.getBackground().mutate();
                        gradient1.setColor(Color.GRAY);
                        Toast.makeText(getActivity(), "Thanks for parking here!", Toast.LENGTH_SHORT).show();
                        bundle.putString("message", "Thanks for parking here!");
                        resultFragment.setArguments(bundle);
                        transaction.replace(R.id.nav_host_fragment, resultFragment);
                        transaction.commit();
                    }else if(dataSnapshot.getValue(Integer.class)==1 && db.isBeaconDetected("Beacon 2")){
                        GradientDrawable gradient1 = (GradientDrawable) slot2.getBackground().mutate();
                        gradient1.setColor(Color.RED);
                        Toast.makeText(getActivity(), "You have parked at Slot 2", Toast.LENGTH_SHORT).show();
                        bundle.putString("message", "You have parked at Slot 2");
                        resultFragment.setArguments(bundle);
                        transaction.replace(R.id.nav_host_fragment, resultFragment);
                        transaction.commit();
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
