package com.example.edrkr.firstpage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.edrkr.R;
import com.example.edrkr.UserIdent;
import com.example.edrkr.h_network.AutoRetryCallback;
import com.example.edrkr.h_network.ResponseUserIdent;
import com.example.edrkr.h_network.RetrofitClient;
import com.example.edrkr.mainpage.MonitoringPage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText idInput, passwordInput;

    //Boolean loginChecked;
    SharedPreferences autoLoginData;
    SharedPreferences.Editor editor;

    Button loginButton;
    TextView signupButton;

    String UserID;
    String UserPW;

    AlertDialog.Builder LoginFailMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeView();
        SetListener();

    }

    //xml id랑 연결 및 기본세팅
    public void InitializeView(){

        idInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        loginButton= findViewById(R.id.loginButton);
        signupButton= findViewById(R.id.signupButton);


        //팝업창
        LoginFailMessage = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        LoginFailMessage.setMessage("아이디와 패스워드를 확인해 주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

    }

    //버튼 리스너 연결
    private void SetListener() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.loginButton:

                        UserID=idInput.getText().toString();
                        UserPW=passwordInput.getText().toString();

                        if (UserID.isEmpty()||UserPW.isEmpty()) {
                            LoginFailMessage.show();
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

                        //로딩중 만들기...
                        LoginNetwork(UserID,UserPW); //로그인 시도



                        break;
                    case R.id.signupButton:
                        //Toast.makeText(getApplicationContext(),"회원가입 버튼 클릭",Toast.LENGTH_LONG).show();

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
    private void LoginNetwork(final String id, String pw){
        Call<String> login = RetrofitClient.getApiService().getLoginCheck(id,pw); //api 콜
        login.enqueue(new AutoRetryCallback<String>() {
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
                    getUserIdnet(id); //로그인 유저 정보 수집(통신)
                }else{
                    //아이디, 비번 잘못됨.
                    Log.d("정보가 없는 회원", response.body());
                    LoginFailMessage.show();
                }

            }
            /*
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
            */
            @Override
            public void onFinalFailure(Call<String> call, Throwable t) {
                    Log.e("로그인 연결실패", t.getMessage());
            }
        });

    }

    //로그인 성공시 유저 정보 접근
    private void getUserIdnet(String id){
        Call<ResponseUserIdent> ident = RetrofitClient.getApiService().getUserIdent(id); //api 콜
        ident.enqueue(new AutoRetryCallback<ResponseUserIdent>() {
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
                Log.w("자료형 통과","정상적인 저장");
                UserIdent.GetInstance().setId(UserID);
                UserIdent.GetInstance().setPw(UserPW);

                UserIdent.GetInstance().printLog(); //확인


                Intent monitoringIntent = new Intent(MainActivity.this, MonitoringPage.class);
                startActivity(monitoringIntent);
                //finish();


            }//통신 실패나 로그아웃하면 싱글턴 비우는 문법 추가하기???
            /*
            @Override
            public void onFailure(Call<ResponseUserIdent> call, Throwable t) {
                Log.e("회원 정보 연결실패", t.getMessage());
            }
            */
            @Override
            public void onFinalFailure(Call<ResponseUserIdent> call, Throwable t) {
                Log.e("회원 정보 연결실패", t.getMessage());
            }

        });


    }
}
