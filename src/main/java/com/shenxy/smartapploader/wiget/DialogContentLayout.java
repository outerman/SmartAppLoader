package com.shenxy.smartapploader.wiget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.shenxy.smartapploader.R;

//import com.chanjet.sharedRes.R;

public class DialogContentLayout extends LinearLayout {

	private int mMaxWidth;
	private int mMaxHeight;

	public DialogContentLayout(Context context) {
		super(context);
		init(context);
	}

	public DialogContentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DialogContentLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		Resources res = getResources();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		final int p_h = res.getDimensionPixelSize(R.dimen.dialog_paddingHorizontal);
		final int window_w = wm.getDefaultDisplay().getWidth();

		if (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			mMaxHeight = res
					.getDimensionPixelSize(R.dimen.dialog_maxHeight_portrait);
		} else {
			mMaxHeight = res
					.getDimensionPixelSize(R.dimen.dialog_maxHeight_landscape);
		}
		mMaxWidth = window_w - 2 * p_h >= 0 ? window_w - 2 * p_h : 0;

		final int minWidth = res.getDimensionPixelSize(R.dimen.dialog_minWidth);
		final int minHeight = res.getDimensionPixelSize(R.dimen.dialog_minHeight);
		setMinimumWidth(minWidth);
		setMinimumHeight(minHeight);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
			int height = MeasureSpec.getSize(heightMeasureSpec);
			int width = MeasureSpec.getSize(widthMeasureSpec);
			if (height > mMaxHeight) {
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight,
						MeasureSpec.AT_MOST);
			}
			if (width > mMaxWidth) {
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth,
						MeasureSpec.AT_MOST);
			}
		} else if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
			measureChildren(getMeasuredLength(widthMeasureSpec, true),
					getMeasuredLength(heightMeasureSpec, false));
			setMeasuredDimension(getMeasuredLength(widthMeasureSpec, true),
					getMeasuredLength(heightMeasureSpec, false));
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private int getMeasuredLength(int length, boolean isWidth) {
		int specSize = MeasureSpec.getSize(length);
		int size;
		if (isWidth) {
			size = specSize > mMaxWidth ? mMaxWidth : specSize;
		} else {
			size = specSize > mMaxHeight ? mMaxHeight : specSize;
		}
		return size;
	}
}
