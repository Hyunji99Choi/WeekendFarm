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
import com.example.edrkr.h_network.RetrofitClient;
import com.github.mikephil.charting.charts.LineChart;


import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Response;

public class sub_page2 extends Fragment {

    private LineChartView chart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.mainpage_sub_page2,container,false);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();

        }
        //chart = view.findViewById(R.id.chart);

        //initChartSetting();


        //그래프 값 통신
        getGreahData();

        return view;

    }
    private void initChartSetting(){

        List<PointValue> values = new ArrayList<PointValue>();
        values.add(new PointValue(0, 2));
        values.add(new PointValue(1, 4));
        values.add(new PointValue(2, 3));
        values.add(new PointValue(3, 4));

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        chart.setLineChartData(data);

        /*
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
         */





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


    //복붙
    public static class PlaceholderFragment extends Fragment {
        public final static String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec",};

        public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};

        private LineChartView chartTop;
        private ColumnChartView chartBottom;

        private LineChartData lineData;
        private ColumnChartData columnData;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.chart_previewlinechart, container, false);

            // *** TOP LINE CHART ***
            chartTop = (LineChartView) rootView.findViewById(R.id.chart_top);

            // Generate and set data for line chart
            generateInitialLineData();

            // *** BOTTOM COLUMN CHART ***

            chartBottom = (ColumnChartView) rootView.findViewById(R.id.chart_bottom);

            generateColumnData();

            return rootView;
        }

    private void generateColumnData() {

        int numSubcolumns = 1;
        int numColumns = months.length;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
            }

            axisValues.add(new AxisValue(i).setLabel(months[i]));

            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }

        columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

        chartBottom.setColumnChartData(columnData);

        // Set value touch listener that will trigger changes for chartTop.
        chartBottom.setOnValueTouchListener(new ValueTouchListener());

        // Set selection mode to keep selected month column highlighted.
        chartBottom.setValueSelectionEnabled(true);

        chartBottom.setZoomType(ZoomType.HORIZONTAL);

        // chartBottom.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // SelectedValue sv = chartBottom.getSelectedValue();
        // if (!sv.isSet()) {
        // generateInitialLineData();
        // }
        //
        // }
        // });

    }

    private void generateInitialLineData() {
        int numValues = 7;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(days[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 110, 6, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

        chartTop.setZoomType(ZoomType.HORIZONTAL);
    }

    private void generateLineData(int color, float range) {
        // Cancel last animation if not finished.
        chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            value.setTarget(value.getX(), (float) Math.random() * range);
        }

        // Start new data animation with 300ms duration;
        chartTop.startDataAnimation(300);
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            generateLineData(value.getColor(), 100);
        }

        @Override
        public void onValueDeselected() {

            generateLineData(ChartUtils.COLOR_GREEN, 0);

        }
    }
}

}
