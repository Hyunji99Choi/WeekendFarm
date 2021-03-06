package com.example.edrkr.h_network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitAPI {

    //로그인 페이지
    @FormUrlEncoded
    @POST("users/login") //로그인
    Call<String> getLoginCheck(
            @Field("id") String id,
            @Field("pw") String pw
    );
    @GET("{userId}") //로그인 후 회원정보 요청
    Call<ResponseUserIdent> getUserIdent(@Path("userId") String userId);


    //회원가입 페이지
    @FormUrlEncoded
    @POST("users/search/ID") //아이디 중복 확인
    Call<String> registerIdCheck(
            @Field("id") String id
    );
    @FormUrlEncoded
    @POST("users/search/NickName") //별명 중복 확인
    Call<String> registerNickNameCheck(
            @Field("nickname") String nickname
    );

    @FormUrlEncoded
    @POST("users/") //회원가입 승인
    Call<String> registerLogin(
            @Field("id") String id,
            @Field("pw") String pw,
            @Field("name") String name,
            @Field("nickname") String nickname,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("key") String key
    );


    //모니터링 페이지

    //센서 통신
    @GET("/sensor/field{farm}") //로그인 후 회원정보 요청
    Call<ResponseSensorJson> getSensor(@Path("farm") String farm);

    //cctv 통신
    @GET("/camera/{farm}") //로그인 후 회원정보 요청
    Call<ResponseCCTVJson> getCCTV(@Path("farm") String farm);


}
