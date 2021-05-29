package com.example.edrkr.managerPage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.R;
import com.example.edrkr.a_Network.Class.manager.GetAllMember;
import com.example.edrkr.a_Network.RetrofitService;
import com.example.edrkr.a_Network.retrofitIdent;
import com.example.edrkr.mainpage.ControlMonitoring;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class show_each_areahas extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private stringadapter mAdapter;
    private Toolbar toolbar;
    private int num = -1;
    private LinearLayoutManager layoutManager;
    private ArrayList<Member> myDataset = new ArrayList<>();
    private String TAG = "areum/show_each_areahas";
    private int farmid;
    private int userid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managerpage_areahas);
        this.InitializeView(); //필요 요소 선언해주는 함수
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.v(TAG,"request : "+requestCode+" result : "+resultCode);
        if(requestCode == 0){
            if(resultCode == 1){
                Log.v(TAG,"getfromserver");
                getfromserver();
            }
        }
    }

    public void InitializeView(){
        // write = (ImageButton)findViewById(R.id.fab_write);
        fab = (FloatingActionButton)findViewById(R.id.fab_areaAdd);

        //fab 버튼의 색깔을 toolbar 색과 같도록 만드는 코드 - 작동안함.
        ColorStateList colorStateList = ContextCompat.getColorStateList(this, ControlMonitoring.GetInstance().getToolbarColor());
        fab.setBackgroundTintList(colorStateList);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.v(TAG,"+ 버튼 눌림");
                Intent intent = new Intent(show_each_areahas.this, SelectMember.class);
                intent.putExtra("farmid",farmid);
                startActivityForResult(intent,0); //writing activity에서 값을 다시 받아오기 위해서 사용
            }
        });

        Log.v(TAG,"toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_eachareahas);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, ControlMonitoring.GetInstance().getToolbarColor()));
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("소유자 목록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v(TAG,"toolbar 완료");

        recyclerView =(RecyclerView) findViewById(R.id.recycler_areahaslist);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        farmid = intent.getIntExtra("id", -1);

        if(farmid <0){
            Toast.makeText(this,"통신 실패 - 나중에 다시 시도해주세요",Toast.LENGTH_SHORT).show();
            finish();
        }

        getfromserver();
        //testData();

        // specify an adapter (see also next example)
        mAdapter = new stringadapter(getApplicationContext(), myDataset,3);
        mAdapter.setFarmId(farmid);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(show_each_areahas.this);
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

        mAdapter.setItemClickListener(new stringadapter.ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
            }

            @Override
            public void DeleteItem(int pos, int uid, int fid) {
                num = pos;
                userid = uid;
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        recyclerView.setAdapter(mAdapter);
        Log.v(TAG,"adapter설정완료");


    }
    public void testData(){
        ArrayList<Member> tmp = new ArrayList<Member>();
        for(int i =0;i<5;i++){
            Member add = new Member(i,null, i+"번째 사람");
            tmp.add(add);
        }
        myDataset = tmp;
    }



    public void getfromserver(){//서버에서 게시글 표지 부분을 받아오는 코드
        Log.v(TAG,"getfromserver");
        myDataset = null;
        final ArrayList<Member> dataset = new ArrayList<>();

        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.getEachFarmUser(Integer.toString(farmid)).enqueue(new Callback<List<GetAllMember>>() {
            @Override
            public void onResponse(@EverythingIsNonNull Call<List<GetAllMember>> call, @EverythingIsNonNull Response<List<GetAllMember>> response) { //서버와 통신하여 반응이 왔다면
                if(response.isSuccessful()){
                    List<GetAllMember> datas = response.body();
                    Log.v(TAG,response.body().toString());
                    if(datas != null){
                        Log.v(TAG, "getMember 받아오기 완료 datas.size = " +datas.size());
                        for(int i = 0;i<datas.size();i++){
                            Log.v(TAG,"getMember" + datas.get(i));
                            //받아온 데이터 Member 클래스에 저장
                            Member m = new Member(datas.get(i).getIdent(),datas.get(i).getUserid(),datas.get(i).getUsername());
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
            public void onFailure(@EverythingIsNonNull Call<List<GetAllMember>> call, @EverythingIsNonNull  Throwable t) { //통신에 실패했을 경우
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
                Log.v("show_each_areahas","home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
