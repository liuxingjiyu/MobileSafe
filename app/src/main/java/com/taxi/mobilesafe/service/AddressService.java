package com.taxi.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.taxi.mobilesafe.engine.AddressDao;
import com.taxi.mobilesafe.utils.ToastUtil;
import com.taxi.mobilesafe.view.AddressToast;

/**
 * Created by shizhengui on 2018/1/1.
 */

public class AddressService extends Service {

    private static final String TAG = "AddressService";
    private TelephonyManager tm;
    private MyPhoneStateListener listener;
    private AddressToast toast;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        toast = new AddressToast(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(listener,PhoneStateListener.LISTEN_NONE);
        listener = null;
    }

    class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e(TAG, "onCallStateChanged: "+"电话闲置" );
                    toast.hide();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e(TAG, "onCallStateChanged: "+"电话接通" );
                    ToastUtil.show(getApplicationContext(),"dianhuajietong");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e(TAG, "onCallStateChanged: "+"电话响铃" );
                    if (!Settings.canDrawOverlays(AddressService.this)){
                        ToastUtil.show(getApplicationContext(),"需要窗口权限");
                    }
                    String location = AddressDao.getAddress(getApplicationContext(),incomingNumber);
                    //ToastUtil.show(getApplicationContext(),incomingNumber);
                    toast.show(location);
                    break;
            }
        }
    }
}
