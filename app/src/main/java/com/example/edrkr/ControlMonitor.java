package com.example.edrkr;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;

public class ControlMonitor {

    private Context contex;

    //생성자
    public ControlMonitor(Context contex){
        this.contex=contex;
    }

    //조도 센서 색 return
    public int sunny_color(int light){
        int color;

        if (light>90){//매우 밝음
            color=contex.getResources().getColor(R.color.sunny_very_bright);
            return color;
        }else if (light>70){ //밝음
            color=contex.getResources().getColor(R.color.sunny_bright);
            return color;
        }else if (light>50){ //약간 밝음
            color=contex.getResources().getColor(R.color.sunny_some_bright);
            return color;
        }else if (light>30){ //약간 어두움
            color=contex.getResources().getColor(R.color.sunny_some_dack);
            return color;
        }else if (light>10){ //어두움
            color=contex.getResources().getColor(R.color.sunny_dack);
            return color;
        }else{ //매우 어두움
            color = contex.getResources().getColor(R.color.sunny_very_dack);
            return color;
        }

    }

    //온도 센서 색 return
    public int hot_color(double temp){
        int color;

        if (temp>30){//매우 더움
            color=contex.getResources().getColor(R.color.hot_very_hot);
            return color;
        }else if (temp>25){ //더움
            color=contex.getResources().getColor(R.color.hot_hot);
            return color;
        }else if (temp>20){ //조금 더움(20~25 적당함)
            color=contex.getResources().getColor(R.color.hot_some_hot);
            return color;
        }else if (temp>10){ //약간 추움
            color=contex.getResources().getColor(R.color.hot_some_cold);
            return color;
        }else if (temp>0){ //추움
            color=contex.getResources().getColor(R.color.hot_cold);
            return color;
        }else{ //매우 추움
            color = contex.getResources().getColor(R.color.hot_very_cold);
            return color;
        }

    }



}
