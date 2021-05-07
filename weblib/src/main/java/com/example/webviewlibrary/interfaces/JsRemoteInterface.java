package com.example.webviewlibrary.interfaces;


/*
* js  native 连接处
*
* */

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;

public final class JsRemoteInterface {

    private final Context mContext;
    private final Handler mHandler = new android.os.Handler(Looper.getMainLooper());
    private AidlCommand aidlCommand;

    public JsRemoteInterface(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * msg from h5
     * @param msg is command from h5.
     * @param param is params from h5.
     *
     * */
    @JavascriptInterface
    public void post(final String msg,final String param) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if(aidlCommand != null) {
                        aidlCommand.exec(mContext,msg,param);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void setAidlCommand(AidlCommand aidlCommand) {
        this.aidlCommand = aidlCommand;
    }

    public interface AidlCommand {
        void exec(Context context, String cmd, String params);
    }


}











