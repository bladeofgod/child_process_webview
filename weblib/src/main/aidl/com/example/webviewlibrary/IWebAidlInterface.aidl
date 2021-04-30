// IWebAidlInterface.aidl
package com.example.webviewlibrary;

// Declare any non-default types here with import statements

import com.example.webviewlibrary.IWebAidlCallback;

interface IWebAidlInterface {

    void handleWebAction(int level, String actionName, String jsonParams, in IWebAidlCallback callback);

}