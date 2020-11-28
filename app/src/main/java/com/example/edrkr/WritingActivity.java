package com.example.edrkr;

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
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class WritingActivity extends AppCompatActivity {

    private EditText title;
    private EditText body;
    private Button buttonsend;
    private ActionBar actionBar;
    private String URL = "http://3.35.55.9:3000/forum/create"; //url부분


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        this.InitializeView(); //필요 요소 선언해주는 함수
        this.SetListener(); //리스너 설정 함수
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

    public void SetListener() {
        //inputMethodManger 객체 선언
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
//                    case R.id.buttonSend:
//                        Log.v("알림", "전송 버튼 눌림");
//                        //현재 값을 저장
//                        Board b = new Board();
//                        b.setName(UserIdent.GetInstance().getNkname());
//                        b.setTitle(title.getText().toString());
//                        b.setBody(body.getText().toString());
//                        long now = System.currentTimeMillis();
//                        Date date = new Date(now);
//                        String date_ = new SimpleDateFormat("yyyy년 MM월 dd일  HH:mm").format(date);
//                        b.setDate(date_);
//                        Log.v("알림", "현재 값 저장완료");
//
//                        sendtoserver(b);
//
//                        Intent intent = getIntent();
//
//                        // localsend(b);
//                        // 페이지 변경
//                        //Intent intent = new Intent();
//                        //페이지 변경
//                        setResult(1, intent);
//                        finish();

                }
            }
        };
        //buttonsend.setOnClickListener(Listener);
    }

    public void sendtoserver(Board b) { //서버로 보내는 코드

        ContentValues values = new ContentValues();
        Log.v("알림","server 확인");
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
        Log.v("알림", "intent에 저장 완료");

        //페이지 변경
        setResult(2, intent);
        Log.v("알림", "intent 전송 완료");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home: { //뒤로가기 버튼 클릭시
                finish();
                return true;
            }
            case R.id.writing_next_button:{ //오른쪽 상단 확인버튼 클릭시
                Log.v("알림", "전송 버튼 눌림");
                //현재 값을 저장
                Board b = new Board();
                b.setName(UserIdent.GetInstance().getNkname());
                b.setTitle(title.getText().toString());
                b.setBody(body.getText().toString());
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String date_ = new SimpleDateFormat("yyyy년 MM월 dd일  HH:mm").format(date);
                b.setDate(date_);
                Log.v("알림", "현재 값 저장완료");

                sendtoserver(b);

                Intent intent = getIntent();

                // localsend(b);
                // 페이지 변경
                //Intent intent = new Intent();
                //페이지 변경
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