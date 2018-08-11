package com.kx.studyview.bookdemo;

import android.app.Application;
import android.content.Context;

import com.kx.studyview.bookdemo.utils.LogUtils;


public class AndroidApplication extends Application {
    public static Context mContext ;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        LogUtils.init();
    }
    public static Context getContext(){
        return mContext;
    }
}
