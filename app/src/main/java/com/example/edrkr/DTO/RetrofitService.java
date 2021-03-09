package com.example.edrkr.DTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {

    //@GET( EndPoint - 자원위치(URL))
    @GET("{post}") //처음 글 로딩 부분
    Call<List<GetResult>> getBoard(@Path(value = "post", encoded = true) String post); //응답이 왔을 떄 CallBack으로 불려질 타입

    @GET("{post}") //각 게시글 보는 부분
    Call<GetEachBoard> getComment(@Path(value = "post", encoded = true) String post);

    @POST("{post}") //글쓰기 부분 call
    Call<PostWriting> postData(@Path(value="post",encoded = true) String post, @Body PostWriting param);

    @POST("{post}") //댓글 쓰기 부분 call
    Call<PostComment> postComment(@Path(value = "post",encoded = true) String post, @Body PostComment comment);
}
