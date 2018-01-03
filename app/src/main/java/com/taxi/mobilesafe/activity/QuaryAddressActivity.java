package com.taxi.mobilesafe.activity;

import android.os.Parcel;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.engine.AddressDao;
import com.taxi.mobilesafe.utils.ToastUtil;

public class QuaryAddressActivity extends AppCompatActivity {

    private EditText et_phone;
    private TextView tv_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quary_address);

        initUi();
    }

    private void initUi() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_location = (TextView) findViewById(R.id.tv_location);

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String address = AddressDao.getAddress(QuaryAddressActivity.this,s.toString());
                tv_location.setText("归属地：" + address);
            }
        });
    }

    public void query(View view){
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            et_phone.startAnimation(shake);
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
            ToastUtil.show(this,"号码不能为空");
        }else {
            String address = AddressDao.getAddress(QuaryAddressActivity.this,phone);
            tv_location.setText("归属地：" + address);
        }
    }
}
