package com.elses.myapplication.ui.notifications;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elses.myapplication.R;
import com.elses.myapplication.ui.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    String GenericTag = "NotificationsFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        refreshScreen();
        return root;
    }
    private void refreshScreen(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(GenericTag,"Refreshing notification fragment");
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.navigation_notifications);
                if(fragment instanceof NotificationsFragment){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.detach(fragment);
                    transaction.attach(fragment);
                    transaction.commit();
                }

            }
        },1000);
    }
}