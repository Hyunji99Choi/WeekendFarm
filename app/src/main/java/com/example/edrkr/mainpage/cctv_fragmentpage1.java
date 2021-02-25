package com.example.edrkr.mainpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.edrkr.ControlMonitoring;
import com.example.edrkr.R;

public class cctv_fragmentpage1 extends Fragment {

    WebView webView1;
    WebSettings webSettings1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.cctv_fragment, container, false);

        webView1=rootView.findViewById(R.id.cctvweb1);
        webSettings1 = webView1.getSettings();
        webSettings1.setJavaScriptEnabled(true);

        //싱글턴 객체에 cctv 페이지 객체 넘겨주기
        ControlMonitoring.GetInstance().setFragmentPage1(this);

        //webView1.loadData("http://180.64.234.241:7070/mjpeg/1");
        //webView1.loadUrl("http://180.64.234.241:7070/mjpeg/1");

        return rootView;
    }
    public void cctvURLSetting(String URL1){
        webView1.loadUrl(URL1); //첫번째 cctv 화면 적용


    }
}
