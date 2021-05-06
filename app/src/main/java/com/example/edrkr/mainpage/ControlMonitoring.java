package com.example.edrkr.mainpage;


import android.content.Context;
import android.util.Log;

import com.example.edrkr.R;
import com.example.edrkr.h_network.ResponseCCTVJson;
import com.example.edrkr.h_network.ResponseSensorJson;
import com.example.edrkr.h_network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControlMonitoring {

    private static final ControlMonitoring Instance=new ControlMonitoring(); //싱글턴 문법, memory leak
    public static ControlMonitoring GetInstance(){
        return Instance;
    }
    private ControlMonitoring(){} //생성자 제한



    //toolbar theme color
    int toolbarColor;
    public int getToolbarColor() { return toolbarColor; }
    public void setToolbarColor(int toolbarColor) { this.toolbarColor = toolbarColor; }

    int toolbarTheme;
    public int getToolbarTheme() { return toolbarTheme; }
    public void setToolbarTheme(int toolbarTheme) { this.toolbarTheme = toolbarTheme; }



    private Context contex; //이거 사용안하는것 같음...
    private sub_page1 page1;
    private sub_page2 page2;
    //모니터링 화면의 context 가져오기
    public void setContextThis(Context contex, sub_page1 montoring){
        this.contex=contex; this.page1 =montoring;
    }
    public void setSubpage2(sub_page2 graph){
        this.page2 = graph;
    }

    private cctv_fragmentpage1 cctv1;
    private cctv_fragmentpage2 cctv2;
    private cctv_fragmentpage3 cctv3;
    //모니터링 화면의 fragment 객체 가져오기
    public void setFragmentPage1(cctv_fragmentpage1 s1){ this.cctv1=s1; }
    public void setFragmentPage2(cctv_fragmentpage2 s2){ this.cctv2=s2; }
    public void setFragmentPage3(cctv_fragmentpage3 s3){ this.cctv3=s3; }

    //그래프 통신
    public void NetworkkGraphCall(){
        page2.getGreahData();
    }


    //센서 통신하기
    public void NetworkSensorCall(int farmid){ //센서 값 통신 함수

        Call<ResponseSensorJson> sensor = RetrofitClient.getApiService().getSensor(String.valueOf(farmid)); //api 콜
        sensor.enqueue(new Callback<ResponseSensorJson>() {
            @Override
            public void onResponse(Call<ResponseSensorJson> call, Response<ResponseSensorJson> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }

                Log.d("센서 통신 성공적", response.body().toString());
                ResponseSensorJson sensorJson = response.body(); //통신 결과 받기

                //센서 값 업데이트 updateSensor(soild,sunny,hot,water);
                updateSensor(Integer.parseInt(sensorJson.getSoil()),Integer.parseInt(sensorJson.getLight()),
                        Double.parseDouble(sensorJson.getTemp()),Integer.parseInt(sensorJson.getHumi()),
                        sensorJson.getComment());

            }

            @Override
            public void onFailure(Call<ResponseSensorJson> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

    }

    //cctv 통신하기
    public void NetworkCCTVCall(int farmid){

        Call<ResponseCCTVJson> cctv = RetrofitClient.getApiService().getCCTV(String.valueOf(farmid)); //api 콜
        cctv.enqueue(new Callback<ResponseCCTVJson>() {
            @Override
            public void onResponse(Call<ResponseCCTVJson> call, Response<ResponseCCTVJson> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }

                Log.d("cctv 통신 성공적", response.body().toString());
                ResponseCCTVJson cctvJson = response.body(); //통신 결과 받기

                //cctv 세팅하기
                SettingCCTV(cctvJson.getCamera1(),cctvJson.getCamera2(),cctvJson.getCamera3());
            }

            @Override
            public void onFailure(Call<ResponseCCTVJson> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

    }


    //cctv 세팅하기
    public void SettingCCTV(String url1,String url2,String url3){
        cctv1.cctvURLSetting(url1);
        cctv2.cctvURLSetting(url2);
        cctv3.cctvURLSetting(url3);
        Log.w("cctv url"," "+url1+" "+url2+" "+url3);
    }


    //센서값들 세팅파기
    public void updateSensor(int soil,int sunny,double hot,int water,String comment){
        //cctv
        Log.w("센서 값"," "+soil+" "+sunny+" "+hot+" "+water);
        Log.w("코멘트"," "+comment);

        //토양센서, 프로세스
        page1.soil_sensor.setProgress(soil);


        //조도센서
        page1.sunny_drawable.setColor(sunny_color(sunny));
        page1.sunny_sensor.setImageDrawable(page1.sunny_drawable);

        //대기온도센서
        page1.hot_drawable.setColor(hot_color(hot));
        page1.hot_sencor.setImageDrawable(page1.hot_drawable);
        String temp = String.format("%.1f",hot); //warning
        page1.hot_text.setText(temp+"c"); //use resource string with placeholders


        //대기습도센서, 프로세스
        page1.water_seneor.setProgress(water);


        //총론 수정 *** 각 값에 따른 수정이 필요함.
        page1.comment.setText(comment);

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
