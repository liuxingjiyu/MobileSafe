package com.taxi.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.utils.ConstantValue;
import com.taxi.mobilesafe.utils.SpUtil;
import com.taxi.mobilesafe.utils.ToastUtil;

public class Setup5Activity extends BaseSetupActivity {
	private CheckBox cb_box;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup5);
		
		initUI();
	}
	
	private void initUI() {
		cb_box = (CheckBox) findViewById(R.id.cb_box);
		//1,是否选中状态的回显
		boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
		//2,根据状态,修改checkbox后续的文字显示
		cb_box.setChecked(open_security);
		if(open_security){
			cb_box.setText("安全设置已开启");
		}else{
			cb_box.setText("安全设置已关闭");
		}
		
//		cb_box.setChecked(!cb_box.isChecked());
		//3,点击过程中,监听选中状态发生改变过程,
		cb_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//4,isChecked点击后的状态,存储点击后状态
				SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
				//5,根据开启关闭状态,去修改显示的文字
				if(isChecked){
					cb_box.setText("安全设置已开启");
				}else{
					cb_box.setText("安全设置已关闭");
				}
			}
		});

	}

	@Override
	public boolean performNext() {
		boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
		if(open_security){
			Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
			startActivity(intent);

			SpUtil.putBoolean(this, ConstantValue.SETUP_OVER, true);
			return false;
		}else{
			ToastUtil.show(getApplicationContext(), "请开启防盗保护");
			return true;
		}

	}

	@Override
	public boolean performPre() {
		Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
		startActivity(intent);
		return false;
	}
}
