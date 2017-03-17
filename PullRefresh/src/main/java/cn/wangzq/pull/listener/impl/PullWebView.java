package cn.wangzq.pull.listener.impl;

import android.webkit.WebView;


import java.lang.ref.WeakReference;

import cn.wangzq.pull.listener.PullHandler;

public class PullWebView implements PullHandler {

    private WeakReference<WebView> mView;

    public PullWebView(WebView mView) {
        this.mView = new WeakReference<WebView>(mView);
    }

    @Override
    public boolean canPullDown() {
        return mView.get().getScrollY() == 0;
    }

    @Override
    public boolean canPullUp() {
        return mView.get().getScrollY() >= mView.get().getContentHeight() * mView.get().getScale()
                - mView.get().getMeasuredHeight();
    }
}
