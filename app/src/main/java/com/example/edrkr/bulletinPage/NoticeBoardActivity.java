package com.example.edrkr.bulletinPage;

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

import com.example.edrkr.a_Network.GetResult;
import com.example.edrkr.a_Network.RetrofitService;
import com.example.edrkr.a_Network.retrofitIdent;
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
    private ArrayList<Board> myDataset = new ArrayList<>(); //리사이클러뷰에 표시할 데이터 리스트 생성
    private String TAG = "areum/noticeboardactivity"; //log에 사용하는 tag 생성

    protected void onActivityResult(int requestCode, int resultCode, Intent data){ //다른 intent 갔다가 돌아왔을 경우 실행하는 함수
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
                Board b = myDataset.get(pos); //해당 viewholder 번호 가져옴.

                // show_each_board로 넘어감
                Intent intent = new Intent(NoticeBoardActivity.this, com.example.edrkr.bulletinPage.show_each_board.class);
                Log.v(TAG,"pos번호 : " + b.getPos());
                intent.putExtra("pos",b.getPos()); //해당 게시글 번호 저장
                Log.v(TAG,"Board값 전송 완료");
                startActivityForResult(intent,1); //intent 변경
                Log.v(TAG,"intent 전송 완료");
            }
        });

    }
    public void InitializeView(){ //버튼 등 view 연결해주는 함수
        fab = (FloatingActionButton)findViewById(R.id.fab_write);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){ //+ 버튼 클릭시 실행
                Log.v(TAG,"글쓰기 버튼 눌림");
                Intent intent = new Intent(NoticeBoardActivity.this, WritingActivity.class);
                startActivityForResult(intent,1); //writing activity에서 값을 다시 받아오기 위해서 사용
            }
        });

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_notice); //끌어 당겨서 새로고침하는 레이아웃

        refreshLayout.setOnRefreshListener( //스와이프 되었을 때 실행
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
        recyclerView.setHasFixedSize(true); //layout 사이즈 고정

        // use a linear layout manager(recyclerview 사용을 위한 설정 단계)
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CustomUsersAdapter(myDataset); //새로운 adapter 생성 - recyclerview를 작동 시킴
        recyclerView.setAdapter(mAdapter); //설정해둔 adapter 적용
        refresh(); //새로고침
        Log.v(TAG,"모두 완료");
    }

    public void refresh(){ //새로 고침 함수
        //myDataset = getfromlocal(); //로컬
        getBoardData(); //비동기 retrofit
        //recyclerview의 adapter를 새로 설정
        mAdapter.changeDataset(myDataset);
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(mAdapter);
        Log.v(TAG,"새로고침 완료");
    }

    public ArrayList<Board> getfromlocal(){ //로컬 생성 함수 - 테스트 용
        ArrayList<Board> dataset = new ArrayList<>();
        for(int i = 0;i<5;i++){
            Board b = new Board(i,"이름","title","body\nadfdfdsfdsfdsfdfhdlkjfhdflh\nadsufhduifhdkjfndkjsfhblkjdsa\nhfldkjsfhdlfhdl",10,"0000-00-00");
            dataset.add(b);
        }
        return dataset;
    }

    public void getBoardData(){ //게시글에 대한 데이터를 받아오는 함수
        myDataset = new ArrayList<Board>();
        final ArrayList<Board> dataset = new ArrayList<Board>();

        Log.v(TAG,"getBoardData 진입완료");

        //레트로핏 통신 기다리게 바꾸기
        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.getBoard("forum/test").enqueue(new Callback<List<GetResult>>() {
            @Override
            public void onResponse(Call<List<GetResult>> call, Response<List<GetResult>> response) { //서버와 통신하여 반응이 왔다면
                if(response.isSuccessful()){
                    List<GetResult> datas = response.body();
                    Log.v(TAG,response.body().toString());
                    if(datas != null){
                        Log.v(TAG, "datas 받아오기 완료 datas.size = " +datas.size());
                        for(int i = 0;i<datas.size();i++){
                            Log.v(TAG,"data" + i + datas.get(i).getTitle()+"");
                            //받아온 데이터 Board 클래스에 저장
                            Board b = new Board(i,datas.get(i).getName(),datas.get(i).getTitle(),datas.get(i).getBody(),datas.get(i).getCommentNum(),datas.get(i).getTime());
                            Log.v(TAG,"board 생성 완료");
                            dataset.add(b); //저장한 Board 클래스 arraylist에 넣음.
                        }
                        Log.v(TAG,"getData2 end================================");
                        Log.v(TAG,"dataset 크기 : "+dataset.size());
                        //adapter 설정
                        mAdapter.changeDataset(dataset);
                        recyclerView.removeAllViewsInLayout();
                        recyclerView.setAdapter(mAdapter);
                        Log.v(TAG,"recyclerview 적용");
                        myDataset = dataset; //Board 데이터 셋을 서버를 통해 받은 데이터 셋으로 변경
                    }
                }else{
                    Log.v(TAG, "onResponse: 실패");
                }
            }
            @Override
            public void onFailure(Call<List<GetResult>> call, Throwable t) { //통신에 실패했을 경우
                Log.v(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){ //optionmenu 생성코드
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.noticeboard_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){ //option 선택될 경우
        switch (item.getItemId()){
            case R.id.notice_patch: //수정
                Log.v(TAG+"noticeboard_menu","patch클릭");
                Toast.makeText(this,"patch onclick",Toast.LENGTH_SHORT).show();
                break;
            case R.id.notice_delete: //삭제
                Log.v(TAG+"noticeboard_menu","delete클릭");
                Toast.makeText(this,"delete onclick",Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home: //x버튼
                Log.v(TAG+"noticeboard_menu","home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.notice_search: //검색
                Log.v(TAG+"noticeboard_menu","search");
                Toast.makeText(this,"search onclick",Toast.LENGTH_SHORT).show();
                break;
            default: //그 외 - 오류
                Log.v(TAG+"noticeboard_menu","default");
                Toast.makeText(this,"오류 발생",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

