package com.example.edrkr.Bulletin;

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

import com.example.edrkr.DTO.GetResult;
import com.example.edrkr.DTO.RetrofitService;
import com.example.edrkr.DTO.retrofitIdent;
import com.example.edrkr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
        Log.v(TAG,"onactivity 함수 실행 resultcode : "+resultCode);
        refresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"oncreate진입");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        this.InitializeView(); //필요 요소 선언해주는 함수
        mAdapter.setOnItemClickListener(new CustomUsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) { //recyclerview 각 viewholder 클릭할 경우
                Log.v(TAG,"게시글 클릭 리스너 눌림 pos : "+pos);
                Board b = myDataset.get(pos);
                Intent intent = new Intent(NoticeBoardActivity.this, show_each_board.class);

                Log.v(TAG,"pos번호 : " + b.getPos());
                intent.putExtra("pos",b.getPos());
                Log.v(TAG,"Board값 전송 완료");
                startActivityForResult(intent,1);
                Log.v(TAG,"intent 전송 완료");
            }
        });

    }
    public void InitializeView(){
        fab = (FloatingActionButton)findViewById(R.id.fab_write);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.v(TAG,"글쓰기 버튼 눌림");
                Intent intent = new Intent(NoticeBoardActivity.this, WritingActivity.class);
                startActivityForResult(intent,1); //writing activity에서 값을 다시 받아오기 위해서 사용
            }
        });

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_notice);

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.v(TAG,"스와이프 확인");
                        refresh();
                        refreshLayout.setRefreshing(false); //새로고침
                    }
                }
        );

        Log.v(TAG,"toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_noticeboard);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v(TAG,"toolbar 완료");

        recyclerView =(RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        Log.v(TAG,"다시 돌아옴");
        Log.v(TAG,"mydataset size 0 이상");
        mAdapter = new CustomUsersAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);
        refresh();
        Log.v(TAG,"모두 완료");
    }

    public void refresh(){
        //myDataset = getfromlocal(); //로컬
        getBoardData(); //비동기 retrofit
        mAdapter.changeDataset(myDataset);
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(mAdapter);
        Log.v(TAG,"새로고침 완료");
    }

    public ArrayList<Board> getfromlocal(){
        ArrayList<Board> dataset = new ArrayList<>();
        for(int i = 0;i<5;i++){
            Board b = new Board(i,"이름","title","body\nadfdfdsfdsfdsfdfhdlkjfhdflh\nadsufhduifhdkjfndkjsfhblkjdsa\nhfldkjsfhdlfhdl",10,"0000-00-00");
            dataset.add(b);
        }
        return dataset;
    }

    public void getBoardData(){
        myDataset = new ArrayList<Board>();
        final ArrayList<Board> dataset = new ArrayList<Board>();

        Log.v(TAG,"getBoardData 진입완료");

        //레트로핏 통신 기다리게 바꾸기
        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.getBoard("forum/test").enqueue(new Callback<List<GetResult>>() {
            @Override
            public void onResponse(Call<List<GetResult>> call, Response<List<GetResult>> response) {
                if(response.isSuccessful()){
                    List<GetResult> datas = response.body();
                    Log.v(TAG,response.body().toString());
                    if(datas != null){
                        Log.v(TAG, "datas 받아오기 완료 datas.size = " +datas.size());
                        for(int i = 0;i<datas.size();i++){
                            Log.v(TAG,"data" + i + datas.get(i).getTitle()+"");
                            Board b = new Board(i,datas.get(i).getName(),datas.get(i).getTitle(),datas.get(i).getBody(),datas.get(i).getCommentNum(),datas.get(i).getTime());
                            Log.v(TAG,"board 생성 완료");
                            dataset.add(b);
                        }
                        Log.v(TAG,"getData2 end================================");
                        Log.v(TAG,"dataset 크기 : "+dataset.size());
                        mAdapter.changeDataset(dataset);
                        recyclerView.removeAllViewsInLayout();
                        recyclerView.setAdapter(mAdapter);
                        Log.v(TAG,"recyclerview 적용");
                        myDataset = dataset;
                    }
                }else{
                    Log.v(TAG, "onResponse: 실패");
                }
            }
            @Override
            public void onFailure(Call<List<GetResult>> call, Throwable t) {
                Log.v(TAG, "onFailure: " + t.getMessage());
            }
        });
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
                Log.v(TAG+"noticeboard_menu","patch클릭");
                Toast.makeText(this,"patch onclick",Toast.LENGTH_SHORT).show();
                break;
            case R.id.notice_delete:
                Log.v(TAG+"noticeboard_menu","delete클릭");
                Toast.makeText(this,"delete onclick",Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                Log.v(TAG+"noticeboard_menu","home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.notice_search:
                Log.v(TAG+"noticeboard_menu","search");
                Toast.makeText(this,"search onclick",Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.v(TAG+"noticeboard_menu","default");
                Toast.makeText(this,"오류 발생",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

