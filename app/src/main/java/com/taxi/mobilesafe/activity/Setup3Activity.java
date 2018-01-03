package com.taxi.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.utils.ConstantValue;
import com.taxi.mobilesafe.utils.SpUtil;
import com.taxi.mobilesafe.utils.ToastUtil;

public class Setup3Activity extends BaseSetupActivity {
	private EditText et_phone_number;
	private Button bt_select_number;
	private static final String TAG = "Setup3Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		initUI();
	}
	private void initUI() {
		//显示电话号码的输入框
		et_phone_number = (EditText) findViewById(R.id.et_phone_number);
		//获取联系人电话号码回显过程
		String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
		et_phone_number.setText(phone);
		
		//点击选择联系人的对话框
		bt_select_number = (Button) findViewById(R.id.bt_select_number);
		bt_select_number.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			//1,返回到当前界面的时候,接受结果的方法
			String phone = data.getStringExtra("phone");
			//2,将特殊字符过滤(中划线转换成空字符串)
			phone = phone.replace("-", "").replace(" ", "").trim();
			et_phone_number.setText(phone);
			
			//3,存储联系人至sp中
			SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean performNext() {

		//点击按钮以后,需要获取输入框中的联系人,再做下一页操作
		String phone = et_phone_number.getText().toString();

		//在sp存储了相关联系人以后才可以跳转到下一个界面
		//String contact_phone = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
		if(!TextUtils.isEmpty(phone)){
			Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
			startActivity(intent);

			//如果现在是输入电话号码,则需要去保存
			SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
			return false;
		}else{
			ToastUtil.show(this,"请输入电话号码");
			return true;
		}

	}

	@Override
	public boolean performPre() {
		Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
		startActivity(intent);
		return false;
	}
}
