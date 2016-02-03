package com.dianjineng.android.lib.pulllayout.pull;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.dianjineng.android.lib.pulllayout.PullLayout;
import com.dianjineng.android.lib.pulllayout.R;
import com.dianjineng.android.lib.pulllayout.enums.FinishState;

import java.lang.ref.WeakReference;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 15-12-27-12-27 00:02
 */
public class PullDefault implements PullLayout.PullStateListener {

    // 下拉箭头的转180°动画
    private RotateAnimation mRotateAnimation;
    // 均匀旋转动画
    private RotateAnimation mRefreshingAnimation;
    // 下拉头
    private View mRefreshView;
    // 下拉的箭头
    private ImageView mImageRefreshArrow;
    // 正在刷新的图标
    private ImageView mImageRefreshLoading;
    // 刷新结果图标
    private ImageView mImageRefreshState;
    // 刷新结果：成功或失败
    private TextView mTextRefreshState;

    // 上拉头
    private View mLoadMoreView;
    // 上拉的箭头
    private ImageView mImageLoadArrow;
    // 正在加载的图标
    private ImageView mImageLoadLoading;
    // 加载结果图标
    private ImageView mImageLoadState;
    // 加载结果：成功或失败
    private TextView mTextLoadState;

    @Override
    public void setRefreshView(View view) {
        this.mRefreshView = view;
    }

    @Override
    public void setLoadView(View view) {
        this.mLoadMoreView = view;
    }

    @Override
    public View getRefreshView() {
        return this.mRefreshView;
    }

    @Override
    public View getLoadView() {
        return this.mLoadMoreView;
    }

    @Override
    public void initView(Context context) {

        if(null == mRefreshView || null == mLoadMoreView){
            return;
        }

        mRotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
        mRefreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        mRotateAnimation.setInterpolator(lir);
        mRefreshingAnimation.setInterpolator(lir);

        mImageRefreshArrow = (ImageView) mRefreshView.findViewById(R.id.refresh_image_arrow);
        mTextRefreshState = (TextView) mRefreshView.findViewById(R.id.refresh_text_state);
        mImageRefreshLoading = (ImageView) mRefreshView.findViewById(R.id.refresh_image_loading);
        mImageRefreshState = (ImageView) mRefreshView.findViewById(R.id.refresh_image_state);
        // 初始化上拉布局
        mImageLoadArrow = (ImageView) mLoadMoreView.findViewById(R.id.load_image_arrow);
        mTextLoadState = (TextView) mLoadMoreView.findViewById(R.id.load_text_state);
        mImageLoadLoading = (ImageView) mLoadMoreView.findViewById(R.id.load_image_loading);
        mImageLoadState = (ImageView) mLoadMoreView.findViewById(R.id.load_image_state);

    }

    @Override
    public void init() {

        mImageRefreshState.setVisibility(View.GONE);
        mTextRefreshState.setText("下拉刷新");
        mImageRefreshArrow.clearAnimation();
        mImageRefreshArrow.setVisibility(View.VISIBLE);
        // 上拉布局初始状态
        mImageLoadState.setVisibility(View.GONE);
        mTextLoadState.setText("上拉加载更多");
        mImageLoadArrow.clearAnimation();
        mImageLoadArrow.setVisibility(View.VISIBLE);

    }

    @Override
    public void releaseToRefresh() {
        mTextRefreshState.setText("释放刷新");
        mImageRefreshArrow.startAnimation(mRotateAnimation);
    }

    @Override
    public void refreshing() {
        mImageRefreshArrow.clearAnimation();
        mImageRefreshLoading.setVisibility(View.VISIBLE);
        mImageRefreshArrow.setVisibility(View.INVISIBLE);
        mImageRefreshLoading.startAnimation(mRefreshingAnimation);
        mTextRefreshState.setText("正在刷新...");
    }

    @Override
    public void releaseToLoad() {
        mTextLoadState.setText("释放加载");
        mImageLoadArrow.startAnimation(mRotateAnimation);
    }

    @Override
    public void loading() {
        mImageLoadArrow.clearAnimation();
        mImageLoadLoading.setVisibility(View.VISIBLE);
        mImageLoadArrow.setVisibility(View.INVISIBLE);
        mImageLoadLoading.startAnimation(mRefreshingAnimation);
        mTextLoadState.setText("加载中...");
    }

    @Override
    public void done() {

    }

    @Override
    public void refreshFinish(FinishState state) {

        mImageRefreshLoading.clearAnimation();
        mImageRefreshLoading.setVisibility(View.GONE);
        switch (state) {
            case SUCCESS:
                // 刷新成功
                mImageRefreshState.setVisibility(View.VISIBLE);
                mTextRefreshState.setText("刷新成功");
                mImageRefreshState.setBackgroundResource(R.mipmap.icon_load_success);
                break;
            case FAILED:
            default:
                // 刷新失败
                mImageRefreshState.setVisibility(View.VISIBLE);
                mTextRefreshState.setText("刷新失败");
                mImageRefreshState.setBackgroundResource(R.mipmap.icon_load_failed);
                break;
        }


    }

    @Override
    public void loadFinish(FinishState state) {

        mImageLoadLoading.clearAnimation();
        mImageLoadLoading.setVisibility(View.GONE);
        switch (state) {
            case SUCCESS:
                // 加载成功
                mImageLoadState.setVisibility(View.VISIBLE);
                mTextLoadState.setText("加载成功");
                mImageLoadState.setBackgroundResource(R.mipmap.icon_load_success);
                break;
            case FAILED:
            default:
                // 加载失败
                mImageLoadState.setVisibility(View.VISIBLE);
                mTextLoadState.setText("加载失败");
                mImageLoadState.setBackgroundResource(R.mipmap.icon_load_failed);
                break;
        }
    }
}
