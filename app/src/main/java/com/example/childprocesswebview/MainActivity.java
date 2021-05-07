package com.example.childprocesswebview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;

import com.example.webviewlibrary.CommonWebFragment;
import com.example.webviewlibrary.base.WebBaseFragment;
import com.example.webviewlibrary.constant.WebConstant;

public class MainActivity extends AppCompatActivity {

    final String localH5 = "file:///android_asset/aidl.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int pid = Process.myPid();
        Log.e("","MainActivity  on create :  当前进程ID 为 " +pid);

        findViewById(R.id.btn_open_jd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.start(MainActivity.this,"京东","https://m.jd.com/");

            }
        });

        findViewById(R.id.btn_local_h5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.start(MainActivity.this,"京东",localH5);
            }
        });

        findViewById(R.id.btn_share_surface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TextureDisplayActivity.class));
            }
        });

    }
}