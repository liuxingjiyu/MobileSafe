package com.taxi.mobilesafe.activity;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.taxi.mobilesafe.PermissionsResultListener;
import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.receiver.DeviceAdmin;
import com.taxi.mobilesafe.utils.ToastUtil;


public class Setup4Activity extends BaseSetupActivity {

	private static final String TAG = "Setup4Activity";
	private static final int REQUEST_CODE_ENABLE_ADMIN = 1;
	private ComponentName mDeviceAdminSample;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		performsRequestPermissions("需要定位手机权限", new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
				103, new PermissionsResultListener() {
					@Override
					public void onPermissionGranted() {

					}

					@Override
					public void onPermissionDenied() {
						ToastUtil.show(Setup4Activity.this,"没有位置权限");
					}
				});
		final ImageView ivLock = (ImageView) findViewById(R.id.iv_lock);
		mDeviceAdminSample = new ComponentName(this, DeviceAdmin.class);
		final DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		findViewById(R.id.bt_action_admin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mDPM.isAdminActive(mDeviceAdminSample)){
					mDPM.removeActiveAdmin(mDeviceAdminSample);
					mDeviceAdminSample = null;
					ivLock.setImageResource(R.drawable.unlock);
				}else {
					Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
					intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
					intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "设备管理者");
					startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
					ivLock.setImageResource(R.drawable.lock);
				}
			}
		});

	}

	@Override
	public boolean performNext() {
		Intent intent = new Intent(getApplicationContext(), Setup5Activity.class);
		startActivity(intent);
		return false;
	}

	@Override
	public boolean performPre() {
		Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
		startActivity(intent);
		return false;
	}

}
