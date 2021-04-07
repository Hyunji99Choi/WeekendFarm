package com.example.edrkr.subpage;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edrkr.R;
import com.example.edrkr.UserIdent;
import com.google.android.material.textfield.TextInputLayout;

public class subpage_userIdnetChange extends AppCompatActivity {

    TextInputLayout namelayout;
    EditText id,name,nkname,change_pw,change_pwck, phone,email;

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

        id.setText(UserIdent.GetInstance().getId());
        // pw는 비워두기

        name.setText(UserIdent.GetInstance().getName());
        nkname.setText(UserIdent.GetInstance().getNkname());

        phone.setText(UserIdent.GetInstance().getPhon());
        email.setText(UserIdent.GetInstance().getEmail());


    }

    //수정 버튼 클릭
    public void changUserIdentOnClick(View view){

    }
}
