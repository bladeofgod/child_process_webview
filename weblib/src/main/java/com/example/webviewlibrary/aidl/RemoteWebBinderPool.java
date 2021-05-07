package com.example.webviewlibrary.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.webviewlibrary.IBinderPool;
import com.example.webviewlibrary.aidl.mainProcess.MainHandleWbeService;
import com.example.webviewlibrary.aidl.mainProcess.MainProAidlInterface;

import java.util.concurrent.CountDownLatch;

/**
 *
 * query binder from main process
 *
 * */


public class RemoteWebBinderPool {

    public static final int BINDER_WEB_AIDL = 1;

    private Context mAppContext;

    private IBinderPool mBinderPool;

    private static volatile RemoteWebBinderPool singleton;

    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private RemoteWebBinderPool(Context context) {
        mAppContext = context.getApplicationContext();
        connectMainProService();
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

    /**
     * return a {@link IBinder} by
     * @param binderCode a binder match code
     *
     * */
    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if(mBinderPool != null) {
                binder = mBinderPool.queryBinder(binderCode);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return binder;
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

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient,0);

            }catch (Exception e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private final IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient,0);
            mBinderPool = null;
            connectMainProService();
        }
    };

    public static class BinderPoolImpl extends IBinderPool.Stub{

        private Context context;

        public BinderPoolImpl(Context context) {
            this.context = context;
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case BINDER_WEB_AIDL:
                    //fetch main process's binder
                    binder = new MainProAidlInterface();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }

}



















