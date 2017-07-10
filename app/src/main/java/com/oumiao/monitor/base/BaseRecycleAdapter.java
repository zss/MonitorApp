package com.oumiao.monitor.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oumiao.monitor.R;
import com.oumiao.monitor.widget.SuperRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @description
 * @user zss
 * @date 2016/7/7
 * @email zss_503@163.com
 */
public abstract class BaseRecycleAdapter<T extends Object> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	public static final int STATE_MORE = 0,STATE_LOADING = 1,STATE_NO_MORE = 2,STATE_ERROR = 3,STATE_NO_DATA=4;
	protected List<T> mDataList = new ArrayList<>();
	protected int mLayoutId;
	protected Context mContext;
	public static final int ITEM_TYPE_FOOTER = Integer.MAX_VALUE;
	public static final int ITEM_TYPE_NORMAL = Integer.MAX_VALUE-1;
	private int mState = STATE_MORE;

	private SuperRefreshLayout.ILoadCallBack mCallBack;
	protected int mLoadMoreText;
	protected int mLoadFinishText;
	protected int mNoDataText;
	protected int mLoadErrorText;

	public BaseRecycleAdapter(int layoutId){
		this.mLayoutId = layoutId;
		this.mLoadMoreText = R.string.loading;
		this.mLoadFinishText = R.string.loading_no_more;
		this.mNoDataText = R.string.error_view_no_data;
		this.mLoadErrorText = R.string.error_view_load_error_click_to_refresh;
	}

	public void setState(int state){
		this.mState = state;
	}

	public int getState(){
		return mState;
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		mContext = parent.getContext();
		View view = null;
		RecyclerView.ViewHolder viewHolder = null;
		if(viewType == ITEM_TYPE_NORMAL) {
			view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
			viewHolder = getViewHolder(view);
		}else if(viewType == ITEM_TYPE_FOOTER){
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_footer, parent, false);
			viewHolder = new FooterViewHolder(view);
		}
		return viewHolder;
	}

	protected RecyclerView.ViewHolder getViewHolder(View view){
		RecyclerView.ViewHolder viewHolder = new BaseViewHolder(view);
		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		return  viewHolder;
	}

	@Override
	public int getItemViewType(int position) {
		if(hasFooterView()){
			if(position == getItemCount()-1){
				return ITEM_TYPE_FOOTER;
			}else{
				return ITEM_TYPE_NORMAL;
			}
		}
		return ITEM_TYPE_NORMAL;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(getItemViewType(position) == ITEM_TYPE_FOOTER && holder instanceof FooterViewHolder){
			FooterViewHolder viewHolder = (FooterViewHolder) holder;
			switch (mState){
				case STATE_MORE:
					viewHolder.msgView.setText(mLoadMoreText);
					viewHolder.progressBar.setVisibility(View.VISIBLE);
					break;
				case STATE_NO_MORE:
					viewHolder.msgView.setText(mLoadFinishText);
					viewHolder.progressBar.setVisibility(View.GONE);
					break;
				case STATE_LOADING:
					viewHolder.msgView.setText(mLoadMoreText);
					viewHolder.progressBar.setVisibility(View.VISIBLE);
					break;
				case STATE_NO_DATA:
					viewHolder.msgView.setText(mNoDataText);
					viewHolder.progressBar.setVisibility(View.GONE);
					break;
				case STATE_ERROR://加载失败，点击重新加载
					viewHolder.progressBar.setVisibility(View.GONE);
					viewHolder.msgView.setText(mLoadErrorText);
					viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if(mCallBack != null) mCallBack.onRefresh();
						}
					});
					break;
			}
		}else{
			convert(holder, position);
		}
	}

	public abstract void convert(RecyclerView.ViewHolder holder,int position);

	public void setDataList(List<T> dataList){
		if(dataList == null)return;
		this.mDataList = dataList;
		notifyDataSetChanged();
	}

	public List<T> getData() {
		return mDataList == null ? (mDataList = new ArrayList<T>()) : mDataList;
	}

	public void addDataList(List<T> dataList){
		if(dataList == null) return;
		if(mDataList == null){
			mDataList = new ArrayList<>();
		}
		mDataList.addAll(dataList);
		notifyDataSetChanged();
	}

	public void removeAll(){
		if(mDataList != null){
			mDataList.clear();
			notifyDataSetChanged();
		}
	}

	public void remove(T t){
		int position = mDataList.indexOf(t);
		notifyItemInserted(position);
		mDataList.remove(t);
	}

	protected boolean hasFooterView(){
		return true;
	}

	public void clear(){
		if(mDataList != null){
			mDataList.clear();
			notifyDataSetChanged();
		}
	}

	@Override
	public int getItemCount() {
		return mDataList.size() + (hasFooterView()?1:0);
	}

	public static class FooterViewHolder extends RecyclerView.ViewHolder{
		@Bind(R.id.progressbar)ProgressBar progressBar;
		@Bind(R.id.text)TextView msgView;
		public FooterViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this,itemView);
		}
	}

	public static class BaseViewHolder extends RecyclerView.ViewHolder{
		private SparseArray<View> mViews;

		public BaseViewHolder(){
			this(null);
		}
		public BaseViewHolder(View itemView) {
			super(itemView);
			mViews = new SparseArray<View>();
		}

		public <T extends View>T findView(int viewId){
			View view = mViews.get(viewId);
			if(view == null){
				view = ButterKnife.findById(itemView,viewId);
				mViews.put(viewId,view);
			}
			return (T)view;
		}

		public BaseViewHolder setText(int viewId, String text) {
			TextView tv = findView(viewId);
			tv.setText(text);
			return this;
		}
		public BaseViewHolder setText(int viewId, int text) {
			TextView tv = findView(viewId);
			tv.setText(text);
			return this;
		}
		public BaseViewHolder setImageResource(int viewId, int imageResourceId) {
			ImageView image = findView(viewId);
			image.setImageResource(imageResourceId);
			return this;
		}
		public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
			ImageView image = findView(viewId);
			image.setImageBitmap(bitmap);
			return this;
		}
		public BaseViewHolder setImageFromUrl(int viewId, String url) {
			ImageView image = findView(viewId);
			//TODO 显示图片
			//WChatUtil.showImage(itemView.getContext(),image,url, R.drawable.common_default_img);
			return this;
		}

		public BaseViewHolder setCicleImageFromUrl(int viewId, String url) {
			ImageView image = findView(viewId);
			//TODO 显示图片
			//WChatUtil.showCircleImage(itemView.getContext(),image,url, R.drawable.common_default_img);
			return this;
		}
	}


	public void setICallBack(SuperRefreshLayout.ILoadCallBack callBack){
		this.mCallBack = callBack;
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		//处理网格布局,最后的item占行
		if(hasFooterView()){
			RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
			if(layoutManager instanceof GridLayoutManager){
				GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
				final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
				final int lastSpanCount = gridLayoutManager.getSpanCount();
				gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
					@Override
					public int getSpanSize(int position) {
						return position == getItemCount()-1?lastSpanCount:(spanSizeLookup == null?1:spanSizeLookup.getSpanSize(position));
					}
				});
			}
		}else{
			super.onAttachedToRecyclerView(recyclerView);
		}
	}

	@Override
	public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
		//处理瀑布流 最后的item占行
		if(hasFooterView() && holder instanceof FooterViewHolder){
			ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
			if(lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
				StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
				p.setFullSpan(true);
			}
		}else{
			super.onViewAttachedToWindow(holder);
		}

	}
}
