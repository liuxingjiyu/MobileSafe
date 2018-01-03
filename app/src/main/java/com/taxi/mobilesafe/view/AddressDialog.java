package com.taxi.mobilesafe.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.taxi.mobilesafe.R;

/**
 * Created by taxi01 on 2018/1/2.
 */

public class AddressDialog extends Dialog {

    private Window window;

    public AddressDialog(@NonNull Context context) {
        super(context);
    }
    @Override
     protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
         super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_address_style);
        window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }
}
