package com.example.edrkr;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;

public class sub_page2 extends Fragment {

    private LineChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.sub_page2,container,false);

        chart=(LineChart) view.findViewById(R.id.LineChart);
        initChartSetting();
        return view;

    }
    private void initChartSetting(){




        final ArrayList<String> labels = new ArrayList<String>();
        labels.add("오전 6시");
        labels.add("오전 8시");
        labels.add("오전 10시");
        labels.add("오후 12시");
        labels.add("오후 2시");
        labels.add("오후 4시");
        labels.add("오후 6시");


        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0, 4f));
        entries.add(new Entry(1, 8f));
        entries.add(new Entry(2, 6f));
        entries.add(new Entry(3, 2f));
        entries.add(new Entry(4, 10f));
        entries.add(new Entry(5, 5f));
        entries.add(new Entry(6, 8f));


        //x축 설정
        //view.setLineData(lineData);
        XAxis xAxis = chart.getXAxis(); // x축에 대한 정보를 View로부터 받아온다.
        //x축 레이블 세팅
        xAxis.setValueFormatter(new ValueFormatter() {
                                    @Override
                                    public String getAxisLabel(float value, AxisBase axis) {
                                        return labels.get((int) value);
                                    }
                                });

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 표시에 대한 위치 설정으로 아래쪽에 위치시킨다.
        xAxis.setTextColor(Color.BLACK); // x축 텍스트 컬러 설정

        YAxis yAxis = chart.getAxisLeft();



        //데이터셋 설정
        LineDataSet lineDataSet = new LineDataSet(entries, "습도");
        //lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        lineDataSet.setColors(ColorTemplate.rgb("#7DEEBC"));
        lineDataSet.setLineWidth(2); // 선 굵기
        //lineDataSet.setCircleRadius(6); // 곡률




        LineData lineData = new LineData(lineDataSet);


        chart.setData(lineData);
        chart.invalidate();

        //chart.animateY(5000);
    }

}
