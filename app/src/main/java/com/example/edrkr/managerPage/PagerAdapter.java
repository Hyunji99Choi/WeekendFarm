package com.example.edrkr.managerPage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.edrkr.managerPage.Listofarea;
import com.example.edrkr.managerPage.Listofmember;

//주석 필요
public class PagerAdapter extends FragmentStatePagerAdapter { //listofarea/ listofmember fragment 올려주는 adapter
    int num;

    public PagerAdapter(FragmentManager fm, int num){
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0: //사용자 리스트
                Listofmember tab1 = new Listofmember();
                return tab1;
            case 1: //밭 리스트
                Listofarea tab2 = new Listofarea();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return num;
    }
}
