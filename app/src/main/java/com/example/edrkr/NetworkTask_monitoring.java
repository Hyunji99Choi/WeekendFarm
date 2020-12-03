package com.example.edrkr;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkTask_monitoring extends AsyncTask<Void, Void, String> {
    String url;
    ContentValues values;

    public boolean finish_Check=false; //통신 완료 시점을 확인하는 변수
    public String result; // 서버에서 받은 string 값

    NetworkTask_monitoring(String url, ContentValues values){
        this.url = url;
        this.values = values;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progress bar를 보여주는 등등의 행위
    }

    @Override
    protected String doInBackground(Void... params) {
        String result;
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        result = requestHttpURLConnection.request(url, values);
        this.result=result;
        this.finish_Check=true;
        return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
    }

    @Override
    protected void onPostExecute(String result) {
        // 통신이 완료되면 호출됩니다.
        // 결과에 따른 UI 수정 등은 여기서 합니다.


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
}
