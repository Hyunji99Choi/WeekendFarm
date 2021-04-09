package com.example.edrkr.mainpage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.transition.Explode;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.edrkr.KeyCreatePage;
import com.example.edrkr.firstpage.MainActivity;
import com.example.edrkr.h_network.AutoRetryCallback;
import com.example.edrkr.h_network.ResponseUserIdent;
import com.example.edrkr.h_network.ResponseWeatherJson;
import com.example.edrkr.h_network.RetrofitClient;
import com.example.edrkr.managerPage.Managerpage;
import com.example.edrkr.bulletinPage.NoticeBoardActivity;
import com.example.edrkr.subpage.subpage_userIdnetChange;
import com.example.edrkr.R;
import com.example.edrkr.UserIdent;
import com.example.edrkr.baner_Adapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Response;

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

    TextView naviHeaderName; //네거티브 메뉴 헤더 이름
    TextView naviHeaderEmail; //네거티브 메뉴 헤더 이메일

    FloatingActionButton fab; //일지 쓰기 버튼, fab 버튼

    Dialog writDialog; //일지 다이로그

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_monitoring);


        //각 요소들 연결 - toolbar, tab, tab-viewpager, drawer 등
        initValueSetting();

        //첫번째 배열 값으로 툴바 textview 타이틀 수정
        Log.w("타이틀 세팅","수정 전");
        if(UserIdent.GetInstance().getFarmCount()!=0) //밭이 0이면 실행안함. --> 쓰레기값이나 빈 값이어도 실행 안되게 하기(수정해야함.)
            farmTitile.setText(UserIdent.GetInstance().getFarmName(UserIdent.GetInstance().getNowMontriongFarm()));

        //날씨 통신
        getWheaterData();

        // view pager 세팅, 하단 베너 --> 대대적인 수정이 있어야함
        //initWeatherSetting();

        //draw 메뉴 클릭 리스너(페이지 이동)
        drawerMenuSetting();


    }

    // 기본 요소들 세팅
    void initValueSetting(){

        //Toolbar를 액션바로 대체
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //textview, 툴바 타이틀(제목, 현재 밭)
        farmTitile = findViewById(R.id.toolbar_textView); // 툴바 타이틀

        //Tab 메뉴
        tabLayout=findViewById(R.id.layout_tab);
        pager=findViewById(R.id.pager);
        adapter=new MyAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        //tabLayout과 ViewPager를 연동
        tabLayout.setupWithViewPager(pager);

        //네거티브 메뉴 연결
        navigationView=findViewById(R.id.navView);
        navigationView.setItemIconTintList(null); //사이드 메뉴에 아이콘 색깔을 원래 아이콘 색으로

        //네거티브 슬라이드 메뉴 헤더 연결
        View header = navigationView.getHeaderView(0);
        Log.w("헤더 세팅","수정 전");
        naviHeaderName = header.findViewById(R.id.navi_header_name);
        naviHeaderName.setText(UserIdent.GetInstance().getNkname());
        naviHeaderEmail = header.findViewById(R.id.navi_header_email);
        naviHeaderEmail.setText(UserIdent.GetInstance().getEmail());
        Log.w("헤더 세팅","완료");

        //드로버 레이아웃(슬라이드 메뉴 레이아웃)
        drawerLayout=findViewById(R.id.dl_main_drawer_root);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle); //누를때마다 아이콘이 팽그르 돈다
        drawerToggle.syncState();//삼선 메뉴 추가

        //일지 쓰기 버튼 연결(fab)
        fab = findViewById(R.id.fab_main);
    }

    /*
    //하단 베너 세팅 및 연결
    void initWeatherSetting(){

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
    }
    */


    //타이틀 클릭 이벤트
    public void onClickTextView(View view){
        //switch(view.getId())
        //case R.id.toolbar...
        FarmDialogSetting(); //다이로그 생성 함수
    }

    //타이틀 밭 이동 리스트
    public void FarmDialogSetting(){
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

                //각 밭 선택에 따른 cctv 통신
                ControlMonitoring.GetInstance().NetworkCCTVCall(UserIdent.GetInstance().getFarmID(i));
                //각 밭 선택에 따른 통신 및 센서값 세팅
                ControlMonitoring.GetInstance().NetworkSensorCall(UserIdent.GetInstance().getFarmID(i));
                //각 밭 선택에 다른 그래프 통신
                ControlMonitoring.GetInstance().NetworkkGraphCall();



                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    //네비게이션뷰에 아이템선택 리스너 추가
    void drawerMenuSetting(){
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
                        Intent keypage = new Intent(MonitoringPage.this, KeyCreatePage.class);
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

    //헤더 클릭 이벤트
    public void onClickHeader(View view){ //메뉴 헤더 클릭시

        Intent userChange = new Intent(MonitoringPage.this, subpage_userIdnetChange.class);
        startActivity(userChange);

        Toast.makeText(this,"헤더 클릭",Toast.LENGTH_SHORT).show();


    }

    //플로팅 버튼 클릭 이벤트
    public void fabOnClick(View view){
        fab.setImageResource(R.drawable.ic_main_fab_writting_button);

        //클릭 에니메이션
        //fab.startAnimation();

        //글쓰기 다이로그 열기
        writDialog = new Dialog(this); //그때그때 객체 생성 고민해보기
        writDialog.setContentView(R.layout.today_writting_custom_dialog);
        settingDialog(writDialog);
        writDialog.show();

    }

    //글쓰기 다이로그 세팅
    private void settingDialog(final Dialog writDialog){ //닫힘 방지 만들기

        //세팅
        TextInputLayout inputLayout = writDialog.findViewById(R.id.inputlayout);
        inputLayout.setCounterEnabled(true);
        inputLayout.setCounterMaxLength(140);
        TextInputEditText editText = writDialog.findViewById(R.id.body);

    }

    public void dialogOnClick(View view){

        switch (view.getId()){
            case R.id.back: // x 버튼 (닫기)
                writDialog.dismiss();
                fab.setImageResource(R.drawable.ic_main_fab_button);
                break;
            case R.id.check:
                //일지 서버에 전송
                writDialog.dismiss();
                fab.setImageResource(R.drawable.ic_main_fab_button);
                break;
        }
    }

    private void getWheaterData(){
        Call<ResponseWeatherJson> wheather = RetrofitClient.getApiService().getWheather(); //api 콜
        wheather.enqueue(new AutoRetryCallback<ResponseWeatherJson>() {
            @Override
            public void onFinalFailure(Call<ResponseWeatherJson> call, Throwable t) {
                Log.e("날씨 정보 연결실패", t.getMessage());
            }

            @Override
            public void onResponse(Call<ResponseWeatherJson> call, Response<ResponseWeatherJson> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적", "error code : " + response.code());
                    return;
                }

                Log.d("날씨 통신 성공적", response.body().toString());
                ResponseWeatherJson weatherJson = response.body(); //통신 결과 받기

                //날씨 정보로 세팅
                Log.d("날씨", weatherJson.getWeather());
                Log.d("날씨", weatherJson.getWeather_imgurl());

            }
        });

    }
}
