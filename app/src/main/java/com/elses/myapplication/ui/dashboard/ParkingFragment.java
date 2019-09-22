package com.elses.myapplication.ui.dashboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ParkingFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private ParkingViewModel parkingViewModel;

    /*Map<String,String> proxiBeacon ;

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
    }*/


    private ZXingScannerView mScannerView;
    private static final int REQUEST_CAMERA = 1;

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(getActivity(), "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ParkingFragment.this);
            }
        }, 2000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("Inside Parking dashboard");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!checkPermission()){
                requestPermissions();
            }
        }
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{CAMERA}, REQUEST_CAMERA);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(this.getActivity(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!checkPermission()){
            requestPermissions();
        }
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}