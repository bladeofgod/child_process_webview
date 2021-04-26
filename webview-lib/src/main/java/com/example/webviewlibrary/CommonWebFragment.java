package com.example.webviewlibrary;

import android.os.Bundle;

import com.example.webviewlibrary.base.WebBaseFragment;
import com.example.webviewlibrary.constant.WebConstant;

public class CommonWebFragment extends WebBaseFragment {

    public static CommonWebFragment newInstance(String url) {
        CommonWebFragment fragment = new CommonWebFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(WebConstant.INTENT_TAG_URL,url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_common_web;
    }
}
