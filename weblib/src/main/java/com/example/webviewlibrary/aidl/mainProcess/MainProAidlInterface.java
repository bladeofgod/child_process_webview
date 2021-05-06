package com.example.webviewlibrary.aidl.mainProcess;

import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import com.example.webviewlibrary.IWebAidlCallback;
import com.example.webviewlibrary.IWebAidlInterface;
import com.example.webviewlibrary.OrderDispatcher;
import com.google.gson.Gson;

import java.util.Map;

public class MainProAidlInterface extends IWebAidlInterface.Stub {
    @Override
    public void handleWebAction(int level, String actionName, String jsonParams, IWebAidlCallback callback) throws RemoteException {
        int pid = Process.myPid();
        Log.e("","handleWebAction   :" + pid);
        try {
            handleRemoteAction(level,actionName,new Gson().fromJson(jsonParams,Map.class),callback);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleRemoteAction(int level, final String actionName, Map paramMap, final IWebAidlCallback callback) {
        Log.e("","handleRemoteAction");
    }
}
