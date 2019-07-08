package com.frxs.order.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by ewu on 2016/9/8.
 */
public class MyCheckBox extends CheckBox {

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    public MyCheckBox(Context context) {
        super(context);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
        this.mOnCheckedChangeListener = listener;
    }

    public void setCheckedNoEvent(final boolean checked) {
        if (mOnCheckedChangeListener == null) {
            setChecked(checked);
        } else {
            super.setOnCheckedChangeListener(null);
            setChecked(checked);
            setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
    }
}
