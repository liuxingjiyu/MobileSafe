package com.taxi.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.taxi.mobilesafe.utils.ConstantValue;
import com.taxi.mobilesafe.utils.SpUtil;

/**
 * Created by taxi01 on 2017/12/27.
 */

public class LocationService extends Service {


    private LocationManager lm;
    private MyLocationListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MobileLocation();
    }

    public void MobileLocation() {
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);
        listener = new MyLocationListener();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(bestProvider, 0, 0, listener);
        }else {
            return;
        }
    }

    class MyLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            String safe_number = SpUtil.getString(LocationService.this, ConstantValue.CONTACT_PHONE,"");
            if (!TextUtils.isEmpty(safe_number)) {
                SmsManager.getDefault().sendTextMessage(safe_number, null, "longitude:" + longitude + "; latitude:" + latitude, null, null);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        lm.removeUpdates(listener);
        super.onDestroy();
    }
}
