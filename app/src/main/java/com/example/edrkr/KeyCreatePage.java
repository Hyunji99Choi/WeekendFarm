package com.example.edrkr;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class KeyCreatePage extends AppCompatActivity {

    EditText phonNumber;
    TextView keyValue;

    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subpage_key_create_page);

        phonNumber = findViewById(R.id.key_phonNumber);
        keyValue = findViewById(R.id.key_TextKeyValue);

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);


        Toolbar toolbar = findViewById(R.id.toolbar_keycreate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("KEY 번호 생성"); // 툴바 이름 변경


    }


    public void onClickKeyCreateButton(View view){ //생성 버튼 클릭


        
    }
}
