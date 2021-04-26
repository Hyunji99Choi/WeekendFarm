package com.example.edrkr.mainpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.edrkr.R;
import com.example.edrkr.dailyMemo.dailyMemo_recyclerview;
import com.google.android.material.button.MaterialButton;

public class sub_page3 extends Fragment implements View.OnClickListener {

    dailyMemo_recyclerview calendar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.mainpage_sub_page3,container,false);

        MaterialButton year = view.findViewById(R.id.year);
        MaterialButton month = view.findViewById(R.id.month);
        MaterialButton week = view.findViewById(R.id.week);

        year.setOnClickListener(this);
        month.setOnClickListener(this);
        week.setOnClickListener(this);

        calendar = view.findViewById(R.id.daily_scrol_recyclerview);

        return view;



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.year:
                calendar.changeMode(1);
                break;
            case R.id.month:
                calendar.changeMode(2);
                break;
            case R.id.week:
                calendar.changeMode(3);
                break;

        }
    }
}
