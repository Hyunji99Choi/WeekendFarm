package com.example.edrkr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class show_each_areahas extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private stringadapter mAdapter;
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
        fab = (FloatingActionButton)findViewById(R.id.Button_areaAdd);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.v("show_each_areahas","+ 버튼 눌림");
                Intent intent = new Intent(show_each_areahas.this, SelectMember.class);
                startActivityForResult(intent,0); //writing activity에서 값을 다시 받아오기 위해서 사용
            }
        });

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


}
