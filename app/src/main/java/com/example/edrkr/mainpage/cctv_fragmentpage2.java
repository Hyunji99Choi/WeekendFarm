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

import com.example.edrkr.R;

public class cctv_fragmentpage2 extends Fragment {

    WebView webView2;
    WebSettings webSettings2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.cctv_fragment2, container, false);

        webView2=rootView.findViewById(R.id.cctvweb2);
        webSettings2 = webView2.getSettings();
        webSettings2.setJavaScriptEnabled(true);

        //싱글턴 객체에 cctv 페이지 객체 넘겨주기
        ControlMonitoring.GetInstance().setFragmentPage2(this);


        return rootView;
    }
    public void cctvURLSetting(String URL2){
        webView2.loadUrl(URL2); //두번째 cctv 화면 적용

    }
}
