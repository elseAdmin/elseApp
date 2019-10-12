package com.elses.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class NewSlotBooking extends Fragment {

    private EditText CarSlot1, CarSlot2, CarSlot3, CarSlot4, CarSlot5,
            CarSlot6, CarSlot7, CarSlot8, CarSlot9, CarSlot10;
    private TextView result;
    private DatabaseReference proxi1,proxi2, databaseReference;
    private static final String GenericTag = "SlotBooking";
    DatabaseHelper db;
    private HashMap<String, String> slotData;

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
        CarSlot1 = root.findViewById(R.id.slot1);
        CarSlot2 = root.findViewById(R.id.slot2);
        CarSlot3 = root.findViewById(R.id.slot3);
        CarSlot4 = root.findViewById(R.id.slot4);
        CarSlot5 = root.findViewById(R.id.slot5);
        CarSlot6 = root.findViewById(R.id.slot6);
        CarSlot7 = root.findViewById(R.id.slot7);
        CarSlot8 = root.findViewById(R.id.slot8);
        CarSlot9 = root.findViewById(R.id.slot9);
        CarSlot10 = root.findViewById(R.id.slot10);
        slotData = new HashMap<>();
        addHashMapData(slotData);
        result = root.findViewById(R.id.text_result);

        proxi1 = db.getDbRootRef().child("CarSlot1");
        proxi2 = db.getDbRootRef().child("CarSlot2");

        ValueEventListener postListenerProxi1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).equals("0")){
                    GradientDrawable gradient1 = (GradientDrawable) CarSlot1.getBackground().mutate();
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
                        GradientDrawable gradient1 = (GradientDrawable) CarSlot1.getBackground().mutate();
                        gradient1.setColor(Color.GREEN);
                            db.getDbRootRef().child(db.getUserId()).child("parkedAt").setValue("CarSlot1");
                            Toast.makeText(getActivity(), "You have parked at Slot 1", Toast.LENGTH_SHORT).show();
                            result.setText("You have parked at Slot 1");
                    }else{
                        GradientDrawable gradient1 = (GradientDrawable) CarSlot1.getBackground().mutate();
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
                        GradientDrawable gradient1 = (GradientDrawable) CarSlot2.getBackground().mutate();
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
                            GradientDrawable gradient1 = (GradientDrawable) CarSlot2.getBackground().mutate();
                            gradient1.setColor(Color.GREEN);
                            db.getDbRootRef().child(db.getUserId()).child("parkedAt").setValue("CarSlot2");
                            Toast.makeText(getActivity(), "You have parked at Slot 2", Toast.LENGTH_SHORT).show();
                            result.setText("You have parked at Slot 2");
                        }else{
                            GradientDrawable gradient1 = (GradientDrawable) CarSlot2.getBackground().mutate();
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

    @Override
    public void onStart() {
        super.onStart();
        Log.w(GenericTag, "Calling update value method ");
        updateValue();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.w(GenericTag, "onViewCreated ");
    }

    private void addHashMapData(HashMap<String,String> slotData){
        slotData.put("CarSlot1","0");
        slotData.put("CarSlot2","0");
        slotData.put("CarSlot3","0");
        slotData.put("CarSlot4","0");
        slotData.put("CarSlot5","0");
        slotData.put("CarSlot6","0");
        slotData.put("CarSlot7","0");
        slotData.put("CarSlot8","0");
        slotData.put("CarSlot9","0");
        slotData.put("CarSlot10","0");
    }

    private void updateValue(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot slotSnapshot : dataSnapshot.getChildren()){
                    String snapshotKey = slotSnapshot.getKey();
                    if(slotData.containsKey(snapshotKey)){
                        String slot = slotSnapshot.getValue(String.class);
                        slotData.put(snapshotKey, slot);
                    }
                    Log.i(GenericTag,"Car Slots data " + Arrays.asList(slotData));

                    for(Map.Entry<String, String> slot : slotData.entrySet()){
//                        Log.i(GenericTag,"Car Slots key " + slot.getKey() + " value " + slot.getValue());
                        switch(slot.getKey()){
                            case "CarSlot1" :
                                if (slot.getValue().equalsIgnoreCase("0")){
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot1.getBackground().mutate();
                                    gradient1.setColor(Color.LTGRAY);
                                }
                                else{
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot1.getBackground().mutate();
                                    gradient1.setColor(Color.RED);
                                }
                                break;

                            case "CarSlot2" :
                                if (slot.getValue().equalsIgnoreCase("0")){
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot2.getBackground().mutate();
                                    gradient1.setColor(Color.LTGRAY);
                                }
                                else{
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot2.getBackground().mutate();
                                    gradient1.setColor(Color.RED);
                                }
                                break;

                            case "CarSlot3" :
                                if (slot.getValue().equalsIgnoreCase("0")){
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot3.getBackground().mutate();
                                    gradient1.setColor(Color.LTGRAY);
                                }
                                else{
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot3.getBackground().mutate();
                                    gradient1.setColor(Color.RED);
                                }
                                break;

                            case "CarSlot4" :
                                if (slot.getValue().equalsIgnoreCase("0")){
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot4.getBackground().mutate();
                                    gradient1.setColor(Color.LTGRAY);
                                }
                                else{
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot4.getBackground().mutate();
                                    gradient1.setColor(Color.RED);
                                }
                                break;

                            case "CarSlot5" :
                                if (slot.getValue().equalsIgnoreCase("0")){
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot5.getBackground().mutate();
                                    gradient1.setColor(Color.LTGRAY);
                                }
                                else{
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot5.getBackground().mutate();
                                    gradient1.setColor(Color.RED);
                                }
                                break;

                            case "CarSlot6" :
                                if (slot.getValue().equalsIgnoreCase("0")){
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot6.getBackground().mutate();
                                    gradient1.setColor(Color.LTGRAY);
                                }
                                else{
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot6.getBackground().mutate();
                                    gradient1.setColor(Color.RED);
                                }
                                break;

                            case "CarSlot7" :
                                if (slot.getValue().equalsIgnoreCase("0")){
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot7.getBackground().mutate();
                                    gradient1.setColor(Color.LTGRAY);
                                }
                                else{
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot7.getBackground().mutate();
                                    gradient1.setColor(Color.RED);
                                }
                                break;

                            case "CarSlot8" :
                                if (slot.getValue().equalsIgnoreCase("0")){
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot8.getBackground().mutate();
                                    gradient1.setColor(Color.LTGRAY);
                                }
                                else{
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot8.getBackground().mutate();
                                    gradient1.setColor(Color.RED);
                                }
                                break;

                            case "CarSlot9" :
                                if (slot.getValue().equalsIgnoreCase("0")){
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot9.getBackground().mutate();
                                    gradient1.setColor(Color.LTGRAY);
                                }
                                else{
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot9.getBackground().mutate();
                                    gradient1.setColor(Color.RED);
                                }
                                break;

                            case "CarSlot10" :
                                if (slot.getValue().equalsIgnoreCase("0")){
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot10.getBackground().mutate();
                                    gradient1.setColor(Color.LTGRAY);
                                }
                                else{
                                    GradientDrawable gradient1 = (GradientDrawable) CarSlot10.getBackground().mutate();
                                    gradient1.setColor(Color.RED);
                                }
                                break;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(GenericTag, "Value change listener failed for slots, ", databaseError.toException());
            }
        });
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
