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

import com.example.edrkr.bulletinPage.PagerAdapter;
import com.example.edrkr.R;
import com.google.android.material.tabs.TabLayout;

public class Managerpage extends AppCompatActivity {
    PagerAdapter adapter; //adapter 변수 선언
    ViewPager viewPager; //viewPager 선언
    private ActionBar actionBar; //엑션바
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("managerpage","managerpage 도착");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managerpage);

        Log.v("managerpage","toolbar 세팅 시작");
        //toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar_manager);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button); //뒤로가기 버튼 이미지
        Log.v("managerpage","toolbar 완료");


        //tablayout 참조, tab 추가
        TabLayout tabLayout = findViewById(R.id.manager_tabLayout);
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("회원 내역")));
        Log.v("managerpage","회원 내역 tab 추가 완료");
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView("밭 내역")));
        Log.v("managerpage","밭 내역 tab 추가 완료");
//                addTab((tabLayout.newTab().setText("회원 내역")));
//        tabLayout.addTab((tabLayout.newTab().setText("밭 내역")));
        tabLayout.setTabGravity((TabLayout.GRAVITY_FILL));
        Log.v("managerpage","tab 추가 완료");

        //ViewPager에 adapter set, TabLayout Listener 선언
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = findViewById(R.id.manager_viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        Log.v("managerpage","lister, adapter 추가 완료");

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

    private View createTabView(String tabName) {
        Log.v("managerpage","createTabView");
        mContext = getApplicationContext();
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        Log.v("managerpage","createTabView");
        TextView txt_name = (TextView) tabView.findViewById(R.id.txt_name);
        Log.v("managerpage","createTabView");
        txt_name.setText(tabName);
        return tabView;

    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.noticeboard_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }

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