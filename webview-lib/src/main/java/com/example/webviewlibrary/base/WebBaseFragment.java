package com.example.webviewlibrary.base;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.webviewlibrary.R;
import com.example.webviewlibrary.constant.WebConstant;
import com.example.webviewlibrary.impl.DefaultWebLifeCycleImpl;
import com.example.webviewlibrary.interfaces.WebLifeCycle;

abstract public class WebBaseFragment extends BaseFragment {

    private WebLifeCycle webLifeCycle;

    public String url;

    private WebView webView;

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
        loadUrl();
    }


    @Override
    public void onResume() {
        super.onResume();
        webLifeCycle.onPause();
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
























