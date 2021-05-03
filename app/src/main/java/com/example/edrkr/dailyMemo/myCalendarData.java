package com.example.edrkr.dailyMemo;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class myCalendarData {
    Calendar calendar;
    int startday, currentmonth, currentyear, dayofweek, weekofmonth;
    private String tag ="areum/myCalendarDate";
    String stringdayofweek;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("E");

    public myCalendarData(int start){
        this.calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, start);
        setThis();
    }

    private void setThis(){ //데이터 저장
        this.startday = calendar.get(Calendar.DAY_OF_MONTH); //날짜
        this.currentmonth = calendar.get(Calendar.MONTH); //달 -1
        this.currentyear = calendar.get(Calendar.YEAR); //년도
        this.dayofweek = calendar.get(Calendar.DAY_OF_WEEK); //일요일이 1 토요일 7
        this.stringdayofweek = dateFormat.format(calendar.getTime()); //월화수목금토일
        this.weekofmonth = calendar.get(Calendar.WEEK_OF_MONTH); //몇주인지
        Log.v(tag,currentyear+","+(currentmonth+1)+","+startday+","+stringdayofweek+","+weekofmonth+"째주");
    }

    public void getNextWeekDay(int nxt){ //다음날 +n, 이전날 -n 로변경
//        Log.v(tag,"getnextweekday");
        calendar.add(Calendar.DATE, nxt);
        setThis();
    }

    public void getNextMonth(int nxt){ //다음달 +n, 이전달 -n 로변경
//        Log.v(tag,"getNextMont");
        calendar.add(Calendar.MONTH,nxt);
        setThis();
    }

    public void getFstDayOfMonth(){ //해당 달의 1일로 변경
        calendar.set(Calendar.DATE,1);
        setThis();
    }

    public void getNextYear(int nxt){ //다음년 +n, 이전년 -n 로변경
        calendar.add(Calendar.YEAR,nxt);
        setThis();
    }


    public int getWeekofmonth() {
        return weekofmonth;
    }

    public void setWeekofmonth(int weekofmonth) {
        this.weekofmonth = weekofmonth;
    }

    public String getWeekDay (){
        return this.stringdayofweek;
    }

    public int getYear(){
        return this.currentyear;
    }
    public int getMonth(){
        return this.currentmonth;
    }
    public int getDay(){
        return this.startday;
    }

}
//https://atanudasgupta.medium.com/creating-a-horizontal-scrolling-calendar-using-android-sdk-95914288b8c3
