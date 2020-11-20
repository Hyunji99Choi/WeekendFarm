package com.example.edrkr;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Managerpage extends AppCompatActivity {
    PagerAdapter adapter; //adapter 변수 선언
    ViewPager viewPager; //viewPager 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("managerpage","managerpage 도착");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managerpage);

        //tablayout 참조, tab 추가
        TabLayout tabLayout = findViewById(R.id.manager_tabLayout);
        tabLayout.addTab((tabLayout.newTab().setText("회원 내역")));
        tabLayout.addTab((tabLayout.newTab().setText("밭 내역")));
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
}