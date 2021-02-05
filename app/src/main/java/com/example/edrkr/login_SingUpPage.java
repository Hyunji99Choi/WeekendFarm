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

import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    TextInputLayout idError,passError,passCheckError,nameError, emailError, phoneError, nknameError,keyError;


    String Sign_URL="http://3.36.69.43::3000/users/";
    String doubleckID_URL="http://3.36.69.43:3000//users/search/ID";
    String doubleckNK_URL="http://3.36.69.43::3000/users/search/NickName";


    NetworkTask signPage_networkTask;

    private String NetworkRESULT=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        initValue(); //초기 세팅
        initListener(); //리스너 연결(중복 체크 확인)

    }

    public void onClickdbCkButton(View view) throws ExecutionException, InterruptedException {
        switch (view.getId()){
            case R.id.sign_iddubButton:
                //id 중복 확인
                getRegisterIdCheck(sing_id.getText().toString());
                /*
                Log.d("통신전","통신전");
                ContentValues dbckIDvalues = new ContentValues();
                dbckIDvalues.put("id",sing_id.getText().toString());
                Log.d("통신전","자료형 만듦");
                signPage_networkTask = new NetworkTask(doubleckID_URL,dbckIDvalues);
                NetworkRESULT=signPage_networkTask.execute().get();
                Log.d("통신전","통신 날림");
                if(NetworkRESULT.equals("중복 안됨")){
                    //task에서 ui 수정해주기. (빨갛게)
                    Toast.makeText(this,"사용 가능한 id 입니다.",Toast.LENGTH_LONG).show();
                    Log.d("통신후","사용 가능");
                    id_double_ck=true;
                }else if(NetworkRESULT.equals("중복됨")){
                    Toast.makeText(this,"이미 사용중인 id 입니다..",Toast.LENGTH_LONG).show();
                    Log.d("통신후","이미 사용중");
                    //나중에 추가할 수 있으면 빨간 테두리(drawable 사용)
                }else{
                    Toast.makeText(this,"인터넷 불완정, 다시 시도해주세요.",Toast.LENGTH_LONG).show();
                    Log.d("통신후","인터넷 불완전");
                }
                //메모리 누수 방지
                signPage_networkTask=null;
                */
                break;
            case R.id.sign_nkdubButton:
                //닉네임 중복 확인
                getRegisterNKnameCheck(sing_nkname.getText().toString());
                /*

                ContentValues dbckNKvalues = new ContentValues();
                dbckNKvalues.put("nickname",sing_id.getText().toString()); //닉네임 변수 확인, 오류 체크 찾았음.

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

                 */
                break;
        }
    }

    //통신 방법 바꾸기 , 밑 경고 문고 부분 퀄리티 높이기(전체)
    public void onClickLoginButton(View view) throws ExecutionException, InterruptedException {

        String pw=sing_pw.getText().toString();
        String pwck=sing_pwck.getText().toString();
        Log.w("pw, pwck",""+pw+" "+pwck);

        if(!pw.equals(pwck)){
            Toast.makeText(this,"비밀번호가 다릅니다. 다시 확인해 주세요.",Toast.LENGTH_LONG).show();
            //Log.w("비번","비번틀림");
            return;
        }
        // 닉네임, id 중복확인 했는지 확인
        if(!(nk_double_ck&&id_double_ck)){
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




    //아이디 중복 체크 통신
    private void getRegisterIdCheck(String id){
        Call<String> idDub = RetrofitClient.getApiService().registerIdCheck(id); //api 콜
        idDub.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    Toast.makeText(login_SingUpPage.this,"인터넷 불완정, 다시 시도해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                //통신 성공
                if(response.body().equals("중복 안됨")){
                    //사용가능한 아이디
                    Log.d("사용가능한 아이디", response.body());
                    idError.setErrorEnabled(false);
                    id_double_ck=true;
                }else if(response.body().equals("중복됨")){
                    Log.d("중복된 아이디", response.body());
                    idError.setError(getResources().getString(R.string.id_not_useing));
                    id_double_ck = false;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
                Toast.makeText(login_SingUpPage.this,"인터넷 불완정, 다시 시도해주세요.",Toast.LENGTH_LONG).show();
            }
        });
    }

    //별명 중복 체크 통신
    private void getRegisterNKnameCheck(String nkname){
        Call<String> nkNameDub = RetrofitClient.getApiService().registerNickNameCheck(nkname);
        nkNameDub.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    Toast.makeText(login_SingUpPage.this,"인터넷 불완정, 다시 시도해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                //통신 성공
                if(response.body().equals("중복 안됨")){
                    //사용가능한 별명
                    Log.d("사용가능한 별명", response.body());
                    nknameError.setErrorEnabled(false);
                    nk_double_ck=true;
                }else if(response.body().equals("중복됨")){
                    Log.d("중복된 별명", response.body());
                    nknameError.setError(getResources().getString(R.string.nickname_not_useing));
                    nk_double_ck = false;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
                Toast.makeText(login_SingUpPage.this,"인터넷 불완정, 다시 시도해주세요.",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void initValue(){

        sing_id=findViewById(R.id.sign_idInput);
        sing_pw=findViewById(R.id.sign_passwordInput);
        sing_pwck=findViewById(R.id.sign_passwordInputCheck);
        sing_name=findViewById(R.id.sign_nameInput);
        sing_nkname=findViewById(R.id.sign_nknameInput);
        sing_email=findViewById(R.id.sign_emailInput);
        sing_phon=findViewById(R.id.sign_phonnumInput);
        keyNumber=findViewById(R.id.sign_keyInput);

        idError=findViewById(R.id.idError);
        passError=findViewById(R.id.passError);
        passCheckError=findViewById(R.id.passCheckError);
        nameError=findViewById(R.id.nameError);
        emailError=findViewById(R.id.emailError);
        phoneError=findViewById(R.id.phoneError);
        nknameError=findViewById(R.id.nknameError);
        keyError=findViewById(R.id.keyError);

    }
    private void initListener(){

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
}
