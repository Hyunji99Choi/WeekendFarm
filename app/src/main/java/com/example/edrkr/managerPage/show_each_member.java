package com.example.edrkr.managerPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.edrkr.R;
import com.example.edrkr.a_Network.Class.manager.GetAllMember;
import com.example.edrkr.a_Network.Class.manager.GetUserEachFarm;
import com.example.edrkr.a_Network.Class.manager.InputFarm;
import com.example.edrkr.a_Network.RetrofitService;
import com.example.edrkr.a_Network.retrofitIdent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class show_each_member extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private Button reset;
    private TextView name;
    private TextView id;
    private TextView pw;
    private int num = -1;
    private TextView phonenumber;
    private ActionBar actionBar;
    private stringadapter mAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Member> myDataset = new ArrayList<>();
    private String TAG = "areum/show_each_member";
    private int userid = -1;
    private int farmid = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managerpage_each_member);
        this.InitializeView(); //필요 요소 선언해주는 함수
        SetListener(); //리스너 설정 함수
    }


    public void InitializeView(){
        fab = (FloatingActionButton)findViewById(R.id.fab_show_each_member);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.v(TAG,"+ 버튼 눌림");
                Intent intent = new Intent(show_each_member.this, SelectArea.class);
                intent.putExtra("userid",userid);
                startActivityForResult(intent,1); //writing activity에서 값을 다시 받아오기 위해서 사용
            }
        });

        Log.v(TAG,"toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_eachmember);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v(TAG,"toolbar 완료");

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

        Intent intent = getIntent();
        userid = intent.getIntExtra("id", -1);

        if(userid <0){
            Toast.makeText(this,"통신 실패 - 나중에 다시 시도해주세요",Toast.LENGTH_SHORT).show();
            finish();
        }
        Log.v(TAG,"userid : "+userid);

        getfromserver();
        //testData();

        // specify an adapter (see also next example)

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(show_each_member.this);
        alertDialogBuilder.setMessage("정말 삭제하시겠습니까?");
        alertDialogBuilder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG,"삭제");
                if(num != -1){
                    DeleteFarm(num);
                }else{
                    Toast.makeText(getApplicationContext(),"통신오류.",Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG,"최소");
                Toast.makeText(getApplicationContext(),"취소되었습니다.",Toast.LENGTH_LONG).show();
            }
        });

        mAdapter = new stringadapter(getApplicationContext(), myDataset,2);
        mAdapter.setUserId(userid);
        mAdapter.setItemClickListener(new stringadapter.ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
            }

            @Override
            public void DeleteItem(int pos, int uid, int fid) {
                num = pos;
                Log.v(TAG,"num : "+num+" pos : "+pos);
                farmid = fid;
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        recyclerView.setAdapter(mAdapter);
        Log.v(TAG,"adapter설정완료");
    }

    public void SetListener() {
        //inputMethodManger 객체 선언
        View.OnClickListener Listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.buttonresetpw:
                        Log.v(TAG,"비번변경 버튼 클릭");
                        //patch코드
                        pw.setText("0000"); //초기화 코드
                        Toast.makeText(getApplicationContext(),"비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        reset.setOnClickListener(Listener);
    }

    public void testData(){
        ArrayList<Member> result = new ArrayList<>();
        for(int i =0;i<5;i++){
            Member tmp = new Member(i,null,i+"번 밭");
            result.add(tmp);
        }
        myDataset = result;
    }

    public void getfromserver(){//서버에서 게시글 표지 부분을 받아오는 코드
        Log.v(TAG,"getfromserver");
        myDataset = null;
        final ArrayList<Member> dataset = new ArrayList<>();

        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.getUserEachFarm(Integer.toString(userid)).enqueue(new Callback<GetUserEachFarm>() {
            @Override
            public void onResponse(@EverythingIsNonNull Call<GetUserEachFarm> call, @EverythingIsNonNull Response<GetUserEachFarm> response) { //서버와 통신하여 반응이 왔다면
                if(response.isSuccessful()){
                    GetUserEachFarm datas = response.body();
                    Log.v(TAG,"isSuccessful");
                    Log.v(TAG,response.body().toString());
                    if(datas != null){
                        name.setText(datas.getUsername());
                        id.setText(Integer.toString(datas.getUserid()));
                        phonenumber.setText(datas.getPhonenum());
                        Log.v(TAG, "getMember 받아오기 완료 datas.size = " +datas.getFarmid().size());
                        for(int i = 0;i<datas.getFarmid().size();i++){
                            Log.v(TAG,"getMember" + datas.getFarname().get(i));
                            //받아온 데이터 Member 클래스에 저장
                            Member m = new Member(datas.getFarmid().get(i),null, datas.getFarname().get(i));
                            dataset.add(m); //저장한 Board 클래스 arraylist에 넣음.
                        }
                        Log.v(TAG,"getMember end================================");
                        Log.v(TAG,"recyclerview 적용");
                        myDataset = dataset; //Board 데이터 셋을 서버를 통해 받은 데이터 셋으로 변경

                        //adapter 설정
                        mAdapter.changeDataset(myDataset);
                        recyclerView.removeAllViewsInLayout();
                        recyclerView.setAdapter(mAdapter);
                    }
                }else{
                    Log.v(TAG, "onResponse: 실패");

                    //adapter 설정
                    mAdapter.changeDataset(myDataset);
                    recyclerView.removeAllViewsInLayout();
                    recyclerView.setAdapter(mAdapter);
                }
            }
            @Override
            public void onFailure(@EverythingIsNonNull Call<GetUserEachFarm> call, @EverythingIsNonNull  Throwable t) { //통신에 실패했을 경우
                Log.v(TAG, "onFailure: " + t.getMessage());
                //adapter 설정
                mAdapter.changeDataset(myDataset);
                recyclerView.removeAllViewsInLayout();
                recyclerView.setAdapter(mAdapter);
            }
        });
    }

    public void DeleteFarm(int position){
        Log.v(TAG,"pos : "+position+" delete클릭");
        if(userid <= 0 && farmid <= 0){
            Toast.makeText(getApplicationContext(),"통신오류 - 잠시후에 시도해주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.deletFarmUser(Integer.toString(userid), Integer.toString(farmid)).enqueue(new Callback<Void>() {
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
                getfromserver();
                Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_LONG).show();
                Log.v(TAG, content);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.v(TAG, "onFailure: " + t.getMessage());
            }
        });
        num = -1;
    }

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
