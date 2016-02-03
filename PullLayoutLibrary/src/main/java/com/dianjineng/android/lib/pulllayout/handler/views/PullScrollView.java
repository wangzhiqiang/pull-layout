package com.dianjineng.android.lib.pulllayout.handler.views;

import android.widget.ScrollView;
import com.dianjineng.android.lib.pulllayout.handler.PullHandler;

import java.lang.ref.WeakReference;

public class PullScrollView implements PullHandler {


    private WeakReference<ScrollView> mView;

    public PullScrollView(ScrollView mView) {
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
