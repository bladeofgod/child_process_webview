package com.example.childprocesswebview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
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


    }
}