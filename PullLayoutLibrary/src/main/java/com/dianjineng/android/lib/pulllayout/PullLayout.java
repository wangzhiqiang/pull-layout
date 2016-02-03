package com.dianjineng.android.lib.pulllayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.dianjineng.android.lib.pulllayout.enums.FinishState;
import com.dianjineng.android.lib.pulllayout.enums.PullState;
import com.dianjineng.android.lib.pulllayout.handler.PullHandler;
import com.dianjineng.android.lib.pulllayout.handler.views.PullView;


public class PullLayout extends FrameLayout {

    public final String TAG = this.getClass().getSimpleName();

    private PullState mState = PullState.INIT;

    // 按下Y坐标，上一个事件点Y坐标
    private float downY, lastY;
    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float pullDownY = 0;
    // 上拉的距离
    private float pullUpY = 0;

    // 释放刷新的距离
    private float mRefreshDist = 200;
    // 释放加载的距离
    private float mLoadMoreDist = 200;

    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isFirstLayout = true;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;
    // 中部的View 暂时只支持一个 如需多个View 可以自行加ViewGroup
    private FrameLayout mCenterView;
    // 过滤多点触碰
    private int mEvents;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean mCanPullDown = true;
    private boolean mCanPullUp = true;

    // 控制是否能执行刷新和加载的接口
    private PullHandler mPullHandler = new PullView();
    //实现了刷新和加载状态 view变化的接口
    private PullStateListener mPullableStateListener = new com.dianjineng.android.lib.pulllayout.pull.PullDefault();
    // 刷新回调接口
    private OnPullListener mListener;

    private Context mContext;

    @IntegerRes
    private int mItemRefresh;
    @IntegerRes
    private int mItemLoadMore;

    private View mViewRefresh;
    private View mViewLoadMore;



    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
                    / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
            if (!isTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (mState == PullState.REFRESHING && pullDownY <= mRefreshDist) {
                    pullDownY = mRefreshDist;
                    timer.cancel();
                } else if (mState == PullState.LOADING && -pullUpY <= mLoadMoreDist) {
                    pullUpY = -mLoadMoreDist;
                    timer.cancel();
                }

            }
            if (pullDownY > 0)
                pullDownY -= MOVE_SPEED;
            else if (pullUpY < 0)
                pullUpY += MOVE_SPEED;
            if (pullDownY < 0) {
                // 已完成回弹
                pullDownY = 0;
//                mImageRefreshArrow.clearAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (mState != PullState.REFRESHING && mState != PullState.LOADING) {
                    changeState(PullState.INIT);
                }
                timer.cancel();
                requestLayout();
            }
            if (pullUpY > 0) {
                // 已完成回弹
                pullUpY = 0;
//                mImageLoadArrow.clearAnimation();
                // 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (mState != PullState.REFRESHING && mState != PullState.LOADING)
                    changeState(PullState.INIT);
                timer.cancel();
                requestLayout();
            }
            Log.d("handle", "handle");
            // 刷新布局,会自动调用onLayout
            requestLayout();
            // 没有拖拉或者回弹完成
            if (pullDownY + Math.abs(pullUpY) == 0)
                timer.cancel();
        }

    };


    public void setPullableStateListener(PullStateListener listener) {
        this.mPullableStateListener = listener;
    }

    public void setPullHandler(PullHandler listener) {
        this.mPullHandler = listener;
    }

    public void setOnRefreshListener(OnPullListener listener) {
        mListener = listener;
    }

    public PullLayout(Context context) {
        super(context);
        initView(context,null);
    }

    public PullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,  attrs);
    }

    public PullLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context,  attrs);
    }

    private void initView(Context context,AttributeSet attrs) {
        mContext = context;
        timer = new MyTimer(updateHandler);
        if(null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullLayout);
            mItemRefresh = a.getResourceId(R.styleable.PullLayout_itemRefresh, -1);
            mItemLoadMore = a.getResourceId(R.styleable.PullLayout_itemLoadMore, -1);
            mRefreshDist = a.getDimensionPixelSize(R.styleable.PullLayout_dist,100);
            mLoadMoreDist = mRefreshDist;
            a.recycle();



            if( -1 != mItemRefresh){
                //noinspection ResourceType
                mViewRefresh = View.inflate(mContext,mItemRefresh,null);
            }
            if(-1 != mItemLoadMore){
                //noinspection ResourceType
                mViewLoadMore = View.inflate(mContext,mItemLoadMore,null);
            }
        }

    }

    private void hide() {
        timer.schedule(5);
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     */
    /**
     * @param state FinishState
     */
    public void refreshFinish(FinishState state) {

        mPullableStateListener.refreshFinish(state);
        if (pullDownY > 0) {
            // 刷新结果停留1秒
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    changeState(PullState.DONE);
                    hide();
                }
            }.sendEmptyMessageDelayed(0, 1000);
        } else {
            changeState(PullState.DONE);
            hide();
        }
    }

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param state FinishState
     */
    public void loadFinish(FinishState state) {
        mPullableStateListener.loadFinish(state);
        if (pullUpY < 0) {
            // 刷新结果停留1秒
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    changeState(PullState.DONE);
                    hide();
                }
            }.sendEmptyMessageDelayed(0, 1000);
        } else {
            changeState(PullState.DONE);
            hide();
        }
    }

    private void changeState(PullState to) {
        mState = to;
        switch (mState) {
            case INIT:
                mPullableStateListener.init();
                break;
            case RELEASE_TO_REFRESH:
                mPullableStateListener.releaseToRefresh();
                break;
            case REFRESHING:
                mPullableStateListener.refreshing();
                break;
            case RELEASE_TO_LOAD:

                mPullableStateListener.releaseToLoad();
                break;
            case LOADING:
                mPullableStateListener.loading();
                break;
            case DONE:
                mPullableStateListener.done();
                break;
        }
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        mCanPullDown = true;
        mCanPullUp = true;
    }

    /*
     *由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                lastY = downY;
                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEvents == 0) {
                    if (pullDownY > 0
                            || (mPullHandler.canPullDown()
                            && mCanPullDown && mState != PullState.LOADING)) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            mCanPullDown = false;
                            mCanPullUp = true;
                        }
                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (mState == PullState.REFRESHING) {
                            // 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                    } else if (pullUpY < 0
                            || (mPullHandler.canPullUp() && mCanPullUp && mState != PullState.REFRESHING)) {
                        // 可以上拉，正在刷新时不能上拉
                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            mCanPullDown = true;
                            mCanPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight())
                            pullUpY = -getMeasuredHeight();
                        if (mState == PullState.LOADING) {
                            // 正在加载的时候触摸移动
                            isTouch = true;
                        }
                    } else
                        releasePull();
                } else
                    mEvents = 0;
                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
                        * (pullDownY + Math.abs(pullUpY))));
                if (pullDownY > 0 || pullUpY < 0)
                    requestLayout();
                if (pullDownY > 0) {
                    if (pullDownY <= mRefreshDist
                            && (mState == PullState.RELEASE_TO_REFRESH || mState == PullState.DONE)) {
                        // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                        changeState(PullState.INIT);
                    }
                    if (pullDownY >= mRefreshDist && mState == PullState.INIT) {
                        // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                        changeState(PullState.RELEASE_TO_REFRESH);
                    }
                } else if (pullUpY < 0) {
                    // 下面是判断上拉加载的，同上，注意pullUpY是负值
                    if (-pullUpY <= mLoadMoreDist
                            && (mState == PullState.RELEASE_TO_LOAD || mState == PullState.DONE)) {
                        changeState(PullState.INIT);
                    }
                    // 上拉操作
                    if (-pullUpY >= mLoadMoreDist && mState == PullState.INIT) {
                        changeState(PullState.RELEASE_TO_LOAD);
                    }

                }
                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((pullDownY + Math.abs(pullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > mRefreshDist || -pullUpY > mLoadMoreDist)
                // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                {
                    isTouch = false;
                }
                if (mState == PullState.RELEASE_TO_REFRESH) {
                    changeState(PullState.REFRESHING);
                    // 刷新操作
                    if (mListener != null)
                        mListener.onRefresh(this);
                } else if (mState == PullState.RELEASE_TO_LOAD) {
                    changeState(PullState.LOADING);
                    // 加载操作
                    if (mListener != null)
                        mListener.onLoadMore(this);
                }
                hide();
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    /**
     * 自动模拟手指滑动的task 用于autoRefresh
     */
    private class AutoRefreshAndLoadTask extends
            AsyncTask<Integer, Float, String> {

        @Override
        protected String doInBackground(Integer... params) {
            while (pullDownY < 4 / 3 * mRefreshDist) {
                pullDownY += MOVE_SPEED;
                publishProgress(pullDownY);
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            changeState(PullState.REFRESHING);
            // 刷新操作
            if (mListener != null)
                mListener.onRefresh(PullLayout.this);
            hide();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (pullDownY > mRefreshDist)
                changeState(PullState.RELEASE_TO_REFRESH);
            requestLayout();
        }

    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        new AutoRefreshAndLoadTask().execute(20);
    }

//    /**
//     * 自动加载
//     */
//    public void autoLoad() {
//        pullUpY = -mLoadMoreDist;
//        requestLayout();
//        changeState(PullState.LOADING);
//        // 加载操作
//        if (mListener != null)
//            mListener.onLoadMore(this);
//    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isFirstLayout) {

            isFirstLayout = false;

            // 这里是第一次进来的时候做一些初始化
//            mRefreshView = getChildAt(0);//
//            mViewLoadMore = getChildAt(2);


//            mCenterView = //getChildAt(1);

            if(null == mCenterView){
                mCenterView = new FrameLayout(mContext);
                LayoutParams params = (LayoutParams) this.getLayoutParams();
                params.width= ViewGroup.LayoutParams.MATCH_PARENT;
                params.height= ViewGroup.LayoutParams.MATCH_PARENT;
                mCenterView.setLayoutParams(params);
            }

            int child = getChildCount();
            List<View> list  = new ArrayList<>();
            for (int i=0;i<child;i++){
                View childView = getChildAt(i);
                list.add(childView);
            }
            for (View v:list) {
                removeView(v);
                mCenterView.addView(v);
            }
            addView(mCenterView);

            addView(mViewRefresh,0);
            addView(mViewLoadMore);

            mPullableStateListener.setRefreshView(mViewRefresh);
            mPullableStateListener.setLoadView(mViewLoadMore);
            mPullableStateListener.initView(mContext);
//
//            mRefreshDist = mViewRefresh.getMeasuredHeight();
//            mLoadMoreDist = mViewLoadMore.getMeasuredHeight();
        }
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        mViewRefresh.layout(0,
                (int) (pullDownY + pullUpY) - mViewRefresh.getMeasuredHeight(),
                mViewRefresh.getMeasuredWidth(), (int) (pullDownY + pullUpY));
        mCenterView.layout(0, (int) (pullDownY + pullUpY),
                mCenterView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
                        + mCenterView.getMeasuredHeight());
        mViewLoadMore.layout(0,
                (int) (pullDownY + pullUpY) + mCenterView.getMeasuredHeight(),
                mViewLoadMore.getMeasuredWidth(),
                (int) (pullDownY + pullUpY) + mCenterView.getMeasuredHeight()
                        + mViewLoadMore.getMeasuredHeight());
    }



    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    /**
     * 刷新加载回调接口
     *
     */
    public interface OnPullListener {
        /**
         * 刷新操作
         */
        void onRefresh(PullLayout layout);

        /**
         * 加载操作
         */
        void onLoadMore(PullLayout layout);
    }


    /**
     * 刷新或加载完成后的回调接口
     */
    private interface FinishListener {
        void refreshFinish(FinishState state);
        void loadFinish(FinishState state);
    }

    /**
     * 初始化HeadView 和FooterView
     *<br/>
     * 执行顺序
     * setRefreshView();
     * setLoadView();
     * initView();
     */
    private  interface ViewInitListener{
        /**
         * 设置下拉刷新的头部View
         * @param view HeadView
         */
        void setRefreshView(View view);

        /**
         * 设置下拉刷新的Footer
         *
         * @param view FooterView
         */
        void setLoadView(View view);

        /**
         * 返回下拉刷新的HeadView
         * @return View
         */
        View getRefreshView();
        /**
         *返回上拉加载更多的FooterView
         * @return View
         */
        View getLoadView();

        void initView(Context context);
    }

    /**
     * 下拉和上拉状态
     */
    public interface PullStateListener extends FinishListener ,ViewInitListener{

        /**
         * 下拉 上拉布局初始状态
         */
        void init();

        /**
         * 释放刷新状态
         */

        void releaseToRefresh();

        /**
         * 正在刷新状态
         */
        void refreshing();

        /**
         * 释放加载状态
         */
        void releaseToLoad();

        /**
         * 正在加载状态
         */

        void loading();

        /**
         * 刷新或加载完毕
         */
        void done();
    }
}
