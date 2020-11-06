package com.example.edrkr;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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

    GradientDrawable sunny_drawable; // 조도 센서 도형
    GradientDrawable hot_drawable; // 온도 센서

    ControlMonitor controlMonitor; //클래스 객체 선언 - 센서 컨트롤


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.sub_page1,container,false);

        soil_sensor= view.findViewById(R.id.soil_sensor);
        sunny_sensor=view.findViewById(R.id.sunny_sensor);
        water_seneor=view.findViewById(R.id.water_sensor);
        hot_text=view.findViewById(R.id.hot_sensor);
        hot_sencor=view.findViewById(R.id.hot_sensor_color);
        sunny_drawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.sunny_circle); // 조도 센서 도형
        hot_drawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.hot_circle); // 온도 센서

        //클래스 객체 선언 - 센서 컨트롤
        controlMonitor = new ControlMonitor(getContext());



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

        //초기세팅
        updateSencor(50,70,25.0,80);


        return view;

    }
    public void updateSencor(int soil,int sunny,double hot,int water){

        //토양센서
        soil_sensor.setProgress(soil);
        //조도센서
        sunny_drawable.setColor(controlMonitor.sunny_color(sunny));
        sunny_sensor.setImageDrawable(sunny_drawable);

        //대기온도센서
        hot_drawable.setColor(controlMonitor.hot_color(hot));
        hot_sencor.setImageDrawable(hot_drawable);
        hot_text.setText(hot+"c");

        //대기습도센서
        water_seneor.setProgress(water);

        //총론 수정

    }

}
