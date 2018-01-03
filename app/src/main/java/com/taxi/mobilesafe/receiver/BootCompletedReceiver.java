package com.taxi.mobilesafe.receiver;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.taxi.mobilesafe.utils.ConstantValue;
import com.taxi.mobilesafe.utils.SpUtil;

/**
 * Created by taxi01 on 2017/12/27.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompletedReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String spSimSerialNumber = SpUtil.getString(context, ConstantValue.SIM_NUMBER, "");
        String safe_number = SpUtil.getString(context,
                ConstantValue.CONTACT_PHONE, "");
        if (TextUtils.isEmpty(safe_number)) {
            return;
        }
        String simSerialNumber = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        simSerialNumber = telephonyManager.getSimSerialNumber();
        Log.e(TAG, "onReceive: " +"手机丢失" );
        if (!spSimSerialNumber.equals(simSerialNumber)){
            SmsManager.getDefault().sendTextMessage(safe_number,null,"手机丢失",null,null);
        }
    }
}
