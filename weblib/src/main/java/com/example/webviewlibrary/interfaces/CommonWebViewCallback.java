package com.example.webviewlibrary.interfaces;


/*
* web view 回调
*
* */

import android.content.Context;
import android.webkit.WebView;

public interface CommonWebViewCallback {

    int getCommandLevel();

    void pageStarted(String url);

    void pageFinished(String url);

    boolean overrideUrlLoading(WebView view, String url);

    void onError();

    void exec(Context context, int orderLvl, String orderName, String params, WebView webView);

}
