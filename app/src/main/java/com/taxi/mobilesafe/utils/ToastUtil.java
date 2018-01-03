package com.taxi.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by taxi01 on 2017/12/15.
 */

public class ToastUtil {
    public static void show(Context context,String string){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }
}
