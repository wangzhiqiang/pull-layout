package com.dianjineng.android.lib.pulllayout.handler.views;

import com.dianjineng.android.lib.pulllayout.handler.PullHandler;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 15-12-25-12-25 17:00
 */
public class PullView implements PullHandler {
    @Override
    public boolean canPullDown() {
        return true;
    }

    @Override
    public boolean canPullUp() {
        return true;
    }
}
