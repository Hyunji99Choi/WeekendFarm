package com.example.edrkr;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectArea extends AppCompatActivity {
    private RecyclerView recyclerView;
    private stringadapter mAdapter;
    private LinearLayoutManager layoutManager;
    private Button OK;
    private ArrayList<Member> myDataset = new ArrayList<>();
    private String URL = "http://52.79.237.95:3000/forum/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);

        OK = (Button)findViewById(R.id.buttonSelectAreaEnd);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_selectArea);
        getfromserver();
        recycler_test(); //테스트용 데이터 저장
        Log.v("SelectMember","recyclerview id 연결");

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
}