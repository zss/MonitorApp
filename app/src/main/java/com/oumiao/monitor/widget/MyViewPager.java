package com.oumiao.monitor.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.oumiao.monitor.utils.TLog;


/**
 * @description
 * @user zss
 * @date 2016/5/13
 * @email zss_503@163.com
 */
public class MyViewPager extends ViewPager {
	private float preX = 0;

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = 0;
		for (int i = 0, len = getChildCount(); i < len; i++) {
			View child = getChildAt(i);
			child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			int childHeight = child.getMeasuredHeight();
			if (childHeight > height) {
				height = childHeight;
			}
		}
		TLog.log("viewPageHeight:" + height);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		boolean res = super.onInterceptTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			preX = event.getX();
		} else {
			if (Math.abs(event.getX() - preX) > 4) {
				return true;
			} else {
				preX = event.getX();
			}
		}
		return res;
	}
}
