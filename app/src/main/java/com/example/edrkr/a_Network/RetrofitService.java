package com.example.edrkr.a_Network;

import com.example.edrkr.a_Network.Class.GetBoard;
import com.example.edrkr.a_Network.Class.GetEachBoard;
import com.example.edrkr.a_Network.Class.PatchBoard;
import com.example.edrkr.a_Network.Class.PostComment;
import com.example.edrkr.a_Network.Class.PostBoard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {

    //@GET( EndPoint - 자원위치(URL))
    @GET("{post}") //처음 글 로딩 부분
    Call<List<GetBoard>> getBoard(@Path(value = "post", encoded = true) String post); //응답이 왔을 떄 CallBack으로 불려질 타입

    @GET("{post}") //각 게시글 보는 부분
    Call<GetEachBoard> getComment(@Path(value = "post", encoded = true) String post);

    @POST("{post}") //글쓰기 부분 call
    Call<PostBoard> postData(@Path(value="post",encoded = true) String post, @Body PostBoard param);

    @POST("{post}") //댓글 쓰기 부분 call
    Call<PostComment> postComment(@Path(value = "post",encoded = true) String post, @Body PostComment comment);

    @DELETE("{post}") //게시글 삭제 부분
    Call<Void> deleteBoard(@Path(value = "post",encoded = true)String post);

    @PATCH("{post}") //게시글 수정부분
    Call<PatchBoard> patchBoard(@Path(value = "post",encoded = true)String post, @Body PatchBoard patch);

    @DELETE("{post}") //댓글 삭제 부분
    Call<Void> deleteComment(@Path(value = "post",encoded = true)String post);

}
