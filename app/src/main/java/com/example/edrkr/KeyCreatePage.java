package com.example.edrkr;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.edrkr.h_network.AutoRetryCallback;
import com.example.edrkr.h_network.RetrofitClient;
import com.example.edrkr.managerPage.stringadapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Response;

import static java.sql.Types.NULL;

public class KeyCreatePage extends AppCompatActivity {

    MaterialButtonToggleGroup toggleGroup;
    MaterialButton managerSelectBtn; //toggle btn
    MaterialButton userSelectBtn; //toggle btn

    CircularProgressButton button;

    TextView keyIndex;
    TextView keyValue;

    ListView listView;
    ArrayAdapter mAdapter;

    AlertDialog.Builder emptyCkMessage;
    AlertDialog.Builder emilCkMessage;


    boolean keyUser = false; //생성하는 키가 user면 true, 관리자이면 false.

    String[] keyArrayName; //
    int[] keyArrayId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subpage_key_create_page);

        toggleGroup = findViewById(R.id.toggleGroup);

        managerSelectBtn = findViewById(R.id.manager);
        userSelectBtn = findViewById(R.id.user);

        keyIndex = findViewById(R.id.key_index);
        keyValue = findViewById(R.id.key);
        keyValue.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("text", ((TextView) v).getText()));
                return false;
            }
        });
        //출처: https://gogorchg.tistory.com/entry/Android-Textview-Copy-Clipboardmanager [항상 초심으로]



        button = findViewById(R.id.creat_btn);


        listView = findViewById(R.id.listview);

        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice,UserIdent.GetInstance().getFarmName());
        listView.setAdapter(mAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //listView.setItemsCanFocus(false); // ?

        //listView.setOnClickListener();


        emptyCkMessage = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog);
        emptyCkMessage.setMessage("생성할 key 항목을 선택하세요")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });



        //email 디자인.... error

        final EditText emil = new EditText(this);
        TextInputLayout layout = new TextInputLayout(this);
        //layout.topMargin =
        emil.setHint("emil 주소를 입력해주세요.");
        layout.addView(emil);

        emilCkMessage = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Presentation);
        emilCkMessage
                .setMessage("생성된 key 번호를 emil로도 전송하시겠습니까?")//.setCancelable(false)
                .setView(layout)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            //확인 버튼
                        Log.w("다일로그","확인");
                        if(emil.getText().toString().length() == 0){
                            Toast.makeText(getApplicationContext(),"emil을 입력해주세요",Toast.LENGTH_SHORT).show();
                            button.revertAnimation();
                        }else {
                            keyNetwork(emil.getText().toString());
                        }

                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //취소 버튼
                Log.w("다일로그","취소");

                //오류류
               keyNetwork("FALSE");
            }
        });


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

        if(toggleGroup.getCheckedButtonId()==View.NO_ID){ //key 항목을 선택하지 않음
            emptyCkMessage.show();
            return;
        }

        button.startAnimation();

        //사용자 key라면
        if(toggleGroup.getCheckedButtonId() == R.id.user){

            keyUser = true;

            SparseBooleanArray clickedItemPositions = listView.getCheckedItemPositions();
            if(clickedItemPositions.size() == 0){ //아무것도 선택하지 않을때
                emptyCkMessage.show();
                button.revertAnimation();
                return;
            }else{ //사용자 key 진행

                keyArrayName = new String[clickedItemPositions.size()];
                keyArrayId = new int[clickedItemPositions.size()];

                int i=0;
                for(int index=0;index<clickedItemPositions.size();index++){
                    // Get the checked status of the current item
                    boolean checked = clickedItemPositions.valueAt(index);

                    if(checked){
                        // If the current item is checked
                        int key = clickedItemPositions.keyAt(index);
                        Log.w("clickedItemPositions",""+key);
                        String item = (String) listView.getItemAtPosition(key);

                        //선택한 밭 id 와 별명 저장.
                        keyArrayId[i] = UserIdent.GetInstance().getFarmID(key);
                        keyArrayName[i++]=item;
                    }
                }

            }
            Log.w("keyArray",keyArrayName[0]);
            Log.w("keyArray",""+keyArrayId[0]);
            //관리자 key
        }else if(toggleGroup.getCheckedButtonId() == R.id.manager){
            keyUser = false;

            keyArrayId = new int[1];
            keyArrayId[0] = -1;
        }


        //이메일 물어보기
        emilCkMessage.show();


        
    }

    // error?
    private void keyNetwork(String email){

        Call<String> key;

        if(email.equals("FALSE")){
            key = RetrofitClient.getApiService().registerkeyCreat(keyArrayId); //api 콜
        }else{
            key = RetrofitClient.getApiService().registerkeyCreat(keyArrayId,email); //api 콜
        }

        key.enqueue(new AutoRetryCallback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    Toast.makeText(getApplicationContext(),"알수 없는 오류로 key가 생성되지 않았습니다. 다시 시도해주세요",Toast.LENGTH_LONG).show();
                    button.revertAnimation();
                    return;
                }

                keyValue.setText(response.body());
                keyValue.setPadding(0,10,0,10);

                if(keyUser == false){ //관리자
                    keyIndex.setText("관리자 계정 key");
                }else if(keyUser == true){

                    keyIndex.setText("");
                    for(int i=0;i<keyArrayName.length -1;i++){
                        keyIndex.append(keyArrayName[i] + ", ");
                    }
                    keyIndex.append(keyArrayName[keyArrayName.length -1] + " 사용자 계정 key");
                }

                button.resetProgress();
                button.setVisibility(View.GONE);


            }

            @Override
            public void onFinalFailure(Call<String> call, Throwable t) {
                Log.e("key 통신 실패", t.getMessage());
                Toast.makeText(getApplicationContext(),"알수 없는 오류로 key가 생성되지 않았습니다. 다시 시도해주세요",Toast.LENGTH_LONG).show();
                button.revertAnimation();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        button.dispose();
        System.gc(); // Garbage Collection 희망
    }
}

