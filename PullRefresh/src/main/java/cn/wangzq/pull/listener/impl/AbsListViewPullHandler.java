package cn.wangzq.pull.listener.impl;

import android.widget.AbsListView;


import java.lang.ref.WeakReference;

import cn.wangzq.pull.listener.PullHandler;

public class AbsListViewPullHandler implements PullHandler {

    private WeakReference<AbsListView> mView;

    public AbsListViewPullHandler(AbsListView view) {
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
