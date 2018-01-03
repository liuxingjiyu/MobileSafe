package com.taxi.mobilesafe.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.service.AddressService;
import com.taxi.mobilesafe.service.LocationService;
import com.taxi.mobilesafe.utils.ActivityUtil;
import com.taxi.mobilesafe.utils.ConstantValue;
import com.taxi.mobilesafe.utils.ServiceStateUtil;
import com.taxi.mobilesafe.utils.SpUtil;
import com.taxi.mobilesafe.utils.ToastUtil;
import com.taxi.mobilesafe.view.AddressDialog;
import com.taxi.mobilesafe.view.SettingItemView;

/**
 * Created by taxi01 on 2017/12/20.
 */

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";
    private static final int OVERLAY_PERMISSION_REQ_CODE = 100;
    private SettingItemView siv_attribution;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initAttribution();

        initUpdate();
    }

    private void initAttribution() {
        siv_attribution = (SettingItemView) findViewById(R.id.siv_attribution);
        SettingItemView siv_attri_style = (SettingItemView) findViewById(R.id.siv_attri_style);

        siv_attribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressIntent = new Intent(getApplicationContext(),AddressService.class);
                if (ServiceStateUtil.serviceRunning(getApplicationContext(),AddressService.class)){
                    siv_attribution.setToggle(false);
                    stopService(addressIntent);
                }else {
                    if (ActivityUtil.hasPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE)){
                        if (!Settings.canDrawOverlays(SettingActivity.this)){
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + SettingActivity.this.getPackageName()));
                            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                        }else {
                            siv_attribution.setToggle(true);
                            startService(addressIntent);
                        }
                    }else {
                        ActivityCompat.requestPermissions(SettingActivity.this,new String[]{Manifest.permission.READ_PHONE_STATE},101);
                    }
                }
            }
        });

        siv_attri_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AddressDialog dialog = new AddressDialog(SettingActivity.this);
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                ToastUtil.show(this,"Permission Denieddd by user.Please Check it in Settings");
            } else {
                Intent addressIntent = new Intent(getApplicationContext(),AddressService.class);
                siv_attribution.setToggle(true);
                startService(addressIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initUpdate() {

        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_auto_update);
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        siv_update.setToggle(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (siv_update.isToggle()){
                   siv_update.setToggle(false);
               }else {
                   siv_update.setToggle(true);
               }
               SpUtil.putBoolean(SettingActivity.this,ConstantValue.OPEN_UPDATE,siv_update.isToggle());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ServiceStateUtil.serviceRunning(this,AddressService.class)){
            siv_attribution.setToggle(true);
        }else {
            siv_attribution.setToggle(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent addressIntent = new Intent(getApplicationContext(),AddressService.class);
                siv_attribution.setToggle(true);
                startService(addressIntent);
            }else {
                ToastUtil.show(getApplicationContext(),"需要电话权限");
            }
        }
    }

    private String[] titles = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
    private int[] icons = new int[] { R.drawable.call_locate_white,
            R.drawable.call_locate_orange, R.drawable.call_locate_blue,
            R.drawable.call_locate_gray, R.drawable.call_locate_green };

    private class AddressAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return titles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View.inflate(SettingActivity.this,R.layout.item_setting_address_dialog,null);
            return null;
        }
    }
}
