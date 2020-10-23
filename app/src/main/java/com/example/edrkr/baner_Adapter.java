package com.example.edrkr;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class baner_Adapter extends FragmentStateAdapter {
    public int mCount_benner;

    public baner_Adapter(FragmentActivity fa, int count){
        super(fa);
        mCount_benner=count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new baner_weather();
        else return new baner_todayCrop();

    }

    //2000번의 슬라이딩. 진짜 무한은 아님.
    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position){
        return position % mCount_benner;
    }


}
