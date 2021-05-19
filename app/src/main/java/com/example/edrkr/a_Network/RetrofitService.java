package com.example.edrkr.a_Network;

import com.example.edrkr.a_Network.Class.bulletin.GetBoard;
import com.example.edrkr.a_Network.Class.bulletin.GetEachBoard;
import com.example.edrkr.a_Network.Class.bulletin.PatchBoard;
import com.example.edrkr.a_Network.Class.bulletin.PatchComment;
import com.example.edrkr.a_Network.Class.bulletin.PostComment;
import com.example.edrkr.a_Network.Class.bulletin.PostBoard;
import com.example.edrkr.a_Network.Class.manager.GetAllFarm;
import com.example.edrkr.a_Network.Class.manager.GetAllMember;
import com.example.edrkr.a_Network.Class.manager.GetUserEachFarm;
import com.example.edrkr.a_Network.Class.manager.inputFarm;
import com.example.edrkr.a_Network.Class.manager.inputUser;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    //게시판 부분
    //@GET( EndPoint - 자원위치(URL))
    @GET("{post}") //처음 글 로딩 부분
    Call<List<GetBoard>> getBoard(@Path(value = "post", encoded = true) String post); //응답이 왔을 떄 CallBack으로 불려질 타입

    @GET("forum") //키워드 서치
    Call<List<GetBoard>> getSearchBoard(@Query("keyword") String keyword);

    @GET("{post}") //각 게시글 보는 부분
    Call<GetEachBoard> getComment(@Path(value = "post", encoded = true) String post);

    @POST("{post}") //글쓰기 부분 call
    Call<PostBoard> postData(@Path(value="post",encoded = true) String post, @Body PostBoard param);

    @POST("{post}") //댓글 쓰기 부분 call
    Call<PostComment> postComment(@Path(value = "post",encoded = true) String post, @Body PostComment comment);

    @Multipart
    @POST("image") //이미지 통신 시도해볼 녀석 <--------------------------------------------
//    Call<String> request(@Part MultipartBody.Part image);
    Call<String> request(@Part("nickname") String post,
                         @Part("userIdent") int userIdent,
                         @Part("title") String title,
                         @Part("content") String content,
                         @Part MultipartBody.Part image);

    @DELETE("{post}") //게시글 삭제 부분
    Call<Void> deleteBoard(@Path(value = "post",encoded = true)String post);

    @PATCH("{post}") //게시글 수정부분
    Call<PatchBoard> patchBoard(@Path(value = "post",encoded = true)String post, @Body PatchBoard patch);

    @DELETE("{post}") //댓글 삭제 부분
    Call<Void> deleteComment(@Path(value = "post",encoded = true)String post);

    @PATCH("{post}") //댓글 수정부분
    Call<PatchComment> patchComment(@Path(value = "post",encoded = true)String post, @Body PatchComment patch);


    //관리자 페이지
    @GET("{post}") //모든 유저 정보
    Call<List<GetAllMember>> getAllUser(@Path(value = "post", encoded = true) String post);

    @GET("{get}") //모든 밭 별명 정보
    Call<List<GetAllFarm>> getAllArea(@Path(value = "get", encoded = true) String post);

    @GET("manage/eachFarm") //밭별 사용자 정보
    Call<List<GetAllMember>> getEachFarmUser(@Query("FarmId") String keyword);

    @GET("manage/eachUser") //사용자별 밭 정보
    Call<GetUserEachFarm> getUserEachFarm(@Query("UserIdent") String keyword);

    @GET("manage/notUsingFarm") //해당 사용자가 사용하지 않는 밭 리스트 - 추가시 보여지는 리스트
    Call<List<GetAllFarm>> getListofAddFarm(@Query("UserIdent") String keyword);

    @GET("manage/notUsingUser") //해당 사용자가 사용하지 않는 밭 유저 - 추가시 보여지는 리스트
    Call<List<GetAllMember>> getListofAddUser(@Query("FarmNum") String keyword);

    @GET("/manage/resetPw") //pw 변경 0000으로
    Call<Void> changeUserPW(@Query("UserIdent") String keyword);

    @POST("manage/eachFarm") //밭별 사용자추가
    Call<String> PostAddNewUser(@Query("FarmId") String post, @Body inputUser inputUser);

    @POST("manage/eachUser") //사용자별 밭 추가
    Call<String> PostAddNewFarm(@Query("UserIdent") String post, @Body inputFarm inputFarm);

    @DELETE("manage/eachUser") //밭별 사용자 & 사용자별 밭 삭제
    Call<Void> deletFarmUser(@Query("UserIdent") String post, @Query("InputFarm") String post2);
}
//https://futurestud.io/tutorials/retrofit-2-how-to-use-dynamic-urls-for-requests - url 여기참고
//https://landroid.tistory.com/6