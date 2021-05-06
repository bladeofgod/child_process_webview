package com.example.webviewlibrary;


import android.os.Handler;
import android.os.Looper;

/**
 *
 * main handler in web process
 *
 *
 * */


public class WebMainHandler extends Handler {

    private static WebMainHandler instance = new WebMainHandler(Looper.getMainLooper());

    protected WebMainHandler(Looper looper) {
        super(looper);
    }

    public static WebMainHandler getInstance() {
        return instance;
    }

    public static void runOnUiThread(Runnable runnable) {
        if(Looper.getMainLooper().equals(Looper.myLooper())) {
            runnable.run();
        } else {
            instance.post(runnable);
        }
    }


}
