package com.example.edrkr;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MonitoringPage extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    private ViewPager2 mPager_b;
    private FragmentStateAdapter pagerAdapter_b;
    private int num_page_b = 2 ;


    TabLayout tabLayout;
    TextView tooolbar_Textview;

    ViewPager pager;
    MyAdapter adapter;


    CharSequence farmManu[]; //밭 list 다이로그


    private DrawerLayout mDrawerLayout;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        //Toolbar를 액션 바로대체하기
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tooolbar_Textview=findViewById(R.id.toolbar_textView); // 툴바 타이틀


        navigationView=findViewById(R.id.navView);
        navigationView.setItemIconTintList(null); //사이드 메뉴에 아이콘 색깔을 원래 아이콘 색으로

        drawerLayout=findViewById(R.id.dl_main_drawer_root);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle); //누를때마다 아이콘이 팽그르 돈다
        drawerToggle.syncState();//삼선 메뉴 추가

        //상단 베너
        //cctv 페이지 세팅 부분
        //Viewpager2
        mPager_b = findViewById(R.id.baner);
        //Adapter
        pagerAdapter_b = new baner_Adapter(this,num_page_b);
        mPager_b.setAdapter(pagerAdapter_b);
        //viewpager setting
        mPager_b.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager_b.setCurrentItem(1000);
        mPager_b.setOffscreenPageLimit(2);

        mPager_b.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        mPager_b.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels==0){
                    mPager_b.setCurrentItem(position);
                }
            }

        });


        //bennar 위에까지


        //Tab 메뉴
        tabLayout=findViewById(R.id.layout_tab);
        pager=findViewById(R.id.pager);
        adapter=new MyAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        //tabLayout과 ViewPager를 연동
        tabLayout.setupWithViewPager(pager);

        //네비게이션뷰에 아이템선택 리스너 추가
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.today:
                        Toast.makeText(MonitoringPage.this,"today",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.community:
                        Toast.makeText(MonitoringPage.this,"community",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_admin:
                        Toast.makeText(MonitoringPage.this,"admin",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_goood:
                        Toast.makeText(MonitoringPage.this,"승인여부",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.setting:
                        Toast.makeText(MonitoringPage.this,"setting",Toast.LENGTH_SHORT).show();
                        break;

                }

                //Drawer를 닫기...
                drawerLayout.closeDrawer(navigationView);

                return false;
            }
        });


    }

    public void onClickTextView(View view){
        //switch(view.getId())
        //case R.id.toolbar...

        Toast.makeText(this,"타이틀 클릭",Toast.LENGTH_SHORT).show();
        FarmDialogSetting(); //다이로그 생성 함수
    }

    public void onClickHeader(View view){ //메뉴 헤더 클릭시
        Toast.makeText(this,"헤더 클릭",Toast.LENGTH_SHORT).show();
    }

    public void FarmDialogSetting(){ //타이틀 밭 이동 리스트
        farmManu=new CharSequence[]{"1번 밭","2번 밭","3번 밭"}; //밭 별명
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("밭 이동");
        builder.setItems(farmManu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { //연동적으로 수정
                switch (i){
                    case 0:
                        Toast.makeText(context, "1번밭", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(context, "2번밭", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(context, "3번밭", Toast.LENGTH_SHORT).show();
                        break;
                }
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
