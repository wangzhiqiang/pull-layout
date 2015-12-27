package com.dianjineng.android.lib.pulllayout.handler.views;

import android.widget.ScrollView;
import com.dianjineng.android.lib.pulllayout.handler.PullHandler;

public class PullScrollView implements PullHandler {


    private ScrollView mView;

    public PullScrollView(ScrollView mView) {
        this.mView = mView;
    }

    @Override
    public boolean canPullDown() {
        return mView.getScrollY() == 0;
    }

    @Override
    public boolean canPullUp() {
        return mView.getScrollY() >= (mView.getChildAt(0).getHeight() - mView.getMeasuredHeight());
    }

}
