package com.example.edrkr.dailyMemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.edrkr.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class dailyMemo_recyclerview extends LinearLayout {
    private String log = "dailyMemo_recyclerview";
    LinearLayout bg;
    RecyclerView recyclerView;
    TextView textView;
    View view;
    Context context;

    private List<MyCalendar> calendarList = new ArrayList<>(); //데이터 리스트
    private CalendarAdapter mAdapter; //recyclerview 어뎁터
    private myCalendarData nextData; //이전 달 데이터
    private myCalendarData lastData; //다음 달 데이터
    private int snapPosition = RecyclerView.NO_POSITION;
    private SnapHelper snapHelper; //중앙에 값 오도록 도와주는 helper
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean init = true; //동시에 진입하지 않도록 하는 key
    private int two_count = 2; //빠르게 넘어가는거 방지 - 2번 당겨야 새로 데이터 가져옴

    public dailyMemo_recyclerview(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public dailyMemo_recyclerview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initView();
    }

    public dailyMemo_recyclerview(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        this.context = context;
        initView();
    }

    private void initView() {
        String intService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(intService);
        View v = li.inflate(R.layout.dailymemo_custom_recyclerview, this, false);
        addView(v);
        bg = (LinearLayout) findViewById(R.id.bg);
        recyclerView = (RecyclerView) findViewById(R.id.holi_recyclerview);
        textView = (TextView) findViewById(R.id.daily_scrolldate);
        view = (View) findViewById(R.id.daily_line);

        mAdapter = new CalendarAdapter(calendarList);

        //view가 슬라이드할 때 중앙에 오도록 함.
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        lastData = new myCalendarData(0);
        nextData = new myCalendarData(0);

        setRecyclerView();
    }

    private void setRecyclerView(){
        Log.v(log,"setRecyclerView");

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mLayoutManager.getChildCount();
                for(int i=0;i<totalItemCount;i++){
                    View childView = recyclerView.getChildAt(i);
                    TextView childTextView = (TextView)(childView.findViewById(R.id.day_1));
                    String childTextViewText = (String)(childTextView.getText());

                    if(childTextViewText.equals("일"))
                        childTextView.setTextColor(Color.RED);
                    else if(childTextViewText.equals("토"))
                        childTextView.setTextColor(Color.BLUE);
                    else
                        childTextView.setTextColor(Color.BLACK);
                    maybeNofitySnapPositionChange(recyclerView);
                }
            }

            //스크롤시 끝에 닿은 지 확인하는 함수
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                if(two_count == 0 && init){
                    if(!recyclerView.canScrollHorizontally(-1)){ //리스트의 앞
                        Log.v(log+"onScrollstate","start");
                        prepareCalendarData(1);
                        mAdapter.notifyDataSetChanged();
                    } else if(!recyclerView.canScrollHorizontally(1)){ //리스트의 끝
                        Log.v(log+"onScrollstate","end");
                        prepareCalendarData(2);
                        mAdapter.notifyDataSetChanged();
                    }
                    two_count = 2;
                }else{
                    two_count--;
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        Log.v(log,"oncreate onscroll");

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) { //클릭시
                MyCalendar calendar = calendarList.get(position);
                TextView childTextView = (TextView) (view.findViewById(R.id.day_1));

                Animation startRotateAnimation = AnimationUtils.makeInChildBottomAnimation(context);
                childTextView.startAnimation(startRotateAnimation);
                childTextView.setTextColor(Color.CYAN);
                Toast.makeText(context, calendar.getMonth()+"."+calendar.getDate()+"."+calendar.getDay() + " is selected!", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onLongClick(View view, int position) { //꾹 누를시
                MyCalendar calendar = calendarList.get(position);

                TextView childTextView = (TextView) (view.findViewById(R.id.day_1));
                childTextView.setTextColor(Color.GREEN);

                Toast.makeText(context, calendar.getDate()+"/" + calendar.getDay()+"/" +calendar.getMonth()+"   selected!", Toast.LENGTH_SHORT).show();
            }
        }));
        Log.v(log,"oncreate addtouch");
        prepareCalendarData(0);
    }

    private void maybeNofitySnapPositionChange(RecyclerView recyclerview){
        //중앙의 position 알기
        View snapView = snapHelper.findSnapView(mLayoutManager);
        int snapPosition = mLayoutManager.getPosition(snapView);

        boolean snapPositionChanged = this.snapPosition != snapPosition;
        if(snapPositionChanged){
            this.snapPosition = snapPosition;
            MyCalendar calendar = calendarList.get(snapPosition);
            textView.setText(calendar.getYear()+"년 "+calendar.getMonth()+calendar.getDate()+"일");
        }

    }

    private void prepareCalendarData(int type){ //type = 0 초기화 / type = 1 이전달 / type = 2 이후 달
        Log.v(log,"preparecalendardata  type : "+type);
        init = false;
        myCalendarData m_calendar = null;//int 값이 아닌 myCalendarData 값을 미리 저장해두면 되지 않을까?
        if(type == 1){ //이전 달 호출
            lastData.getNextWeekDay(-31);
            m_calendar = lastData;
            Log.v(log, "month : "+(lastData.getMonth()+1)+"date : "+m_calendar.getDay());
        }else if(type == 2){ //이후 달 호출
            m_calendar = nextData;
            Log.v(log, "month : "+(lastData.getMonth()+1)+"date : "+m_calendar.getDay());
        }else if(type == 0){ //초기화
            m_calendar = new myCalendarData(0);
        }
        for(int i=0;i<31;i++){
            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()),String.valueOf(m_calendar.getMonth()),String.valueOf(m_calendar.getYear()),i);
            m_calendar.getNextWeekDay(1);
            if(type == 1){
                calendarList.add(i,calendar);
            }else{
                calendarList.add(calendar);
            }
        }
        if(type == 1){
            recyclerView.scrollToPosition(snapPosition+31);
            m_calendar.getNextWeekDay(-31);
            lastData = m_calendar;
            Log.v(log,"save_lastdata : "+(lastData.getMonth()+1)+","+lastData.getDay());
        }else{
            nextData = m_calendar;
        }
        init = true;
        Log.v(log,"preparecalendardata 끝");
    }
}

//https://github.com/atanudasgupta/myHorizontalCalendar/blob/master/app/src/main/java/com/emerald/mycalendar/MainActivity.java


//중앙 값 가져오기 : https://pythonq.com/so/android/91216
//https://www.javaer101.com/en/article/16366527.html
//스냅 : https://rubensousa.github.io/2016/08/recyclerviewsnap
//스냅 중앙값 코틀린 : https://medium.com/over-engineering/detecting-snap-changes-with-androids-recyclerview-snaphelper-9e9f5e95c424
// recyclerview 끝 감지 : https://medium.com/@ydh0256/android-recyclerview-%EC%9D%98-%EC%B5%9C%EC%83%81%EB%8B%A8%EA%B3%BC-%EC%B5%9C%ED%95%98%EB%8B%A8-%EC%8A%A4%ED%81%AC%EB%A1%A4-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EA%B0%90%EC%A7%80%ED%95%98%EA%B8%B0-f0e5fda34301

