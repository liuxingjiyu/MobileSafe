package com.taxi.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by taxi01 on 2017/12/20.
 */

public class SpUtil {

    private static SharedPreferences sp;

    public static boolean putBoolean(Context context, String key, boolean value){
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue){
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue);
    }

    public static boolean putString(Context context, String key, String value){
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.edit().putString(key,value).commit();
    }

    public static String getString(Context context, String key, String defValue){
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key,defValue);
    }

    /**删除节点
     * @param context 上下文
     * @param key   键值
     */
    public static void remove(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
       sp.edit().remove(key).commit();
    }
}
