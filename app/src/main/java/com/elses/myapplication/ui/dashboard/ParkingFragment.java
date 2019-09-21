package com.elses.myapplication.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.elses.myapplication.R;

import java.util.HashMap;
import java.util.Map;

public class ParkingFragment extends Fragment {

    private ParkingViewModel parkingViewModel;

    Map<String,String> proxiBeacon ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        parkingViewModel =
                ViewModelProviders.of(this).get(ParkingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_parking, container, false);
        final TextView textView = root.findViewById(R.id.text_parking);
        parkingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        populateSensorBeaconMapping();
        return root;
    }

    private void populateSensorBeaconMapping(){
        if(proxiBeacon==null){
        proxiBeacon = new HashMap<>();
        proxiBeacon.put("CarSlot1","Beacon 1");
        proxiBeacon.put("CarSlot2","Beacon 2");
        }
    }
}