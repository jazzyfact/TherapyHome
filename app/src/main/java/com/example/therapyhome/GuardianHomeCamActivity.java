package com.example.therapyhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GuardianHomeCamActivity extends AppCompatActivity {


    private WebView webView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_home_cam);
        webView = findViewById(R.id.web_view);

        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();


        webView.setPadding(0, 0, 0, 0);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);


        //webView.loadUrl("http://192.168.0.2:8090/stream_simple.html");
        webView.loadUrl("http://192.168.0.2:8090/javascript_simple.html");


      
    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0,0);//엑티비티 종료 시 애니메이션 없애기
    }
}












