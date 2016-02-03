package com.dianjineng.android.lib.pulllayout.handler.views;

import android.widget.AbsListView;
import com.dianjineng.android.lib.pulllayout.handler.PullHandler;

import java.lang.ref.WeakReference;

public class PullAbsListView implements PullHandler {

    private WeakReference<AbsListView> mView;

    public PullAbsListView(AbsListView view) {
        this.mView = new WeakReference<AbsListView>(view);
    }

    @Override
    public boolean canPullDown() {
        if (mView.get().getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else // 滑到顶部了
            return mView.get().getFirstVisiblePosition() == 0
                    && mView.get().getChildAt(0).getTop() >= 0;
    }

    @Override
    public boolean canPullUp() {
        if (mView.get().getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (mView.get().getLastVisiblePosition() == (mView.get().getCount() - 1)) {
            // 滑到底部了
            if (mView.get().getChildAt(mView.get().getLastVisiblePosition() - mView.get().getFirstVisiblePosition()) != null
                    && mView.get().getChildAt(
                    mView.get().getLastVisiblePosition()
                            - mView.get().getFirstVisiblePosition()).getBottom() <= mView.get().getMeasuredHeight())
                return true;
        }
        return false;
    }

}
