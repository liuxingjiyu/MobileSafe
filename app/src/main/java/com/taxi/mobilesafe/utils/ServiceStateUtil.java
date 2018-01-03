package com.taxi.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by shizhengui on 2018/1/1.
 */

public class ServiceStateUtil {

    private static final String TAG = "ServiceStateUtil";

    public static boolean serviceRunning(Context context, Class<? extends Service> clazz){
        ActivityManager systemService = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = systemService.getRunningServices(50);
        for (ActivityManager.RunningServiceInfo info : runningServices){
            ComponentName service = info.service;
            String className = service.getClassName();
            /*Log.e(TAG, "serviceRunning: " + className );
            Log.w(TAG, "serviceRunning: " + clazz.getName() );*/
            if (className.equals(clazz.getName())){
                return true;
            }
        }
        return false;
    }
}
