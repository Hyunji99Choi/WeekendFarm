package com.example.edrkr.dailyMemo;

import android.graphics.Camera;
import android.util.Log;

import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyCalendar {
    private String day;
    private String date, month, year, week;

    private int imonth;
    private int pos;
    private String tag = "areum/Mycalendar";

    public MyCalendar() {
    }

    public MyCalendar(String day, String date, String week, String month, String year, int i) {
//        Log.v(tag,"생성자");
        this.day = day;  //요일
        this.date = date;   //날짜
        this.week = week+"째주";
        this.month = getMonthStr(month);
        this.year = year;
        this.pos = i;
//        Log.v(tag,day+","+date+","+week+","+month+","+year);
//        Log.v(tag,this.day+","+this.date+","+this.week+","+this.month+","+this.year);

    }
    private String getMonthStr(String month){
//        Log.v(tag,"getmonthstr");

//        Calendar cal = Calendar.getInstance();
        int monthnum = Integer.parseInt(month);
//        Log.v(tag,"monthnum : "+monthnum);
        String month_name = (monthnum + 1)+"월";
//        cal.set(Calendar.MONTH,monthnum);
        imonth = monthnum;
//
//        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
//        String month_name = month_date.format(cal.getTime());
//        Log.v(tag,"month_name : "+month_name);
        return month_name;
    }

    public int getWeekFirstDay(){
       String[] DayOfWeek = {"일","월","화","수","목","금","토"};
       int i;
       for(i = 0;i<DayOfWeek.length;i++){
           if(day.compareTo(DayOfWeek[i]) == 0) break;
       }
       if(i == DayOfWeek.length) return -1;
       return i;
    }

    public int getImonth() {
        return imonth;
    }

    public int getPos() {
        return pos;
    }

    public String getDay() {
        return day;
    }
    public void setDay(String date) {
        this.day = date;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }
    public void setWeek(String week) {
        this.week = week;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    public String getMonth() {
        return month;
    }


    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
}
