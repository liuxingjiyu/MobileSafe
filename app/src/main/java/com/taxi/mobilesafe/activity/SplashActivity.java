package com.taxi.mobilesafe.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.utils.ConstantValue;
import com.taxi.mobilesafe.utils.SpUtil;
import com.taxi.mobilesafe.utils.Streamutil;
import com.taxi.mobilesafe.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private static final int UPDATE_VERSION = 100;
    private static final int ENTER_HOME = 101;
    private static final int IO_ERROR = 102;
    private static final int URL_ERROR = 103;
    private static final int JSON_ERROR = 104;
    private static final String TAG = "SplashActivity";
    private static final String writePermission[] ={Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private TextView tv_version_name;
    private int mVersionCode;
    private String mDescription;
    private String mDownloadURL;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(SplashActivity.this,"网络异常");
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(SplashActivity.this,"url异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(SplashActivity.this,"JSON解析异常");
                    enterHome();
                    break;
                default:
            }
        }
    };
    private RelativeLayout rl_root;
    private ProgressBar pb_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initUI();

        initData();

        initAnimation();

        initDB();
    }

    private void initDB() {
        initAdressDB("address.db");
    }

    private void initAdressDB(final String dbName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File files = getFilesDir();
                File file = new File(files, dbName);
                if (file.exists()&&file.length()>0){
                    return;
                }
                InputStream stream = null;
                FileOutputStream fos = null;
                try {
                    stream = getAssets().open(dbName);
                    fos = new FileOutputStream(file);
                    byte[] bs = new byte[1024];
                    int temp = -1;
                    while((temp = stream.read(bs)) != -1){
                        fos.write(bs,0,temp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (stream != null && fos != null){
                        try {
                            stream.close();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }

    /**
     * 动画
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(2000);
        rl_root.setAnimation(alphaAnimation);
    }

    /**
     * 弹出对话框提示用户更新
     */
    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK)
                .setIcon(R.drawable.warn)
                .setTitle("版本更新")
                .setMessage(mDescription)
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!hasWritePermission()){
                            ActivityCompat.requestPermissions(SplashActivity.this,writePermission,0);
                        }else {
                            downloadAPK();
                        }
                    }
                })
                .setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterHome();
                    }
                });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.alpha = 0.9f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    /**
     * 下载APK
     */
    private void downloadAPK() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String s = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MobileSafe.apk";
            RequestParams params = new RequestParams();
            params.setUri(mDownloadURL);
            params.setSaveFilePath(s);
            params.setAutoRename(true);
            x.http().post(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onWaiting() {
                    Log.e(TAG, "onWaiting: " + "下载等待" );
                }

                @Override
                public void onStarted() {
                    Log.e(TAG, "onWaiting: " + "下载开始" );
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    Log.e(TAG, "onWaiting: " + "正在下载" );
                }

                @Override
                public void onSuccess(File result) {
                    Log.e(TAG, "onWaiting: " + "下载成功" );
                    //apk下载完成后，调用系统的安装方法
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                    SplashActivity.this.startActivityForResult(intent,0);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e(TAG, "onWaiting: " + "下载错误" );
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.e(TAG, "onWaiting: " + "下载取消" );
                }

                @Override
                public void onFinished() {
                    Log.e(TAG, "onWaiting: " + "下载完成" );
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * @return 检查是否有权限
     */
    private boolean hasWritePermission() {
        // TODO Auto-generated method stub

        PackageManager pm = this.getPackageManager();
        for (String auth : writePermission) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //关闭导航界面
        finish();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_version_name.setText("版本名称:" + getVersionName());

        mVersionCode = getVersionCode();
        if (SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE,false)) {
            checkVersion();
        }else {
            pb_check.setVisibility(View.INVISIBLE);
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,2000);
        }
    }

    /**
     * 检查版本号
     */
    private void checkVersion() {
        new Thread(){
            @Override
            public void run() {
                Message message = Message.obtain();
                long start = System.currentTimeMillis();
                try {
                    URL url = new URL("http://192.168.1.114:8080/update.json");
                    try {
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(2000);
                        connection.setReadTimeout(2000);
                        connection.setRequestMethod("GET");
                        if (connection.getResponseCode() == 200){
                            InputStream is = connection.getInputStream();
                            String json = Streamutil.stream2Sring(is);

                            JSONObject object = new JSONObject(json);
                            mDescription = object.getString("description");
                            mDownloadURL = object.getString("download_url");
                            String version_code = object.getString("version_code");
                            String version_name = object.getString("version_name");

                            if (mVersionCode < Integer.parseInt(version_code)) {
                                message.what = UPDATE_VERSION;
                            }else {
                                message.what = ENTER_HOME;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        message.what =IO_ERROR;
                    }catch (JSONException e){
                        e.printStackTrace();
                        message.what = JSON_ERROR;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = URL_ERROR;
                }finally {
                    long end = System.currentTimeMillis();
                    if (end - start < 2000){
                        try {
                            Thread.sleep(2000-(end - start));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 获取版本号
     */
    private int getVersionCode() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(),0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称:清单文件中:getPackageManage
     */
    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(),0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        pb_check = (ProgressBar) findViewById(R.id.pb_check);
    }

    /**
     * 权限请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            downloadAPK();
        }else {
            ToastUtil.show(this,"需要存储权限");
        }
    }
}
