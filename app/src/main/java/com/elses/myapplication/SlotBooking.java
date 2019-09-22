package com.elses.myapplication;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SlotBooking extends AppCompatActivity {

    private EditText editText;
    private Button parkedBtn, vacantBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_booking);

        editText = findViewById(R.id.edittext);
        parkedBtn = findViewById(R.id.parkedbtn);
        vacantBtn = findViewById(R.id.vacantbtn);

        parkedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable gradient = (GradientDrawable) editText.getBackground().mutate();
                gradient.setColor(Color.RED);
            }
        });

        vacantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable gradient = (GradientDrawable) editText.getBackground().mutate();
                gradient.setColor(Color.WHITE);
            }
        });
    }
}
