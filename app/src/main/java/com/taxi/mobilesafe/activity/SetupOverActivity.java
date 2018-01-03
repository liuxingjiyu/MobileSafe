package com.taxi.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.utils.ConstantValue;
import com.taxi.mobilesafe.utils.SpUtil;

/**
 * Created by taxi01 on 2017/12/21.
 */

public class SetupOverActivity extends AppCompatActivity{
    private static final String TAG = "SetupOverActivity";
    private TextView enter_setup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (setup_over) {
            setContentView(R.layout.activity_setup_over);
        }else {
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
            return;
        }

        TextView safe_number = (TextView) findViewById(R.id.tv_safe_number);
        enter_setup = (TextView)findViewById(R.id.tv_enter_setup);
        String safeNumber = SpUtil.getString(this,"contact_phone","");
        if (!TextUtils.isEmpty(safeNumber)) {
            safe_number.setText(safeNumber);
        }
        enter_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupOverActivity.this,Setup1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: " + event.getX());
        return super.onTouchEvent(event);
    }*/

}
