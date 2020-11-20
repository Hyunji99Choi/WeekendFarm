package com.example.edrkr;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ControlMonitoring {

    private static final ControlMonitoring Instance=new ControlMonitoring(); //싱글턴 문법
    public static ControlMonitoring GetInstance(){
        return Instance;
    }
    private ControlMonitoring(){} //생성자 제한


    private Context contex;
    private sub_page1 page;
    //모니터링 화면의 context 가져오기
    public void setContextThis(Context contex, sub_page1 montoring){
        this.contex=contex; this.page=montoring;
    }

    private String monitoring_URL="http://52.79.237.95:3000/sensor/field"; // 센서값 값 가져올 서버 url





    //센서 통신하기
    public void NetworkSensorCall(int farmid){ //센서 값 통신 함수
        NetworkTask_monitoring monitoring_networkTask = new NetworkTask_monitoring(monitoring_URL+farmid,null);
        monitoring_networkTask.execute(); //비동기 통신,get
    }

    //cctv 통신하기
    public void NetworkCCTVCall(int farmid){

    }

    //cctv 세팅하기
    public void SettingCCTV(String cctvURL){

    }


    //센서값들 세팅파기
    public void updateSensor(int soil,int sunny,double hot,int water){
        String comment="";
        //cctv
        Log.w("센서 값"," "+soil+" "+sunny+" "+hot+" "+water);
        //토양센서
        page.soil_sensor.setProgress(soil);
        //조도센서
        page.sunny_drawable.setColor(sunny_color(sunny));
        page.sunny_sensor.setImageDrawable(page.sunny_drawable);

        //대기온도센서
        page.hot_drawable.setColor(hot_color(hot));
        page.hot_sencor.setImageDrawable(page.hot_drawable);
        String temp = String.format("%.1f",hot);
        page.hot_text.setText(temp+"c");

        //대기습도센서
        page.water_seneor.setProgress(water);

        //총론 수정 *** 각 값에 따른 수정이 필요함.
        page.comment.setText(comment);

    }







    //조도 센서 색 return
    public int sunny_color(int light){
        int color;

        if (light>300){//매우 밝음
            color=contex.getResources().getColor(R.color.sunny_very_bright);
            return color;
        }else if (light>250){ //밝음
            color=contex.getResources().getColor(R.color.sunny_bright);
            return color;
        }else if (light>150){ //약간 밝음
            color=contex.getResources().getColor(R.color.sunny_some_bright);
            return color;
        }else if (light>100){ //약간 어두움
            color=contex.getResources().getColor(R.color.sunny_some_dack);
            return color;
        }else if (light>90){ //어두움
            color=contex.getResources().getColor(R.color.sunny_dack);
            return color;
        }else{ //매우 어두움
            color = contex.getResources().getColor(R.color.sunny_very_dack);
            return color;
        }

    }

    //온도 센서 색 return
    public int hot_color(double temp){
        int color;

        if (temp>30){//매우 더움
            color=contex.getResources().getColor(R.color.hot_very_hot);
            return color;
        }else if (temp>25){ //더움
            color=contex.getResources().getColor(R.color.hot_hot);
            return color;
        }else if (temp>20){ //조금 더움(20~25 적당함)
            color=contex.getResources().getColor(R.color.hot_some_hot);
            return color;
        }else if (temp>10){ //약간 추움
            color=contex.getResources().getColor(R.color.hot_some_cold);
            return color;
        }else if (temp>0){ //추움
            color=contex.getResources().getColor(R.color.hot_cold);
            return color;
        }else{ //매우 추움
            color = contex.getResources().getColor(R.color.hot_very_cold);
            return color;
        }

    }



}
