package com.example.webviewlibrary.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.webviewlibrary.aidl.mainProcess.MainHandleWbeService;

import java.util.concurrent.CountDownLatch;

/**
 *
 * query binder from main process
 *
 * */


public class RemoteWebBinderPool {

    public static final int BINDER_WEB_AIDL = 1;

    private Context mAppContext;

    private static volatile RemoteWebBinderPool singleton;

    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private RemoteWebBinderPool(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public static RemoteWebBinderPool getInstance(Context context) {
        if(singleton == null) {
            synchronized (RemoteWebBinderPool.class) {
                if(singleton == null) {
                    singleton = new RemoteWebBinderPool(context);
                }
            }
        }

        return singleton;
    }

    private synchronized void connectMainProService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mAppContext, MainHandleWbeService.class);
        mAppContext.bindService(service,serviceConnection,Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}



















