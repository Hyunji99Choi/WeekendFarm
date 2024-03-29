package com.example.edrkr.a_Network;

import android.util.Log;

import com.example.edrkr.a_Network.Class.Post;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class Builder {
    private String tag = "areum/Builder";
    private String str;

    public void tryPost(Call call) {
        Log.v(tag, "tryPost 진입");
        call.enqueue(new Callback<Post>() { //비동기 작업
            @Override
            public void onResponse(@EverythingIsNonNull Call<Post> call,@EverythingIsNonNull  Response<Post> response) { //성공 - 메인 스레드에서 처리
                if (response.isSuccessful()) {
                    //정상적으로 통신이 성공한 경우
                    Log.v(tag, "onResponse: 성공, 결과\n" + response.body().toString());
                } else {
                    //통신이 실패한 경우(응답코드 3xx,4xx 등)
                    str = "onResponse: 실패";
                    Log.d(tag, str);
                }
            }

            @Override
            public void onFailure(@EverythingIsNonNull Call<Post> call,@EverythingIsNonNull  Throwable t) { //실패 - 메인 스레드에서 처리
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적인 이유)
                str = t.getMessage();
                Log.d(tag, "onFailure: " + str);
            }
        });
    }
}

