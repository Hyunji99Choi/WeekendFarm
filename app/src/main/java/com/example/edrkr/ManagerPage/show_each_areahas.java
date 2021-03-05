package com.example.edrkr.ManagerPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class show_each_areahas extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private stringadapter mAdapter;
    private ActionBar actionBar;
    private LinearLayoutManager layoutManager;
    private ArrayList<Member> myDataset = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_each_areahas);
        this.InitializeView(); //필요 요소 선언해주는 함수
//        mAdapter.setOnItemClickListener(new CustomUsersAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int pos) {
//                Log.v("알림","게시글 클릭 리스너 눌림 pos : "+pos);
//                String s = myDataset.get(pos);
//                Intent intent = new Intent(show_each_areahas.this, show_each_areahas.class);
//
//                intent.putExtra("Board",s);
//                intent.putExtra("pos",pos);
//                Log.v("알림","Board값 전송 완료");
//                //startActivity(intent);
//                //setResult(1,intent);
//                startActivityForResult(intent,2);
//                Log.v("알림","intent 전송 완료");
//                //finish();
//            }
//        });
    }

    public void InitializeView(){
        // write = (ImageButton)findViewById(R.id.fab_write);
        fab = (FloatingActionButton)findViewById(R.id.fab_areaAdd);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.v("show_each_areahas","+ 버튼 눌림");
                Intent intent = new Intent(show_each_areahas.this, SelectMember.class);
                startActivityForResult(intent,0); //writing activity에서 값을 다시 받아오기 위해서 사용
            }
        });

        Log.v("show_each_area","toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_eachareahas);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setTitle("소유자 목록");
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v("show_each_area","toolbar 완료");

        recyclerView =(RecyclerView) findViewById(R.id.recycler_areahaslist);

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
        mAdapter = new stringadapter(myDataset,3);
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
    public ArrayList<Member> testData(){
        ArrayList<Member> tmp = new ArrayList<Member>();
        for(int i =0;i<5;i++){
            Member add = new Member(null, i+"번째 사람");
            tmp.add(add);
        }
        return tmp;
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
                Log.v("show_each_areahas","home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
