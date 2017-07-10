package com.oumiao.monitor.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.oumiao.monitor.R;
import com.oumiao.monitor.base.BaseRecycleAdapter;


/**
 * @description
 * @user zss
 * @date 2016/11/8
 * @email zss_503@163.com
 */

public class SuperRefreshLayout extends SwipeRefreshLayout {

	private static ILoadCallBack mLoadCallBack;
	private RecyclerView mRecyclerView;
	private BaseRecycleAdapter mAdapter;
	private boolean isRefresh = false;
	private float mPrevX;
	private float mTouchSlop;

	public SuperRefreshLayout(Context context) {
		this(context,null);
	}

	public SuperRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setColorSchemeResources(
				R.color.swiperefresh_color1, R.color.swiperefresh_color2,
				R.color.swiperefresh_color3, R.color.swiperefresh_color4);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public void setILoadCallBack(final ILoadCallBack callBack){
		this.mLoadCallBack = callBack;
	}

	public void autoRefresh(){
		if(mLoadCallBack != null){
			mLoadCallBack.onRefresh();
		}
	}

	public void setAdapter(RecyclerView recyclerView, BaseRecycleAdapter baseRecycleAdapter){
		this.mRecyclerView = recyclerView;
		this.mAdapter = baseRecycleAdapter;
		this.mRecyclerView.setAdapter(mAdapter);
		this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if(mLoadCallBack != null
						&& !isRefreshing()
						&& (mAdapter.getState() == BaseRecycleAdapter.STATE_MORE || mAdapter.getState() == BaseRecycleAdapter.STATE_ERROR)
						&& newState == RecyclerView.SCROLL_STATE_IDLE
						&& !ViewCompat.canScrollVertically(mRecyclerView,1)){
					mAdapter.setState(BaseRecycleAdapter.STATE_LOADING);
					mLoadCallBack.loadMore();
				}
			}
		});
	}

	/**
	 * 解决水平滑动冲突
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mPrevX = MotionEvent.obtain(event).getX();
				break;

			case MotionEvent.ACTION_MOVE:
				final float eventX = event.getX();
				float xDiff = Math.abs(eventX - mPrevX);

				if (xDiff > mTouchSlop) {
					return false;
				}
		}
		return super.onInterceptTouchEvent(event);
	}


	public void loadComplete(boolean hasMore){
		if(mAdapter == null){
			throw new RuntimeException("must call setAdapter to bind data");
		}
		mAdapter.setState(hasMore?BaseRecycleAdapter.STATE_MORE:BaseRecycleAdapter.STATE_NO_MORE);
	}

	public void loadError(){
		if(mAdapter == null){
			throw new RuntimeException("must call setAdapter to bind data");
		}
		mAdapter.setState(BaseRecycleAdapter.STATE_ERROR);
	}

	public void setRefreshEnable(boolean enable){
		if(isEnabled() && !enable){
			setEnabled(false);
		}else if(!isEnabled() && enable){
			setEnabled(true);
		}
	}

	public static interface ILoadCallBack{
		void onRefresh();
		void loadMore();
	}

	@Override
	public void setRefreshing(boolean refreshing) {
		super.setRefreshing(refreshing);
		this.isRefresh = isRefreshing();
	}

}
