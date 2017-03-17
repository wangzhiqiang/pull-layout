package cn.wangzq.pull.listener.impl;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


import java.lang.ref.WeakReference;

import cn.wangzq.pull.listener.PullHandler;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 15-12-23-12-23 13:45
 */
public class RecyclerViewPullHandler implements PullHandler {

    private WeakReference<RecyclerView> mView;

    public RecyclerViewPullHandler(RecyclerView view) {
        this.mView = new WeakReference<RecyclerView>(view);
    }

    @Override
    public boolean canPullDown() {
        // 判断条件 没有child 或 第一个已经显示了
        if (mView.get().getAdapter() == null) {
            return false;
        }
        return mView.get().getAdapter().getItemCount() == 0 || (getFirstVisiblePosition() == 0 && mView.get().getChildAt(0).getTop() >= 0);
    }

    @Override
    public boolean canPullUp() {
        if (mView.get().getAdapter() == null) {
            return false;
        }
        if (getLastVisiblePosition() == (mView.get().getAdapter().getItemCount() - 1)) {
            // 滑到底部了
            if (mView.get().getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && mView.get().getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= mView.get().getMeasuredHeight())
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
        if (mView.get().getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) mView.get().getLayoutManager()).findFirstVisibleItemPosition();
        } else if (mView.get().getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) mView.get().getLayoutManager()).findFirstVisibleItemPosition();
        } else if (mView.get().getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mView.get().getLayoutManager();
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
        if (mView.get().getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) mView.get().getLayoutManager()).findLastVisibleItemPosition();
        } else if (mView.get().getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) mView.get().getLayoutManager()).findLastVisibleItemPosition();
        } else if (mView.get().getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mView.get().getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = mView.get().getLayoutManager().getItemCount() - 1;
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
