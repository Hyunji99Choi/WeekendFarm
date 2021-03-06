package com.example.edrkr.DTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retrofitIdent {
    private String path = "3.35.216.131";
    private static final retrofitIdent Instance = new retrofitIdent(); //싱글턴 문법
    public static retrofitIdent GetInstance(){return Instance;}

    public Retrofit getRetrofit(){return retrofit;}
    public RetrofitService getService(){
        return service1;
    }
//    public RetrofitService[] getServicelist(){
//        return service2;
//    }

    public void setTmp(String str){
        tmp = str;
    }
    public String getTmp(){
        String str = tmp;
     //   tmp = null;
        return str;
    }
    private Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://"+path+":3000/") //base Url 반드시 '/'로 마무리 해야함.
            .addConverterFactory(GsonConverterFactory.create(gson)) //json을 변환시켜줄 Gson 변환기 등록
            .build();

    private RetrofitService service1 = retrofit.create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
//    private RetrofitService[] service2 = retrofit.create(RetrofitService[].class); //레트로핏 인스턴스로 인터페이스 객체 구현

    private String tmp = null;
}
