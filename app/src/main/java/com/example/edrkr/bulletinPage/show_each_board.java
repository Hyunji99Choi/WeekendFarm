package com.example.edrkr.bulletinPage;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.edrkr.a_Network.Builder;
import com.example.edrkr.a_Network.GetComment;
import com.example.edrkr.a_Network.GetEachBoard;
import com.example.edrkr.a_Network.GetResult;
import com.example.edrkr.a_Network.PostComment;
import com.example.edrkr.a_Network.RetrofitService;
import com.example.edrkr.a_Network.retrofitIdent;
import com.example.edrkr.NetworkTask;
import com.example.edrkr.R;
import com.example.edrkr.UserIdent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class show_each_board extends AppCompatActivity {
    private TextView show_title;
    private TextView show_name;
    private TextView show_date;
    private TextView show_body;
    private TextView show_goodcount;
    private TextView show_saycount;
    private RecyclerView show_recyclerview;
    private EditText show_EditText;
    private Button show_addbutton;
    private ActionBar actionBar;
    private CommentAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private InputMethodManager manager;
    private Board b = new Board(-1);
    private ArrayList<Comment> myDataset = new ArrayList<>();
    Intent intent;

    private String URL = "forum/";
    private String TAG = "areum/show_each_board";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_each_board);
        this.InitializeView(); //필요 요소 선언해주는 함수
        this.SetListener(); //리스너 설정 함수
    }

    public void InitializeView(){
        show_title = (TextView) findViewById(R.id.show_title);
        show_name = (TextView) findViewById(R.id.show_name);
        show_date = (TextView) findViewById(R.id.show_date);
        show_body = (TextView) findViewById(R.id.show_body);
        show_goodcount = (TextView)findViewById(R.id.show_goodcount);
        show_saycount = (TextView)findViewById(R.id.show_saycount);
        show_recyclerview = (RecyclerView)findViewById(R.id.show_recyclerview);
        show_EditText = (EditText)findViewById(R.id.show_edittext_write_comment);
        show_addbutton = (Button)findViewById(R.id.show_button_add_comment);
        manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        intent = getIntent();

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_board);

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
        Toolbar toolbar = findViewById(R.id.toolbar_eachboard);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v(TAG,"toolbar 완료");

        Log.v(TAG,"show_each_board 클래스 실행");
        int pos = intent.getIntExtra("pos",0);
        Log.v(TAG,"pos : " + pos);
        URL = URL + (pos+1);
        Log.v(TAG,"URL :"+URL);

        show_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(true);
        show_recyclerview.setLayoutManager(layoutManager);

        //서버 통신 성공 할 시
//        myDataset = getfromserver();
        getBoardData();

        // specify an adapter (see also next example)
        mAdapter = new CommentAdapter(myDataset);
        show_recyclerview.setAdapter(mAdapter);
        Log.v(TAG,"adapter 설정 완료");
    }

    public ArrayList<Comment> getlocal() {
        ArrayList<Comment> dataset = new ArrayList<>();
        for(int i = 0;i<10;i++){
            Comment c = new Comment("NAME"+i,"BODYdfafdfds\nfdsfdsfdsfdfdfdfddfd\ndfdddfddddfdfdfd123\n4f56d14651461"+i,"0000-00-00");
            dataset.add(c);
        }
        return dataset;
    }

    public void getBoardData(){
        final ArrayList<Comment> dataset = new ArrayList<>();

        Log.v(TAG,"getBoardData 진입완료");

        //레트로핏 통신 기다리게 바꾸기
        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.getComment(URL).enqueue(new Callback<GetEachBoard>() {
            @Override
            public void onResponse(Call<GetEachBoard> call, Response<GetEachBoard> response) {
                if(response.isSuccessful()){
                    Log.v(TAG,response.body().toString());
                    GetEachBoard datas = response.body();
                    List<GetResult> board = datas.getPost();
                    List<GetComment> comment = datas.getComment();
                    if(board != null) {
                        Board b = new Board(board.get(0).getId(), board.get(0).getName(), board.get(0).getTitle(), board.get(0).getBody(), board.get(0).getCommentNum(), board.get(0).getTime());
                        setView(b);
                    }
                    if(comment != null){
                        Log.v(TAG, "comment 받아오기 완료 comment.size = " +comment.size());
                        for(int i = 0;i<comment.size();i++){
                            Log.v(TAG,"comment" + i + comment.get(i).getContent()+"");
                            Comment c = new Comment(comment.get(i).getUsername(),comment.get(i).getContent(),comment.get(i).getTime());
                            Log.v(TAG,"comment 생성 완료");
                            dataset.add(c);
                        }
                        Log.v(TAG,"getData2 end================================");
                        Log.v(TAG,"dataset 크기 : "+dataset.size());
                        mAdapter.changeDataset(dataset);
                        show_recyclerview.removeAllViewsInLayout();
                        show_recyclerview.setAdapter(mAdapter);
                        Log.v(TAG,"recyclerview 적용");
                        myDataset = dataset;
                    }
                }else{
                    Log.v(TAG, "onResponse: 실패");
                }
            }
            @Override
            public void onFailure(Call<GetEachBoard> call, Throwable t) {
                Log.v(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void refresh(){
        //        myDataset = getfromlocal();
//        myDataset = getfromserver();
        getBoardData();
//        mAdapter.changeDataset(myDataset);
//        show_recyclerview.removeAllViewsInLayout();
//        show_recyclerview.setAdapter(mAdapter);
//        Log.v(TAG+"refresh","새로고침 완료");
    }

    public void setView(Board b){
        show_title.setText(b.getTitle());
        show_name.setText(b.getName());
        show_date.setText(b.getDate());
        show_body.setText(b.getBody());
        show_saycount.setText(""+b.getChat_count());
        show_goodcount.setText(""+b.getGood_count());
        Log.v(TAG+"setview", "board 잘 받음");
    }

    public void SetListener() {
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.show_button_add_comment:
                        Log.v(TAG+"setlistener", "작성 버튼 클릭됨");
                        if(!show_EditText.getText().toString().replace(" ","").equals("")) {
                            addcomment(); // comment를 board에 추가해주고 recycler view를 새로고침.
                            Log.v(TAG+"setlistener", "add 완료");
                            show_EditText.setText("");
                            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                }
            }
        };
        show_addbutton.setOnClickListener(Listener);
    }

    public void posttoserver(){ //retrofit2를 사용하여 댓글을 서버로 보내는 코드
        Log.v(TAG,"posttoserver 진입완료");
        PostComment comment = new PostComment();
        comment.setName( UserIdent.GetInstance().getNkname());
        comment.setContent(show_EditText.getText().toString());
//        comment.setId(Integer.toString(b.getPos()));
        Log.v(TAG,"put 완료");

        Call<PostComment> call = retrofitIdent.GetInstance().getService().postComment(URL, comment);
        Builder builder = new Builder();
        try {
            builder.tryConnect(TAG, call);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.v(TAG,"tryconnect 완료");
    }

    public void sendtoserver(){ //서버로 보내는 코드
        String tag ="sendtoserver";
        Log.v(TAG+tag,"sendto server확인");

        ContentValues values = new ContentValues();
        String name = UserIdent.GetInstance().getNkname();
        Log.v(TAG+tag,"name : "+ name );
        String content = show_EditText.getText().toString();
        Log.v(TAG+tag,"content : "+content );
        if(b.getPos() == -1){
            Log.v(TAG+tag,"통신실패");
            Toast.makeText(getApplicationContext(), "연결에 실패했습니다. 네트워크를 확인해주세요", Toast.LENGTH_LONG).show();
            return;
        }
        String id = Integer.toString(b.getPos());
        Log.v(TAG+tag,"id : "+id );

        values.put("name",name);
        values.put("content",content);
        values.put("id",id);
        Log.v(TAG+tag,"값 지정 완료 name : "+name+"content : "+content + "id : "+id);

        NetworkTask sendComment_networkTask = new NetworkTask(URL,values); //networktast 설정 부분
        try {
            sendComment_networkTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.v(TAG+tag,"전송완료");

        // String result = [{"id":1,"userName":null,"userId":"ghd8119","title","fsadkfjd","content","dafsdf"},{"id":1,"userName":null,"userId":"ghd8119","title","fsadkfjd","content","dafsdf"}
    }

    public void addcomment(){ // 댓글 추가 기능
        String tag = "addcomment";
        Log.v(TAG+tag,"addcomment함수 입장");

        //server 통신 성공시 - 서버로 보내는 코드
        posttoserver();
        //server에서 받아오는 코드
        refresh();
        //myDataset = b.getComments();

        Log.v(TAG+tag,"mydataset 수정완료");

        show_recyclerview.removeAllViewsInLayout();
        mAdapter = new CommentAdapter(myDataset);
        show_recyclerview.setAdapter(mAdapter);
        setView(b);
        Log.v(TAG+tag,"새로고침 완료");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){

            case android.R.id.home:
                Log.v(TAG,"home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
