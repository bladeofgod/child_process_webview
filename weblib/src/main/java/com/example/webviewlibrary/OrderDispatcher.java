package com.example.webviewlibrary;


import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebView;

import com.example.webviewlibrary.aidl.RemoteWebBinderPool;
import com.example.webviewlibrary.interfaces.Action;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * dispatch order
 *
 *
 * */

public class OrderDispatcher {
    private static OrderDispatcher singleton;

    private OrderDispatcher() {}

    public static OrderDispatcher getInstance() {
        if(singleton == null) {
            synchronized (OrderDispatcher.class) {
                if(singleton == null) {
                    singleton = new OrderDispatcher();
                }
            }
        }
        return singleton;
    }

    // aidl (main binder)
    private IWebAidlInterface webAidlInterface;

    public IWebAidlInterface getWebAidlInterface(Context context) {
        if(webAidlInterface == null) {
            initAidlConnect(context,null);
        }
        return webAidlInterface;
    }

    public void initAidlConnect(final Context context, final Action action) {
        if(webAidlInterface != null) {
            // 已连接
            if(action != null) {
                action.call();
            }
            return;
        }
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                Log.i("AIDL", "Begin to connect with main process");
                RemoteWebBinderPool binderPool = RemoteWebBinderPool.getInstance(context);
                IBinder iBinder = binderPool.queryBinder(RemoteWebBinderPool.BINDER_WEB_AIDL);
                webAidlInterface = IWebAidlInterface.Stub.asInterface(iBinder);
                Log.i("AIDL", "Connect success with main process");
                if(action != null) {
                    action.call();
                }
            }
        });
    }


    /*
    * order entry
    * filter order type
    *
    * */
    public void exec(Context context, int orderLvl, String cmd, String params, WebView webView,
                     DispatcherCallBack callBack) {

    }

    private void execUI(final Context context,final  int commandLevel,final  String cmd, final String params,
                        final WebView webView, final DispatcherCallBack dispatcherCallBack) {

    }

    private void execNonUI(Context context, int commandLevel, String cmd, String params,final  WebView webView,
                           final DispatcherCallBack dispatcherCallBack) {

    }

    private void handleCallback(final int responseCode, final String actionName, final String response,
                                final WebView webView, final DispatcherCallBack dispatcherCallBack) {

    }

    public interface DispatcherCallBack {
        boolean preHandleBeforeCallback(int responseCode, String actionName, String response);
    }
}
















