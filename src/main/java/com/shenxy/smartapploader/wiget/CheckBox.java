package com.shenxy.smartapploader.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.shenxy.smartapploader.R;

/**
 * 选择按钮
 * Created by szh on 2014-02-11.
 */
public class CheckBox extends ImageButton {
    private boolean _isChecked;

    public CheckBox(Context context) {
        super(context);
        init(context);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
//        setBackgroundColor(Color.WHITE);

        setImageResource(R.drawable.tw_checkbox);

        setChecked(false);
    }

    public boolean isChecked() {
        return _isChecked;
    }

    public void setChecked(boolean isChecked) {
        if (_isChecked != isChecked) {
            _isChecked = isChecked;

            setBackground(_isChecked);
        }
    }

    private void setBackground(boolean isChecked) {
        if (isChecked) {
            setImageResource(R.drawable.tw_checkboxed);
        } else {
            setImageResource(R.drawable.tw_checkbox);
        }
    }
    public void setGirdChecked(boolean isChecked) {
        if (_isChecked != isChecked) {
            _isChecked = isChecked;
         }
        setCheckedBackground(_isChecked);
    }
    private void setCheckedBackground(boolean isChecked) {
        if (isChecked) {
            setImageResource(R.drawable.msg_checked);
        } else {
            setImageResource(R.drawable.msg_unchecked);
        }
    }
}
