package com.io.myapplication.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.io.myapplication.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWebView=(WebView)findViewById(R.id.webView);
        mWebView.loadUrl("http://interactionone.com");
        mWebView.getSettings().setJavaScriptEnabled(true);
    }
}
