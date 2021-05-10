package com.example.edrkr.subpage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edrkr.R;
import com.example.edrkr.UserIdent;
import com.example.edrkr.h_network.AutoRetryCallback;
import com.example.edrkr.h_network.RequestUpdateUser;
import com.example.edrkr.h_network.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Response;

public class subpage_userIdnetChange extends AppCompatActivity {

    EditText id,name,nkname,change_pw,change_pwck, phone,email;

    AlertDialog.Builder emptyCkMessage; //제대로 확인 안됬을 때
    AlertDialog.Builder finishMessage; //완료

    TextInputLayout passCheckError;
    boolean pw_same_ck = false; //비번 동일 확인

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subpage_userident_change);

        initValue();

    }
    private void initValue(){
        id = findViewById(R.id.change_idInput);

        change_pw = findViewById(R.id.change_newpasswordInput);
        change_pwck = findViewById(R.id.change_newpasswordInputCheck);

        name = findViewById(R.id.change_nameInput);
        nkname = findViewById(R.id.change_nknameInput);

        phone = findViewById(R.id.change_phonnumInput);
        email = findViewById(R.id.change_emailInput);

        passCheckError = findViewById(R.id.newpassCheckError);

        id.setText(UserIdent.GetInstance().getId());
        // pw는 비워두기

        name.setText(UserIdent.GetInstance().getName());
        nkname.setText(UserIdent.GetInstance().getNkname());

        phone.setText(UserIdent.GetInstance().getPhon());
        email.setText(UserIdent.GetInstance().getEmail());


        //다이로그
       emptyCkMessage = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
       emptyCkMessage.setMessage("입력되지 않은 값이 존재합니다. 다시 확인해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        finishMessage = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        finishMessage.setMessage("회원 정보가 변경되었습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(1);
                        finish();
                    }
                });

        //비밀번호 확인
        //비밀번호 확인 text
        change_pwck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력 끝났을 때
                String pw=change_pw.getText().toString();
                String pwck=change_pwck.getText().toString();
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

    //수정 버튼 클릭
    public void changUserIdentOnClick(View view){

        if(!pw_same_ck){ //비번 확인 안됨
            emptyCkMessage.show();
            return;
        }

        String pw= change_pw.getText (). toString ();
        String name= this.name.getText (). toString ();
        String email= this.email.getText (). toString ();
        String phone= this.phone.getText (). toString ();

        //값이 비어있을때
        if (name. isEmpty () || email. isEmpty () || phone. isEmpty ()
                || pw. isEmpty () || change_pwck.getText (). toString (). isEmpty ()) {
            emptyCkMessage.show();
            return;
        }

        //통신
        networkUpdate(pw, name, phone, email);


    }

    private void networkUpdate(String pw, String name, String phone, String email){

        RequestUpdateUser requestUpdateUser = new RequestUpdateUser();
        requestUpdateUser.setPw(pw);    requestUpdateUser.setName(name);
        requestUpdateUser.setPhone(phone);  requestUpdateUser.setEmail(email);

        Call<String> update = RetrofitClient.getApiService().updateUserIdent(Integer.toString(UserIdent.GetInstance().getUserIdent()),requestUpdateUser); //api 콜
        update.enqueue(new AutoRetryCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    //실패시 메시지 띄우는거 추가하기
                    Toast.makeText(getApplicationContext(),"통신 실패, 다시 시도해주세요",Toast.LENGTH_LONG).show();
                    return;
                }

                //통신 성공적
                if(response.body().equals("수정완료")){
                    //성공적인 수정
                    Log.d("수정 성공적", response.body());


                    UserIdent.GetInstance().setPw(pw);
                    UserIdent.GetInstance().setName(name);
                    UserIdent.GetInstance().setPhon(phone);
                    UserIdent.GetInstance().setEmail(email);


                    finishMessage.show();
                }else{
                    Log.d("수정 실패", response.body());
                    Toast.makeText(getApplicationContext(),"수정 실패, 다시 시도해주세요",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFinalFailure(Call<String> call, Throwable t) {
                Log.e("로그인 연결실패", t.getMessage());
                //Toast.makeText(getApplicationContext(),"통신 실패, 다시 시도해주세요",Toast.LENGTH_LONG).show();
            }
        });
    }
}
