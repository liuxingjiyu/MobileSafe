package com.taxi.mobilesafe.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.taxi.mobilesafe.PermissionsResultListener;
import com.taxi.mobilesafe.R;
import com.taxi.mobilesafe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by taxi01 on 2017/12/22.
 */

public class ContactListActivity extends BaseSetupActivity{

    private ListView lv_contact;
    private List<HashMap<String ,String>> contactList = new ArrayList<>();
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            lv_contact.setAdapter(new MyAdapter());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initUI();
        initData();
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null){
                view = View.inflate(getApplicationContext(), R.layout.listview_contact_item, null);
            }else {
                view = convertView;
            }

            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_phone = view.findViewById(R.id.tv_phone);
            tv_name.setText(getItem(position).get("name"));
            tv_phone.setText(getItem(position).get("number"));
            return view;
        }
    }

    private void initData() {
        performsRequestPermissions("读取联系人信息,发送短信", new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS},
                101, new PermissionsResultListener() {
                    @Override
                    public void onPermissionGranted() {
                        new Thread() {
                            @Override
                            public void run() {
                                ContentResolver resolver = getContentResolver();
                                Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,null,null,null);
                                contactList.clear();
                                while (cursor.moveToNext()){
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    String name = cursor.getString(cursor.
                                            getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                    String number = cursor.getString(cursor.
                                            getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    //Log.e(TAG, "run: "+name + ":" + number );
                                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)){
                                        hashMap.put("name",name);
                                        hashMap.put("number",number);
                                    }
                                    if (hashMap.size() > 0){
                                        contactList.add(hashMap);
                                    }
                                }
                                cursor.close();
                                mHandle.sendEmptyMessage(0);
                            }
                        }.start();
                    }

                    @Override
                    public void onPermissionDenied() {
                        ToastUtil.show(ContactListActivity.this,"需要设置安全号码权限");
                    }
                });

    }

    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> hashMap = contactList.get(position);
                String phone = hashMap.get("number");
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                setResult(0,intent);
                finish();
            }
        });
    }

    @Override
    public boolean performNext() {
        return false;
    }

    @Override
    public boolean performPre() {
        return false;
    }
}
