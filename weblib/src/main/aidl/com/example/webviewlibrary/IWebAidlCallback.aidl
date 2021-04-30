// IWebAidlCallback.aidl
package com.example.webviewlibrary;

// Declare any non-default types here with import statements

// responseCode 1: success 0 : failed

interface IWebAidlCallback {

    void onResult(int responseCode, String actionName, String response);
}