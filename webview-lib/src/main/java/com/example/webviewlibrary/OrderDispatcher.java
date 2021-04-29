package com.example.webviewlibrary;


import android.content.Context;
import android.webkit.WebView;

import com.example.webviewlibrary.interfaces.Action;

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

    public void initAidlConnect(final Context context, final Action action) {}


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
















