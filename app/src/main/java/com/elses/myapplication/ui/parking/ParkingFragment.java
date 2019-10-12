package com.elses.myapplication.ui.parking;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elses.myapplication.DatabaseHelper;
import com.elses.myapplication.NewSlotBooking;
import com.elses.myapplication.R;
import com.elses.myapplication.ui.home.HomeFragment;
import com.elses.myapplication.ui.notifications.NotificationsFragment;
import com.google.zxing.Result;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ParkingFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private static final int REQUEST_CAMERA = 1;
    private DatabaseHelper db;

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(getActivity(), "looking for available slots", Toast.LENGTH_SHORT).show();
        Log.i("Barcode", rawResult.getText());

        db.setIsUserInPremise(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Fragment slotFragment = new NewSlotBooking();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, slotFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }, 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new DatabaseHelper();

        db.setCurrentFragment("Parking");
        if (!checkPermission()) {
            requestPermissions();
        }
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;

//        if(!db.isIsUserInPremise()) {
//        }else{
//            //faulty
//
//            View root = inflater.inflate(R.layout.fragment_new_slot_booking, container, false);
//
//           /*Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("NewSlotBooking");
//           if(fragment instanceof NewSlotBooking){
//               FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//               transaction.detach(fragment);
//               transaction.attach(fragment);
//               transaction.commit();
//           }*/
//            return root;
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Barcode", "On Start method called");

        Log.i("Barcode", "Current fragment :::: "+db.getCurrentFragment());
        if(db.isIsUserInPremise()) {
            // Fetch current Fragment and display
            List<Fragment> fragmentList = getActivity().getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragmentList){
                if(fragment.isVisible() && db.getCurrentFragment().equals("Parking")){
                    Log.i("Barcode","Parking -----------------------"+fragment.toString());
                    Fragment slotFragment = new NewSlotBooking();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(fragment.getId(), slotFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        }
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
        if(!db.isIsUserInPremise()) {
            if (!checkPermission()) {
                requestPermissions();
            }
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!db.isIsUserInPremise()) {
            mScannerView.stopCamera();
        }
    }
}