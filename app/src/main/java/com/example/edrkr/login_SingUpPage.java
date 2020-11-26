package com.example.edrkr;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.concurrent.ExecutionException;

public class login_SingUpPage extends AppCompatActivity {

    TextView sing_id;
    TextView sing_pw;
    TextView sing_pwck;
    TextView sing_name;
    TextView sing_nkname;
    TextView sing_email;
    TextView sing_phon;
    TextView keyNumber;

    boolean id_double_ck = false; //아이디 중복 확인
    boolean nk_double_ck = false; //닉네임 중복 확인

    String Sign_URL="http://52.79.237.95:3000/users/";

    String doubleckID_URL="http://52.79.237.95:3000/users/search/ID";
    String doubleckNK_URL="http://52.79.237.95:3000/users/search/NickName";


    NetworkTask signPage_networkTask;

    private String NetworkRESULT=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        Toolbar toolbar = findViewById(R.id.toolbar_sign);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("회원가입"); // 툴바 이름 변경

        sing_id=findViewById(R.id.sign_idInput);
        sing_pw=findViewById(R.id.sign_passwordInput);
        sing_pwck=findViewById(R.id.sign_passwordInputCheck);
        sing_name=findViewById(R.id.sign_nameInput);
        sing_nkname=findViewById(R.id.sign_nknameInput);
        sing_email=findViewById(R.id.sign_emailInput);
        sing_phon=findViewById(R.id.sign_phonnumInput);
        keyNumber=findViewById(R.id.sign_keyInput);

        sing_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //입렫하기 전
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //editText에 변화가 있을 때
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력이 끝났을 때
                Log.w("변경 입력 검사","아아디 변경 확인");
                id_double_ck=false;
            }
        });

        sing_nkname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력이 끝났을 때
                Log.w("변경 입력 검사","닉네임 변경 확인");
                nk_double_ck=false;
            }
        });

    }
    public void onClickdbCkButton(View view) throws ExecutionException, InterruptedException {
        switch (view.getId()){
            case R.id.sign_iddubButton:
                //id 중복 확인
                ContentValues dbckIDvalues = new ContentValues();
                dbckIDvalues.put("id",sing_id.getText().toString());

                signPage_networkTask = new NetworkTask(doubleckID_URL,dbckIDvalues);
                NetworkRESULT=signPage_networkTask.execute().get();

                if(NetworkRESULT.equals("중복 안됨")){
                    //task에서 ui 수정해주기. (빨갛게)
                    Toast.makeText(this,"사용 가능한 id 입니다.",Toast.LENGTH_LONG).show();
                    id_double_ck=true;
                }else if(NetworkRESULT.equals("중복됨")){
                    Toast.makeText(this,"이미 사용중인 id 입니다..",Toast.LENGTH_LONG).show();
                    //나중에 추가할 수 있으면 빨간 테두리(drawable 사용)
                }else{
                    Toast.makeText(this,"인터넷 불완정, 다시 시도해주세요.",Toast.LENGTH_LONG).show();
                }
                //메모리 누수 방지
                signPage_networkTask=null;
                break;

            case R.id.sign_nkdubButton:
                //닉네임 중복 확인
                ContentValues dbckNKvalues = new ContentValues();
                dbckNKvalues.put("nickname",sing_id.getText().toString()); //닉네임 변수 확인

                signPage_networkTask = new NetworkTask(doubleckNK_URL,dbckNKvalues);
                NetworkRESULT=signPage_networkTask.execute().get();

                if(NetworkRESULT.equals("중복 안됨")){
                    Toast.makeText(this,"사용 가능한 닉네임 입니다.",Toast.LENGTH_LONG).show();
                    nk_double_ck=true;
                }else if(NetworkRESULT.equals("중복됨")){
                    Toast.makeText(this,"이미 사용중인 닉네임 입니다..",Toast.LENGTH_LONG).show();
                    //나중에 추가할 수 있으면 빨간 테두리(drawable 사용)
                }else{
                    Toast.makeText(this,"인터넷 불완정, 다시 시도해주세요.",Toast.LENGTH_LONG).show();
                }

                //메모리 누수 방지
                signPage_networkTask=null;
                break;

        }
    }
    public void onClickLoginButton(View view) throws ExecutionException, InterruptedException {

        String pw=sing_pw.getText().toString();
        String pwck=sing_pwck.getText().toString();
        Log.w("pw, pwck",""+pw+" "+pwck);

        if(pw.equals(pwck)==false){
            Toast.makeText(this,"비밀번호가 다릅니다. 다시 확인해 주세요.",Toast.LENGTH_LONG).show();
            //Log.w("비번","비번틀림");
            return;
        }
        // 닉네임, id 중복확인 했는지 확인
        if(!(nk_double_ck==true&&id_double_ck==true)){
            Toast.makeText(this,"중복확인이 되지 않았습니다.",Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues dbckIDvalues = new ContentValues();
        dbckIDvalues.put("id",sing_id.getText().toString());
        dbckIDvalues.put("pw",pw);
        dbckIDvalues.put("name",sing_name.getText().toString());
        dbckIDvalues.put("nickname",sing_nkname.getText().toString()); //변수 이름 바꿔야함
        dbckIDvalues.put("email",sing_email.getText().toString());
        dbckIDvalues.put("phone",sing_phon.getText().toString());
        dbckIDvalues.put("key",keyNumber.getText().toString());

        signPage_networkTask = new NetworkTask(Sign_URL,dbckIDvalues);
        NetworkRESULT=signPage_networkTask.execute().get();

        if(NetworkRESULT.equals("Key권한 없음")){
            Toast.makeText(this,"잘못된 key정보입니다.",Toast.LENGTH_LONG).show();
            id_double_ck=true;
        }else if(NetworkRESULT.equals("회원가입이 완료되었습니다.")){ //성공적인 가입
            Toast.makeText(this,"회원가입 완료",Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this,"인터넷 불완정, 다시 시도해주세요.",Toast.LENGTH_LONG).show();
        }

        //메모리 누수 방지
        signPage_networkTask=null;

    }
}
