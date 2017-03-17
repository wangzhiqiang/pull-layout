package cn.wangzq.pull.listener.impl;

import android.widget.ScrollView;


import java.lang.ref.WeakReference;

import cn.wangzq.pull.listener.PullHandler;

public class ScrollViewPullHandler implements PullHandler {


    private WeakReference<ScrollView> mView;

    public ScrollViewPullHandler(ScrollView mView) {
        this.mView =new WeakReference(mView);
    }

    @Override
    public boolean canPullDown() {
        return mView.get().getScrollY() == 0;
    }

    @Override
    public boolean canPullUp() {
        return mView.get().getScrollY() >= (mView.get().getChildAt(0).getHeight() - mView.get().getMeasuredHeight());
    }

}
