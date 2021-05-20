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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.R;
import com.example.edrkr.dailyMemo.dailyMemo_recyclerview;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;


public class sub_page3 extends Fragment implements View.OnClickListener {

    dailyMemo_recyclerview calendar;
    MaterialButtonToggleGroup toggleGroup;

    //메모 내용 리사잌클러뷰
    RecyclerView contents_recyclerView;
    ArrayList<Integer> id;
    ArrayList<String> day;
    ArrayList<String> contents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.mainpage_sub_page3,container,false);

        toggleGroup = view.findViewById(R.id.toggleGroup);
        MaterialButton year = view.findViewById(R.id.year);
        MaterialButton month = view.findViewById(R.id.month);
        MaterialButton week = view.findViewById(R.id.week);

        year.setOnClickListener(this);
        month.setOnClickListener(this);
        week.setOnClickListener(this);

        calendar = view.findViewById(R.id.daily_scrol_recyclerview);


        // 선택한 기간 날짜의 메모 내용 관련
        getDailyContents();
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        contents_recyclerView = view.findViewById(R.id.daily_contents);
        contents_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        DailymemoAdapter adapter = new DailymemoAdapter(id,day, contents);
        contents_recyclerView.setAdapter(adapter);



        return view;

    }

    public void getDailyContents(){
        contents = new ArrayList<>();
        day = new ArrayList<>();
        id = new ArrayList<>();
        for (int i=0; i<100; i++) {
            contents.add(String.format("일기 내용 TEXT %d", i));
            day.add(String.format("오늘 날짜 %d", i));
            id.add(i);
        }
    }


    @Override
    public void onClick(View v) {
        //전부 선택 해제
        if(toggleGroup.getCheckedButtonId()==View.NO_ID){
            calendar.changeMode(0);
            return;
        }

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
