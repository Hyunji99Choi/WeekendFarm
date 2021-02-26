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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.edrkr.DTO.Builder;
import com.example.edrkr.DTO.Post;
import com.example.edrkr.DTO.PostResult;
import com.example.edrkr.DTO.PostWriting;
import com.example.edrkr.DTO.retrofitIdent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;


public class NoticeBoardActivity extends AppCompatActivity implements LifecycleObserver {
    //private ImageButton write;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private CustomUsersAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private ActionBar actionBar;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<Board> myDataset = new ArrayList<>(); //리사이클러뷰에 표시할 데이터 리스트 생성 -> 서버 생기면 처음에 받아오는 코드 만들기
    private String URL = "http://15.165.74.84:3000/forum/test";
    private String TAG = "areum/noticeboardactivity";

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.v("알림","onactivity 함수 실행 resultcode : "+resultCode);
        refresh();
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

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_notice);

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.v("noticeboard","스와이프 확인");
                        refresh();
                        refreshLayout.setRefreshing(false); //새로고침
                    }
                }
        );

        Log.v("noticeboard","toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_noticeboard);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v("noticeboard","toolbar 완료");

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

//        myDataset = getfromlocal();
        myDataset = getfromserver();

        // specify an adapter (see also next example)
        mAdapter = new CustomUsersAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);
        Log.v("noticeboard","adapter설정완료");

    }

    public void refresh(){
        //        myDataset = getfromlocal();
        myDataset = getfromserver();
        mAdapter.changeDataset(myDataset);
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(mAdapter);
        Log.v("알림","새로고침 완료");
    }

    public ArrayList<Board> getfromlocal(){
        ArrayList<Board> dataset = new ArrayList<>();
        for(int i = 0;i<5;i++){
            Board b = new Board(i,"이름","title","body\nadfdfdsfdsfdsfdfhdlkjfhdflh\nadsufhduifhdkjfndkjsfhblkjdsa\nhfldkjsfhdlfhdl",10,"0000-00-00");
            dataset.add(b);
        }
        return dataset;
    }

    public ArrayList<Board> getfromserver(){ //retrofit2를 사용하여 서버로 보내는 코드
        Log.v(TAG,"posttoserver 진입완료");
        ArrayList<Board> dataset = new ArrayList<Board>();
        ArrayList<PostResult> post = new ArrayList<>();

        Call<List<PostResult>> call = retrofitIdent.GetInstance().getService().getBoard("forum/test");
        Builder builder = new Builder();
        try {
            dataset = builder.listtryConnect(TAG, call);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.v(TAG,"listtryconnect 완료");
        for(int i=0;i<post.size();i++){
            Log.v(TAG, i+": board 추가");
            PostResult p = post.get(i);
            Board b = new Board(p.getId(),p.getName(),p.getTitle(),p.getBody(),p.getCommentNum(),p.getTime());
            Log.v(TAG,p.getId()+", "+p.getName()+", "+p.getTitle()+", "+p.getBody()+", "+p.getCommentNum()+", "+p.getTime());
            dataset.add(b);
        }
        Log.v(TAG,"getfromserver 완료");

        return dataset;
    }
//
//    public ArrayList<Board> getfromserver(){//서버에서 게시글 표지 부분을 받아오는 코드
//
//        Log.v("알림","getfromserver 확인");
//        ArrayList<Board> dataset = new ArrayList<Board>();
//
//        //테스트 용 111은 서버를 연결하지 않고 시도
////        if(UserIdent.GetInstance().getId() == "111"){
////            Log.v("noticeboard","id == 111");
////            dataset = getfromlocal();
////            return dataset;
////        }
//        //values.put("id","00");
//        //values.put("pw",UserPW)
//
//       NetworkTask getboardlist_networkTask = new NetworkTask(URL,null); //networktast 설정 부분
//        Log.v("NoticeBoardActivity","networktask 입력 성곧");
//        try {
//            getboardlist_networkTask.execute().get(); //설정한 networktask 실행
//            Log.v("NoticeBoardActiviy","get 실행 완료");
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//            Log.v("NoticeBoardActivity", "executionexception");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            Log.v("NoticeBoardActivity", "interruptedexception");
//        }
//        Log.v("noticeboard","execute 확인");
//        String result = getboardlist_networkTask.result;
//        Log.v("noticeboard","result 확인 result : "+result);
//
//        if(result == null){
//            Log.v("noticeboard","통신실패");
//            Toast.makeText(getApplicationContext(), "연결에 실패했습니다. 네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
//            return dataset;
//        }
//
//        JSONArray jsonArray = null;
//        try {
//            jsonArray = new JSONArray(result);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.v("getfromserver","json으로 변환");
//
//        Log.v("getfromserver","board 변환 작업 시작");
//        if(jsonArray == null){
//            Log.v("getfrom","가져오지 못함");
//        }else {
//            for (int i = 0; i < jsonArray.length(); i++) {
//
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = jsonArray.getJSONObject(i);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                String id = null;
//                String userId = null;
//                String title = null;
//                String content = null;
//                String time = null;
//                String commentnum = null;
//                try {
//                    id = jsonObject.getString("id");
//                    Log.v("Noticeboard","id : "+id);
//                    userId = jsonObject.getString("UserName");
//                    Log.v("Noticeboard","userid : "+userId);
//                    title = jsonObject.getString("Title");
//                    Log.v("Noticeboard","title  : "+title);
//                    content = jsonObject.getString("Content");
//                    Log.v("Noticeboard","content : "+content);
//                    commentnum = jsonObject.getString("CommentNum");
//                    Log.v("Board 통신", "commentnum : "+ commentnum);
//                    time = jsonObject.getString("createdAt");
//                    Log.v("Board 통신", "createdAt : "+ time);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Log.v("getfromserver", i + " 값 가져오기 성공");
//                Board b = new Board(Integer.parseInt(id), userId, title, content, Integer.parseInt(commentnum),time);
//                Log.v("getfromserver", "board 추가 완료");
//                dataset.add(b);
//            }
//        }
//        return dataset;
//    }

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

