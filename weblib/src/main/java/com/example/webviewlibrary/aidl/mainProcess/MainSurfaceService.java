package com.example.webviewlibrary.aidl.mainProcess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;


/**
 *
 * share main process surface
 *
 * */

public class MainSurfaceService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MainShareSurfaceBinder(getApplicationContext());
    }
}
