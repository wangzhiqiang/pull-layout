package com.dianjineng.android.lib.pulllayout.handler.views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import com.dianjineng.android.lib.pulllayout.handler.PullHandler;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 15-12-23-12-23 13:45
 */
public class PullRecyclerView implements PullHandler {

    private RecyclerView mRecycleView;

    public PullRecyclerView(RecyclerView view) {
        this.mRecycleView = view;
    }

    @Override
    public boolean canPullDown() {
        return getFirstVisiblePosition() == 0  && mRecycleView.getChildAt(0).getTop() >= 0;
    }

    @Override
    public boolean canPullUp() {
        if (getLastVisiblePosition() == (mRecycleView.getAdapter().getItemCount() - 1)) {
            // 滑到底部了
            if (mRecycleView.getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && mRecycleView.getChildAt( getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= mRecycleView.getMeasuredHeight())
                return true;
        }
        return false;
    }

    /**
     * 获取第一条展示的位置
     *
     * @return
     */
    private int getFirstVisiblePosition() {
        int position;
        if (mRecycleView.getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) mRecycleView.getLayoutManager()).findFirstVisibleItemPosition();
        } else if (mRecycleView.getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) mRecycleView.getLayoutManager()).findFirstVisibleItemPosition();
        } else if (mRecycleView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecycleView.getLayoutManager();
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获得当前展示最小的position
     *
     * @param positions
     * @return
     */
    private int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    private int getLastVisiblePosition() {
        int position;
        if (mRecycleView.getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) mRecycleView.getLayoutManager()).findLastVisibleItemPosition();
        } else if (mRecycleView.getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) mRecycleView.getLayoutManager()).findLastVisibleItemPosition();
        } else if (mRecycleView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecycleView.getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = mRecycleView.getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }


}
