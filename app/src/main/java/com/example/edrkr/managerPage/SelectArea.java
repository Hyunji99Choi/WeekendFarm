package com.example.edrkr.managerPage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.a_Network.Class.manager.GetAllFarm;
import com.example.edrkr.a_Network.Class.manager.inputFarm;
import com.example.edrkr.a_Network.RetrofitService;
import com.example.edrkr.a_Network.retrofitIdent;
import com.example.edrkr.R;
import com.example.edrkr.mainpage.ControlMonitoring;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class SelectArea extends AppCompatActivity { //밭 선택해서 추가하는 페이지 코드
    private RecyclerView recyclerView;
    private stringadapter mAdapter;
    private LinearLayoutManager layoutManager;
    private Toolbar toolbar;
    private ArrayList<Member> myDataset = new ArrayList<>();
    private String URL = "manage/allFarmInfo/"; //서버 주소
    private String TAG = "areum/SelectArea";
    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managerpage_selectarea);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_selectArea);
//        recycler_test(); //테스트용 데이터 저장
        Log.v(TAG, "recyclerview id 연결");

        Intent intent = getIntent();
        userid = intent.getIntExtra("userid",-1);
        if(userid < 0){
            Toast.makeText(this,"통신 실패 - 나중에 다시 시도해주세요",Toast.LENGTH_SHORT).show();
            finish();
        }
        Log.v(TAG,"first - userid : "+userid);

        getfromserver();
        Log.v(TAG, " toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_selectarea);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, ControlMonitoring.GetInstance().getToolbarColor()));
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("소유밭 추가");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_goout); //뒤로가기 버튼 이미지
        Log.v(TAG, "toolbar 완료");

        recyclerView.setHasFixedSize(true);
        mAdapter = new stringadapter(getBaseContext(), myDataset, 1);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Log.v(TAG, "layout adapter 연결");
        recyclerView.setAdapter(mAdapter);

        mAdapter.setItemClickListener(new stringadapter.ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.v(TAG, "게시글 클릭 리스너 눌림 pos : " + pos);
                if (!myDataset.get(pos).getChecked_()) {
                    myDataset.get(pos).setChecked_(true);
                    v.setBackgroundColor(Color.GREEN);
                } else {
                    myDataset.get(pos).setChecked_(false);
                    v.setBackgroundColor(Color.WHITE);
                }
            }
            @Override
            public void DeleteItem(int pos, int uid, int fid) {

            }
        });
    }

    public void recycler_test() {
        ArrayList<Member> test = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Member tmp = new Member(i, null, "밭" + i);
            test.add(tmp);
        }
        myDataset = test;
    }

    public void getfromserver() {
        Log.v(TAG, "getfromserver");
        myDataset = null;
        final ArrayList<Member> dataset = new ArrayList<>();
        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        Log.v(TAG,"userid : "+userid);
        service.getListofAddFarm(Integer.toString(userid)).enqueue(new Callback<List<GetAllFarm>>() {
            @Override
            public void onResponse(@EverythingIsNonNull Call<List<GetAllFarm>> call, @EverythingIsNonNull Response<List<GetAllFarm>> response) { //서버와 통신하여 반응이 왔다면
                if (response.isSuccessful()) {
                    List<GetAllFarm> datas = response.body();
                    Log.v(TAG, response.body().toString());
                    if (datas != null) {
                        Log.v(TAG, "GetAllFarm 받아오기 완료 datas.size = " + datas.size());
                        for (int i = 0; i < datas.size(); i++) {
                            Log.v(TAG, "GetAllFarm" + datas.get(i).getFarmname());
                            //받아온 데이터 Member 클래스에 저장
                            Member m = new Member(datas.get(i).getFarmid(), null, datas.get(i).getFarmname());
                            dataset.add(m); //저장한 Board 클래스 arraylist에 넣음.
                        }
                        Log.v(TAG, "GetAllFarm end================================");
                        Log.v(TAG, "recyclerview 적용");
                        myDataset = dataset; //Board 데이터 셋을 서버를 통해 받은 데이터 셋으로 변경

                        //adapter 설정
                        mAdapter.changeDataset(myDataset);
                        recyclerView.removeAllViewsInLayout();
                        recyclerView.setAdapter(mAdapter);
                    }
                } else {
                    Log.v(TAG, "onResponse: 실패");

                    //adapter 설정
                    mAdapter.changeDataset(myDataset);
                    recyclerView.removeAllViewsInLayout();
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(@EverythingIsNonNull Call<List<GetAllFarm>> call, @EverythingIsNonNull Throwable t) { //통신에 실패했을 경우
                Log.v(TAG, "onFailure: " + t.getMessage());
                //adapter 설정
                mAdapter.changeDataset(myDataset);
                recyclerView.removeAllViewsInLayout();
                recyclerView.setAdapter(mAdapter);
            }
        });

    }

    public void puttoserver() {
        Log.v(TAG,"puttoserver 진입완료");
        inputFarm list_userid = patchtoserver();
       // Log.v(TAG,"put 완료 size : ");
        Log.v(TAG, "size : "+list_userid.getInputfarm().length);

        Call<String> call = retrofitIdent.GetInstance().getService().PostAddNewFarm(Integer.toString(userid), list_userid);
        call.enqueue(new Callback<String>() { //비동기 작업
            @Override
            public void onResponse(@EverythingIsNonNull Call<String> call, @EverythingIsNonNull  Response<String> response) { //성공 - 메인 스레드에서 처리
                if (response.isSuccessful()) {
                    //정상적으로 통신이 성공한 경우
                    Log.v(TAG, "onResponse: 성공, 결과\n" + response.body().toString());
                    setResult(1);
                    finish();
                } else {
                    //통신이 실패한 경우(응답코드 3xx,4xx 등)
                    Log.d(TAG,  "onResponse: 실패");
                }
            }

            @Override
            public void onFailure(@EverythingIsNonNull Call<String> call,@EverythingIsNonNull  Throwable t) { //실패 - 메인 스레드에서 처리
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적인 이유)
                Log.d(TAG, "onFailure: " +  t.getMessage());
            }
        });
        Log.v(TAG, "tryconnect 완료");
    }

    private inputFarm patchtoserver() {
        ArrayList<Integer> list_userid = new ArrayList<>();
        for(Member m : myDataset){
            if(m.getChecked_()){
                list_userid.add(m.getIdent_());
                Log.v(TAG,"선택 : "+m.getIdent_());
            }
        }
        int[] list_int = new int[list_userid.size()];
        for(int i = 0; i < list_userid.size(); i++) {
            list_int[i] = list_userid.get(i);
        }
        inputFarm tmp = new inputFarm(list_int);
        return tmp;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_manager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.v("selectarea", "home");
                Toast.makeText(this, "home onclick", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.writing_next_button:
                Log.v("selectMember", "선택완료버튼 눌림");
                //patch 코드
                puttoserver();
        }
        return super.onOptionsItemSelected(item);
    }

}