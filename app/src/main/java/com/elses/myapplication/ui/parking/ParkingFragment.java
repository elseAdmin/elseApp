package com.elses.myapplication.ui.parking;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elses.myapplication.NewSlotBooking;
import com.elses.myapplication.R;
import com.google.zxing.Result;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ParkingFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private static final int REQUEST_CAMERA = 1;

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(getActivity(), "looking for available slots", Toast.LENGTH_SHORT).show();
        Log.i("Barcode", rawResult.getText());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment slotFragment = new NewSlotBooking();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, slotFragment);
                transaction.commit();
            }
        }, 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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