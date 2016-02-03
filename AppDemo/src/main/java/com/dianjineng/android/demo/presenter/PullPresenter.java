package com.dianjineng.android.demo.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.dianjineng.android.demo.interfaceview.IPullLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 16-2-3-02-03 15:30
 */
public class PullPresenter<T> {

    private final int TYPE_REFRESH = 0x010;
    private final int TYPE_MORE = 0x011;

    private IPullLayout<T> mIView;

    private int mOffset =0;
    private int mLimit =15;

    public PullPresenter(IPullLayout<T> mIView) {
        this.mIView = mIView;
    }

    public void doLoadMore(){

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                    Message message = mHandler.obtainMessage();
                    message.what = TYPE_MORE;
                    List<T> list = new ArrayList<T>();
                    for (int  i=0;i<mLimit;i++){
                        mOffset++;
                        list.add((T) ("data"+mOffset));
                        message.obj = list;
                    }
                    mHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    mIView.onLoadError(e.getMessage());
                }

            }
        }.start();


    }

    public void doRefresh(){

        mOffset =0;
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                    Message message = mHandler.obtainMessage();
                    message.what = TYPE_REFRESH;
                    List<T> list = new ArrayList<T>();
                    for (int  i=0;i<mLimit;i++){
                        mOffset++;
                        list.add((T) ("data"+mOffset));
                        message.obj = list;
                    }
                    mHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    mIView.onLoadError(e.getMessage());
                }
            }
        }.start();

    }

    private void afterLoadData(int type ,T data){


        if (type  == TYPE_MORE){
            mIView.loadMoreData(data);
        }else if (type == TYPE_REFRESH){
            mIView.refreshData(data);
        }else {
            mIView.onLoadError("未知类型");
        }
    }

      Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            afterLoadData(msg.what, (T) msg.obj);
        }
    };


}
