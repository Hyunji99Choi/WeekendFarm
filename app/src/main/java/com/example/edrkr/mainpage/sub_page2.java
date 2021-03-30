package com.example.edrkr.mainpage;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.edrkr.R;
import com.example.edrkr.UserIdent;
import com.example.edrkr.h_network.AutoRetryCallback;
import com.example.edrkr.h_network.ResponseGraphJson;
import com.example.edrkr.h_network.ResponseWeatherJson;
import com.example.edrkr.h_network.RetrofitClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class sub_page2 extends Fragment {

    private LineChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.mainpage_sub_page2,container,false);

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

        //그래프 값 통신
        getGreahData();
    }

    private void getGreahData(){
        Call<List<ResponseGraphJson>> graph = RetrofitClient.getApiService()
                .getGraph(String.valueOf(UserIdent.GetInstance().getNowMontriongFarm())); //api 콜
        graph.enqueue(new AutoRetryCallback<List<ResponseGraphJson>>() {
            @Override
            public void onFinalFailure(Call<List<ResponseGraphJson>> call, Throwable t) {
                Log.e("그래프 정보 연결실패", t.getMessage());
            }

            @Override
            public void onResponse(Call<List<ResponseGraphJson>> call, Response<List<ResponseGraphJson>> response) {
                if(!response.isSuccessful()){
                    Log.e("그래프 연결이 비정상적", "error code : " + response.code());
                    return;
                }

                Log.d("그래프 통신 성공적", response.body().toString());
                List<ResponseGraphJson> graphJsons = response.body(); //통신 결과 받기

                //그래프 정보로 세팅
                Log.d("날짜", graphJsons.get(0).getDate()+" "+graphJsons.get(1).getDate()
                        +" "+graphJsons.get(2).getDate()+" "+graphJsons.get(3).getDate()
                        +" "+graphJsons.get(4).getDate()+" "+graphJsons.get(5).getDate()
                        +" "+graphJsons.get(6).getDate());
                Log.d("평균", graphJsons.get(0).getSoilavg()+" "+graphJsons.get(1).getSoilavg()
                        +" "+graphJsons.get(2).getSoilavg()+" "+graphJsons.get(3).getSoilavg()
                        +" "+graphJsons.get(4).getSoilavg()+" "+graphJsons.get(5).getSoilavg()
                        +" "+graphJsons.get(6).getSoilavg());


            }
        });
    }

}
