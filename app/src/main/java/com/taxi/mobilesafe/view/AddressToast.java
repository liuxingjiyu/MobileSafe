package com.taxi.mobilesafe.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.taxi.mobilesafe.R;

/**
 * Created by shizhengui on 2018/1/1.
 */

public class AddressToast {
    private final Context mContext;
    private final WindowManager mWM;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private View view;
    private TextView mLocation;
    private int startX;
    private int startY;

    public AddressToast(Context context){
        this.mContext = context;
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        /*| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE   默认可以触摸*/
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
    }

    public void show(String location){
        view = View.inflate(mContext, R.layout.toast_address, null);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) (event.getRawX() + 0.5f);
                        startY = (int) (event.getRawY() + 0.5f);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int newX = (int)(event.getRawX() + 0.5f);
                        int newY = (int)(event.getRawY() + 0.5f);

                        int diffX = newX - startX;
                        int diffY = newY - startY;

                        mParams.x += diffX;
                        mParams.y += diffY;
                        mWM.updateViewLayout(view,mParams);

                        startX = (int) (event.getRawX() + 0.5f);
                        startY = (int) (event.getRawY() + 0.5f);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
        mLocation = view.findViewById(R.id.tv_location_toast);
        mLocation.setText(location);
        mWM.addView(view,mParams);
    }

    public void hide(){
        if (view != null){
            if (view.getParent() != null){
                mWM.removeView(view);
            }
        }
    }
}
