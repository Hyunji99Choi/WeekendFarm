package com.example.edrkr.h_network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    @POST("users/signUp") //회원가입 승인
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

    //날씨 통신
    @GET("wheather")
    Call<ResponseWeatherJson> getWheather();

    //센서 통신
    @GET("sensor/field{farm}") //로그인 후 회원정보 요청
    Call<ResponseSensorJson> getSensor(@Path("farm") String farm);

    //cctv 통신
    @GET("camera/{Farmid}") //로그인 후 회원정보 요청
    Call<ResponseCCTVJson> getCCTV(@Path("Farmid") String Farmid);

    //일주일 그래프 통신
    @GET("sensor/soilavg{farmID}")
    Call<List<ResponseGraphJson>> getGraph(@Path("farmID") String farmID);


    //일지 관련 통신

    //일지 작성
    @FormUrlEncoded
    @POST("diary")
    Call<String> setDiary(
            @Field("userIdent") int userIdent,
            @Field("content") String content
    );

    //일지 수정
    @FormUrlEncoded
    @PATCH("diary") //게시글 수정부분
    Call<String> updateDiary(
            @Query("id") int id,
            @Field("content") String content
    );
    //일지 삭제
    @DELETE("diary")
    Call<String> deleteDiary(
            @Query("id") int id
    );

    //일별 일지
    @GET("diary")
    Call<List<ResponseDailyMemoJson>> getTodayDailyMemo(
            @Query("userIdent") int userIdent,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );

    //주별 일지
    @GET("diary")
    Call<List<ResponseDailyMemoJson>> getWeekDailyMemo(
            @Query("userIdent") int userIdent,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate
    );

    //월별 일지
    @GET("diary/month")
    Call<List<ResponseDailyMemoJson>> getMonthDailyMemo(
            @Query("userIdent") int userIdent,
            @Query("month") String month
    );

    //년별 일지
    @GET("diary/year")
    Call<List<ResponseDailyMemoJson>> getYearDailyMemo(
            @Query("userIdent") int userIdent,
            @Query("year") String year
    );


    //서브 페이지

    //회원정보 수정
    @PATCH("users/info")
    Call<String> updateUserIdent(
            @Query("UserIdent") String UserIdent,
            @Body RequestUpdateUser requestUpdateUser
    );


    //새로운 밭의 키 값 생성, email 전송
    @FormUrlEncoded
    @POST("manage/newBie")
    Call<String> getKeyCreat(
            @Field("farm") int[] farm, //밭 아이디 순
            @Field("email") String email
    );
    //key 생성, email 전송 안할시
    @FormUrlEncoded
    @POST("manage/newBie")
    Call<String> getKeyCreat(
            @Field("farm") int[] farm //밭 아이디 순

    );






    //싱싱정보통 병합
    //이민영
    @GET("api/variety") //싱싱정보통 요청
    Call<String> getInfo();//String으로 오더라....
    // Call<List<ResponseShingShingInfo>> getInfo();



}
