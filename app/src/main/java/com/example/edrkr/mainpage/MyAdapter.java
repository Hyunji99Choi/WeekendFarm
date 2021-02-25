package com.example.edrkr.mainpage;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {

    Fragment[] fragments=new Fragment[3];
    String[] pageTitels = new String[]{"My Farm","일일 모니터링","오늘의 일지"};

    public MyAdapter(FragmentManager fm) {
        super(fm);

        fragments[0]= new sub_page1();
        fragments[1]= new sub_page2();
        fragments[2]= new sub_page3();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    //뷰페이저와 연동된 텝레이아웃의 탭 버튼들의
    //글씨를 리턴해주는 메소드
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitels[position];
    }
}
