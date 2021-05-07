package com.example.webviewlibrary;


import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebView;

import com.example.webviewlibrary.aidl.RemoteWebBinderPool;
import com.example.webviewlibrary.interfaces.Action;
import com.example.webviewlibrary.interfaces.ResultCallback;
import com.example.webviewlibrary.order.OrderManager;
import com.google.gson.Gson;

import java.util.Map;
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
    private Gson gson = new Gson();

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


    /**
     *
     * order entry point
     * execute order from web view
     *
     *  * may cross process or thread
     * */
    public void exec(Context context, int orderLvl, String orderName, String params, WebView webView,
                     DispatcherCallBack callBack) {
        //cross the process or thread so need try/catch
        try {
            if(OrderManager.getInstance().isUiOrder(orderLvl,orderName)) {
                execUI(context,orderLvl,orderName,params,webView,callBack);
            }
        }catch (Exception e) {
            logOrderException(e);
        }

    }

    private void execUI(final Context context,final int orderLvl, String orderName, final String params,
                        final WebView webView, final DispatcherCallBack dispatcherCallBack) {
        Map map = gson.fromJson(params,Map.class);
        OrderManager.getInstance().findAndExecUiOrder(context, orderLvl, orderName, map, new ResultCallback() {
            @Override
            public void onResult(int status, String orderName, Object result) {
                try {
                    handleCallback(status,orderName,gson.toJson(result),webView,dispatcherCallBack);
                }catch (Exception e) {
                    logOrderException(e);
                }
            }
        });

    }

    private void execNonUI(Context context, int orderLvl, String orderName, String params,final  WebView webView,
                           final DispatcherCallBack dispatcherCallBack) {

    }

    private void handleCallback(final int responseCode, final String orderName, final String response,
                                final WebView webView, final DispatcherCallBack dispatcherCallBack) {
        WebMainHandler.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("order dispatcher","handle native result call back! ");
            }
        });

    }

    public interface DispatcherCallBack {
        boolean preHandleBeforeCallback(int responseCode, String actionName, String response);
    }


    private void logOrderException(Exception e) {
        Log.e("OrderDispatcher","order execute occur an error !" , e);
    }

}
















