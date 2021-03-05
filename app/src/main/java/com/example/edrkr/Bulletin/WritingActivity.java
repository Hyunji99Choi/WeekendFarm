package com.example.edrkr.Bulletin;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edrkr.Bulletin.Board;
import com.example.edrkr.DTO.Builder;
import com.example.edrkr.DTO.PostWriting;
import com.example.edrkr.DTO.retrofitIdent;
import com.example.edrkr.NetworkTask;
import com.example.edrkr.R;
import com.example.edrkr.UserIdent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;


public class WritingActivity extends AppCompatActivity {

    private EditText title;
    private EditText body;
    private Button buttonsend;
    private ActionBar actionBar;
    private String URL = "http://15.165.74.84:3000/forum/create"; //url부분
    private String TAG = "areum/Writingactivity"; //태그


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        this.InitializeView(); //필요 요소 선언해주는 함수
    }


    public void InitializeView() { //id 연결 함수
        title = (EditText) findViewById((R.id.title));
        body = (EditText) findViewById(R.id.body);
      //  buttonsend = (Button) findViewById(R.id.buttonSend);


        Toolbar toolbar = findViewById(R.id.toolbar_writing);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); //기존 타이틀 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goout); //뒤로가기 버튼 이미지
    }

    public void posttoserver(Board b){ //retrofit2를 사용하여 서버로 보내는 코드
        Log.v(TAG,"posttoserver 진입완료");
        PostWriting post = new PostWriting();
        post.setName(b.getName());
        post.setTitle(b.getTitle());
        post.setContent(b.getBody());
        Log.v(TAG,"put 완료");

        Call<PostWriting> call = retrofitIdent.GetInstance().getService().postData("forum/create", post);
        Builder builder = new Builder();
        try {
            builder.tryConnect(TAG, call);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.v(TAG,"tryconnect 완료");
    }

    public void sendtoserver(Board b) { //서버로 보내는 코드

        ContentValues values = new ContentValues();
        Log.v(TAG,"server 확인");
        ArrayList<Board> dataset = new ArrayList<Board>();

        //values.put("id",b.getPos());
        values.put("name",b.getName());
        values.put("title",b.getTitle());
        values.put("content",b.getBody());
        Log.v("semdtoserver","put 완료");

        NetworkTask sendboard_networkTask = new NetworkTask(URL,values); //networktast 설정 부분
        try {
            sendboard_networkTask.execute().get(); //설정한 networktask 실행
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result = sendboard_networkTask.result;
        Log.v("result : ",result);
    }

    public void localsend(Board b){
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
                //현재 값을 저장
                Board b = new Board();
                b.setName(UserIdent.GetInstance().getNkname());
                b.setTitle(title.getText().toString());
                b.setBody(body.getText().toString());
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String date_ = new SimpleDateFormat("yyyy년 MM월 dd일  HH:mm").format(date);
                b.setDate(date_);
                Log.v(TAG, "현재 값 저장완료");

                posttoserver(b);

                Intent intent = getIntent();

                // localsend(b);
                setResult(1, intent);
                finish();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_writing,menu);

        return true;
    }
}