package com.example.webviewlibrary.interfaces;

import android.content.Context;

import java.util.Map;

public interface IOrder {

    String getName();

    void exec(Context context, Map params,ResultCallback callback);
}
