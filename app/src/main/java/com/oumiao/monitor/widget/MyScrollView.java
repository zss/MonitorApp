package com.oumiao.monitor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @description 解决ScrollView中嵌套使用ViewPager
 * @user zss
 * @date 2016/5/12
 * @email zss_503@163.com
 */
public class MyScrollView extends ScrollView {

	private GestureDetector mGestureDetector;

	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new MyScrollGestureListener());
		setFadingEdgeLength(0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}

	class MyScrollGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return Math.abs(distanceY) > Math.abs(distanceX);
		}
	}


}
