package com.example.childprocesswebview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.example.webviewlibrary.ISurfaceAidlInterface;
import com.example.webviewlibrary.aidl.mainProcess.MainShareSurfaceBinder;
import com.example.webviewlibrary.aidl.mainProcess.MainSurfaceService;

public class TextureDisplayActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private TextureView textureView;
    private Surface sf;
    private ISurfaceAidlInterface surfaceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textureView = new TextureView(this);
        textureView.setSurfaceTextureListener(this);
        setContentView(textureView);
        connectSurfaceService();
    }

    ///connect remote service
    private void connectSurfaceService() {
        Intent service = new Intent(this, MainSurfaceService.class);
        bindService(service,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            logMsg("onServiceConnected");
            surfaceBinder = ISurfaceAidlInterface.Stub.asInterface(service);
            try {
                surfaceBinder.shareSurface(sf);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            logMsg("onServiceDisconnected");
            surfaceBinder = null;

        }
    };

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        logMsg("onSurfaceTextureAvailable");
         sf = new Surface(textureView.getSurfaceTexture());
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
        logMsg("onSurfaceTextureSizeChanged");

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        logMsg("onSurfaceTextureDestroyed");
        sf.release();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
        logMsg("onSurfaceTextureUpdated");

    }

    private void logMsg(String msg) {
        Log.e("TextureDisplayActivity","---" + msg);
    }

}