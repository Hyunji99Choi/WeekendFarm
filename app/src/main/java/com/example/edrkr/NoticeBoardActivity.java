package com.example.edrkr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class NoticeBoardActivity extends Activity implements LifecycleObserver {
    //private ImageButton write;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private CustomUsersAdapter mAdapter;
    private LinearLayoutManager layoutManager;
//    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Board> myDataset = new ArrayList<>(); //리사이클러뷰에 표시할 데이터 리스트 생성 -> 서버 생기면 처음에 받아오는 코드 만들기
    private String URL = "http://52.79.237.95:3000/forum/test";

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.v("알림","onactivity 함수 실행 resultcode : "+resultCode);

        myDataset = getfromserver();
        mAdapter.changeDataset(myDataset);
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(mAdapter);
        Log.v("알림","새로고침 완료");

//        switch(resultCode){
//            case 2: //writing activity에서 신호를 받을 경우
//                Log.v("알림","2 noticeboard에서 신호를 잡음");
////                Board b = (Board)data.getSerializableExtra("Board");
////                Log.v("알림","board 값 잘 받아옴");
////                myDataset.add(b);
////                Log.v("알림","데이터 값 추가 완료");
//                recyclerView.removeAllViewsInLayout();;
//                recyclerView.setAdapter(mAdapter);
//                Log.v("알림","새로고침 완료");
//                break;
//
//            case 1:
//                Log.v("알림","1 noticeboard에서 신호를 잡음");
////                String test = data.getStringExtra("test");
////                Log.v("알림",test+"정상 프린트");
////                Board board = (Board)data.getSerializableExtra("show_Board");
////                Log.v("알림","Board값 잘 받음 / board의 body : "+board.getBody());
////                myDataset.set(board.getPos(),board);
////                Log.v("알림","mydataset 변경완료");
//                recyclerView.removeAllViewsInLayout();;
//                recyclerView.setAdapter(mAdapter);
//                break;
//        }
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
        //list 초기화
        /*for(int i=0;i<10;i++){
            Board b = new Board();
            b.setTitle(String.format("title %d",i));
            b.setBody(String.format("body %d",i));
            myDataset.add(b);
         }*/

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
        myDataset = getfromserver();

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

