package com.example.edrkr.firstpage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edrkr.R;
import com.example.edrkr.h_network.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_SingUpPage extends AppCompatActivity {

    TextView sing_id,sing_pw,sing_pwck,sing_name,
            sing_nkname,sing_email,sing_phon,keyNumber;

    TextInputLayout idError,passError,passCheckError,
            nameError, emailError, phoneError, nknameError,keyError;

    boolean id_double_ck = false; //아이디 중복 확인
    boolean nk_double_ck = false; //닉네임 중복 확인
    boolean pw_same_ck = false; //비번 동일 확인


    AlertDialog.Builder emptyCkMessage; //제대로 확인 안됬을 때
    AlertDialog.Builder noKeyMessage; //잘못된 key 번호
    AlertDialog.Builder okRegisterMessage; //회원가입 성공

    /*
    String Sign_URL="http://3.36.69.43::3000/users/";
    String doubleckID_URL="http://3.36.69.43:3000//users/search/ID";
    String doubleckNK_URL="http://3.36.69.43::3000/users/search/NickName";

    NetworkTask signPage_networkTask;

    private String NetworkRESULT=null;

     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);

        initValue(); //초기 세팅
        initListener(); //리스너 연결(중복 체크 확인)

    }

    //중복확인 버튼
    public void onClickdbCkButton(View view) throws ExecutionException, InterruptedException {
        switch (view.getId()){
            case R.id.sign_iddubButton:
                //id 중복 확인
                if (sing_id.getText (). toString (). isEmpty ()) {
                    idError.setError(getResources().getString(R.string.id_not_useing));
                    id_double_ck = false;
                }else{
                    getRegisterIdCheck(sing_id.getText().toString());
                }

                break;

            case R.id.sign_nkdubButton:
                //닉네임 중복 확인
                if (sing_nkname.getText (). toString (). isEmpty ()) {
                    nknameError.setError(getResources().getString(R.string.nickname_not_useing));
                    nk_double_ck = false;
                }else{
                    getRegisterNKnameCheck(sing_nkname.getText().toString());
                }

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

        // 비번 체크, 닉네임, id 중복확인 했는지 확인
        if(!(nk_double_ck&&id_double_ck&&pw_same_ck)){
            emptyCkMessage.show();
            return;
        }

        String id =sing_id.getText (). toString ();
        String pw= sing_pw.getText (). toString ();
        String name=sing_name.getText (). toString ();
        String nickname=sing_nkname.getText (). toString ();
        String email=sing_email.getText (). toString ();
        String phone=sing_phon.getText (). toString ();
        String key=keyNumber.getText (). toString ();

        //값이 비어있을때
        if (name. isEmpty () || email. isEmpty () || phone. isEmpty () ||
                key. isEmpty () || id. isEmpty () || pw. isEmpty () ||
                nickname. isEmpty () || sing_pwck.getText (). toString (). isEmpty ()) {
            emptyCkMessage.show();
            return;
        }

        //통신
        getRegisterUser(id, pw, name, nickname, email, phone, key);

        /*
        ContentValues dbckIDvalues = new ContentValues();
        dbckIDvalues.put("id",sing_id.getText().toString());
        dbckIDvalues.put("pw",sing_pw.getText().toString());
        dbckIDvalues.put("name",sing_name.getText().toString());
        dbckIDvalues.put("nickname",sing_nkname.getText().toString()); //변수 이름 바꿔야함
        dbckIDvalues.put("email",sing_email.getText().toString());
        dbckIDvalues.put("phone",sing_phon.getText().toString());
        dbckIDvalues.put("key",keyNumber.getText().toString());

        signPage_networkTask = new NetworkTask(Sign_URL,dbckIDvalues);
        NetworkRESULT=signPage_networkTask.execute().get();

        if(NetworkRESULT.equals("Key권한 없음")){
            Toast.makeText(this,"잘못된 key정보입니다.",Toast.LENGTH_LONG).show();

        }else if(NetworkRESULT.equals("회원가입이 완료되었습니다.")){ //성공적인 가입
            Toast.makeText(this,"회원가입 완료",Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this,"인터넷 불완정, 다시 시도해주세요.",Toast.LENGTH_LONG).show();
        }

        //메모리 누수 방지
        signPage_networkTask=null;

         */
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
                    //idError.setErrorEnabled(false);
                    idError.setHelperText(getResources().getString(R.string.id_use_success));
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
                    //nknameError.setErrorEnabled(false);
                    nknameError.setHelperText(getResources().getString(R.string.nickname_use_success));
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

    //회원가입 통신
    private void getRegisterUser(String id, String pw, String name, String nickname, String email,String phone, String key){
        Call<String> register = RetrofitClient.getApiService().registerLogin(id,pw,name,nickname,email,phone,key);
        register.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    Toast.makeText(login_SingUpPage.this,"인터넷 불완정, 다시 시도해주세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                //통신 성공
                if(response.body().equals("Key권한 없음")){
                    Log.d("Key권한 없음", response.body());
                    noKeyMessage.show();
                    keyError.setError(getResources().getString(R.string.key_not_useing));
                }else if(response.body().equals("회원가입이 완료되었습니다.")){
                    Log.d("회원가입 성공", response.body());
                    okRegisterMessage.show();

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


        idError.setHelperTextColor(ColorStateList.valueOf(Color.BLUE));
        nknameError.setHelperTextColor(ColorStateList.valueOf(Color.BLUE));

        emptyCkMessage = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        emptyCkMessage.setMessage("입력되지 않은 값이 존재합니다. 모든 값을 입력해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        noKeyMessage = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        noKeyMessage.setMessage("잘못된 key 번호입니다. 다시 확인해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        okRegisterMessage = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        okRegisterMessage.setMessage("회원가입이 완료되었습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

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
                idError.setErrorEnabled(false);
                idError.setHelperTextEnabled(false);
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
                nknameError.setErrorEnabled(false);
                nknameError.setHelperTextEnabled(false);
                nk_double_ck=false;
            }
        });

        //비밀번호 확인 text
        sing_pwck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력 끝났을 때
                String pw=sing_pw.getText().toString();
                String pwck=sing_pwck.getText().toString();
                //Log.w("pw, pwck",""+pw+" "+pwck);

                if(!pw.equals(pwck)){
                    passCheckError.setError(getResources().getString(R.string.passwd_no_same));
                    pw_same_ck=false;
                }else{
                    passCheckError.setErrorEnabled(false);
                    pw_same_ck=true;
                }
            }
        });

    }
}
