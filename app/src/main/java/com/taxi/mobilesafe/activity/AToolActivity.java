package com.taxi.mobilesafe.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.utils.ActivityUtil;

public class AToolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);
        findViewById(R.id.siv_address_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startActivity(AToolActivity.this,QuaryAddressActivity.class);
            }
        });
    }

}
