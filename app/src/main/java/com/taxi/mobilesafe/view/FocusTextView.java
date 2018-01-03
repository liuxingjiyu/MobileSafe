package com.taxi.mobilesafe.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;



/**
 * Created by taxi01 on 2017/12/20.
 */

public class FocusTextView extends AppCompatTextView {
    public FocusTextView(Context context) {
        super(context);
    }

    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }
}
