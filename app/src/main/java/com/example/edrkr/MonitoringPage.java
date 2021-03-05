package com.example.edrkr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.edrkr.Bulletin.NoticeBoardActivity;
import com.example.edrkr.ManagerPage.Managerpage;
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

    ViewPager pager;
    MyAdapter adapter;


    CharSequence[] farmManu; //밭 list 다이로그

    TextView farmTitile; //타이틀 글자 (밭 별명)

    TextView naviHeaderName;
    TextView naviHeaderEmail;


    private DrawerLayout mDrawerLayout;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        //textview
        farmTitile = findViewById(R.id.toolbar_textView); // 툴바 타이틀
        //첫번째 배열 값으로 툴바 textview 타이틀 수정
        Log.w("타이틀 세팅","수정 전");
        if(UserIdent.GetInstance().getFarmCount()!=0) //밭이 0이면 실행안함.
            farmTitile.setText(UserIdent.GetInstance().getFarmName(UserIdent.GetInstance().getNowMontriongFarm())); //****** 통신 완료시 풀어줘야함



        //Toolbar를 액션 바로대체하기
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        navigationView=findViewById(R.id.navView);
        navigationView.setItemIconTintList(null); //사이드 메뉴에 아이콘 색깔을 원래 아이콘 색으로

        //슬라이드 메뉴 헤더 수정
        //navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        Log.w("헤더 세팅","수정 전");
        naviHeaderName = header.findViewById(R.id.navi_header_name);
        naviHeaderName.setText(UserIdent.GetInstance().getNkname());
        naviHeaderEmail = header.findViewById(R.id.navi_header_email);
        naviHeaderEmail.setText(UserIdent.GetInstance().getEmail());
        Log.w("헤더 세팅","완료");


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
                        //다음학기
                        Toast.makeText(MonitoringPage.this,"today",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.community:
                        //이아름
                        Intent intent = new Intent(MonitoringPage.this, NoticeBoardActivity.class);
                        startActivity(intent);
                        Toast.makeText(MonitoringPage.this,"community",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_admin_key:
                        Intent keypage = new Intent(MonitoringPage.this,KeyCreatePage.class);
                        startActivity(keypage);

                        Toast.makeText(MonitoringPage.this,"key생성페이지",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_admin_list:
                        //이아름
                        intent = new Intent(MonitoringPage.this, Managerpage.class);
                        startActivity(intent);
                        Toast.makeText(MonitoringPage.this,"회원정보 열람",Toast.LENGTH_SHORT).show();
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
        FarmDialogSetting(); //다이로그 생성 함수
    }

    public void onClickHeader(View view){ //메뉴 헤더 클릭시
        Toast.makeText(this,"헤더 클릭",Toast.LENGTH_SHORT).show();
    }

    public void FarmDialogSetting(){ //타이틀 밭 이동 리스트
        farmManu=new CharSequence[UserIdent.GetInstance().getFarmCount()]; //밭 별명
        //밭 별명 주기
        for(int i=0;i<UserIdent.GetInstance().getFarmCount();i++){
            farmManu[i]=UserIdent.GetInstance().getFarmName(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("밭 이동");
        builder.setItems(farmManu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { //연동적으로 수정

                //i 배열에 있는 밭 번호
                Toast.makeText(context, ""+UserIdent.GetInstance().getFarmName(i), Toast.LENGTH_SHORT).show();
                UserIdent.GetInstance().setNowMontriongFarm(i); //이제 통신할 값은 이 밭이라고 선언.
                farmTitile.setText(UserIdent.GetInstance().getFarmName(i));

                //각 밭 선택에 따른 통신 및 센서값 세팅
                ControlMonitoring.GetInstance().NetworkSensorCall(UserIdent.GetInstance().getFarmID(i));


                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
