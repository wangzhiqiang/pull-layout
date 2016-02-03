package com.dianjineng.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends ActivityBase {

    @Bind(R.id.recyclerView)
    Button mBtnRecyclerView;
    @Bind(R.id.listView)
    Button mBtnListView;
    @Bind(R.id.gridView)
    Button mBtnGridView;
    @Bind(R.id.scrollView)
    Button mBtnScrollView;
    @Bind(R.id.webView)
    Button mBtnWebView;
    @Bind(R.id.textView)
    Button mBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({
            R.id.recyclerView,
            R.id.listView,
            R.id.gridView,
            R.id.scrollView,
            R.id.webView,
            R.id.textView,
            R.id.self
    })
    public void onBtnClicked(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.recyclerView:
                intent.setClass(mContext, DemoRecyclerView.class);
                break;
            case R.id.listView:
                break;
            case R.id.gridView:
                break;
            case R.id.scrollView:
                break;
            case R.id.webView:
                break;
            case R.id.textView:
                break;
            case R.id.self:
                break;
            default:
                break;
        }

        if (intent.getComponent() != null) {
            startActivity(intent);
        }
    }
}
