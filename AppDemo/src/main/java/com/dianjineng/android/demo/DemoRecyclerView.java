package com.dianjineng.android.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dianjineng.android.demo.adapter.RecyclerViewAdapter;
import com.dianjineng.android.demo.interfaceview.IPullLayout;
import com.dianjineng.android.demo.presenter.PullPresenter;
import com.dianjineng.android.lib.pulllayout.PullLayout;
import com.dianjineng.android.lib.pulllayout.enums.StateFinish;
import com.dianjineng.android.lib.pulllayout.handler.views.PullRecyclerView;

import java.util.List;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 16-2-3-02-03 15:04
 */
public class DemoRecyclerView extends ActivityBase implements PullLayout.OnPullListener ,IPullLayout<List<String>>{

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.pull_layout)
    PullLayout mPullLayout;

    private RecyclerViewAdapter mAdapter;
    private PullPresenter<List<String>> mPresenter = new PullPresenter<>(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.generateLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecyclerViewAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);

        mPullLayout.setPullHandler(new PullRecyclerView(mRecyclerView));
        mPullLayout.setOnPullListener(this);

    }

    @Override
    public void onRefresh(PullLayout layout) {
        mPresenter.doRefresh();
    }

    @Override
    public void onLoadMore(PullLayout layout) {
        mPresenter.doLoadMore();
    }

    @Override
    public void refreshData(List<String> data) {
        mPullLayout.refreshFinish(StateFinish.SUCCESS);
        mAdapter.notifyDataSetChanged(data,true);
        mRecyclerView.getLayoutManager().scrollToPosition(0);
    }

    @Override
    public void loadMoreData(List<String> data) {
        mPullLayout.loadFinish(StateFinish.SUCCESS);
        mAdapter.notifyDataSetChanged(data,false);
    }

    @Override
    public void onLoadError(String error) {
        mPullLayout.refreshFinish(StateFinish.FAILED);
        mPullLayout.loadFinish(StateFinish.FAILED);
    }
}
