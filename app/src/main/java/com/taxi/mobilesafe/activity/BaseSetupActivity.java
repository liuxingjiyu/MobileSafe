package com.taxi.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.taxi.mobilesafe.PermissionsResultListener;
import com.taxi.mobilesafe.R;

/**
 * Created by shizhengui on 2017/12/26.
 */

public abstract class BaseSetupActivity extends AppCompatActivity{

    private static final int REQUESTCODE = 100;
    private GestureDetector mDetector;
    private PermissionsResultListener mListener;
    private int mRequestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getRawX() - e2.getRawX() > 100){
                    doNext();
                    return true;
                }
                if (e2.getRawX() - e1.getRawX() > 100){
                    doPre();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    private void doNext() {
        if (performNext()){
            return;
        }
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        finish();
    }

    private void doPre() {
        if(performPre()){
            return;
        }
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
        finish();
    }

    public abstract boolean performNext();
    public abstract boolean performPre();

    protected void nextPage(View view){
        if (performNext()){
            return;
        }
        finish();
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }

    protected void prePage(View view){
        if (performPre()){
            return;
        }
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    protected void performsRequestPermissions(String desc, String[] permissions, int requestCode, PermissionsResultListener listener) {
        if (permissions == null || permissions.length == 0) {
            return;
        }
        mRequestCode = requestCode;
        mListener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkEachSelfPermission(permissions)) {// 检查是否声明了权限
                requestEachPermissions(desc, permissions, requestCode);
            } else {// 已经申请权限
                if (mListener != null) {
                    mListener.onPermissionGranted();
                }
            }
        } else {
            if (mListener != null) {
                mListener.onPermissionGranted();
            }
        }
    }

    /**
     * 申请权限前判断是否需要声明
     *
     * @param desc
     * @param permissions
     * @param requestCode
     */
    private void requestEachPermissions(String desc, String[] permissions, int requestCode) {
        if (shouldShowRequestPermissionRationale(permissions)) {// 需要再次声明
            showRationaleDialog(desc, permissions, requestCode);
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    /**
     * 弹出声明的 Dialog
     *
     * @param desc
     * @param permissions
     * @param requestCode
     */
    private void showRationaleDialog(String desc, final String[] permissions, final int requestCode) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限用途")
                .setMessage(desc)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(BaseSetupActivity.this, permissions, requestCode);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * 再次申请权限时，是否需要声明
     *
     * @param permissions
     * @return
     */
    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检察每个权限是否申请
     *
     * @param permissions
     * @return true 需要申请权限,false 已申请权限
     */
    private boolean checkEachSelfPermission(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 申请权限结果的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mRequestCode) {
            if (checkEachPermissionsGranted(grantResults)) {
                if (mListener != null) {
                    mListener.onPermissionGranted();
                }
            } else {// 用户拒绝申请权限
                if (mListener != null) {
                    mListener.onPermissionDenied();
                }
            }
        }
    }

    /**
     * 检查回调结果
     *
     * @param grantResults
     * @return
     */
    private boolean checkEachPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
