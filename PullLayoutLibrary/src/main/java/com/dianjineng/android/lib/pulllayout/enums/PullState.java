package com.dianjineng.android.lib.pulllayout.enums;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 15-12-27-12-27 17:50
 */
public enum PullState {
    //初始状态
    INIT,
    // 释放刷新
    RELEASE_TO_REFRESH,
    // 正在刷新
    REFRESHING,
    // 释放加载
    RELEASE_TO_LOAD,
    // 正在加载
    LOADING,
    // 操作完毕
    DONE
}
