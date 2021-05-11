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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.a_Network.Builder;
import com.example.edrkr.a_Network.Class.manager.GetAllMember;
import com.example.edrkr.a_Network.Class.manager.patchAddFarm;
import com.example.edrkr.a_Network.Class.manager.patchAddUser;
import com.example.edrkr.a_Network.RetrofitService;
import com.example.edrkr.a_Network.retrofitIdent;
import com.example.edrkr.bulletinPage.BulletinAdapter;
import com.example.edrkr.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class SelectMember extends AppCompatActivity { //맴버 선택해서 추가하는 코드
    private RecyclerView recyclerView;
    private stringadapter mAdapter;
    private LinearLayoutManager layoutManager;
    private ActionBar actionBar;
    private ArrayList<Member> myDataset = new ArrayList<>();
    private String URL = "manage/allMemberInfo/"; //서버 주소
    private String TAG = "areum/SelectMember";
    int farmid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managerpage_select_member);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_selectmember);

        Intent intent = getIntent();
        farmid = intent.getIntExtra("farmid",-1);
        if(farmid <0){
            Toast.makeText(this,"통신 실패 - 나중에 다시 시도해주세요",Toast.LENGTH_SHORT).show();
            finish();
        }

        Log.v("selctmember","toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_selectmember);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("소유자 추가");
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goout); //뒤로가기 버튼 이미지
        Log.v("selctmember","toolbar 완료");

        getfromserver();
 //       recycler_test(); //테스트용 데이터 저장
        Log.v("SelectMember","recyclerview id 연결");

        recyclerView.setHasFixedSize(true);
        mAdapter = new stringadapter(myDataset,0);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Log.v("SelectMember","layout adapter 연결");
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BulletinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.v("SelectMember","게시글 클릭 리스너 눌림 pos : "+pos);
                if(!myDataset.get(pos).getChecked_()){
                    myDataset.get(pos).setChecked_(true);
                    v.setBackgroundColor(Color.GREEN);
                }else{
                    myDataset.get(pos).setChecked_(false);
                    v.setBackgroundColor(Color.WHITE);
                }
            }
        });
    }

    public void recycler_test(){
        ArrayList<Member> test = new ArrayList<>();
        for(int i = 0;i<20;i++){
            Member tmp = new Member(i,i+"",i+"번째 사용자");
            test.add(tmp);
        }
        myDataset = test;
    }

    public void getfromserver(){
        myDataset = null;
        final ArrayList<Member> dataset = new ArrayList<>();

        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.getListofAddUser(Integer.toString(farmid)).enqueue(new Callback<List<GetAllMember>>() {
            @Override
            public void onResponse(@EverythingIsNonNull Call<List<GetAllMember>> call, @EverythingIsNonNull Response<List<GetAllMember>> response) { //서버와 통신하여 반응이 왔다면
                if (response.isSuccessful()) {
                    List<GetAllMember> datas = response.body();
                    Log.v(TAG, response.body().toString());
                    if (datas != null) {
                        Log.v(TAG, "getMember 받아오기 완료 datas.size = " + datas.size());
                        for (int i = 0; i < datas.size(); i++) {
                            Log.v(TAG, "getMember" + datas.get(i).getId() + " " + datas.get(i).getUserid() + " " + datas.get(i).getUsername());
                            //받아온 데이터 Member 클래스에 저장
                            Member m = new Member(datas.get(i).getId(),datas.get(i).getUserid(), datas.get(i).getUsername());
                            dataset.add(m); //저장한 Board 클래스 arraylist에 넣음.
                        }
                        Log.v(TAG, "getMember end================================");
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
            public void onFailure(@EverythingIsNonNull Call<List<GetAllMember>> call,@EverythingIsNonNull  Throwable t) { //통신에 실패했을 경우
                Log.v(TAG, "onFailure: " + t.getMessage());
                //adapter 설정
                mAdapter.changeDataset(myDataset);
                recyclerView.removeAllViewsInLayout();
                recyclerView.setAdapter(mAdapter);
            }
        });
    }

    public void puttoserver() {
        Log.v(TAG,"patchtoserver 진입완료");
        ArrayList<Integer> list_farmid = patchtoserver();
        Log.v(TAG,"put 완료");

        Call<List<Integer>> call = retrofitIdent.GetInstance().getService().PostAddNewUser(Integer.toString(farmid), list_farmid);
        Builder builder = new Builder();
        try {
            builder.tryPost(call);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v(TAG, "tryconnect 완료");
    }

    private ArrayList<Integer> patchtoserver() {
        ArrayList<Integer> list_farmid = new ArrayList<>();
        for(Member m : myDataset){
            if(m.getChecked_()){
                list_farmid.add(m.getId_());
            }
        }
        return list_farmid;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_writing,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Log.v("selectmember","home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.writing_next_button:
                Log.v("selectMember","선택완료버튼 눌림");
                //patch 코드
                puttoserver();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}