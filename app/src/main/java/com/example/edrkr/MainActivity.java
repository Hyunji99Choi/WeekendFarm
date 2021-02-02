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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText idInput, passwordInput;
    CheckBox autoLogin;
    //Boolean loginChecked;
    SharedPreferences autoLoginData;
    SharedPreferences.Editor editor;

    Button loginButton;
    Button signupButton;

    public String login_URL="http://3.35.55.9:3000/users/login";
    private String UserData_URL="http://3.35.55.9:3000/";

    String UserID="ghd";
    String UserPW="1234";

    String NetworkRESULT=null;
    String userIdentResult=null;


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

                        UserID=idInput.getText().toString();
                        UserPW=passwordInput.getText().toString();

                        if(UserID.getBytes().length<=0||UserPW.getBytes().length<=0){
                            Toast.makeText(getApplicationContext(), "로그인 정보를 입력해주세요", Toast.LENGTH_LONG).show();
                            return;
                        }

                        //*********************************************
                        //디비그를 위한 로컬 진입 **** 나중에 지워줘야함
                        if(UserID.equals("111")&&UserPW.equals("111")){
                            //무명 회원 (로컬) 회원가입
                            UserIdent.GetInstance().settingNothing();

                            Intent intent1 = new Intent(MainActivity.this, MonitoringPage.class);
                            startActivity(intent1);
                            finish();

                            return;

                        }
                        //*********************************************

                        LoginNetwork(UserID,UserPW); //로그인 시도

                        /*
                        //예전 통신
                        ContentValues values = new ContentValues();
                        values.put("id",UserID);
                        values.put("pw",UserPW);

                        NetworkTask Login_networkTask = new NetworkTask(login_URL,values);

                        //로그인 여부 확인 통신

                        try {
                            NetworkRESULT=Login_networkTask.execute().get();    //네트워크 통신(동기)
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(NetworkRESULT==null){
                            Log.w("로그인 시도","실패 : "+NetworkRESULT);
                            Toast.makeText(getApplicationContext(), "인터넷 연결 불안정", Toast.LENGTH_LONG).show();

                        } else if(NetworkRESULT.equals("성공")){
                            Log.w("result login페이지",""+Login_networkTask.result);

                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                            NetworkTask UserData_networkTask=new NetworkTask(UserData_URL+UserID,null);// 고쳐야함

                            // 회원 정보 요청 통신
                            try {
                                userIdentResult=UserData_networkTask.execute().get(); //네트워크 통신(동기)
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if(userIdentResult==null){
                                Log.w("회원정보요청 통신","실패 : "+userIdentResult);
                                Toast.makeText(getApplicationContext(), "인터넷 연결 불안정", Toast.LENGTH_LONG).show();
                            }else{
                                Log.w("회원정보","싱글턴 저장 중");
                                try {
                                    JSONObject USER = new JSONObject(userIdentResult);
                                    UserIdent.GetInstance().setJSONUserIdent(USER);
                                    UserIdent.GetInstance().printLog();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            UserIdent.GetInstance().setId(UserID);
                            UserIdent.GetInstance().setPw(UserPW);

                            Intent intent1 = new Intent(MainActivity.this, MonitoringPage.class);
                            startActivity(intent1);
                            finish();

                        }else if(NetworkRESULT.equals("실패")){
                            Log.w("로그인 시도","잘못된 정보 : "+NetworkRESULT);
                            Toast.makeText(getApplicationContext(), "잘못된 로그인 정보", Toast.LENGTH_LONG).show();
                        }else{ //중복 코드. 나중에 고치기
                            Log.w("로그인 시도","실패 : "+NetworkRESULT);
                            Toast.makeText(getApplicationContext(), "인터넷 연결 불안정", Toast.LENGTH_LONG).show();
                        }

                        */

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

    //로그인 통신
    private void LoginNetwork(String id,String pw){
        Call<String> login = RetrofitClient.getApiService().getLoginCheck(id,pw); //api 콜
        login.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }

                //통신 성공적
                if(response.body().equals("성공")){
                    //성공적인 로그인
                    Log.d("로그인 성공적", response.body());
                    getUserIdnet(id); //로그인 유저 정보 수집
                }else{
                    //아이디, 비번 잘못됨.
                    Log.d("정보가 없는 회원", response.body());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

    }

    //로그인 성공시 유저 정보 접근
    private void getUserIdnet(String id){
        Call<ResponseUserIdent> ident = RetrofitClient.getApiService().getUserIdent(id); //api 콜
        ident.enqueue(new Callback<ResponseUserIdent>() {
            @Override
            public void onResponse(Call<ResponseUserIdent> call, Response<ResponseUserIdent> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }

                Log.d("유저 정보 접근이 성공적", response.body().toString());
                ResponseUserIdent userIdent = response.body(); //통신 결과 받기

                //싱글턴 저장
                UserIdent.GetInstance().setResponseUserIdent(userIdent);
                UserIdent.GetInstance().setId(UserID);
                UserIdent.GetInstance().setPw(UserPW);

                UserIdent.GetInstance().printLog(); //확인

                /*
                Intent intent1 = new Intent(MainActivity.this, MonitoringPage.class);
                startActivity(intent1);
                finish();
                */

            }//통신 실패나 로그아웃하면 싱글턴 비우는 문법 추가하기???

            @Override
            public void onFailure(Call<ResponseUserIdent> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }

        });


    }


}
