package com.example.edrkr.bulletinPage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.edrkr.a_Network.Builder;
import com.example.edrkr.a_Network.Class.bulletin.GetComment;
import com.example.edrkr.a_Network.Class.bulletin.GetEachBoard;
import com.example.edrkr.a_Network.Class.bulletin.GetBoard;
import com.example.edrkr.a_Network.Class.bulletin.PatchComment;
import com.example.edrkr.a_Network.Class.bulletin.PostComment;
import com.example.edrkr.a_Network.RetrofitService;
import com.example.edrkr.a_Network.retrofitIdent;
import com.example.edrkr.R;
import com.example.edrkr.UserIdent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class show_each_board extends AppCompatActivity {
    private TextView show_title;
    private TextView show_name;
    private TextView show_date;
    private TextView show_body;
    private TextView show_saycount;
    private RecyclerView show_recyclerview;
    private EditText show_EditText;
    private Button show_addbutton;
    private ActionBar actionBar;
    private CommentAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private InputMethodManager manager;
    private int num = -1;
    private String usernickname;
    private Board b = new Board(-1);
    private ArrayList<Comment> myDataset = new ArrayList<>();
    Intent intent;

    private String URL = "forum/";
    private String Edit_URL = "forum/com/";
    private String TAG = "areum/show_each_board";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletinpage_each_board);
        this.InitializeView(); //필요 요소 선언해주는 함수
        this.SetListener(); //리스너 설정 함수
    }

    public void InitializeView(){
        show_title = (TextView) findViewById(R.id.show_title);
        show_name = (TextView) findViewById(R.id.show_name);
        show_date = (TextView) findViewById(R.id.show_date);
        show_body = (TextView) findViewById(R.id.show_body);
        show_saycount = (TextView)findViewById(R.id.show_saycount);
        show_recyclerview = (RecyclerView)findViewById(R.id.show_recyclerview);
        show_EditText = (EditText)findViewById(R.id.show_edittext_write_comment);
        show_addbutton = (Button)findViewById(R.id.show_button_add_comment);
        manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        intent = getIntent();
        usernickname = UserIdent.GetInstance().getNkname();

        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_board);

        refreshLayout.setOnRefreshListener( //끌어당기면 새로고침 코드
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.v(TAG,"스와이프 확인");
                        getBoardData();
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
        int pos = intent.getIntExtra("pos",0); //몇번 게시글이 클릭 되었는지 가져옴.
        Log.v(TAG,"pos : " + pos);
        URL = URL + (pos);
        Log.v(TAG,"URL :"+URL);

        //recyclerview 세팅
        show_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(false);
        show_recyclerview.setLayoutManager(layoutManager);
        getBoardData(); //통신으로 게시글 정보 가져옴

        // specify an adapter (see also next example)
        mAdapter = new CommentAdapter(this,myDataset);
        show_recyclerview.setAdapter(mAdapter);
        Log.v(TAG,"adapter 설정 완료");
    }

    public ArrayList<Comment> getlocal() {  //로컬로 게시글 세팅하는 함수
        ArrayList<Comment> dataset = new ArrayList<>();
        for(int i = 0;i<10;i++){
            Comment c = new Comment(i,"NAME"+i,"BODYdfafdfds\nfdsfdsfdsfdfdfdfddfd\ndfdddfddddfdfdfd123\n4f56d14651461"+i,"0000-00-00");
            dataset.add(c);
        }
        return dataset;
    }

    public void getBoardData(){ //서버에서 게시글 데이터 가져오는 함수
        final ArrayList<Comment> dataset = new ArrayList<>();

        Log.v(TAG,"getBoardData 진입완료");

        //레트로핏 통신 기다리게 바꾸기
        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.getComment(URL).enqueue(new Callback<GetEachBoard>() {
            @Override
            public void onResponse(Call<GetEachBoard> call, Response<GetEachBoard> response) { //통신 성공시
                if(response.isSuccessful()){
                    Log.v(TAG,response.body().toString());
                    GetEachBoard datas = response.body();
                    List<GetBoard> board = datas.getPost(); //게시글 부분
                    List<GetComment> comment = datas.getComment(); //댓글 부분
                    if(board != null) { //게시글 부분 가져오는 코드
                        Log.v(TAG,"board가 null");
                        Board b = new Board(board.get(0).getId(), board.get(0).getName(), board.get(0).getTitle(), board.get(0).getBody(), board.get(0).getCommentNum(), board.get(0).getTime());
                        setView(b);
                    }
                    if(comment != null){ //댓글 가져오는 코드
                        Log.v(TAG, "comment 받아오기 완료 comment.size = " +comment.size());
                        for(int i = 0;i<comment.size();i++){
                            Log.v(TAG,"comment" + comment.get(i).getId() + comment.get(i).getContent()+"");
                            Comment c = new Comment(comment.get(i).getId(),comment.get(i).getUsername(),comment.get(i).getContent(),comment.get(i).getTime());
                            Log.v(TAG,"comment 생성 완료");
                            dataset.add(c);
                        }
                        Log.v(TAG,"comment end================================");
                        Log.v(TAG,"dataset 크기 : "+dataset.size());
                        //recyclerview dataset 변경
                        mAdapter.changeDataset(dataset);
                        show_recyclerview.removeAllViewsInLayout();
                        show_recyclerview.setAdapter(mAdapter);
                        Log.v(TAG,"recyclerview 적용");
                        myDataset = dataset;
                    }
                }else{ //통신은 성공 but, 내부에서 실패
                    Log.v(TAG, "onResponse: 실패");
                }
            }
            @Override
            public void onFailure(Call<GetEachBoard> call, Throwable t) { //통신 아예 실패
                Log.v(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void setView(Board b){   //게시판을 화면에 보이게 하는 함수
        show_title.setText(b.getTitle());
        show_name.setText(b.getName());
        show_date.setText(b.getDate());
        show_body.setText(b.getBody());
        show_saycount.setText(""+b.getChat_count());
        Log.v(TAG+"setview", "board 잘 받음");
    }

    public void sendButtonClicked(View view){
        Log.v(TAG+"setlistener", "작성 버튼 클릭됨");
        if(TextUtils.isEmpty(show_EditText.getText().toString())){
            Log.v(TAG,"빈 댓글");
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("댓글내용을 입력해주세요");
            alertDialogBuilder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            android.app.AlertDialog emptyDialog = alertDialogBuilder.create();
            emptyDialog.show();
        }
        else if(!show_EditText.getText().toString().replace(" ","").equals("")) {
            Log.v(TAG+"setlistener", "add 시작");
            addcomment(); // comment를 board에 추가해주고 recycler view를 새로고침.
            Log.v(TAG+"setlistener", "add 완료");
            show_EditText.setText("");
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void SetListener() { //+버튼 클릭시
        final Dialog editDialog = new Dialog(this);
        editDialog.setContentView(R.layout.dialog_editcomment);

        ImageButton Ok = (ImageButton)editDialog.findViewById(R.id.ok_edit);
        ImageButton Cancle = (ImageButton)editDialog.findViewById(R.id.cancel_edit);

        final EditText content = (EditText)editDialog.findViewById(R.id.edit_content);

        Ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(content.getText().toString().length() >0){
                    setEdit(content.getText().toString());
                    editDialog.dismiss();
                }
            }
        });
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("정말 삭제하시겠습니까?");
        alertDialogBuilder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG,"삭제");
                if(num != -1) {
                    setDelete(myDataset.get(num).getId());
                    Log.v(TAG, myDataset.get(num).getId()+" onDeleteclick 삭제완료");
                    num = -1;
                }else{
                    Log.v(TAG, "통신오류");
                }
            }
        });
        alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG,"최소");
                Toast.makeText(getApplication(),"취소되었습니다.",Toast.LENGTH_LONG).show();
            }
        });

        mAdapter.setSwipeClickListener(new CommentAdapter.onSwipeClickListener(){
            @Override
            public void onDeleteCommentClick(View v, int pos){
                Log.v(TAG, myDataset.get(pos).getName()+", "+usernickname);
                if(myDataset.get(pos).getName() == null ||usernickname == null){
                    Log.v(TAG,"null 확인");
                    return;
                }
                if(myDataset.get(pos).getName().compareTo(usernickname)==0){
                    Log.v(TAG,"onDelete 클릭 pos "+pos);
                    num = pos;
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else {
                    Toast.makeText(getApplication(), "삭제할 권한이 없습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onEditCommentClick(View v, int pos){
                Log.v(TAG, myDataset.get(pos).getName()+", "+usernickname);
                if(myDataset.get(pos).getName() == null ||usernickname == null){
                    Log.v(TAG,"null 확인");
                    return;
                }
                if(myDataset.get(pos).getName().compareTo(usernickname)==0) {
                    Edit_URL+=myDataset.get(pos).getId()+"/";
                    content.setText(myDataset.get(pos).getBody());
                    editDialog.show();
                }else {
                    Toast.makeText(getApplication(), "수정할 권한이 없습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setEdit(String content){
        Log.v(TAG,"patchtoserver 진입완료");
        PatchComment post = new PatchComment();
        post.setContent(content);
        Log.v(TAG,"put 완료");

        if(Edit_URL.compareTo("forum/com/")!=0) {
            Call<PatchComment> call = retrofitIdent.GetInstance().getService().patchComment(Edit_URL, post);
            Builder builder = new Builder();
            try {
                builder.tryPost(call);
                getBoardData();
                Edit_URL = "forum/com/";
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.v(TAG, "tryconnect 완료");
        }else{
            Toast.makeText(this,"잠시후에 다시 시도해주세요",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void setDelete(int pos) {
        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.deleteComment("forum/com/"+pos+"/").enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    //통신이 실패한 경우(응답코드 3xx,4xx 등)
                    Log.d(TAG, "onResponse: 실패");
                    Toast.makeText(getApplicationContext(),"삭제에 실패했습니다. 잠시후에 시도해주세요",Toast.LENGTH_LONG).show();
                    return;
                }
                String content = "";
                content += "code: " + response.code() + "\n";
                content += "정상적으로 삭제되었습니다.";
                getBoardData();
                Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_LONG).show();
                Log.v(TAG, content);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.v(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void posttoserver(){ //retrofit2를 사용하여 댓글을 서버로 보내는 코드
        Log.v(TAG,"posttoserver 진입완료");
        PostComment comment = new PostComment();
        comment.setName( UserIdent.GetInstance().getNkname());
        comment.setContent(show_EditText.getText().toString());
        comment.setUserIdent(UserIdent.GetInstance().getUserIdent());
        Log.v(TAG,"put 완료");

        Call<PostComment> call = retrofitIdent.GetInstance().getService().postComment(URL, comment);
        Builder builder = new Builder();
        try {
            builder.tryPost(call);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.v(TAG,"tryconnect 완료");
    }

    public void addcomment(){ // 댓글 추가 기능
        String tag = "addcomment";
        Log.v(TAG+tag,"addcomment함수 입장");

        //server 통신 성공시 - 서버로 보내는 코드
        posttoserver();
        //server에서 받아오는 코드
        getBoardData();
        //myDataset = b.getComments();

        Log.v(TAG+tag,"mydataset 수정완료");

        show_recyclerview.removeAllViewsInLayout();
        mAdapter = new CommentAdapter(this,myDataset);
        show_recyclerview.setAdapter(mAdapter);
        setView(b);
        Log.v(TAG+tag,"새로고침 완료");
        this.SetListener(); //리스너 설정 함수
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){  //뒤로가기 메뉴 클릭시
            case android.R.id.home:
                Log.v(TAG,"home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
