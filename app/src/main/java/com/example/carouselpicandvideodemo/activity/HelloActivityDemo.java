package com.example.carouselpicandvideodemo.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.carouselpicandvideodemo.R;
import com.example.carouselpicandvideodemo.application.BaseActivity;

/**
 * Created by Administrator on 2017/11/23.
 */

public class HelloActivityDemo extends BaseActivity {

    private WebView webView;

    @Override
    protected void initContentView() {
        setContentView(R.layout.webview_layout);
    }

    @Override
    protected void initView() {
        webView = findViewById(R.id.wv_view);
    }

    @Override
    protected void initData() {
        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new AppWebViewClients());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        // https://view.officeapps.live.com/op/view.aspx?src
        webView.loadUrl("http://60.223.238.80:8080/ECPse/corresponding/EPCIntroduce.docx");
    }

    public class AppWebViewClients extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("http://60.223.238.80:8080/ECPse/corresponding/EPCIntroduce.docx");
            intent.setDataAndType(uri, "application/msword");
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    protected void setListener() {

    }
}
