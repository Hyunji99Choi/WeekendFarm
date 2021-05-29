package com.example.edrkr.dailyMemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.edrkr.R;
import com.example.edrkr.mainpage.ControlDailyMomo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@RequiresApi(api = Build.VERSION_CODES.O)
public class dailyMemo_recyclerview extends LinearLayout {
    private String log = "areum/dailyMemo_recyclerview";
    private RecyclerView recyclerView;
    private TextView textView;
    private Context context;
    private CalendarAdapter mAdapter; //recyclerview 어뎁터

    private List<MyCalendar> calendarList = new ArrayList<>(); //데이터 리스트
    private myCalendarData nextData; //이전 달 데이터
    private myCalendarData lastData; //다음 달 데이터
    private View snapView;
    private int snapPosition = 0;
    private boolean init = true; //동시에 진입하지 않도록 하는 key
    private boolean timer = true;
    private TimerThread thread = null;

    private int mode = 0; // 0 : 일간/ 1 : 년간 / 2 : 월간 / 3 : 주간

    private SnapHelper snapHelper; //중앙에 값 오도록 도와주는 helper
    private RecyclerView.LayoutManager mLayoutManager;

    //생성자
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

    private void setModeinitialize() { //모드가 바뀔때 초기화하는 함수
        calendarList = null;
        lastData = null;
        nextData = null;

        calendarList = new ArrayList<>();
        lastData = new myCalendarData(0);
        nextData = new myCalendarData(0);
        snapPosition = 0;
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        snapView = snapHelper.findSnapView(mLayoutManager);

//        two_count = init_count;
        timer = true;
        init = true;
    }

    private void initView() {
        Log.v(log, "initView");
        String intService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(intService);
        View v = li.inflate(R.layout.dailymemo_custom_recyclerview, this, false);
        addView(v);
        snapPosition = 0;

        recyclerView = (RecyclerView) findViewById(R.id.holi_recyclerview);
        textView = (TextView) findViewById(R.id.daily_scrolldate);
        mAdapter = new CalendarAdapter(calendarList, mode);

        //리사이클 뷰 세팅
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        //현재 값 중앙으로 오게 세팅팅
       snapHelper = new LinearSnapHelper(); //선택한 객체가 자연스럽게 중앙에 오도록 설정
        snapHelper.attachToRecyclerView(recyclerView); //스넵 헬처가 중앙으로 떙겨주는 헬퍼
        snapView = snapHelper.findSnapView(mLayoutManager); //현재 중앙에 있는 view

        lastData = new myCalendarData(0); //제일 왼쪽, 0은 현재 날짜
        nextData = new myCalendarData(0);

 //       two_count = init_count;
        setRecyclerView();
    }

    public void changeMode(int mode) { //모드 변경 함수
        Log.v(log, "changmode");
        try {
            thread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mode = mode;
        setModeinitialize(); //초기화
        setRecyclerView();
        mAdapter.chanteData(calendarList);
        mAdapter.changeMode(mode);
    }

    private void setRecyclerView() { //recyclerview 이벤트 처리
        //       Log.v(log, "setRecyclerView");
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) { //스크롤시
                super.onScrolled(recyclerView, dx, dy);

                //시간 세는 thread 정지
                try {
//                    Log.v(log,"thread 정지");
                    thread.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int totalItemCount = mLayoutManager.getChildCount();
                //             Log.v(log,"onScrolled total : "+totalItemCount);
                for (int i = 0; i < totalItemCount; i++) {
                    View childView = recyclerView.getChildAt(i);
                    //                  Log.v(log,"childView");
                    if (mode == 0) {
                        //                      Log.v(log,"(mode == 0)");
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
                try {
//                    Log.v(log,"thread 시작");
                    thread = new TimerThread(); //타이머 스레드 생성
                    threadStart(thread); //타이머 시작
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //스크롤시 끝에 닿은 지 확인하는 함수
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (init && timer) {
                    timer = false;
                    if (!recyclerView.canScrollHorizontally(-1)) { //리스트의 앞
                        Log.v(log + "onScrollstate", "start");
                        checkModetoprpare(1);
                    } else if (!recyclerView.canScrollHorizontally(1)) { //리스트의 끝
                        Log.v(log + "onScrollstate", "end");
                        checkModetoprpare(2);
                    }
 //                   two_count = init_count;

                    //여기 타이머 넣기
                    Timer timer = new Timer();
                    threadStart(timer);
//                    Log.v(log, "timer 넣음");
                } else {
 //                   two_count--;
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        Log.v(log, "oncreate onscroll");

        // row click listener
        recyclerView.addOnItemTouchListener(new

                RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) { //클릭시, 현재는 toast만 뜸

                MyCalendar calendar = calendarList.get(position);
                //주석하면 클릭한 색이 바뀌는 코드
             /*   TextView childTextView = (TextView) (view.findViewById(R.id.day_1));

                Animation startRotateAnimation = AnimationUtils.makeInChildBottomAnimation(context);
                childTextView.startAnimation(startRotateAnimation);
                childTextView.setTextColor(Color.CYAN);*/

                Toast.makeText(context, calendar.getMonth() + "." + calendar.getDate() + "." + calendar.getDay() + " is selected!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) { //꾹 누를시

                MyCalendar calendar = calendarList.get(position);
   /*
                TextView childTextView = (TextView) (view.findViewById(R.id.day_1));
                childTextView.setTextColor(Color.GREEN);
    */
                Toast.makeText(context, calendar.getDate() + "/" + calendar.getDay() + "/" + calendar.getMonth() + "   selected!", Toast.LENGTH_SHORT).show();


            }
        }));
        Log.v(log, "oncreate addtouch");

        checkModetoprpare(0); //type을 보고 함수를 지정해주는 함수

        thread = new TimerThread(); //타이머 스레드 생성
        threadStart(thread); //타이머 시작

    }

    private void maybeNofitySnapPositionChange(RecyclerView recyclerview) {
        //중앙의 position 알기
        //Log.v(log,"maybeNofitySnapPositionChange");
        View snapView = snapHelper.findSnapView(mLayoutManager);
        int snapPosition = mLayoutManager.getPosition(snapView);

        boolean snapPositionChanged = this.snapPosition != snapPosition;
        if (snapPositionChanged) { // 중앙의 포지션이 바뀌었을 경우
            // Log.v(log,"snapPositionChanged");
            this.snapPosition = snapPosition;
            this.snapView = snapView;
            MyCalendar calendar = calendarList.get(snapPosition);

            if (mode < 4) {
                //중앙의 원 보이도록 함.
                snapView = snapHelper.findSnapView(mLayoutManager);
            }

            switch (mode) { //위의 텍스트를 지정해줌, 한줄짜리 textview(지금 날짜는~)
                case 0: //일간
                    Log.v(log, "daily");
                    textView.setText(calendar.getYear() + "년 " + calendar.getMonth() + calendar.getDate() + "일");
                    break;
                case 1: //년간
                    Log.v(log, "year");
                    textView.setText(calendar.getYear() + "년");
                    break;
                case 2: //월간
                    Log.v(log, "month");
                    textView.setText(calendar.getYear() + "년 " + calendar.getMonth());
                    break;
                case 3: //주간
                    textView.setText(calendar.getYear() + "년 " + calendar.getMonth() + " " + calendar.getWeek());
                    break;
            }
        }
    }

    private void checkModetoprpare(int type) { //type을 보고 함수를 지정해주는 함수
        Log.v(log, "checkModetoprpare mode : " + mode);
        switch (mode) {
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
        mAdapter.notifyDataSetChanged();
    }


    // 끝에 닿았을때 초기화.
    //일간, 스크롤 연관
    private void prepareCalendarDate(int type) {
        //type = 0 초기화 / type = 1 이전달 / type = 2 이후 달
        Log.v(log, "preparecalendardata  type : " + type);
        try {
            View snapView = snapHelper.findSnapView(mLayoutManager);
            if (snapView != null) {
                this.snapPosition = mLayoutManager.getPosition(snapView);
                Log.v(log, "snapposition changed position : " + this.snapPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myCalendarData m_calendar = null;//int 값이 아닌 myCalendarData 값을 미리 저장해두면 되지 않을까?
        if (type == 1) { //이전 달 호출
            lastData.getNextWeekDay(-31);
            m_calendar = lastData;
            Log.v(log, "month : " + (lastData.getMonth() + 1) + "date : " + m_calendar.getDay());
        } else if (type == 2) { //이후 달 호출
            m_calendar = nextData;
            Log.v(log, "month : " + (lastData.getMonth() + 1) + "date : " + m_calendar.getDay());
        } else if (type == 0) { //초기화
            myCalendarData tmp = new myCalendarData(-3);
            lastData = tmp;
            m_calendar = new myCalendarData(-3);
        }
        for (int i = 0; i < 31; i++) {
            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()), String.valueOf(m_calendar.getWeekofmonth()), String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i);
            m_calendar.getNextWeekDay(1); //0보다 크면 이후
            if (type == 1) {
                calendarList.add(i, calendar); //데이터 셋에 저장 (인덱스, 데이터)
            } else {
                calendarList.add(calendar); //데이터 셋에 저장
            }
        }
        if (type == 1) { //이전 달 호출
            recyclerView.scrollToPosition(snapPosition + 34); //스크롤 할 떄의 중앙자리로 이동
            View snapView = snapHelper.findSnapView(mLayoutManager);
            if (snapView != null) {
                this.snapPosition = mLayoutManager.getPosition(snapView);
                Log.v(log, "snapposition changed position : " + this.snapPosition);
            }
            m_calendar.getNextWeekDay(-31); //지난 달 데이터 지정
            lastData = m_calendar; //왼쪽 맨 끝값
            Log.v(log, "save_lastdata : " + (lastData.getMonth() + 1) + "," + lastData.getDay());
        } else {
            nextData = m_calendar; //다음달 데이터 저장, 오른쪽 맨 끝값
        }
        init = true;
        Log.v(log, "preparecalendardata 끝");
    }

    //년간
    private void prepareCalendarYear(int type) {
        //type = 0 초기화 / type = 1 이전달 / type = 2 이후 달
        Log.v(log, "prepareCalendarYear  type : " + type);
        try {
            View snapView = snapHelper.findSnapView(mLayoutManager);
            if (snapView != null) {
                this.snapPosition = mLayoutManager.getPosition(snapView);
                Log.v(log, "snapposition changed position : " + this.snapPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myCalendarData m_calendar = null;

        if (type == 1) { //이전 년 호출
            m_calendar = lastData;
            m_calendar.getNextYear(-10);
            Log.v(log, "year : " + m_calendar.getYear());
        } else if (type == 2) { //이후 달 호출
            m_calendar = nextData;
            Log.v(log, "year : " + m_calendar.getYear());
        } else if (type == 0) { //초기화
            myCalendarData tmp = new myCalendarData(0);
            tmp.getNextYear(-2);
            lastData = tmp;
            m_calendar = new myCalendarData(0);
            m_calendar.getNextYear(-2);
        }

        for (int i = 0; i < 10; i++) {
            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()), String.valueOf(m_calendar.getWeekofmonth()), String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i);
            m_calendar.getNextYear(1);
            if (type == 1) {
                calendarList.add(i, calendar);
            } else {
                calendarList.add(calendar);
            }
        }

        if (type == 1) {
            Log.v(log, "snapposition : " + snapPosition);
            recyclerView.scrollToPosition(snapPosition + 12);
            View snapView = snapHelper.findSnapView(mLayoutManager);
            if (snapView != null) {
                this.snapPosition = mLayoutManager.getPosition(snapView);
                Log.v(log, "snapposition changed position : " + this.snapPosition);
            }
            m_calendar.getNextYear(-10);
            lastData = m_calendar;
            Log.v(log, "save_lastyear : " + lastData.getYear());
        } else {
            nextData = m_calendar;
        }
        init = true;
        Log.v(log, "prepareCalendarYear 끝");
    }

    //월간
    private void prepareCalendarMonth(int type) {
        //type = 0 초기화 / type = 1 이전달 / type = 2 이후 달
        Log.v(log, "prepareCalendarMonth  type : " + type);
        try {
            View snapView = snapHelper.findSnapView(mLayoutManager);
            if (snapView != null) {
                this.snapPosition = mLayoutManager.getPosition(snapView);
                Log.v(log, "snapposition changed position : " + this.snapPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myCalendarData m_calendar = null;

        if (type == 1) { //이전 년 호출
            lastData.getNextMonth(-10);
            m_calendar = lastData;
            Log.v(log, "month : " + (m_calendar.getMonth() + 1));
        } else if (type == 2) { //이후 달 호출
            m_calendar = nextData;
            Log.v(log, "month : " + (m_calendar.getMonth() + 1));
        } else if (type == 0) { //초기화
            myCalendarData tmp = new myCalendarData(0);
            tmp.getNextMonth(-2);
            lastData = tmp;
            m_calendar = new myCalendarData(0);
            m_calendar.getNextMonth(-2);
        }

        for (int i = 0; i < 10; i++) {
            MyCalendar calendar = new MyCalendar(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()), String.valueOf(m_calendar.getWeekofmonth()), String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i);
            m_calendar.getNextMonth(1);
            if (type == 1) {
                calendarList.add(i, calendar);
            } else {
                calendarList.add(calendar);
            }
        }

        if (type == 1) {
            recyclerView.scrollToPosition(snapPosition + 12);
            View snapView = snapHelper.findSnapView(mLayoutManager);
            if (snapView != null) {
                this.snapPosition = mLayoutManager.getPosition(snapView);
                Log.v(log, "2snapposition changed position : " + this.snapPosition);
            }
            m_calendar.getNextMonth(-10);
            lastData = m_calendar;
            Log.v(log, "save_lastmonth : " + (lastData.getMonth() + 1));
        } else {
            nextData = m_calendar;
        }
        init = true;
        Log.v(log, "prepareCalendarMonth 끝");
    }

    //주간
    private void prpareCalendarWeek(int type) {
        //해당 월의 1일로 초기화 / 이전달 이전 달의 1일로 이동 / 다음달 다음 달의 1일로 이동
        //type = 0 초기화 / type = 1 이전달 / type = 2 이후 달
        Log.v(log, "prpareCalendarWeek  type : " + type);
        try {
            View snapView = snapHelper.findSnapView(mLayoutManager);
            if (snapView != null) {
                this.snapPosition = mLayoutManager.getPosition(snapView);
                Log.v(log, "snapposition changed position : " + this.snapPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myCalendarData m_calendar = null;
//        int thisweek = -1;
        if (type == 1) { //이전 주 호출
            lastData.getNextMonth(-1);
            lastData.getFstDayOfMonth(); //캘린더를 1일로 하는 코드
            m_calendar = lastData;
            Log.v(log, "month : " + (lastData.getMonth() + 1) + "week :" + lastData.getWeekofmonth() + "째주");
        } else if (type == 2) { //이후 주 호출
            nextData.getNextMonth(1);
            nextData.getFstDayOfMonth(); //캘린더를 1일로 하는 코드
            m_calendar = nextData;
            Log.v(log, "month : " + (m_calendar.getMonth() + 1) + "week :" + m_calendar.getWeekofmonth() + "째주");
        } else if (type == 0) { //초기화
            m_calendar = new myCalendarData(0);
            m_calendar.getFstDayOfMonth(); //캘린더를 1일로 하는 코드
//            thisweek = m_calendar.getWeekofmonth();
        }

        int month = m_calendar.getMonth();
        int weekofmonth = m_calendar.getWeekofmonth();
        int i = 0;
        MyCalendar calendar;

        while (m_calendar.getMonth() == month) { //같은 달의 주간 추가
            calendar = new MyCalendar(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()), String.valueOf(m_calendar.getWeekofmonth()), String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i);
            if (type == 1) {
                calendarList.add(i, calendar);
            } else {
                calendarList.add(calendar);
            }
            month = m_calendar.getMonth();
            weekofmonth = m_calendar.getWeekofmonth(); //주차 저장

            m_calendar.getNextWeekDay(7);
            i++;
        }

        while (m_calendar.getMonth() != month) { //마지막 주차 찾기
            m_calendar.getNextWeekDay(-1);
        }
        if (m_calendar.getWeekofmonth() != weekofmonth) {  //마지막 주차 추가
            calendar = new MyCalendar(m_calendar.getWeekDay(), String.valueOf(m_calendar.getDay()), String.valueOf(m_calendar.getWeekofmonth()), String.valueOf(m_calendar.getMonth()), String.valueOf(m_calendar.getYear()), i);
            weekofmonth = m_calendar.getWeekofmonth();
            if (type == 1) {
                calendarList.add(i, calendar);
            } else {
                calendarList.add(calendar);
            }
        }
        if (type == 1) { //이전달
            recyclerView.scrollToPosition(snapPosition + weekofmonth+3);
            View snapView = snapHelper.findSnapView(mLayoutManager);
            if (snapView != null) {
                this.snapPosition = mLayoutManager.getPosition(snapView);
                Log.v(log, "snapposition changed position : " + this.snapPosition);
            }
            lastData = m_calendar;
            Log.v(log, "2save_lastmonth : " + (lastData.getMonth() + 1) + lastData.getWeekofmonth() + "째주");
        } else { //이후달, 초기화
            nextData = m_calendar;
        }
        Log.v(log, "prepareCalendarMonth 끝");
    }

    private void threadStart(Thread t){
        t.setDaemon(true); //main 스레드 종료되면 같이 종료한게 하는 코드
        t.start(); //타이머 시작
    }

    //스크롤 하지 않고 정지된 시간 구하는 thread
    class TimerThread extends Thread {
        public void run() {
         //   Log.v(log, "TimerThread");
            try {
                for (int i = 0; i < 1; i++) { //3초동안 기다림
//                    Log.v(log, "Thread 초세기 : " + (i + 1) + " 초");
                    Thread.sleep(1500);
                }
                Log.v(log, "Thread 초세기 완료");
                requestServer();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    public void requestServer() {
        Log.v(log, "requestServer");
        String start = null; //일지를 가져올 기간 설정 ex) 23일 하루 - start : 23, end : 23
        String end = null;
        String str_month;
        String str_date;
        MyCalendar calendar = calendarList.get(snapPosition);
        Log.v(log,"snapPosition : "+snapPosition);

        myCalendarData mydata = new myCalendarData(0);
        mydata.setAll(Integer.parseInt(calendar.getYear()),calendar.getImonth(),Integer.parseInt(calendar.getDate()));
        Log.v(log,"set 완료");

        str_month = String.format("%02d", (mydata.getMonth()+1));
//        Log.v(log,"month : "+str_month);
        str_date = String.format("%02d", mydata.getDay());
//        Log.v(log," date : "+str_date);
        switch (mode) {
            case 0: //일간
                end = start = calendar.getYear()+"-"+str_month+"-"+str_date;
                ControlDailyMomo.GetInstance().getTodayDaily(start);
                Log.v(log, "requestServer 일간");
                break;
            case 1: //년간
                end = start = calendar.getYear();
                ControlDailyMomo.GetInstance().getYearDaily(start);
                Log.v(log, "requestServer 년간");
                break;
            case 2: //월간
                end = start = calendar.getYear()+"-"+str_month;
                ControlDailyMomo.GetInstance().getMonthDaily(start);
                Log.v(log, "requestServer 월간");
                break;
            case 3: //주간
                Log.v(log, "requestServer 주간");
                myCalendarData tmp = new myCalendarData(0);
                tmp.setAll(Integer.parseInt(calendar.getYear()),calendar.getImonth(),Integer.parseInt(calendar.getDate()));
                Log.v(log,"set 완료");
                int num = calendar.getWeekFirstDay();
                if(num == -1){
                    Log.v(log,"error");
                    break;
                }
                tmp.getNextWeekDay(-1*(num));
                                Log.v(log,"start 완료");
                str_month = String.format("%02d",(tmp.getMonth()+1));
                str_date = String.format("%02d",tmp.getDay());
                start = tmp.getYear()+"-"+str_month+"-"+str_date;
                tmp.getNextWeekDay(6);

                str_month = String.format("%02d",(tmp.getMonth()+1));
                str_date = String.format("%02d",tmp.getDay());
                end = tmp.getYear()+"-"+str_month+"-"+str_date;
                ControlDailyMomo.GetInstance().getWeekDaily(start,end);
                break;
        }
        Log.v(log,"start : "+start+" endday : "+end);
    }

    class Timer extends Thread{
        public void run() {
            try {
                Thread.sleep(2000);
                timer = true;
                Log.v(log, "Timer 초세기 완료 "+ timer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
//https://github.com/atanudasgupta/myHorizontalCalendar/blob/master/app/src/main/java/com/emerald/mycalendar/MainActivity.java


//중앙 값 가져오기 : https://pythonq.com/so/android/91216
//https://www.javaer101.com/en/article/16366527.html
//스냅 : https://rubensousa.github.io/2016/08/recyclerviewsnap
//스냅 중앙값 코틀린 : https://medium.com/over-engineering/detecting-snap-changes-with-androids-recyclerview-snaphelper-9e9f5e95c424
// recyclerview 끝 감지 : https://medium.com/@ydh0256/android-recyclerview-%EC%9D%98-%EC%B5%9C%EC%83%81%EB%8B%A8%EA%B3%BC-%EC%B5%9C%ED%95%98%EB%8B%A8-%EC%8A%A4%ED%81%AC%EB%A1%A4-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EA%B0%90%EC%A7%80%ED%95%98%EA%B8%B0-f0e5fda34301

