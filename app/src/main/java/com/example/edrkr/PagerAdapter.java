package com.example.edrkr;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int num;

    public PagerAdapter(FragmentManager fm, int num){
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0:
                Listofmember tab1 = new Listofmember();
                return tab1;
            case 1:
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
