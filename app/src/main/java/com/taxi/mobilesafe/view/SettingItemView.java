package com.taxi.mobilesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taxi.mobilesafe.R;



public class SettingItemView extends RelativeLayout {

    private boolean isToggle = false;
    private ImageView mIvToggle;
    private View mView;

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = View.inflate(context, R.layout.setting_item_view, this);

        initView(context,attrs);

    }

    private void initView(Context context,AttributeSet attrs) {
        TextView tv_title = findViewById(R.id.tv_title);
        mIvToggle = findViewById(R.id.iv_toggle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        String title = array.getString(R.styleable.SettingItemView_title);
        boolean seting_isToggle = array.getBoolean(R.styleable.SettingItemView_isToggle,false);
        int itbackground = array.getInt(R.styleable.SettingItemView_itbackground,1);
        array.recycle();
        setToggle(isToggle);
        tv_title.setText(title);
        mIvToggle.setVisibility(seting_isToggle ? View.INVISIBLE : View.VISIBLE);
        switch (itbackground){
            case 0:
                mView.setBackgroundResource(R.drawable.first_selected);
                break;
            case 1:
                mView.setBackgroundResource(R.drawable.middle_selected);
                break;
            case 2:
                mView.setBackgroundResource(R.drawable.last_selected);
                break;
            default:
        }
    }

    public boolean isToggle(){
        return isToggle;
    }

    public void setToggle(boolean state) {
        this.isToggle = state;
        if (isToggle){
            mIvToggle.setImageResource(R.drawable.switch_on);
        }else {
            mIvToggle.setImageResource(R.drawable.switch_off);
        }
    }

}
