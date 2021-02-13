package com.example.testapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class ArticleWebView extends AppCompatActivity {
    private Context context;
    private Toolbar webview_toolbar;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_webview);

        context = getApplicationContext();
        Intent intent = getIntent();
        webview_toolbar = (Toolbar) findViewById(R.id.webview_toolbar);
        webView = (WebView) findViewById(R.id.webview);

        webview_toolbar.setTitle(intent.getExtras().getString("source"));
        setSupportActionBar(webview_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ↓툴바의 홈버튼의 이미지를 변경(기본 이미지는 뒤로가기 화살표)
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        webview_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(intent.getExtras().getString("url"));
        webView.setWebChromeClient(new WebChromeClient());

    }

}
