package com.example.edrkr.mainpage;

import android.util.Log;

import com.example.edrkr.UserIdent;
import com.example.edrkr.h_network.AutoRetryCallback;
import com.example.edrkr.h_network.ResponseDailyMemoJson;
import com.example.edrkr.h_network.ResponseGraphJson;
import com.example.edrkr.h_network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ControlDailyMomo {

    private static final ControlDailyMomo Instance=new ControlDailyMomo(); //싱글턴 문법, memory leak
    public static ControlDailyMomo GetInstance(){
        return Instance;
    }
    private ControlDailyMomo(){} //생성자 제한


    //통신
    public void getTodatDaily(){
        Call<List<ResponseDailyMemoJson>> wheather = RetrofitClient.getApiService().getTodayDailyMemo(
                UserIdent.GetInstance().getUserIdent(),"시작날짜","종료날짜"); //api 콜
        wheather.enqueue(new AutoRetryCallback<List<ResponseDailyMemoJson>>() {
            @Override
            public void onFinalFailure(Call<List<ResponseDailyMemoJson>> call, Throwable t) {
                Log.e("일별 정보 연결실패", t.getMessage());
            }

            @Override
            public void onResponse(Call<List<ResponseDailyMemoJson>> call, Response<List<ResponseDailyMemoJson>> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }


                Log.d("일별 통신 성공적", response.body().toString());
                List<ResponseDailyMemoJson> DailyMemoJson = response.body(); //통신 결과 받기

                //일별 정보 확인
                Log.d("id", ""+DailyMemoJson.get(0).getDiaryId());
                Log.d("날짜", DailyMemoJson.get(0).getDate());
                Log.d("내용", DailyMemoJson.get(0).getContent());


                //수정하기
                //문자열 자르기
                String[] oneWeek = new String[7];
                //값 float 형식으로 바꾸기
                float[] oneWeekData = new float[7];
                for(int i=0 ;i<7 ;i++){
                    //String[] weekDate = graphJsons.get(i).getDate().split("-");
                    //oneWeek[i]=weekDate[1]+"/"+weekDate[2]; // 0/0 날짜 포맷

                    //oneWeekData[i] = Float.parseFloat(graphJsons.get(i).getSoilavg()); //평균

                    //표에 데이터 입력
                    //day_textView[i].setText(oneWeek[i]);
                    //value_textView[i].setText(graphJsons.get(i).getSoilavg());
                }





            }
        });
    }

}
