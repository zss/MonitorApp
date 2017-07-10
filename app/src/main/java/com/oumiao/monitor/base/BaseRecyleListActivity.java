package com.oumiao.monitor.base;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.loopj.android.http.TextHttpResponseHandler;
import com.oumiao.monitor.R;
import com.oumiao.monitor.interf.OnRecyclerItemClicklistener;
import com.oumiao.monitor.model.Entity;
import com.oumiao.monitor.model.ListEntity;
import com.oumiao.monitor.ui.empty.EmptyLayout;
import com.oumiao.monitor.utils.Constants;
import com.oumiao.monitor.utils.TLog;
import com.oumiao.monitor.widget.DividerItemDecoration;
import com.oumiao.monitor.widget.SuperRefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

@SuppressLint("NewApi")
public abstract class BaseRecyleListActivity<T extends Entity> extends BaseActivity {

    @Bind(R.id.swipe_layout)
    protected SuperRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.lay_content)
    protected FrameLayout mContentLayout;

    @Bind(R.id.list)
    protected RecyclerView mListView;

    @Bind(R.id.rootLayout)
    protected LinearLayout mRootLayout;

    protected BaseRecycleAdapter<T> mAdapter;

    @Bind(R.id.fl_error_item)
    protected FrameLayout mTopTipLayout;

    @Bind(R.id.error_layout)
    protected EmptyLayout mErrorLayout;

    protected int mStoreEmptyState = -1;

    protected int mCurrentPage = 0;

    protected int mStartPage = 0;//请求数据开始页码数

    private ParserTask mParserTask;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pull_refresh_recyle_listview_simple;
    }

    @Override
    public void initView() {
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                refreshLoad();
            }
        });
        if (getLayoutManager() != null) {
            mListView.setLayoutManager(getLayoutManager());
        }
        if (getItemDecoration() != null) {
            mListView.addItemDecoration(getItemDecoration());
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAdapter.getState() == BaseRecycleAdapter.STATE_LOADING) return;
                mAdapter.setState(BaseRecycleAdapter.STATE_LOADING);
                mCurrentPage = mStartPage;
                setSwipeRefreshLoadingState();
                sendRequestData();
            }
        });

        mSwipeRefreshLayout.setILoadCallBack(new SuperRefreshLayout.ILoadCallBack() {
            @Override
            public void onRefresh() {
                mCurrentPage = mStartPage;
                setSwipeRefreshLoadingState();
                sendRequestData();
            }

            @Override
            public void loadMore() {
                mCurrentPage++;
                sendRequestData();
            }
        });

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        } else {
            mAdapter = getListAdapter();
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            mSwipeRefreshLayout.setAdapter(mListView, mAdapter);
            mSwipeRefreshLayout.autoRefresh();
        }

        if (mStoreEmptyState != -1) {
            mErrorLayout.setErrorType(mStoreEmptyState);
        }
    }

    /**
     * 重新加载
     */
    protected void refreshLoad() {
        mCurrentPage = mStartPage;
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        requestData();
    }

    public RecyclerView.LayoutManager getLayoutManager(){
        return getDefaultLayoutManager();
    }

    public RecyclerView.ItemDecoration getItemDecoration(){
        return getDefaultItemDecoration();
    }

    /**
     * 竖向布局
     *
     * @return
     */
    public LinearLayoutManager getDefaultLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }


    /**
     * 竖向布局的分割线
     *
     * @return
     */
    public RecyclerView.ItemDecoration getDefaultItemDecoration() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        return itemDecoration;
    }


    protected void setOnRecyclerItemClickListener(OnRecyclerItemClicklistener onRecyclerItemClickListener) {
        if (onRecyclerItemClickListener != null)
            mListView.addOnItemTouchListener(onRecyclerItemClickListener);
    }


    @Override
    public void onDestroy() {
        mStoreEmptyState = mErrorLayout.getErrorState();
        cancelParserTask();
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected abstract BaseRecycleAdapter<T> getListAdapter();

    protected ListEntity<T> parseList(String result) {
        return null;
    }

    protected ListEntity<T> readList(Serializable seri) {
        return null;
    }


    /***
     * 获取列表数据
     *
     * @return void
     */
    protected void requestData() {
        // 取新的数据
        sendRequestData();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    protected void sendRequestData() {
    }


    protected TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            executeOnLoadDataError(getResources().getString(R.string.tip_network_req_error));
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            executeParserTask(responseString);
        }
    };

    protected void executeOnLoadDataSuccess(List<T> data) {
        if (data == null) {
            data = new ArrayList<T>();
        }
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        if (mCurrentPage == mStartPage) {
            mAdapter.clear();
        }
        int dataCount = 0;
        if (mAdapter.hasFooterView()) {
            dataCount = mAdapter.getItemCount() - 1;
        } else {
            dataCount = mAdapter.getItemCount();
        }
        int state = BaseRecycleAdapter.STATE_MORE;
        if (data.isEmpty() && dataCount == 0) {
            state = BaseRecycleAdapter.STATE_NO_DATA;
        } else if (data.isEmpty() || data.size() < getPageSize()) {
            state = BaseRecycleAdapter.STATE_NO_MORE;
        }
        mAdapter.setState(state);
        mAdapter.addDataList(data);

        if (needShowEmptyNoData() && state == BaseRecycleAdapter.STATE_NO_DATA) {
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
        }
    }


    protected int getPageSize() {
        return Constants.PAGE_SIZE;
    }


    protected void executeOnLoadDataError(String error) {
        mErrorLayout.setErrorMessage(error);
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        mSwipeRefreshLayout.loadError();
        executeOnLoadFinish();

    }

    // 完成刷新
    protected void executeOnLoadFinish() {
        setSwipeRefreshLoadedState();
    }

    /**
     * 设置顶部正在加载的状态
     */
    protected void setSwipeRefreshLoadingState() {
        TLog.log("setSwipeRefreshLoadingState start");
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setRefreshEnable(false);
        }
    }

    /**
     * 设置顶部加载完毕的状态
     */
    protected void setSwipeRefreshLoadedState() {
        TLog.log("setSwipeRefreshLoadedState done");
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setRefreshEnable(true);
        }
    }

    private void executeParserTask(String data) {
        cancelParserTask();
        mParserTask = new ParserTask(data);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    protected boolean needShowEmptyNoData() {
        return true;
    }

    class ParserTask extends AsyncTask<Void, Void, ListEntity<T>> {

        private final String reponseData;
        private List<T> list;

        public ParserTask(String data) {
            this.reponseData = data;
        }

        @Override
        protected ListEntity<T> doInBackground(Void... params) {
            //解析xml数据
            ListEntity<T> data = parseList(reponseData);
            if (data != null) list = data.getList();
            return data;
        }

        @Override
        protected void onPostExecute(ListEntity<T> result) {
            super.onPostExecute(result);
            executeOnLoadDataSuccess(list);
            executeOnLoadFinish();
        }
    }

}
