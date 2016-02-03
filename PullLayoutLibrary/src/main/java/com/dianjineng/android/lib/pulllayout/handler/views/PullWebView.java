package com.dianjineng.android.lib.pulllayout.handler.views;

import android.webkit.WebView;
import com.dianjineng.android.lib.pulllayout.handler.PullHandler;

import java.lang.ref.WeakReference;

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
