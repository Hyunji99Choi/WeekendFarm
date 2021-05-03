package com.example.edrkr;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.edrkr.h_network.AutoRetryCallback;
import com.example.edrkr.h_network.RetrofitClient;
import com.example.edrkr.managerPage.stringadapter;
import com.google.android.material.button.MaterialButton;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Response;

public class KeyCreatePage extends AppCompatActivity {

    MaterialButton managerSelectBtn; //toggle btn
    MaterialButton userSelectBtn; //toggle btn

    CircularProgressButton button;

    TextView keyIndex;
    TextView keyValue;


    ListView listView;

    ArrayAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subpage_key_create_page);

        managerSelectBtn = findViewById(R.id.manager);
        userSelectBtn = findViewById(R.id.user);

        keyIndex = findViewById(R.id.key_index);
        keyValue = findViewById(R.id.key);

        button = findViewById(R.id.creat_btn);


        listView = findViewById(R.id.listview);

        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice,UserIdent.GetInstance().getFarmName());
        listView.setAdapter(mAdapter);




    }

    public void select(View view){

        switch (view.getId()){
            case R.id.manager:
                listView.setVisibility(View.INVISIBLE);
                break;
            case R.id.user:
                listView.setVisibility(View.VISIBLE);
                break;
        }

    }


    public void keyCreate(View view){ //생성 버튼 클릭
        button.startAnimation();




        
    }

    private void keyNetwork(String email){
        Call<String> key = RetrofitClient.getApiService().registerkeyCreat(id,email); //api 콜
        key.enqueue(new AutoRetryCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    button.revertAnimation();
                    return;
                }

                keyValue.setText(response.body());
                keyIndex.setText("관리자 아니면 나열~");// 수정하기
                button.setVisibility(View.GONE);



            }

            @Override
            public void onFinalFailure(Call<String> call, Throwable t) {
                Log.e("key 통신 실패", t.getMessage());
                button.revertAnimation();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        button.dispose();
    }
}

