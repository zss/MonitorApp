package com.oumiao.monitor.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.oumiao.monitor.R;
import com.oumiao.monitor.interf.BaseViewInterface;
import com.oumiao.monitor.interf.DialogControl;

import butterknife.ButterKnife;


/**
 * 碎片基类
 * 
 *
 */
public class BaseFragment extends Fragment implements
        View.OnClickListener, BaseViewInterface {
    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;//正在加载
    public static final int STATE_LOADMORE = 2;//加载更多
    public static final int STATE_NOMORE = 3;//没有更多
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;
    protected String mPageName;
    protected InputMethodManager inputMethodManager;
    protected LayoutInflater mInflater;
    protected Context context;
    protected View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mPageName = this.getClass().getName();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected String getPageName(){
        return mPageName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        if(getLayoutId() != 0){
            mView = inflateView(getLayoutId());
            ButterKnife.bind(this, mView);
        }else {
            mView = super.onCreateView(inflater, container, savedInstanceState);
        }
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart(getPageName());
    }

    @Override
    public void onPause() {
        super.onPause();
      // MobclickAgent.onPageEnd(getPageName());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected int getLayoutId() {
        return 0;
    }

    protected View inflateView(int resId) {
        return this.mInflater.inflate(resId, null);
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            ((DialogControl) activity).hideWaitDialog();
        }
    }

    protected ProgressDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    protected ProgressDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected ProgressDialog showWaitDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(str);
        }
        return null;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
