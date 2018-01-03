package com.taxi.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.utils.ConstantValue;
import com.taxi.mobilesafe.utils.MD5Util;
import com.taxi.mobilesafe.utils.SpUtil;
import com.taxi.mobilesafe.utils.ToastUtil;


public class HomeActivity extends AppCompatActivity{

    private static final String TAG = "HomeActivity";
    private GridView gv_home;
    private String[] mTitleStrs;
    private int[] mDrawableIds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();

        initData();
    }

    private void initData() {
        //准备数据(文字(9组),图片(9张))
        mTitleStrs = new String[]{
                "手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"
        };

        mDrawableIds = new int[]{
                R.drawable.home_safe,R.drawable.home_callmsgsafe,
                R.drawable.home_apps,R.drawable.home_taskmanager,
                R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
        };

        gv_home.setAdapter(new MyAdapter());

        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this,R.anim.main_item_anim));
        lac.setOrder(LayoutAnimationController.ORDER_RANDOM);
        gv_home.setLayoutAnimation(lac);
        gv_home.startLayoutAnimation();

        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "点击位置为: " + position);
                switch (position){
                    case 0:
                        showDialog();
                        break;
                    case 7:
                        openActivity(AToolActivity.class);
                        break;
                    case 8:
                        openActivity(SettingActivity.class);
                        break;
                }
            }
        });
    }

    private void openActivity(Class clazz){
        Intent intent = new Intent(HomeActivity.this,clazz);
        startActivity(intent);
    }

    /**
     * 显示密码对话框
     */
    private void showDialog() {
        String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(psd)){
            showSetPsdDialog();
        }else {
            showConfirmPsdDialog();
        }
    }

    /**
     * 确认密码对话框
     */
    private void showConfirmPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        final View view = View.inflate(this,R.layout.dialog_confirm_psd,null);
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button bt_submit =  view.findViewById(R.id.bt_submit);
        Button bt_cancel =  view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confirm_psd = view.findViewById(R.id.et_confirm_psd);

                String confirmPsd = et_confirm_psd.getText().toString();

                if (!TextUtils.isEmpty(confirmPsd)){
                    String psd = SpUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, "");
                    confirmPsd = MD5Util.encoder(confirmPsd);
                    if (psd.equals(confirmPsd)){
                        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }else {
                        ToastUtil.show(getApplicationContext(),"确认密码错误");
                    }
                }else {
                    ToastUtil.show(getApplicationContext(),"请输入密码");
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 设置密码对话框
     */
    private void showSetPsdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        final View view = View.inflate(this,R.layout.dialog_set_psd,null);
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button bt_submit =  view.findViewById(R.id.bt_submit);
        Button bt_cancel =  view.findViewById(R.id.bt_cancel);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_psd = view.findViewById(R.id.et_set_psd);
                EditText et_confirm_psd = view.findViewById(R.id.et_confirm_psd);

                String psd = et_set_psd.getText().toString();
                String confirmPsd = et_confirm_psd.getText().toString();

                if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)){
                    if (psd.equals(confirmPsd)){
                        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                        SpUtil.putString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD, MD5Util.encoder(confirmPsd));
                    }else {
                        ToastUtil.show(getApplicationContext(),"确认密码错误");
                    }
                }else {
                    ToastUtil.show(getApplicationContext(),"请输入密码");
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mTitleStrs.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStrs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            ImageView imageView = view.findViewById(R.id.im_icon);
            TextView textView = view.findViewById(R.id.tv_title);

            imageView.setBackgroundResource(mDrawableIds[position]);
            textView.setText(mTitleStrs[position]);
            return view;
        }

    }
}
