package com.taxi.mobilesafe;

import android.app.Application;

import org.xutils.x;

/**
 * Created by taxi01 on 2017/12/18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
