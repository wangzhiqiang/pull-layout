package cn.wangzq.pull.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Locale;

import cn.wangzq.pull.listener.PullHandler;
import cn.wangzq.pull.listener.impl.DefaultViewPullHandler;

public class PullRefreshLayout extends FrameLayout {

    private String TAG = getClass().getSimpleName();


    public PullRefreshLayout(Context context) {
        super(context);
        mContentLayout = new FrameLayout(context);
        initView();
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context);
        mContentLayout = new FrameLayout(context, attrs);
        initView();
    }

    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context);
        mContentLayout = new FrameLayout(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context);
        mContentLayout = new FrameLayout(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    void initView() {
    }

    //第一版只有垂直方向的  后续添加横向的

    private float mDownY = 0f;
    private float mMovedY = 0f;
    private float mSpaceY = 0f;

    private PullHandler mPullHandler = new DefaultViewPullHandler();
    private FrameLayout mContentLayout;

    private boolean isFirstLayout = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        单手指操作：ACTION_DOWN---ACTION_MOVE----ACTION_UP
//        多手指操作：ACTION_DOWN---ACTION_POINTER_DOWN---ACTION_MOVE--ACTION_POINTER_UP---ACTION_UP.
//        Log.v(TAG, String.format(Locale.CHINESE, "Action:%d  X:%f   Y:%f", ev.getAction(), ev.getX(), ev.getY()));
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mSpaceY = 0;
                //TODO 取消原有的滑动操作等
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                //TODO 过滤多点
                break;
            case MotionEvent.ACTION_MOVE:

                mMovedY = ev.getY();
                mSpaceY = mMovedY - mDownY;
                //TODO 判断拉动的方向
                if (isPullDown()) {
                    if (mPullHandler.canPullDown()) {
                        //TODO  通知ViewHeader 做对应操作
                        requestLayout();
                    }
                } else {
                    if (mPullHandler.canPullUp()) {
                        //TODO  通知ViewFooter做对应操作
                        requestLayout();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                mSpaceY = 0;
//                requestLayout();
                //TODO 判断释放还是???
            default:
                break;
        }
        super.dispatchTouchEvent(ev);
        return true;
    }


    //判断下拉 还是 上拉
    private boolean isPullDown() {
        return mMovedY - mDownY > 0;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Log.v(TAG, String.format(Locale.CHINESE, "onLayout:  changed=%b left=%d top=%d right=%d bottom=%d", changed, left, top, right, bottom));
        super.onLayout(changed, left, top, right, bottom);
        if (isFirstLayout) {
            View child = getChildAt(0);
            removeView(child);
            addView(mContentLayout);
            mContentLayout.addView(child);
            isFirstLayout = false;

            if(isInEditMode()){
                mContentLayout.layout(0, 0, getWidth(), getHeight());
            }
        } else {
            //TODO  判断移动操作等
            mContentLayout.layout(
                    0,
                    (int) (mSpaceY),
                    mContentLayout.getMeasuredWidth(),
                    (int) (mSpaceY) + mContentLayout.getMeasuredHeight());
        }

    }


//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//    }

    public void setPullHandler(PullHandler handler) {
        this.mPullHandler = handler;
    }

    /**
     * 模拟自动下拉操作
     */
    public void autoRefresh() {

    }

}
