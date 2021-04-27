package com.example.edrkr.bulletinPage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.edrkr.a_Network.Builder;
import com.example.edrkr.a_Network.Class.GetBoard;
import com.example.edrkr.a_Network.Class.GetComment;
import com.example.edrkr.a_Network.Class.GetEachBoard;
import com.example.edrkr.a_Network.Class.PatchBoard;
import com.example.edrkr.a_Network.Class.PostBoard;
import com.example.edrkr.a_Network.RetrofitService;
import com.example.edrkr.a_Network.retrofitIdent;
import com.example.edrkr.R;
import com.example.edrkr.UserIdent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//확인 필요
public class WritingActivity extends AppCompatActivity {

    private EditText title;
    private EditText body;
    private ActionBar actionBar;
    private int type;
    private String TAG = "areum/Writingactivity"; //태그
    private int pos;
    private String URL = "forum/";
    private Board b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletinpage_writing);
        this.InitializeView(); //필요 요소 선언해주는 함수
    }


    public void InitializeView() { //id 연결 함수
        Log.v(TAG,"initialize");
        Intent intent = getIntent();
        b = (Board) intent.getSerializableExtra("board");
        type = intent.getIntExtra("type",0);
        title = (EditText) findViewById((R.id.title));
        body = (EditText) findViewById(R.id.body);

        Toolbar toolbar = findViewById(R.id.toolbar_writing);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); //기존 타이틀 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goout); //뒤로가기 버튼 이미지

        if(type == 1){
            pos = intent.getIntExtra("pos",-1);
            getBoardData();
        }
    }

    public void getBoardData(){ //서버에서 게시글 데이터 가져오는 함수
        final ArrayList<Comment> dataset = new ArrayList<>();

        Log.v(TAG,"getBoardData 진입완료");

        //레트로핏 통신 기다리게 바꾸기
        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.getComment(URL+pos).enqueue(new Callback<GetEachBoard>() {
            @Override
            public void onResponse(Call<GetEachBoard> call, Response<GetEachBoard> response) { //통신 성공시
                if(response.isSuccessful()){
                    Log.v(TAG,response.body().toString());
                    GetEachBoard datas = response.body();
                    List<GetBoard> board = datas.getPost(); //게시글 부분
                    if(board.size()!=0) { //게시글 부분 가져오는 코드
                        Log.v(TAG,"board가 null이 아님");
                        title.setText(board.get(0).getTitle());
                        body.setText( board.get(0).getBody());
                        Log.v(TAG,"title : "+board.get(0).getTitle()+" body : "+ board.get(0).getBody());
                    }
                    else{
                        Log.v(TAG,"board size 0");
                    }
                }else{ //통신은 성공 but, 내부에서 실패
                    Log.v(TAG, "onResponse: 실패");
                }
            }
            @Override
            public void onFailure(Call<GetEachBoard> call, Throwable t) { //통신 아예 실패
                Log.v(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void posttoserver(Board b){ //retrofit2를 사용하여 서버로 보내는 코드
        Log.v(TAG,"posttoserver 진입완료");
        PostBoard post = new PostBoard();
        post.setNickname(b.getName());
        post.setTitle(b.getTitle());
        post.setContent(b.getBody());
        post.setUserIdent(UserIdent.GetInstance().getUserIdent());
        Log.v(TAG,"put 완료");

        Call<PostBoard> call = retrofitIdent.GetInstance().getService().postData("forum/", post);
        Builder builder = new Builder();
        try {
            builder.tryPost(call);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.v(TAG,"tryconnect 완료");
    }

    public void localsend(Board b){ //로컬로 데이터셋 지정
        //값을 notice로 넘기기 위한 작업
        Intent intent = new Intent();
        intent.putExtra("Board", b);
        intent.putExtra("chat_count", 0);
        Log.v(TAG, "intent에 저장 완료");

        //페이지 변경
        setResult(2, intent);
        Log.v(TAG, "intent 전송 완료");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home: { //뒤로가기 버튼 클릭시
                finish();
                return true;
            }
            case R.id.writing_next_button:{ //오른쪽 상단 확인버튼 클릭시
                Log.v(TAG, "전송 버튼 눌림");
                if(TextUtils.isEmpty(title.getText().toString()) ||TextUtils.isEmpty(body.getText().toString())){
                    Log.v(TAG,"내용이 빔");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder((this));
                    alertDialogBuilder.setMessage("제목과 내용을 입력해주세요");
                    alertDialogBuilder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return true;
                }
                //현재 값을 저장
                b.setName(UserIdent.GetInstance().getNkname());
                b.setTitle(title.getText().toString());
                b.setBody(body.getText().toString());
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String date_ = new SimpleDateFormat("yyyy년 MM월 dd일  HH:mm").format(date);
                b.setDate(date_);
                Log.v(TAG, "현재 값 저장완료");
                if(type == 0){
                    posttoserver(b);
                }else if(type == 1){
                    patchtoserver(b);
                }

                Intent intent = getIntent();

                // localsend(b);
                setResult(1, intent);
                finish();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void patchtoserver(Board b) {
        Log.v(TAG,"patchtoserver 진입완료");
        PatchBoard post = new PatchBoard();
        post.setTitle(b.getTitle());
        post.setContent(b.getBody());
        Log.v(TAG,"put 완료");

        if(pos != -1) {
            Call<PatchBoard> call = retrofitIdent.GetInstance().getService().patchBoard("forum/"+pos, post);
            Builder builder = new Builder();
            try {
                builder.tryPost(call);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.v(TAG, "tryconnect 완료");
        }else{
            Toast.makeText(this,"잠시후에 다시 시도해주세요",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_writing,menu);
        return true;
    }
}