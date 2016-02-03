package com.dianjineng.android.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 16-2-3-02-03 15:15
 */
public class ActivityBase extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();

    protected ActivityBase mActivity;
    protected Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = this;
        this.mContext = getBaseContext();
    }
}
