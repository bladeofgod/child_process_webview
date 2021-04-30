package com.example.webviewlibrary.aidl.mainProcess;

import android.os.RemoteException;

import com.example.webviewlibrary.IWebAidlCallback;
import com.example.webviewlibrary.IWebAidlInterface;

public class MainProAidlInterface extends IWebAidlInterface.Stub {
    @Override
    public void handleWebAction(int level, String actionName, String jsonParams, IWebAidlCallback callback) throws RemoteException {

    }
}
