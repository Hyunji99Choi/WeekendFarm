package com.example.edrkr.mainpage;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.edrkr.R;
import com.example.edrkr.UserIdent;
import com.example.edrkr.h_network.AutoRetryCallback;
import com.example.edrkr.h_network.ResponseGraphJson;
import com.example.edrkr.h_network.RetrofitClient;


import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class sub_page2 extends Fragment {

    sub_Page2_PlaceholderFragment graphFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.mainpage_sub_page2,container,false);

        ControlMonitoring.GetInstance().setSubpage2(this); // 싱글턴에 객체 저장.

        graphFragment = new sub_Page2_PlaceholderFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, graphFragment).commit();

        }


        //그래프 값 통신
        getGreahData();

        return view;

    }

    public void getGreahData(){
        Call<List<ResponseGraphJson>> graph = RetrofitClient.getApiService()
                .getGraph(String.valueOf(UserIdent.GetInstance().getNowMontriongFarm())); //api 콜
        graph.enqueue(new AutoRetryCallback<List<ResponseGraphJson>>() {
            @Override
            public void onFinalFailure(Call<List<ResponseGraphJson>> call, Throwable t) {
                Log.e("그래프 정보 연결실패", t.getMessage());
            }

            @Override
            public void onResponse(Call<List<ResponseGraphJson>> call, Response<List<ResponseGraphJson>> response) {
                if(!response.isSuccessful()){
                    Log.e("그래프 연결이 비정상적", "error code : " + response.code());
                    return;
                }

                Log.d("그래프 통신 성공적", response.body().toString());
                List<ResponseGraphJson> graphJsons = response.body(); //통신 결과 받기

                //그래프 정보로 세팅
                Log.d("날짜", graphJsons.get(0).getDate()+" "+graphJsons.get(1).getDate()
                        +" "+graphJsons.get(2).getDate()+" "+graphJsons.get(3).getDate()
                        +" "+graphJsons.get(4).getDate()+" "+graphJsons.get(5).getDate()
                        +" "+graphJsons.get(6).getDate());
                Log.d("평균", graphJsons.get(0).getSoilavg()+" "+graphJsons.get(1).getSoilavg()
                        +" "+graphJsons.get(2).getSoilavg()+" "+graphJsons.get(3).getSoilavg()
                        +" "+graphJsons.get(4).getSoilavg()+" "+graphJsons.get(5).getSoilavg()
                        +" "+graphJsons.get(6).getSoilavg());


                //문자열 자르기
                String[] oneWeek = new String[7];
                //값 float 형식으로 바꾸기
                float[] oneWeekData = new float[7];
                for(int i=0 ;i<7 ;i++){
                    String[] weekDate = graphJsons.get(i).getDate().split("-");
                    oneWeek[i]=weekDate[1]+"/"+weekDate[2]; // 0/0 날짜 포맷

                    oneWeekData[i] = Float.parseFloat(graphJsons.get(i).getSoilavg()); //평균
                }


                Log.d("날짜 포맷", oneWeek[0]);
                graphFragment.setOneWeek(oneWeek,oneWeekData); // 일주일 x 좌표
                //값 세팅 하기


            }
        });
    }




}
