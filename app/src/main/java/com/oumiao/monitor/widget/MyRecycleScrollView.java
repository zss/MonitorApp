package com.oumiao.monitor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * @description 配合recycleview使用
 * @user zss
 * @date 2016/6/3
 * @email zss_503@163.com
 */
public class MyRecycleScrollView extends ScrollView {
	private int downX;
	private int downY;
	private int mTouchSlop;
	private GestureDetector mGestureDetector;

	public MyRecycleScrollView(Context context) {
		this(context, null);
	}

	public MyRecycleScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyRecycleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mGestureDetector = new GestureDetector(context, new MyScrollGestureListener());
		setFadingEdgeLength(0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int action = e.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				downX = (int) e.getRawX();
				downY = (int) e.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				int moveY = (int) e.getRawY();
				int moveX = (int) e.getRawX();
				if (Math.abs(moveY - downY) > mTouchSlop) {
					return true;
				}
		}
		return super.onInterceptTouchEvent(e) && mGestureDetector.onTouchEvent(e);
	}

	class MyScrollGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return Math.abs(distanceY) > Math.abs(distanceX);
		}
	}
}
