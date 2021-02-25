package com.example.edrkr;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.edrkr.mainpage.cctv_Adapter;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator3;


public class sub_page1 extends Fragment {

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3 ;
    private CircleIndicator3 mIndicator;

    public CircleProgressBar soil_sensor;
    public ImageView sunny_sensor;
    public CircleProgressBar water_seneor;
    public TextView hot_text;
    public ImageView hot_sencor;
    public TextView comment; // 총론

    GradientDrawable sunny_drawable; // 조도 센서 도형
    GradientDrawable hot_drawable; // 온도 센서

    TimerTask timerTask;//센서통신할 타이머
    Timer timer; //센서통신할 타이머


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.sub_page1,container,false);

        soil_sensor= view.findViewById(R.id.soil_sensor);
        sunny_sensor=view.findViewById(R.id.sunny_sensor);
        water_seneor=view.findViewById(R.id.water_sensor);
        hot_text=view.findViewById(R.id.hot_sensor);
        hot_sencor=view.findViewById(R.id.hot_sensor_color);
        comment=view.findViewById(R.id.textViewComment);
        sunny_drawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.sunny_circle); // 조도 센서 도형
        hot_drawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.hot_circle); // 온도 센서

        //싱글턴 클래스 context 값, 현재 객체 넣어주기 - 센서 컨트롤
        ControlMonitoring.GetInstance().setContextThis(getContext(),this);



        //cctv 페이지 세팅 부분
        //Viewpager2
        mPager = view.findViewById(R.id.viewpager);
        //Adapter
        pagerAdapter = new cctv_Adapter(getActivity(),num_page);
        mPager.setAdapter(pagerAdapter);
        //Indicator
        mIndicator=view.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page,0);
        //viewpager setting
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(1000);
        mPager.setOffscreenPageLimit(3);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels==0){
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%num_page);
            }
        });

        final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        mPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (mPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(mPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-myOffset);
                    } else {
                        page.setTranslationX(myOffset);
                    }
                } else {
                    page.setTranslationY(myOffset);
                }
            }
        });


        if(UserIdent.GetInstance().getFarmCount()==0){ //밭이 0개이면 실행.
            //밭이 없으니 0으로 세팅
            ControlMonitoring.GetInstance().updateSensor(0,0,0.0,0);
        }


        //정민이 통신
        //ControlMonitoring.GetInstance().NetworkManager(1);

        return view;

    }

    public void Start_SensorTimer(){
        timerTask = new TimerTask() {
            int count=0;
            @Override
            public void run() {

                Log.w("timer",""+count++);
                Log.w("현재 통신대상",""+UserIdent.GetInstance().getNowMontriongFarm());
                ControlMonitoring.GetInstance().NetworkSensorCall(UserIdent.GetInstance().getFarmID(UserIdent.GetInstance().getNowMontriongFarm()));

                //cctv
                Log.w("ccctv","cctv 통신");
                ControlMonitoring.GetInstance().NetworkCCTVCall(UserIdent.GetInstance().getFarmID(UserIdent.GetInstance().getNowMontriongFarm()));
            }
        };

        timer=new Timer();
        timer.schedule(timerTask,0,10000); //0초 후의 실행 후 10초마다 반복
    }

    //활동이 재개됨 상태
    @Override
    public void onResume() {
        Log.i("test","onResume");
        if(UserIdent.GetInstance().getFarmCount()!=0){ //밭이 존재햐아만 실행
            Start_SensorTimer();//타이머 시작
        }

        super.onResume();


    }


    //사용자가 활동을 떠나는 것을 나타내는 첫번째 신호
    @Override
    public void onPause() {
        Log.i("test","onPause");
        if(UserIdent.GetInstance().getFarmCount()!=0){ //밭이 존재햐아만 실행
            timer.cancel();//타이머 중지
        }

        super.onPause();
    }

}
