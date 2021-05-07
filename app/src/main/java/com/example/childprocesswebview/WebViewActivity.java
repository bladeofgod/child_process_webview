package com.example.childprocesswebview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;

import com.example.webviewlibrary.CommonWebFragment;
import com.example.webviewlibrary.base.WebBaseFragment;
import com.example.webviewlibrary.constant.WebConstant;

public class WebViewActivity extends AppCompatActivity {


    private String title;
    private String url;

    WebBaseFragment webFragment;

    public static void start(Context context,String title,String url) {
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra(WebConstant.INTENT_TAG_TITLE,title);
        intent.putExtra(WebConstant.INTENT_TAG_URL,url);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        int pid = Process.myPid();
        Log.e("","WebViewActivity  on create :  当前进程ID 为 " +pid);

        title = getIntent().getStringExtra(WebConstant.INTENT_TAG_TITLE);
        url = getIntent().getStringExtra(WebConstant.INTENT_TAG_URL);
        setTitle(title);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        webFragment = CommonWebFragment.newInstance(url);

        transaction.replace(R.id.fragment_web_view,webFragment).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webFragment != null && webFragment instanceof WebBaseFragment) {
            boolean flag = webFragment.onKeyDown(keyCode, event);
            if (flag) {
                return flag;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}