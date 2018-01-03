package com.taxi.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.service.LocationService;
import com.taxi.mobilesafe.utils.ConstantValue;
import com.taxi.mobilesafe.utils.SpUtil;

import static android.content.Context.DEVICE_POLICY_SERVICE;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
        final DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
        if (open_security) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            StringBuffer buffer = new StringBuffer();
            for (Object object : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                if (smsMessage == null) {
                    continue;
                }
                buffer.append(smsMessage.getDisplayMessageBody());
            }
            String messageBody = buffer.toString();
            abortBroadcast();
            if (messageBody.contains("#*alarm*#")) {
                //Log.e(TAG, "onReceive: " + "#*alarm*#");
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            } else if (messageBody.contains("#*location*#")) {
                //Log.e(TAG, "onReceive: " + "#*location*#");
                context.startService(new Intent(context, LocationService.class));
            } else if (messageBody.contains("#*lockscreen*#")) {
                ComponentName who = new ComponentName(context, DeviceAdmin.class);
                if (mDPM.isAdminActive(who)) {
                    mDPM.resetPassword("123", 0);
                    mDPM.lockNow();
                }
            } else if (messageBody.contains("#*wipedata*#")) {
                ComponentName who = new ComponentName(context, DeviceAdmin.class);
                if (mDPM.isAdminActive(who)) {
                    mDPM.wipeData(0);
                }
            }
        }
    }
}
