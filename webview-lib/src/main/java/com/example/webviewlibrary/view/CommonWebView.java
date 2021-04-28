package com.example.webviewlibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.webviewlibrary.interfaces.CommonWebViewCallback;
import com.example.webviewlibrary.interfaces.JsRemoteInterface;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommonWebView extends WebView {

    private static final ExecutorService SINGLE_THREAD_POOL = Executors.newSingleThreadExecutor();

    private static final String TAG = "CommonWebView";

    protected Context context;

    boolean isReady;

    private CommonWebViewCallback commonWebViewCallback;

    private Map<String,String> mHeaders;

    private JsRemoteInterface jsRemoteInterface;


    public CommonWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CommonWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommonWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void init(Context context) {
        this.context = context;
        WebDefaultSettingManager.getInstance().toSetting(this);
        setWebChromeClient(new WebChromeClient());

        //链接 web —— native
        if(jsRemoteInterface == null) {
            jsRemoteInterface = new JsRemoteInterface(context);
            jsRemoteInterface.setAidlCommand(new JsRemoteInterface.AidlCommand() {
                @Override
                public void exec(Context context, String cmd, String params) {
                    if(commonWebViewCallback != null) {
                        commonWebViewCallback.exec(context,commonWebViewCallback.getCommandLevel(),
                                cmd,params,CommonWebView.this);
                    }
                }
            });
        }
        setJavascriptInterface(jsRemoteInterface);

    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled", "JavascriptInterface"})
    public void setJavascriptInterface(JsRemoteInterface obj) {
        addJavascriptInterface(obj, "webview");
    }

    public void registerWebViewCallBack(CommonWebViewCallback callback) {
        commonWebViewCallback = callback;
    }

    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }
}















