package com.example.edrkr;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectArea extends AppCompatActivity {
    private RecyclerView recyclerView;
    private stringadapter mAdapter;
    private LinearLayoutManager layoutManager;
    private Button OK;
    private ActionBar actionBar;
    private ArrayList<Member> myDataset = new ArrayList<>();
    private String URL = "http://3.35.55.9:3000/forum/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);

        OK = (Button)findViewById(R.id.buttonSelectAreaEnd);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_selectArea);
        getfromserver();
        recycler_test(); //테스트용 데이터 저장
        Log.v("SelectMember","recyclerview id 연결");

        Log.v("selectarea","toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_selectarea);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v("selectarea","toolbar 완료");

        recyclerView.setHasFixedSize(true);
        mAdapter = new stringadapter(myDataset,0);

        // layoutManager.setReverseLayout(true);
        //  layoutManager.setStackFromEnd(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Log.v("SelectMember","layout adapter 연결");
        recyclerView.setAdapter(mAdapter);
        SetListener();

        mAdapter.setOnItemClickListener(new CustomUsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.v("SelectMember","게시글 클릭 리스너 눌림 pos : "+pos);
                if(!myDataset.get(pos).getChecked_()){
                    myDataset.get(pos).setChecked_(true);
                    v.setBackgroundColor(Color.GREEN);
                }else{
                    myDataset.get(pos).setChecked_(false);
                    v.setBackgroundColor(Color.WHITE);
                }
            }
        });
    }
    public void SetListener() {
        //inputMethodManger 객체 선언
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonSelectAreaEnd:
                        Log.v("selectMember","선택완료버튼 눌림");
                        //patch 코드
                        puttoserver();
                        finish();
                }
            }
        };
        OK.setOnClickListener(Listener);
    }

    public void recycler_test(){
        ArrayList<Member> test = new ArrayList<>();
        for(int i = 0;i<20;i++){
            Member tmp = new Member(null,"밭"+i);
            test.add(tmp);
        }
        myDataset = test;
    }

    public void getfromserver(){

    }

    public void puttoserver(){

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.noticeboard_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Log.v("selectarea","home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}