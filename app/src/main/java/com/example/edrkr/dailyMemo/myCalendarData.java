package com.example.edrkr.dailyMemo;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class myCalendarData {
    Calendar calendar;
    int startday, currentmonth, currentyear, dayofweek;
    private String tag ="myCalendarDate";
    String stringdayofweek;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("E");

    public myCalendarData(int start){
        this.calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, start);
        setThis();
    }

    private void setThis(){
//        Log.v(tag,"setthis");
        this.startday = calendar.get(Calendar.DAY_OF_MONTH); //날짜
//        Log.v(tag,"startday :"+startday);
        this.currentmonth = calendar.get(Calendar.MONTH); //달 -1
//        Log.v(tag,"currentmonth :"+currentmonth);
        this.currentyear = calendar.get(Calendar.YEAR); //년도
//        Log.v(tag,"currentyear :"+currentyear);
        this.dayofweek = calendar.get(Calendar.DAY_OF_WEEK); //일요일이 1 토요일 7
//        Log.v(tag,"dayofweek :"+dayofweek);
        this.stringdayofweek = dateFormat.format(calendar.getTime()); //월화수목금토일
//        Log.v(tag,"setthis"+stringdayofweek);
        Log.v(tag,currentyear+","+(currentmonth+1)+","+startday+","+stringdayofweek);
    }

    public void getNextWeekDay(int nxt){
//        Log.v(tag,"getnextweekday");
        calendar.add(Calendar.DATE, nxt);
        setThis();
    }

    public void getNextMonth(int nxt){
//        Log.v(tag,"getNextMont");
        calendar.add(Calendar.MONTH,nxt);
        setThis();
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
