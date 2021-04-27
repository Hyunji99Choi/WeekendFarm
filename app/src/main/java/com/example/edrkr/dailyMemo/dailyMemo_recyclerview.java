package com.example.edrkr.dailyMemo;

import android.content.Context;
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
import java.util.List;

public class dailyMemo_recyclerview extends LinearLayout {
    private String log = "dailyMemo_recyclerview";
    private RecyclerView recyclerView;
    private TextView textView;
    private Context context;

    private List<MyCalendar> calendarList = new ArrayList<>(); //데이터 리스트
    private CalendarAdapter mAdapter; //recyclerview 어뎁터
    private myCalendarData nextData; //이전 달 데이터
    private myCalendarData lastData; //다음 달 데이터
    private int snapPosition = RecyclerView.NO_POSITION;
    private SnapHelper snapHelper; //중앙에 값 오도록 도와주는 helper
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean init = true; //동시에 진입하지 않도록 하는 key
    private int two_count = 2; //빠르게 넘어가는거 방지 - 2번 당겨야 새로 데이터 가져옴

    private int mode = 0; // 0 : 일간/ 1 : 년간 / 2 : 월간 / 3 : 주간


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
    private void setModeinitialize(){ //모드가 바뀔때 초기화하는 함수
        calendarList = null;
        lastData = null;
        nextData = null;
        mAdapter = null;

        calendarList = new ArrayList<>();
        lastData = new myCalendarData(0);
        nextData = new myCalendarData(0);
        mAdapter = new CalendarAdapter(calendarList, mode);
    }

    private void initView() {
        String intService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(intService);
        View v = li.inflate(R.layout.dailymemo_custom_recyclerview, this, false);
        addView(v);
        recyclerView = (RecyclerView) findViewById(R.id.holi_recyclerview);
        textView = (TextView) findViewById(R.id.daily_scrolldate);

        mAdapter = new CalendarAdapter(calendarList,mode);

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

    public void changeMode(int mode){ //모드 변경 함수
        this.mode = mode;
        setModeinitialize();
        setRecyclerView();
    }

    private void setRecyclerView(){ //recyclerview 이벤트 처리
        Log.v(log,"setRecyclerView");

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mLayoutManager.getChildCount();
                for(int i=0;i<totalItemCount;i++){
                    View childView = recyclerView.getChildAt(i);
                    if(mode == 0) {
                        TextView childTextView = (TextView) (childView.findViewById(R.id.day_1));
                        String childTextViewText = (String) (childTextView.getText());

                        if (childTextViewText.equals("일"))
                            childTextView.setTextColor(Color.RED);
                        else if (childTextViewText.equals("토"))
                            childTextView.setTextColor(Color.BLUE);
                        else
                            childTextView.setTextColor(Color.BLACK);
                    }
                    maybeNofitySnapPositionChange(recyclerView);
                }
            }

            //스크롤시 끝에 닿은 지 확인하는 함수
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                if(two_count == 0 && init){
                    if(!recyclerView.canScrollHorizontally(-1)){ //리스트의 앞
                        Log.v(log+"onScrollstate","start");
                        checkModetoprpare(1);
                        mAdapter.notifyDataSetChanged();
                    } else if(!recyclerView.canScrollHorizontally(1)){ //리스트의 끝
                        Log.v(log+"onScrollstate","end");
                        checkModetoprpare(2);
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
             /*   TextView childTextView = (TextView) (view.findViewById(R.id.day_1));

                Animation startRotateAnimation = AnimationUtils.makeInChildBottomAnimation(context);
                childTextView.startAnimation(startRotateAnimation);
                childTextView.setTextColor(Color.CYAN);*/

                Toast.makeText(context, calendar.getMonth()+"."+calendar.getDate()+"."+calendar.getDay() + " is selected!", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onLongClick(View view, int position) { //꾹 누를시

                MyCalendar calendar = calendarList.get(position);
   /*
                TextView childTextView = (TextView) (view.findViewById(R.id.day_1));
                childTextView.setTextColor(Color.GREEN);
    */
                Toast.makeText(context, calendar.getDate()+"/" + calendar.getDay()+"/" +calendar.getMonth()+"   selected!", Toast.LENGTH_SHORT).show();


            }
        }));
        Log.v(log,"oncreate addtouch");
        checkModetoprpare(0);
    }

    private void maybeNofitySnapPositionChange(RecyclerView recyclerview){
        //중앙의 position 알기
        View snapView = snapHelper.findSnapView(mLayoutManager);
        int snapPosition = mLayoutManager.getPosition(snapView);

        boolean snapPositionChanged = this.snapPosition != snapPosition;
        if(snapPositionChanged){
            this.snapPosition = snapPosition;
            MyCalendar calendar = calendarList.get(snapPosition);
            Log.v(log,"mode : "+mode);
            switch (mode){
                case 0: //일간
                    Log.v(log,"daily");
                    textView.setText(calendar.getYear()+"년 "+calendar.getMonth()+calendar.getDate()+"일");
                    break;
                case 1: //년간
                    Log.v(log,"year");
                    textView.setText(calendar.getYear()+"년");
                    break;
                case 2: //월간
                    Log.v(log,"month");
                    textView.setText(calendar.getYear()+"년 "+calendar.getMonth());
                    break;
                case 3: //주간
                    textView.setText(calendar.getYear()+"년 "+calendar.getMonth()+" "+calendar.getWeek());
                    break;
            }
        }

    }
    private void checkModetoprpare(int type){
        Log.v(log, "checkModetoprpare mode : "+mode);
        switch (mode){
            case 0: //일간
                prepareCalendarDate(type);
                break;
            case 1: //년간
                prepareCalendarYear(type);
                break;
            case 2: //월간
                prepareCalendarMonth(type);
                break;
            case 3: //주간
                prpareCalendarWeek(type);
                break;
        }
    }

    private void prepareCalendarDate(int type){//일간
        //type = 0 초기화 / type = 1 이전달 / type = 2 이후 달
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
            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()), String.valueOf(m_calendar.getWeekofmonth()) ,String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i);
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

    private void prepareCalendarYear(int type){ //년간
        //type = 0 초기화 / type = 1 이전달 / type = 2 이후 달
        Log.v(log,"prepareCalendarYear  type : "+type);
        init = false;
        myCalendarData m_calendar = null;

        if(type == 1){ //이전 년 호출
            lastData.getNextYear(-10);
            m_calendar = lastData;
            Log.v(log, "year : "+ lastData.getYear());
        }else if(type == 2){ //이후 달 호출
            m_calendar = nextData;
            Log.v(log, "year : "+ lastData.getYear());
        }else if(type == 0){ //초기화
            m_calendar = new myCalendarData(0);
        }

        for(int i=0;i<10;i++){
            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()), String.valueOf(m_calendar.getWeekofmonth()) ,String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i);
            m_calendar.getNextYear(1);
            if(type == 1){
                calendarList.add(i,calendar);
            }else{
                calendarList.add(calendar);
            }
        }

        if(type == 1){
            recyclerView.scrollToPosition(snapPosition+10);
            m_calendar.getNextYear(-10);
            lastData = m_calendar;
            Log.v(log, "save_lastyear : "+ lastData.getYear());
        }else{
            nextData = m_calendar;
        }
        init = true;
        Log.v(log,"prepareCalendarYear 끝");
    }

    private void prepareCalendarMonth(int type){ //월간
        //type = 0 초기화 / type = 1 이전달 / type = 2 이후 달
        Log.v(log,"prepareCalendarMonth  type : "+type);
        init = false;
        myCalendarData m_calendar = null;

        if(type == 1){ //이전 년 호출
            lastData.getNextMonth(-10);
            m_calendar = lastData;
            Log.v(log, "month : "+(lastData.getMonth()+1));
        }else if(type == 2){ //이후 달 호출
            m_calendar = nextData;
            Log.v(log, "month : "+(lastData.getMonth()+1));
        }else if(type == 0){ //초기화
            m_calendar = new myCalendarData(0);
        }

        for(int i=0;i<10;i++){
            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()), String.valueOf(m_calendar.getWeekofmonth()) ,String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i);
            m_calendar.getNextMonth(1);
            if(type == 1){
                calendarList.add(i,calendar);
            }else{
                calendarList.add(calendar);
            }
        }

        if(type == 1){
            recyclerView.scrollToPosition(snapPosition+10);
            m_calendar.getNextMonth(-10);
            lastData = m_calendar;
            Log.v(log, "save_lastmonth : "+(lastData.getMonth()+1));
        }else{
            nextData = m_calendar;
        }
        init = true;
        Log.v(log,"prepareCalendarMonth 끝");
    }

    private void prpareCalendarWeek(int type){ //주간
        //해당 월의 1일로 초기화 / 이전달 이전 달의 1일로 이동 / 다음달 다음 달의 1일로 이동
        //type = 0 초기화 / type = 1 이전달 / type = 2 이후 달
//        Log.v(log, "prpareCalendarWeek  type : " + type);
//        init = false;
//        myCalendarData m_calendar = null;
//
//        if (type == 1) { //이전 주 호출
//            lastData.getNextWeekDay(-70);
//            m_calendar = lastData;
//            Log.v(log, "month : " + (lastData.getMonth() + 1) + lastData.getWeekofmonth() + "째주");
//        } else if (type == 2) { //이후 주 호출
//            m_calendar = nextData;
//            Log.v(log, "month : " + (lastData.getMonth() + 1) + lastData.getWeekofmonth() + "째주");
//        } else if (type == 0) { //초기화
//            m_calendar = new myCalendarData(0);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()), String.valueOf(m_calendar.getWeekofmonth()) ,String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i);
//            m_calendar.getNextWeekDay(7);
//            if (type == 1) {
//                calendarList.add(i, calendar);
//            } else {
//                calendarList.add(calendar);
//            }
//        }
//
//        if (type == 1) {
//            recyclerView.scrollToPosition(snapPosition + 70);
//            m_calendar.getNextMonth(-70);
//            lastData = m_calendar;
//            Log.v(log, "save_lastmonth : " + (lastData.getMonth() + 1) + lastData.getWeekofmonth() + "째주");
//        } else {
//            nextData = m_calendar;
//        }
//        init = true;
//        Log.v(log, "prepareCalendarMonth 끝");
    }

}

//https://github.com/atanudasgupta/myHorizontalCalendar/blob/master/app/src/main/java/com/emerald/mycalendar/MainActivity.java


//중앙 값 가져오기 : https://pythonq.com/so/android/91216
//https://www.javaer101.com/en/article/16366527.html
//스냅 : https://rubensousa.github.io/2016/08/recyclerviewsnap
//스냅 중앙값 코틀린 : https://medium.com/over-engineering/detecting-snap-changes-with-androids-recyclerview-snaphelper-9e9f5e95c424
// recyclerview 끝 감지 : https://medium.com/@ydh0256/android-recyclerview-%EC%9D%98-%EC%B5%9C%EC%83%81%EB%8B%A8%EA%B3%BC-%EC%B5%9C%ED%95%98%EB%8B%A8-%EC%8A%A4%ED%81%AC%EB%A1%A4-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EA%B0%90%EC%A7%80%ED%95%98%EA%B8%B0-f0e5fda34301

