package com.example.edrkr.managerPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edrkr.a_Network.Class.manager.GetAllMember;
import com.example.edrkr.a_Network.RetrofitService;
import com.example.edrkr.a_Network.retrofitIdent;
import com.example.edrkr.bulletinPage.BulletinAdapter;
import com.example.edrkr.NetworkTask;
import com.example.edrkr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

//listofarea와 동일
public class Listofmember extends Fragment { //사용자별 밭 보여주는 fragment
    private RecyclerView recyclerView;
    private stringadapter mAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Member> myDataset = new ArrayList<>();
    private String URL = "manage/allMemberInfo/"; //서버 주소
    private String TAG = "areum/Listofmember";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "member 도착");
        View view = inflater.inflate(R.layout.managerpage_listof_member, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_memberlist);

//        getfromserver();
        recycler_test();
        recyclerView.setHasFixedSize(true);
        mAdapter = new stringadapter(myDataset, 0);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BulletinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.v(TAG, "게시글 클릭 리스너 눌림 pos : " + pos);
                Member s = myDataset.get(pos);
                Intent intent = new Intent(getActivity(), show_each_member.class);

                intent.putExtra("id", s.getId_());
                startActivityForResult(intent, 1);
                Log.v(TAG, "intent 전송 완료");
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        recycler_test();
        // getfromserver(); //서버와 통신
    }

    public void recycler_test() {
        ArrayList<Member> test = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Member tmp = new Member(i,Integer.toString(i), "사람" + i);
            test.add(tmp);
        }
        myDataset = test;
    }

    public void getfromserver() {//서버에서 게시글 표지 부분을 받아오는 코드
        myDataset = null;
        final ArrayList<Member> dataset = new ArrayList<>();

        RetrofitService service = retrofitIdent.GetInstance().getRetrofit().create(RetrofitService.class); //레트로핏 인스턴스로 인터페이스 객체 구현
        service.getAllUser(URL).enqueue(new Callback<List<GetAllMember>>() {
            @Override
            public void onResponse(@EverythingIsNonNull Call<List<GetAllMember>> call,@EverythingIsNonNull  Response<List<GetAllMember>> response) { //서버와 통신하여 반응이 왔다면
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
                    recycler_test(); //테스트용 데이터 저장 - local

                    //adapter 설정
                    mAdapter.changeDataset(myDataset);
                    recyclerView.removeAllViewsInLayout();
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(@EverythingIsNonNull Call<List<GetAllMember>> call,@EverythingIsNonNull  Throwable t) { //통신에 실패했을 경우
                Log.v(TAG, "onFailure: " + t.getMessage());
                recycler_test(); //테스트용 데이터 저장 - local
                //adapter 설정
                mAdapter.changeDataset(myDataset);
                recyclerView.removeAllViewsInLayout();
                recyclerView.setAdapter(mAdapter);
            }
        });
    }
}
