package com.taxi.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.taxi.mobilesafe.R;

/**
 * Created by taxi01 on 2017/12/21.
 */

public class Setup1Activity extends BaseSetupActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    public boolean performNext() {
        Intent intent = new Intent(this,Setup2Activity.class);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean performPre() {
        return true;
    }
}
