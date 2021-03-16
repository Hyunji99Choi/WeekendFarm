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

import com.example.edrkr.bulletinPage.CustomUsersAdapter;
import com.example.edrkr.NetworkTask;
import com.example.edrkr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Listofarea extends Fragment { //밭별 사용자 fragment
    private RecyclerView recyclerView;
    private stringadapter mAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Member> myDataset = new ArrayList<>();
    private String URL = "http://3.35.55.9:3000/forum/"; //서버 주소 바꿀듯

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.v("listofare","area 도착");

        View view = inflater.inflate(R.layout.listofarea, container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_arealist);
        recycler_test(); //테스트용 데이터 저장 - local
        Log.v("listofare","recyclerview id 연결");

        recyclerView.setHasFixedSize(true);
        mAdapter = new stringadapter(myDataset,1);

       // layoutManager.setReverseLayout(true);
      //  layoutManager.setStackFromEnd(true);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        Log.v("listofare","layout adapter 연결");
        recyclerView.setAdapter(mAdapter);

        this.InitializeView(); //필요 요소 선언해주는 함수
        mAdapter.setOnItemClickListener(new CustomUsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) { //각 밭 클릭시 해당 밭의 사용자 보여주는 page로 이동하는 함수
                Log.v("알림","게시글 클릭 리스너 눌림 pos : "+pos);
                Member s = myDataset.get(pos);
                Intent intent = new Intent(getActivity(), show_each_areahas.class);

                intent.putExtra("name", s.getName_());
                intent.putExtra("pos",pos);
                Log.v("알림","Board값 전송 완료");
                //startActivity(intent);
                //setResult(1,intent);
                startActivityForResult(intent,1);
                Log.v("알림","intent 전송 완료");
                //finish();
            }
        });
        return view;
    }

    public void InitializeView(){ //초기화 - 아직 들어가는 코드 없음
//
        //myDataset 받는 코드 들어가야함
        //  try {
        //    myDataset = getfromserver();
        // } catch (JSONException e) {
        //     Log.e("trycatch","error : "+ e);
        //     e.printStackTrace();
        //  }

    }

    public void recycler_test(){ //로컬로 데이터 넣는 함수
        ArrayList<Member> test = new ArrayList<>();
        for(int i = 0;i<5;i++){
            Member tmp = new Member(i+"","밭"+i);
            test.add(tmp);
        }
        myDataset = test;
    }

    public ArrayList<String> getfromserver() throws JSONException {//서버에서 게시글 표지 부분을 받아오는 코드

        Log.v("알림","server 확인");
        ArrayList<String> dataset = new ArrayList<String>();

        //values.put("id","00");
        //values.put("pw",UserPW)

        NetworkTask getboardlist_networkTask = new NetworkTask(URL,null); //networktast 설정 부분
        try {
            getboardlist_networkTask.execute().get(); //설정한 networktask 실행
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = getboardlist_networkTask.result;

        // String result = [{"id":1,"userName":null,"userId":"ghd8119","title","fsadkfjd","content","dafsdf"},{"id":1,"userName":null,"userId":"ghd8119","title","fsadkfjd","content","dafsdf"}]

        JSONArray jsonArray = null;
        jsonArray = new JSONArray(result);
        Log.v("getfromserver","json으로 변환");

        Log.v("getfromserver","board 변환 작업 시작");
        for(int i = 0;i<jsonArray.length();i++){

            JSONObject jsonObject = null;
            jsonObject = jsonArray.getJSONObject(i);

            String id = jsonObject.getString("id");
            String userId = jsonObject.getString("userId");
            Log.v("getfromserver",i+" 값 가져오기 성공");

            Log.v("getfromserver","board 추가 완료");
            dataset.add(userId);
        }
        return dataset;
    }

}
