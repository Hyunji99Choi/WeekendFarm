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

public class cctv_fragmentpage3 extends Fragment {

    WebView webView3;
    WebSettings webSettings3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.cctv_fragment3, container, false);

        webView3=rootView.findViewById(R.id.cctvweb3);
        webSettings3 = webView3.getSettings();
        webSettings3.setJavaScriptEnabled(true);

        //싱글턴 객체에 cctv 페이지 객체 넘겨주기
        ControlMonitoring.GetInstance().setFragmentPage3(this);

        return rootView;
    }
    public void cctvURLSetting(String URL3){
        webView3.loadUrl(URL3); //세번째 cctv 화면 적용

    }
}
