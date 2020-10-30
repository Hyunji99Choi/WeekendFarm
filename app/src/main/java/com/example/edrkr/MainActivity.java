package com.example.edrkr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText idInput, passwordInput;
    CheckBox autoLogin;
    //Boolean loginChecked;
    SharedPreferences autoLoginData;
    SharedPreferences.Editor editor;

    Button loginButton;
    Button signupButton;

    public String login_URL="http://192.168.43.10:3000/users/login";
    private String UserData_URL="http://192.168.43.10:3000/manage/";

    String UserID="ghd";
    String UserPW="1234";

    String NetworkRESULT=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeView();
        SetListener();



    }

    //xml id랑 연결
    public void InitializeView(){

        idInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        autoLogin = (CheckBox) findViewById(R.id.checkBox);

        loginButton=(Button)findViewById(R.id.loginButton);
        signupButton=(Button)findViewById(R.id.signupButton);

        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("로그인"); // 툴바 이름 변경
    }

    //리스너 연결
    private void SetListener() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.loginButton:
                        Toast.makeText(getApplicationContext(),"로그인 버튼 클릭",Toast.LENGTH_LONG).show();

                        UserID=idInput.getText().toString();
                        UserPW=passwordInput.getText().toString();

                        ContentValues values = new ContentValues();
                        values.put("id",UserID);
                        values.put("pw",UserPW);

                        NetworkTask Login_networkTask = new NetworkTask(login_URL,values);


                        try {
                            NetworkRESULT=Login_networkTask.execute().get();    //네트워크 통신(동기)
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }



                        if(NetworkRESULT.equals("성공")){
                            Log.w("로그인 시도","성공");
                            //쓰레드 문제 고민해보기
                            Log.w("result login페이지",""+Login_networkTask.result);

                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                            NetworkTask UserData_networkTask=new NetworkTask(UserData_URL+UserID,null);// 고쳐야함
                            UserData_networkTask.execute();

                            //값 넘겨주는 문제 고민해보기
                            Intent intent1 = new Intent(MainActivity.this, MonitoringPage.class);
                            startActivity(intent1);
                            finish();

                        }else if(NetworkRESULT.equals("실패")){
                            Log.w("로그인 시도","잘못된 정보 : "+NetworkRESULT);
                            Toast.makeText(getApplicationContext(), "잘못된 로그인 정보", Toast.LENGTH_LONG).show();
                        }else{
                            Log.w("로그인 시도","실패 : "+NetworkRESULT);
                            Toast.makeText(getApplicationContext(), "인터넷 연결 불안정", Toast.LENGTH_LONG).show();
                        }


                        //지워야하는 부분...
                        //페이지 넘어가기
                        //Intent intent1 = new Intent(MainActivity.this, MonitoringPage.class);
                        //startActivity(intent1);
                        //뒤로 버튼 나오면 바로 종료
                        //finish();

                        break;
                    case R.id.signupButton:
                        Toast.makeText(getApplicationContext(),"회원가입 버튼 클릭",Toast.LENGTH_LONG).show();

                        //페이지 넘어가기
                        Intent intent2 = new Intent(MainActivity.this, login_SingUpPage.class);
                        startActivity(intent2);
                        break;
                }
            }
        };
        loginButton.setOnClickListener(listener);
        signupButton.setOnClickListener(listener);




    }


}
