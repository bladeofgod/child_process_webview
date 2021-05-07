package com.example.webviewlibrary.aidl.mainProcess;

// shared a surface to child process from main process

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.Surface;

import com.example.webviewlibrary.ISurfaceAidlInterface;

public class MainShareSurfaceBinder extends ISurfaceAidlInterface.Stub {

    String url_hls = "https://zhstatic.zhihu.com/cfe/griffith/zhihu2018_hd.mp4";

    private final Context context;
    MainShareSurfaceBinder(Context context) {
        this.context = context;
    }

    MediaPlayer mMediaPlayer;
    @Override
    public void shareSurface(Surface surface) throws RemoteException {
        int pid = Process.myPid();
        Log.e("","MainShareSurfaceBinder  on create :  当前进程ID 为 " +pid);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(context, Uri.parse(url_hls));
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
