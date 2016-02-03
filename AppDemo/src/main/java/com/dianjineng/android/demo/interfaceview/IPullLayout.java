package com.dianjineng.android.demo.interfaceview;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 16-2-3-02-03 15:26
 */
public interface IPullLayout<T> {

    /**
     * 刷新数据
     *
     * @param data
     */
    void refreshData(T data);
    /**
     * 加载更多数据
     * @param data
     */
    void loadMoreData(T data);

    void onLoadError(String error);
}
