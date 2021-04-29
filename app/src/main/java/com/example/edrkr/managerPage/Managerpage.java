package com.example.edrkr.managerPage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.edrkr.R;
import com.google.android.material.tabs.TabLayout;

public class Managerpage extends AppCompatActivity { //메니저 페이지 - 2개의 프레그먼트가 올려져있음.
    PagerAdapter adapter; //adapter 변수 선언 - pageadapter
    ViewPager viewPager; //viewPager 선언
    private TabLayout tabLayout;
    private ActionBar actionBar; //엑션바
    private Context mContext;
    private String TAG ="areum/Managerpage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"managerpage 도착");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managerpage_main);

        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_manager);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지


        //tablayout 참조, tab 추가
        tabLayout = findViewById(R.id.manager_tabLayout);

        //ViewPager에 adapter set, TabLayout Listener 선언
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = findViewById(R.id.manager_viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Tab 이벤트에 대한 Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                adapter.notifyDataSetChanged();
                Log.v(TAG,"tab 눌림");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private View createTabView(String tabName) { //+버튼 클릭시
        Log.v(TAG,"createTabView");
        mContext = getApplicationContext();
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.managerpage_custom_tab, null);
        TextView txt_name = (TextView) tabView.findViewById(R.id.txt_name);
        txt_name.setText(tabName);
        return tabView;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){ //optionitem 선택시
        switch (item.getItemId()){
            case android.R.id.home: //뒤로가기 버튼 클릭시
                Log.v(TAG,"home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}