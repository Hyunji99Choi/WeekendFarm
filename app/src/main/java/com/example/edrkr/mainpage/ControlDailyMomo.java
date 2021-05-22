package com.example.edrkr.mainpage;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.UserIdent;
import com.example.edrkr.h_network.AutoRetryCallback;
import com.example.edrkr.h_network.ResponseDailyMemoJson;
import com.example.edrkr.h_network.ResponseGraphJson;
import com.example.edrkr.h_network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ControlDailyMomo {

    private static final ControlDailyMomo Instance=new ControlDailyMomo(); //싱글턴 문법, memory leak
    public static ControlDailyMomo GetInstance(){
        return Instance;
    }
    private ControlDailyMomo(){} //생성자 제한

    ArrayList<Integer> id;
    ArrayList<String> day; //날짜
    ArrayList<String> contents; //내용

    public Context getContext() { return context; }
    public void setContext(Context context) { this.context = context; }

    private Context context;
    private RecyclerView recyclerView;
    private DailymemoAdapter adapter;

    public RecyclerView getRecyclerView() { return recyclerView; }
    public void setRecyclerView(RecyclerView recyclerView) { this.recyclerView = recyclerView; }
    public DailymemoAdapter getAdapter() { return adapter; }
    public void setAdapter(DailymemoAdapter adapter) { this.adapter = adapter; }



    //수정 통신
    public void updateDaily(int id, int position, String ctx){
        Call<String> update = RetrofitClient.getApiService().updateDiary(id,ctx); //api 콜
        update.enqueue(new AutoRetryCallback<String>() {
            @Override
            public void onFinalFailure(Call<String> call, Throwable t) {
                Log.e("일지수정 통신 연결실패", t.getMessage());
            }

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }

                if(response.body().equals("수정성공")){
                    Log.d("일지수정 통신 성공적", response.body());
                    adapter.change(position, ctx);

                }else{
                    Log.d("일지수정 통신 실패", response.body());
                    Toast.makeText(context,"수정 실패",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //삭제 통신
    public void deleteDaily(int id, int position){
        Call<String> delete = RetrofitClient.getApiService().deleteDiary(id); //api 콜
        delete.enqueue(new AutoRetryCallback<String>() {
            @Override
            public void onFinalFailure(Call<String> call, Throwable t) {
                Log.e("일지삭제 통신 연결실패", t.getMessage());
            }

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }

                if(response.body().equals("삭제성공")){
                    Log.d("일지삭제 통신 성공적", response.body());
                    adapter.delete(position);

                }else{
                    Log.d("일지삭제 통신 실패", response.body());
                    Toast.makeText(context,"삭제 실패",Toast.LENGTH_LONG).show();
                }

            }
        });
    }



    //일별 통신, 2021-05-01 (%Y-%m-%d)
    public void getTodayDaily(String today){
        Call<List<ResponseDailyMemoJson>> todayN = RetrofitClient.getApiService().getTodayDailyMemo(
                UserIdent.GetInstance().getUserIdent(),today,today); //api 콜
        todayN.enqueue(new AutoRetryCallback<List<ResponseDailyMemoJson>>() {
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
                if(response.body().isEmpty()){ //일지 내용이 없는 경우
                    adapter.clear();
                } else {
                    List<ResponseDailyMemoJson> DailyMemoJson = response.body(); //통신 결과 받기

                    //일별 정보 확인
                    Log.d("id", ""+DailyMemoJson.get(0).getDiaryId());
                    Log.d("날짜", DailyMemoJson.get(0).getDate());
                    Log.d("내용", DailyMemoJson.get(0).getContent());

                    id = null;  day = null; contents = null;
                    id = new ArrayList<>(); day = new ArrayList<>();    contents = new ArrayList<>();
                    for(int i = 0 ; i< DailyMemoJson.size() ; i++){
                        id.add(DailyMemoJson.get(i).getDiaryId());
                        day.add(DailyMemoJson.get(i).getDate());
                        contents.add(DailyMemoJson.get(i).getContent());
                    }

                    adapter.updateData(id,day,contents);
                }

                /*
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
                */

            }
        });
    }

    //주별 통신, 2021-05-02 (%Y-%m-%d), 2021-05-08 (%Y-%m-%d)
    public void getWeekDaily(String start, String end){
        Call<List<ResponseDailyMemoJson>> week = RetrofitClient.getApiService().getWeekDailyMemo(
                UserIdent.GetInstance().getUserIdent(),start,end); //api 콜
        week.enqueue(new AutoRetryCallback<List<ResponseDailyMemoJson>>() {
            @Override
            public void onFinalFailure(Call<List<ResponseDailyMemoJson>> call, Throwable t) {
                Log.e("주별 정보 연결실패", t.getMessage());
            }

            @Override
            public void onResponse(Call<List<ResponseDailyMemoJson>> call, Response<List<ResponseDailyMemoJson>> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }

                Log.d("주별 통신 성공적", response.body().toString());
                if(response.body().isEmpty()){ //일지 내용이 없는 경우
                    adapter.clear();
                } else {
                    List<ResponseDailyMemoJson> DailyMemoJson = response.body(); //통신 결과 받기

                    //일별 정보 확인
                    Log.d("id", ""+DailyMemoJson.get(0).getDiaryId());
                    Log.d("날짜", DailyMemoJson.get(0).getDate());
                    Log.d("내용", DailyMemoJson.get(0).getContent());

                    id = null;  day = null; contents = null;
                    id = new ArrayList<>(); day = new ArrayList<>();    contents = new ArrayList<>();
                    for(int i = 0 ; i< DailyMemoJson.size() ; i++){
                        id.add(DailyMemoJson.get(i).getDiaryId());
                        day.add(DailyMemoJson.get(i).getDate());
                        contents.add(DailyMemoJson.get(i).getContent());
                    }

                    adapter.updateData(id,day,contents);
                }


            }
        });
    }

    //월별 통신, 2021-05 (%Y-%m-%d)
    public void getMonthDaily(String month){
        Call<List<ResponseDailyMemoJson>> monthN = RetrofitClient.getApiService().getMonthDailyMemo(
                UserIdent.GetInstance().getUserIdent(),month); //api 콜
        monthN.enqueue(new AutoRetryCallback<List<ResponseDailyMemoJson>>() {
            @Override
            public void onFinalFailure(Call<List<ResponseDailyMemoJson>> call, Throwable t) {
                Log.e("월별 정보 연결실패", t.getMessage());
            }

            @Override
            public void onResponse(Call<List<ResponseDailyMemoJson>> call, Response<List<ResponseDailyMemoJson>> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }

                Log.d("월별 통신 성공적", response.body().toString());
                if(response.body().isEmpty()){ //일지 내용이 없는 경우
                    adapter.clear();
                } else {
                    List<ResponseDailyMemoJson> DailyMemoJson = response.body(); //통신 결과 받기

                    //일별 정보 확인
                    Log.d("id", ""+DailyMemoJson.get(0).getDiaryId());
                    Log.d("날짜", DailyMemoJson.get(0).getDate());
                    Log.d("내용", DailyMemoJson.get(0).getContent());

                    id = null;  day = null; contents = null;
                    id = new ArrayList<>(); day = new ArrayList<>();    contents = new ArrayList<>();
                    for(int i = 0 ; i< DailyMemoJson.size() ; i++){
                        id.add(DailyMemoJson.get(i).getDiaryId());
                        day.add(DailyMemoJson.get(i).getDate());
                        contents.add(DailyMemoJson.get(i).getContent());
                    }

                    adapter.updateData(id,day,contents);
                }


            }
        });
    }

    //년별 통신, 2021 (%Y)
    public void getYearDaily(String year){
        Call<List<ResponseDailyMemoJson>> yearN = RetrofitClient.getApiService().getYearDailyMemo(
                UserIdent.GetInstance().getUserIdent(),year); //api 콜
        yearN.enqueue(new AutoRetryCallback<List<ResponseDailyMemoJson>>() {
            @Override
            public void onFinalFailure(Call<List<ResponseDailyMemoJson>> call, Throwable t) {
                Log.e("년별 정보 연결실패", t.getMessage());
            }

            @Override
            public void onResponse(Call<List<ResponseDailyMemoJson>> call, Response<List<ResponseDailyMemoJson>> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }

                Log.d("년별 통신 성공적", response.body().toString());
                if(response.body().isEmpty()){ //일지 내용이 없는 경우
                    adapter.clear();
                } else {
                    List<ResponseDailyMemoJson> DailyMemoJson = response.body(); //통신 결과 받기

                    //일별 정보 확인
                    Log.d("id", ""+DailyMemoJson.get(0).getDiaryId());
                    Log.d("날짜", DailyMemoJson.get(0).getDate());
                    Log.d("내용", DailyMemoJson.get(0).getContent());

                    id = null;  day = null; contents = null;
                    id = new ArrayList<>(); day = new ArrayList<>();    contents = new ArrayList<>();
                    for(int i = 0 ; i< DailyMemoJson.size() ; i++){
                        id.add(DailyMemoJson.get(i).getDiaryId());
                        day.add(DailyMemoJson.get(i).getDate());
                        contents.add(DailyMemoJson.get(i).getContent());
                    }

                    adapter.updateData(id,day,contents);
                }


            }
        });
    }


}
