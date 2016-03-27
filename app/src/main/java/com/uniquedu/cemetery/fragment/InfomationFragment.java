package com.uniquedu.cemetery.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.uniquedu.cemetery.BaseFragment;
import com.uniquedu.cemetery.MyWebChromeClient;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.activity.WebInfomationActivity;

/**
 * Created by ZhongHang on 2016/3/4.
 */
public class InfomationFragment extends BaseFragment {
    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infomation, null);
        mWebView = (WebView) view.findViewById(R.id.webview);
        //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

//        mWebView.loadUrl("file:///android_asset/index.html");
        mWebView.addJavascriptInterface(new MyJs(), "useAndroidMethod");
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.loadUrl("http://123.56.236.240/app-news/news-list.html");
        return view;
    }

    class MyJs {
        @JavascriptInterface
        public void toWebActivity(String title, String url) {
            Intent intent = new Intent(getActivity(), WebInfomationActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("title", title);
            startActivity(intent);
        }
    }
}
