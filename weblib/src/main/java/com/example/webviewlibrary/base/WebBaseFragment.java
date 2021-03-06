package com.example.webviewlibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.webviewlibrary.OrderDispatcher;
import com.example.webviewlibrary.R;
import com.example.webviewlibrary.WebMainHandler;
import com.example.webviewlibrary.constant.WebConstant;
import com.example.webviewlibrary.impl.DefaultWebLifeCycleImpl;
import com.example.webviewlibrary.interfaces.Action;
import com.example.webviewlibrary.interfaces.CommonWebViewCallback;
import com.example.webviewlibrary.interfaces.WebLifeCycle;
import com.example.webviewlibrary.view.CommonWebView;

abstract public class WebBaseFragment extends BaseFragment implements CommonWebViewCallback {

    private WebLifeCycle webLifeCycle;

    public String url;

    private CommonWebView webView;

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            url = bundle.getString(WebConstant.INTENT_TAG_URL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(),container,false);
        webView = view.findViewById(R.id.web_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webLifeCycle = new DefaultWebLifeCycleImpl(webView);
        webView.registerWebViewCallBack(this);
        OrderDispatcher.getInstance().initAidlConnect(getContext(), new Action() {
            @Override
            public void call() {
                WebMainHandler.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadUrl();
                    }
                });
            }
        });
    }

    @Override
    public int getCommandLevel() {
        return 0;
    }

    @Override
    public void pageStarted(String url) {

    }

    @Override
    public void pageFinished(String url) {

    }

    @Override
    public boolean overrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public void onError() {

    }

    @Override
    public void exec(Context context, int orderLvl, String orderName, String params, WebView webView) {
        OrderDispatcher.getInstance().exec(context,orderLvl,orderName,params,webView,getDispatcherCallback());
    }

    @Override
    public void onResume() {
        super.onResume();
        webLifeCycle.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webLifeCycle.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webLifeCycle.onDestroy();
    }

    protected void loadUrl() {
        webView.loadUrl(url);
    }

    protected OrderDispatcher.DispatcherCallBack getDispatcherCallback() {
        return null;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return handleBackAction();
        }
        return false;
    }

    protected boolean handleBackAction() {
        if(webView != null) {
            if(webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
























