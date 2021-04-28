package com.example.webviewlibrary.view;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    private boolean mTouchByUser;


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
        setWebViewClient(new CommonWebViewClient());
        //setWebChromeClient(new WebChromeClient());

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
        //setJavascriptInterface(jsRemoteInterface);

    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled", "JavascriptInterface"})
    public void setJavascriptInterface(JsRemoteInterface obj) {
        addJavascriptInterface(obj, "webview");
    }

    @Override
    public void loadUrl(@NonNull String url) {
        super.loadUrl(url);
        Log.e(TAG,"Common Web view load url : " + url);
        resetAllStateInternal(url);
    }

    @Override
    public void loadUrl(@NonNull String url, @NonNull Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
        Log.e(TAG,"Common Web view load url : " + url);
        resetAllStateInternal(url);
    }


    private void resetAllStateInternal(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            return;
        }
        resetUserState();
    }

    private void resetUserState() {
        mTouchByUser = false;
    }

    public void registerWebViewCallBack(CommonWebViewCallback callback) {
        commonWebViewCallback = callback;
    }

    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    public boolean isTouchByUser() {
        return mTouchByUser;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchByUser =true;
                break;
        }
        return super.onTouchEvent(event);
    }

    /*
    * Web view client
    *
    * */
    public class CommonWebViewClient extends WebViewClient{
        public static final String SCHEME_SMS = "sms:";

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.e(TAG,"onPageStarted url :" + url);
            if(commonWebViewCallback != null) {
                commonWebViewCallback.pageStarted(url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e(TAG,"onPageFinished url :" + url);
            if(commonWebViewCallback != null) {
                commonWebViewCallback.pageFinished(url);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.e(TAG,"onReceivedError  url " + request.getUrl().toString());
            if(commonWebViewCallback != null) {
                commonWebViewCallback.onError();
            }
        }


        /**
        *
         * sll 证书错误， 待处理
         *
        *
        * */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG, "shouldOverrideUrlLoading url: " + url);
            // 当前链接的重定向, 通过是否发生过点击行为来判断
            if (!isTouchByUser()) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            // 如果链接跟当前链接一样，表示刷新
            if (getUrl().equals(url)) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            if (handleLinked(url)) {
                return true;
            }
            if (commonWebViewCallback != null && commonWebViewCallback.overrideUrlLoading(view, url)) {
                return true;
            }
            // 控制页面中点开新的链接在当前webView中打开
            view.loadUrl(url, mHeaders);
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.e(TAG, "shouldOverrideUrlLoading url: " + request.getUrl().toString());
            // 当前链接的重定向, 通过是否发生过点击行为来判断
            if (!isTouchByUser()) {
                return super.shouldOverrideUrlLoading(view, request);
            }
            // 如果链接跟当前链接一样，表示刷新
            if (getUrl().equals(request.getUrl().toString())) {
                return super.shouldOverrideUrlLoading(view, request);
            }
            if (handleLinked(request.getUrl().toString())) {
                return true;
            }
            if (commonWebViewCallback != null
                    && commonWebViewCallback.overrideUrlLoading(view, request.getUrl().toString())) {
                return true;
            }
            // 控制页面中点开新的链接在当前webView中打开
            view.loadUrl(request.getUrl().toString(), mHeaders);
            return true;
        }

        /**
         * 支持电话、短信、邮件、地图跳转，跳转的都是手机系统自带的应用
         */
        private boolean handleLinked(String url) {
            if (url.startsWith(WebView.SCHEME_TEL)
                    || url.startsWith(SCHEME_SMS)
                    || url.startsWith(WebView.SCHEME_MAILTO)
                    || url.startsWith(WebView.SCHEME_GEO)) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ignored) {
                    ignored.printStackTrace();
                }
                return true;
            }
            return false;
        }
    }








}















