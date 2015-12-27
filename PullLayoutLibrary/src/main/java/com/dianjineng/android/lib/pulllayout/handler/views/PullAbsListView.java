package com.dianjineng.android.lib.pulllayout.handler.views;

import android.widget.AbsListView;
import com.dianjineng.android.lib.pulllayout.handler.PullHandler;

public class PullAbsListView implements PullHandler {

    private AbsListView mAbsListView;

    public PullAbsListView(AbsListView view) {
        this.mAbsListView = view;
    }

    @Override
    public boolean canPullDown() {
        if (mAbsListView.getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else // 滑到顶部了
            return mAbsListView.getFirstVisiblePosition() == 0
                    && mAbsListView.getChildAt(0).getTop() >= 0;
    }

    @Override
    public boolean canPullUp() {
        if (mAbsListView.getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (mAbsListView.getLastVisiblePosition() == (mAbsListView.getCount() - 1)) {
            // 滑到底部了
            if (mAbsListView.getChildAt(mAbsListView.getLastVisiblePosition() - mAbsListView.getFirstVisiblePosition()) != null
                    && mAbsListView.getChildAt(
                    mAbsListView.getLastVisiblePosition()
                            - mAbsListView.getFirstVisiblePosition()).getBottom() <= mAbsListView.getMeasuredHeight())
                return true;
        }
        return false;
    }

}
