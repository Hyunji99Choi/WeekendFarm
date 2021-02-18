package com.example.edrkr;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Managerpage extends AppCompatActivity {
    PagerAdapter adapter; //adapter 변수 선언
    ViewPager viewPager; //viewPager 선언
    private ActionBar actionBar; //엑션바

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managerpage);

        Log.v("managerpage","managerpage 도착");
        initSetting();
    }

    void initSetting(){ //toolbar, tab, viewPage 등 세팅

        Toolbar toolbar = findViewById(R.id.toolbar_manager); //toolbar를 액션바로 대체
        TabLayout tabLayout = findViewById(R.id.manager_tabLayout); //tablayout 참조
        viewPager = findViewById(R.id.manager_viewPager); //viewPager 참조


        //toolbar 연결결
        setSupportActionBar(toolbar); //Toolbar을 액티비티 레이아웃으로 지정
        actionBar = getSupportActionBar();  //액티비티 엡바 지정

        //ViewPager에 adapter set, TabLayout Listener 선언
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        Log.v("managerpage","lister, adapter 추가 완료");

        //앱바(app bar) 커스텀
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v("managerpage","toolbar 완료");

        //tab 메뉴 추가
        tabLayout.addTab((tabLayout.newTab().setText("회원 내역")));
        tabLayout.addTab((tabLayout.newTab().setText("밭 내역")));
        tabLayout.setTabGravity((TabLayout.GRAVITY_FILL));
        Log.v("managerpage","tab 추가 완료");


        //Tab 이벤트에 대한 Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                adapter.notifyDataSetChanged();
                Log.v("managerpage","tab 눌림");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
       switch (item.getItemId()){
            case android.R.id.home:
                Log.v("managerpage","home");
                Toast.makeText(this,"home onclick",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}