package com.dianjineng.android.lib.pulllayout.handler.views;

import android.webkit.WebView;
import com.dianjineng.android.lib.pulllayout.handler.PullHandler;

public class PullWebView implements PullHandler {

    private WebView mView;

    public PullWebView(WebView mView) {

        this.mView = mView;
    }

    @Override
    public boolean canPullDown() {
        return mView.getScrollY() == 0;
    }

    @Override
    public boolean canPullUp() {
        return mView.getScrollY() >= mView.getContentHeight() * mView.getScale()
                - mView.getMeasuredHeight();
    }
}
