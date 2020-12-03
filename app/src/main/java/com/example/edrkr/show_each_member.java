package com.example.edrkr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class show_each_member extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private Button reset;
    private TextView name;
    private TextView id;
    private TextView pw;
    private TextView phonenumber;
    private ActionBar actionBar;
    private stringadapter mAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Member> myDataset = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_each_member);
        this.InitializeView(); //필요 요소 선언해주는 함수
        SetListener(); //리스너 설정 함수
    }

    public void InitializeView(){
        // write = (ImageButton)findViewById(R.id.fab_write);
        fab = (FloatingActionButton)findViewById(R.id.fab_show_each_member);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.v("SHOW_EACH_member","+ 버튼 눌림");
                Intent intent = new Intent(show_each_member.this, SelectArea.class);
                startActivityForResult(intent,1); //writing activity에서 값을 다시 받아오기 위해서 사용
            }
        });

        Log.v("eachmember","toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_eachmember);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v("eachmember","toolbar 완료");

        reset = (Button)findViewById(R.id.buttonresetpw);
        name = (TextView) findViewById(R.id.member_name);
        id = (TextView) findViewById(R.id.textViewshowid);
        pw = (TextView) findViewById(R.id.textViewshowpw);
        phonenumber = (TextView) findViewById(R.id.textViewshowphone);

        recyclerView =(RecyclerView) findViewById(R.id.recyclerShowEachMember);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        //myDataset 받는 코드 들어가야함
        // myDataset = null;
       //myDataset = getfromserver();
        myDataset = testData();
        // specify an adapter (see also next example)
        mAdapter = new stringadapter(myDataset,2);
        recyclerView.setAdapter(mAdapter);
        Log.v("noticeboard","adapter설정완료");

//        swipeContainer =(SwipeRefreshLayout) findViewById((R.id.swipeContainer);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                fetchTimelineAsync(0);
//            }
//        });

    }
    public void SetListener() {
        //inputMethodManger 객체 선언
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonresetpw:
                        Log.v("show_each_member","비번변경 버튼 클릭");
                        //patch코드
                        pw.setText("0000"); //초기화 코드
                        Toast.makeText(getApplicationContext(),"비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        reset.setOnClickListener(Listener);
    }

    public ArrayList<Member> testData(){
        ArrayList<Member> result = new ArrayList<>();
        for(int i =0;i<5;i++){
            Member tmp = new Member(null,i+"번 밭");
            result.add(tmp);
        }
        return result;
    }

    public void getfromserver(){

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
                Log.v("eachmember","home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
