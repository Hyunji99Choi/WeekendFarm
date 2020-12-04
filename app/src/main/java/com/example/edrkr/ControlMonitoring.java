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

    private static final ControlMonitoring Instance=new ControlMonitoring(); //싱글턴 문법, memory leak
    public static ControlMonitoring GetInstance(){
        return Instance;
    }
    private ControlMonitoring(){} //생성자 제한


    private Context contex; //이거 사용안하는것 같음...
    private sub_page1 page;
    //모니터링 화면의 context 가져오기
    public void setContextThis(Context contex, sub_page1 montoring){
        this.contex=contex; this.page=montoring;
    }

    private cctv_fragmentpage1 cctv1;
    private cctv_fragmentpage2 cctv2;
    private cctv_fragmentpage3 cctv3;
    //모니터링 화면의 fragment 객체 가져오기
    public void setFragmentPage1(cctv_fragmentpage1 s1){ this.cctv1=s1; }
    public void setFragmentPage2(cctv_fragmentpage2 s2){ this.cctv2=s2; }
    public void setFragmentPage3(cctv_fragmentpage3 s3){ this.cctv3=s3; }


    private String monitoring_URL="http://3.35.55.9:3000/sensor/field"; // 센서값 값 가져올 서버 url
    private String cctv_URL="http://3.35.55.9:3000/camera/";




    //센서 통신하기
    public void NetworkSensorCall(int farmid){ //센서 값 통신 함수
        NetworkTask_monitoring monitoring_networkTask = new NetworkTask_monitoring(monitoring_URL+farmid,null);
        monitoring_networkTask.execute(); //비동기 통신,get
    }

    //센서통신 josn 통신
    public void SensorJsonConvert(String result){
        //String cctvURL=""; //cctv url
        int soild=0; //토양 습도
        int sunny=0; //조도
        float hot=0.0f; //온도
        int water=0; // 대기습도

        Log.w("센서 통신",result);
        JSONObject SENSOR = null;
        try {
            SENSOR = new JSONObject(result);

            //Log.w("오브젝트 변환","오브젝트 변환 완료");
            soild = Integer.parseInt(SENSOR.getString("soil"));
            sunny = Integer.parseInt(SENSOR.getString("light"));
            hot = Float.parseFloat(SENSOR.getString("temp"));
            water = (int)Float.parseFloat(SENSOR.getString("humi"));

            //cctvURL = SENSOR.getString("URL");

        } catch (JSONException e) {
            Log.w("json","에러");
            e.printStackTrace();
        }



        ControlMonitoring.GetInstance().updateSensor(soild,sunny,hot,water); //센서값들 새로 세팅.
    }

    //cctv 통신하기
    public void NetworkCCTVCall(int farmid){
        NetworkTask_cctv monitoring_cctv = new NetworkTask_cctv(cctv_URL+farmid,null);
        monitoring_cctv.execute(); //비동기 통신,get
    }

    public void CctvJsonConvert(String result){
        String cctvURL1=""; //cctv url
        String cctvURL2="";
        String cctvURL3="";

        Log.w("cctv 통신",result);
        JSONObject CCTV = null;
        try {
            CCTV = new JSONObject(result);

            //Log.w("오브젝트 변환","오브젝트 변환 완료");
            cctvURL1=CCTV.getString("camera1"); //변수 이름*** 수정 요망
            cctvURL2=CCTV.getString("camera2");
            cctvURL3=CCTV.getString("camera3");


        } catch (JSONException e) {
            Log.w("json","에러");
            e.printStackTrace();
        }

        ControlMonitoring.GetInstance().SettingCCTV(cctvURL1,cctvURL2,cctvURL3); //cctv 세팅
    }
    //cctv 세팅하기
    public void SettingCCTV(String url1,String url2,String url3){
        cctv1.cctvURLSetting(url1);
        cctv2.cctvURLSetting(url2);
        cctv3.cctvURLSetting(url3);
    }


    //센서값들 세팅파기
    public void updateSensor(int soil,int sunny,double hot,int water){
        String comment="";
        //cctv
        Log.w("센서 값"," "+soil+" "+sunny+" "+hot+" "+water);

        //토양센서, 프로세스
        //원래 값 가져오기, 변할 값 비교
        /*
        if(page.soil_sensor.getProgress()<soil){ //변해야 되는 값이 더 클 경우
            for(int i=page.soil_sensor.getProgress();i<=soil;i++){
                page.soil_sensor.setProgress(i);
            }
        }else{
            for(int i=page.soil_sensor.getProgress();i>=soil;i--){ //변해야 되는 값이 더 작을 경우
                page.soil_sensor.setProgress(i);
            }
        }*/
        page.soil_sensor.setProgress(soil);


        //조도센서
        page.sunny_drawable.setColor(sunny_color(sunny));
        page.sunny_sensor.setImageDrawable(page.sunny_drawable);

        //대기온도센서
        page.hot_drawable.setColor(hot_color(hot));
        page.hot_sencor.setImageDrawable(page.hot_drawable);
        String temp = String.format("%.1f",hot);
        page.hot_text.setText(temp+"c");


        //대기습도센서, 프로세스
        //원래 값 가져오기, 변할 값 비교
        /*
        if(page.water_seneor.getProgress()<water){ //변해야 되는 값이 더 클 경우
            for(int i=page.water_seneor.getProgress();i<=water;i++){
                page.water_seneor.setProgress(water);
            }
        }else{
            for(int i=page.water_seneor.getProgress();i>=water;i--){ //변해야 되는 값이 더 작을 경우
                page.water_seneor.setProgress(water);
            }
        }
         */
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
