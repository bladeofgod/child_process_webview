package com.example.webviewlibrary.impl;

import android.os.Looper;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.webviewlibrary.interfaces.WebLifeCycle;

public class DefaultWebLifeCycleImpl implements WebLifeCycle {

    private final WebView mWebView;

    public DefaultWebLifeCycleImpl(WebView mWebView) {
        this.mWebView = mWebView;
    }

    @Override
    public void onResume() {
        if(mWebView != null) {
            mWebView.onResume();
        }

    }

    @Override
    public void onPause() {
        if(mWebView != null) {
            mWebView.onPause();
        }

    }

    @Override
    public void onDestroy() {
        if(mWebView != null) {
            clearWebView(mWebView);
        }

    }

    private void clearWebView(WebView mWebView) {
        if(mWebView == null) return;

        if(Looper.myLooper() != Looper.getMainLooper())
            return;
        mWebView.stopLoading();
        if(mWebView.getHandler() != null) {
            mWebView.getHandler().removeCallbacksAndMessages(null);
        }
        mWebView.removeAllViews();
        ViewGroup viewGroup = (ViewGroup) mWebView.getParent();
        if(viewGroup != null) {
            viewGroup.removeView(mWebView);
        }
        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.setTag(null);
        mWebView.clearHistory();
        mWebView.destroy();
        mWebView = null;

    }
}
