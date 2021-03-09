package com.example.edrkr.DTO;

import android.util.Log;

import com.example.edrkr.Bulletin.Board;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Builder {
    private String tag = "areum/Builder";
    private String str;

    public void tryConnect(final String TAG, Call call) {
        Log.v(tag, "tryconnect 진입");
        call.enqueue(new Callback<Post>() { //비동기 작업
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) { //성공 - 메인 스레드에서 처리
                if (response.isSuccessful()) {
                    //정상적으로 통신이 성공한 경우
                    Log.v(TAG, "onResponse: 성공, 결과\n" + response.body().toString());
//                    PostResult result = response.body();
//                    str = result.toString();
//                    retrofitIdent.GetInstance().setTmp(str);
//                    Log.d(TAG,"onResponse: 성공, 결과\n"+ str);
                } else {
                    //통신이 실패한 경우(응답코드 3xx,4xx 등)
                    str = "onResponse: 실패";
//                    retrofitIdent.GetInstance().setTmp(str);
                    Log.d(TAG, str);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) { //실패 - 메인 스레드에서 처리
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적인 이유)
                str = t.getMessage();
                Log.d(TAG, "onFailure: " + str);
            }
        });
//        str = retrofitIdent.GetInstance().getTmp();
//        Log.d(TAG+"/"+tag,"결과 : " + str);
//        return str;
    }

    public ArrayList<Board> listtryConnect(final String TAG, Call call) {
        final ArrayList<Board> dataset = new ArrayList<Board>();
        Log.v(tag, "listtryconnect 진입");
        final ArrayList<GetResult> data = new ArrayList<>();
        call.enqueue(new Callback<List<Post>>() { //비동기 작업
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) { //성공 - 메인 스레드에서 처리
                if (response.isSuccessful()) {
                    //정상적으로 통신이 성공한 경우
                    List<Post> post = response.body();
                    for(int i=0;i<post.size();i++){
                        GetResult p = (GetResult) post.get(i);
//                        Log.v(TAG, "for "+i+": "+post.get(i).toString());
                        Board b = new Board(p.getId(),p.getName(),p.getTitle(),p.getBody(),p.getCommentNum(),p.getTime());
                        Board tmp = dataset.get(i);
                        Log.v(tag,tmp.getPos()+", "+tmp.getName()+", "+tmp.getTitle()+", "+tmp.getBody()+", "+tmp.getChat_count()+", "+tmp.getDate());
                    }
                    Log.v(TAG, "onResponse: 성공, 결과\n" + post.toString());
                } else {
                    //통신이 실패한 경우(응답코드 3xx,4xx 등)
                    Log.d(TAG, "onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) { //실패 - 메인 스레드에서 처리
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적인 이유)
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        Log.v(TAG,"함수 빠져나옴");
        Board tmp = dataset.get(1);
        Log.v(TAG,tmp.getPos()+", "+tmp.getName()+", "+tmp.getTitle()+", "+tmp.getBody()+", "+tmp.getChat_count()+", "+tmp.getDate());
        Log.v(TAG,"dataset 전송");
        return dataset;
    }
}
