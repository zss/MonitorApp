package com.oumiao.monitor.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @description ScrollView 嵌套 RecyclerView 自适应 LinearLayout
 * @user zss
 * @email zss_503@163.com
 */
public class FullyLinearLayoutManager extends LinearLayoutManager {
	public static final String TAG = FullyLinearLayoutManager.class.getCanonicalName();
	private int[] mMeasuredDimension = new int[2];

	public FullyLinearLayoutManager(Context context) {
		super(context);
	}

	public FullyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public FullyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
		super(context, orientation, reverseLayout);
	}

	@Override
	public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
		final int widthMode = View.MeasureSpec.getMode(widthSpec);
		final int heightMode = View.MeasureSpec.getMode(heightSpec);
		final int widthSize = View.MeasureSpec.getSize(widthSpec);
		final int heightSize = View.MeasureSpec.getSize(heightSpec);

//		Log.i(TAG, "onMeasure called. \nwidthMode " + widthMode
//				+ " \nheightMode " + heightSpec
//				+ " \nwidthSize " + widthSize
//				+ " \nheightSize " + heightSize
//				+ " \ngetItemCount() " + getItemCount());
		int width = 0;
		int height = 0;
		int itemCount = getItemCount();
		for (int i = 0; i < itemCount; i++) {
			measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
					View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), mMeasuredDimension);
			if (getOrientation() == HORIZONTAL) {
				width = width + mMeasuredDimension[0];
				if (i == 0) {
					height = mMeasuredDimension[1];
				}
			} else {
				height = height + mMeasuredDimension[1];
				if (i == 0) {
					width = mMeasuredDimension[0];
				}
			}
		}

		switch (widthMode) {
			case View.MeasureSpec.EXACTLY:
				width = widthSize;
				break;
			case View.MeasureSpec.AT_MOST:
			case View.MeasureSpec.UNSPECIFIED:
		}

		switch (heightMode) {
			case View.MeasureSpec.EXACTLY:
				height = heightSize;
				break;
			case View.MeasureSpec.AT_MOST:
			case View.MeasureSpec.UNSPECIFIED:
		}

		setMeasuredDimension(width, height);

	}

	private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measureDimension) {
		if (position > getItemCount()) return;
		try {
			View view = recycler.getViewForPosition(0);
			if (view != null) {
				RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
				int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), p.width);
				int childHeightSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingTop() + getPaddingBottom(), p.height);

				view.measure(childWidthSpec, childHeightSpec);
				measureDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
				measureDimension[1] = view.getMeasuredHeight() + p.topMargin + p.bottomMargin;
				recycler.recycleView(view);
			}
		} catch (Exception e) {
		}
	}
}
