package com.oumiao.monitor.interf;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.oumiao.monitor.utils.TLog;


/**
 * @description
 * @user zss
 * @date 2016/5/24
 * @email zss_503@163.com
 */
public abstract class OnRecyclerItemClicklistener implements RecyclerView.OnItemTouchListener {

	private GestureDetectorCompat mGestureDetector;
	private RecyclerView mRecyclerView;

	public OnRecyclerItemClicklistener(RecyclerView recyclerView) {
		this.mRecyclerView = recyclerView;
		this.mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());

	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
		TLog.log("recycler onInterceptTouchEvent");
		mGestureDetector.onTouchEvent(e);
		return false;
	}

	@Override
	public void onTouchEvent(RecyclerView rv, MotionEvent e) {
		TLog.log("recycler onTouchEvent");
		mGestureDetector.onTouchEvent(e);
	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
	}

	public abstract void onItemClick(RecyclerView.ViewHolder vh);

	public abstract void onLongClick(RecyclerView.ViewHolder vh);


	private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
			if (child != null) {
				RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(child);
				onItemClick(vh);
			}
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
			if (child != null) {
				RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(child);
				onLongClick(vh);
			}
		}
	}
}
