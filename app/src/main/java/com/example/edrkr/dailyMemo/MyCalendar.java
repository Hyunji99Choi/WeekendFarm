package com.example.edrkr.dailyMemo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyCalendar {
    private String day;
    private String date, month, year;
    private int pos;
    private String tag = "Mycalendar";

    public MyCalendar() {
    }

    public MyCalendar(String day, String date, String month, String year, int i) {
//        Log.v(tag,"생성자");
        this.day = day;
        this.date = date;
        this.month = getMonthStr(month);
        this.year = year;
        this.pos =i;

    }
    private String getMonthStr(String month){
//        Log.v(tag,"getmonthstr");

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        int monthnum = Integer.parseInt(month);
        cal.set(Calendar.MONTH,monthnum);
        String month_name = month_date.format(cal.getTime());
        return month_name;

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
