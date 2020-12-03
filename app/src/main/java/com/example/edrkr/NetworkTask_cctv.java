package com.example.edrkr;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkTask_cctv extends AsyncTask<Void, Void, String> {
    String url;
    ContentValues values;

    public boolean finish_Check=false; //통신 완료 시점을 확인하는 변수
    public String result; // 서버에서 받은 string 값

    NetworkTask_cctv(String url, ContentValues values){
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
}
