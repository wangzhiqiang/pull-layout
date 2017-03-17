package cn.wangzq.pull.listener.impl;


import cn.wangzq.pull.listener.PullHandler;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 15-12-25-12-25 17:00
 */
public class DefaultViewPullHandler implements PullHandler {
    @Override
    public boolean canPullDown() {
        return true;
    }

    @Override
    public boolean canPullUp() {
        return true;
    }
}
