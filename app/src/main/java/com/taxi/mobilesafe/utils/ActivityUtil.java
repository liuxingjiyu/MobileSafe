package com.taxi.mobilesafe.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by shizhengui on 2017/12/31.
 */

public class ActivityUtil {

    public static void startActivity(Context context, Class<?> clazz){
        Intent intent = new Intent(context,clazz);
        context.startActivity(intent);
    }

    public static boolean hasPermission(Context context, String persmission){
        if (ContextCompat.checkSelfPermission(context,persmission) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }
}
