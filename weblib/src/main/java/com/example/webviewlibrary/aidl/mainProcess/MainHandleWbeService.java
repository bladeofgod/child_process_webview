package com.example.webviewlibrary.aidl.mainProcess;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;


/**
 *
 * handle for web on main process
 *
 * */

public class MainHandleWbeService extends Service {

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int pid = Process.myPid();
        Log.e("","MainHandleWbeViewService  on bind : " +pid);
        return null;
    }
}
