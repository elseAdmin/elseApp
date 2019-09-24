package com.elses.myapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elses.myapplication.ui.home.HomeViewModel;

public class ResultFragment extends Fragment {

    //private String res = "Your Car is Parked at M121";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_result, container, false);
        System.out.println("Inside Result dashboard");
        String strText=getArguments().getString("message");
        View root = inflater.inflate(R.layout.fragment_result, container, false);
        final TextView textView = root.findViewById(R.id.text_result);
        textView.setText(strText);
        return root;
    }
}
