package com.example.edrkr;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class NoticeBoardActivity extends AppCompatActivity implements LifecycleObserver {
    //private ImageButton write;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private CustomUsersAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private ActionBar actionBar;
    private ArrayList<Board> myDataset = new ArrayList<>(); //리사이클러뷰에 표시할 데이터 리스트 생성 -> 서버 생기면 처음에 받아오는 코드 만들기
    private String URL = "http://52.79.237.95:3000/forum/test";

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.v("알림","onactivity 함수 실행 resultcode : "+resultCode);
        myDataset = getfromlocal();
        //myDataset = getfromserver();
        mAdapter.changeDataset(myDataset);
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(mAdapter);
        Log.v("알림","새로고침 완료");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("noticeboard","oncreate진입");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        this.InitializeView(); //필요 요소 선언해주는 함수
       // this.SetListener(); //리스너 설정 함수
        mAdapter.setOnItemClickListener(new CustomUsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) { //recyclerview 각 viewholder 클릭할 경우
                Log.v("알림","게시글 클릭 리스너 눌림 pos : "+pos);
                Board b = myDataset.get(pos);
                Intent intent = new Intent(NoticeBoardActivity.this, show_each_board.class);

                Log.v("NoticeBoard","pos번호 : " + b.getPos());
                intent.putExtra("pos",b.getPos());
                Log.v("알림","Board값 전송 완료");
                //startActivity(intent);
                //setResult(1,intent);
                startActivityForResult(intent,1);
                Log.v("알림","intent 전송 완료");
                //finish();
            }
        });

    }
    public void InitializeView(){
       // write = (ImageButton)findViewById(R.id.fab_write);
        fab = (FloatingActionButton)findViewById(R.id.fab_write);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.v("알림","글쓰기 버튼 눌림");
                Intent intent = new Intent(NoticeBoardActivity.this, WritingActivity.class);
                startActivityForResult(intent,1); //writing activity에서 값을 다시 받아오기 위해서 사용
            }
        });

        Log.v("noticeboard","toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_noticeboard);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v("noticeboard","타이틀 보이게");

        recyclerView =(RecyclerView) findViewById(R.id.my_recycler_view);

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

        myDataset = getfromlocal();
        // myDataset = getfromserver();

        // specify an adapter (see also next example)
        mAdapter = new CustomUsersAdapter(myDataset);
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


//    public void SetListener() {
//        //inputMethodManger 객체 선언
//        //final InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        View.OnClickListener Listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.fab_write:
//                        Log.v("알림","글쓰기 버튼 눌림");
//                        Intent intent = new Intent(NoticeBoardActivity.this, WritingActivity.class);
//                        startActivityForResult(intent,0); //writing activity에서 값을 다시 받아오기 위해서 사용
//                }
//
//            }
//        };
//
//        write.setOnClickListener(Listener);

//    }

    public ArrayList<Board> getfromlocal(){
        ArrayList<Board> dataset = new ArrayList<>();
        Board b = new Board();
        dataset.add(b);
        return dataset;
    }

    public ArrayList<Board> getfromserver(){//서버에서 게시글 표지 부분을 받아오는 코드

        Log.v("알림","getfromserver 확인");
        ArrayList<Board> dataset = new ArrayList<Board>();

        //values.put("id","00");
        //values.put("pw",UserPW)

       NetworkTask getboardlist_networkTask = new NetworkTask(URL,null); //networktast 설정 부분
        Log.v("NoticeBoardActivity","networktask 입력 성곧");
        try {
            getboardlist_networkTask.execute().get(); //설정한 networktask 실행
            Log.v("NoticeBoardActiviy","get 실행 완료");
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.v("NoticeBoardActivity", "executionexception");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.v("NoticeBoardActivity", "interruptedexception");
        }
        Log.v("noticeboard","execute 확인");
        String result = getboardlist_networkTask.result;
        Log.v("noticeboard","result 확인 result : "+result);

        if(result == null){
            Log.v("noticeboard","통신실패");
            Toast.makeText(getApplicationContext(), "연결에 실패했습니다. 네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
            return dataset;
        }

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("getfromserver","json으로 변환");

        Log.v("getfromserver","board 변환 작업 시작");
        if(jsonArray == null){
            Log.v("getfrom","가져오지 못함");
        }else {
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String id = null;
                String userId = null;
                String title = null;
                String content = null;
                String time = null;
                String commentnum = null;
                try {
                    id = jsonObject.getString("id");
                    Log.v("Noticeboard","id : "+id);
                    userId = jsonObject.getString("UserName");
                    Log.v("Noticeboard","userid : "+userId);
                    title = jsonObject.getString("Title");
                    Log.v("Noticeboard","title  : "+title);
                    content = jsonObject.getString("Content");
                    Log.v("Noticeboard","content : "+content);
                    commentnum = jsonObject.getString("CommentNum");
                    Log.v("Board 통신", "commentnum : "+ commentnum);
                    time = jsonObject.getString("createdAt");
                    Log.v("Board 통신", "createdAt : "+ time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("getfromserver", i + " 값 가져오기 성공");
                Board b = new Board(Integer.parseInt(id), userId, title, content, Integer.parseInt(commentnum),time);
                Log.v("getfromserver", "board 추가 완료");
                dataset.add(b);
            }
        }
        return dataset;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.noticeboard_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.notice_patch:
                Log.v("noticeboard_menu","patch클릭");
                Toast.makeText(this,"patch onclick",Toast.LENGTH_SHORT).show();
                break;
            case R.id.notice_delete:
                Log.v("noticeboard_menu","delete클릭");
                Toast.makeText(this,"delete onclick",Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                Log.v("noticeboard_menu","home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.notice_search:
                Log.v("noticeboard_menu","search");
                Toast.makeText(this,"search onclick",Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.v("noticeboard_menu","default");
                Toast.makeText(this,"오류 발생",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    public void comfromboard(){
//        myDataset = getfromserver();
//        recyclerView.removeAllViewsInLayout();;
//        recyclerView.setAdapter(mAdapter);
//        Log.v("알림","새로고침 완료");
//    }
    /*
    public void fetchTimelineAsync(int page)
    {

    }
    public void AddMainboard(){

    }
    @Override
    public void onRefresh(){
        //새로고침 코드

        //새로고침 완료

    }*/
}

