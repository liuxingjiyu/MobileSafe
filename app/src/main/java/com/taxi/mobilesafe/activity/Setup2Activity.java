package com.taxi.mobilesafe.activity;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.taxi.mobilesafe.PermissionsResultListener;
import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.utils.ConstantValue;
import com.taxi.mobilesafe.utils.SpUtil;
import com.taxi.mobilesafe.utils.ToastUtil;
import com.taxi.mobilesafe.view.SettingItemView;


public class Setup2Activity extends BaseSetupActivity {
	private static final String TAG = "Setup2Activity" ;
	private Button bt_sim_bound;
	private ImageView ivState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
        initUI();
	}
	
	private void initUI() {
		bt_sim_bound = (Button) findViewById(R.id.bt_sim_bound);
		ivState = (ImageView) findViewById(R.id.iv_unlock);
		//1,回显(读取已有的绑定状态,用作显示,sp中是否存储了sim卡的序列号)
		String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
		//2,判断是否序列卡号为""
		if (!TextUtils.isEmpty(sim_number)){
			ivState.setImageResource(R.drawable.lock);
		}

		bt_sim_bound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String result = SpUtil.getString(Setup2Activity.this, ConstantValue.SIM_NUMBER, "");
				if(TextUtils.isEmpty(result)){
					//6,存储(序列卡号)
					getSIMNumber();
				}else{
					//7,将存储序列卡号的节点,从sp中删除掉
					SpUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
					ivState.setImageResource(R.drawable.unlock);
				}
			}
		});
	}

	public void getSIMNumber(){
		performsRequestPermissions("监测手机卡变更信息", new String[]{Manifest.permission.READ_PHONE_STATE},
				100, new PermissionsResultListener() {
					@Override
					public void onPermissionGranted() {
						//6.1获取sim卡序列号TelephoneManager
						TelephonyManager manager = (TelephonyManager)
								getSystemService(Context.TELEPHONY_SERVICE);
						//6.2获取sim卡的序列卡号
						String simSerialNumber = manager.getSimSerialNumber();
						//6.3存储
						SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);
						ivState.setImageResource(R.drawable.lock);
					}

					@Override
					public void onPermissionDenied() {
						ToastUtil.show(Setup2Activity.this,"需要相关权限");
						ivState.setImageResource(R.drawable.unlock);
					}
				});
	}
	/*@Override
	public void prePage(View view){
		overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
	}*/

	@Override
	public boolean performNext() {
		String serialNumber = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
		if(!TextUtils.isEmpty(serialNumber)){
			Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
			startActivity(intent);
			return false;
		}else{
			ToastUtil.show(this,"请绑定sim卡");
			return true;
		}

	}

	@Override
	public boolean performPre() {
		Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
		startActivity(intent);
		return false;
	}

}
