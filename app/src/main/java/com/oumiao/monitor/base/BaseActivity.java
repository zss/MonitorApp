package com.oumiao.monitor.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.oumiao.monitor.AppManager;
import com.oumiao.monitor.R;
import com.oumiao.monitor.interf.BaseViewInterface;
import com.oumiao.monitor.interf.DialogControl;
import com.oumiao.monitor.utils.DialogHelp;
import com.oumiao.monitor.utils.StringUtils;
import com.oumiao.monitor.utils.TLog;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 *setNavigationOnClickListener只能在其后setSupportActionBar设置
 * Toolbar title只能在setSupportActionBar方法之前设置，如果设置后，需要修改title，需要重新调用setSupportActionBar更新
 */
public abstract class BaseActivity extends AppCompatActivity implements DialogControl,View.OnClickListener,BaseViewInterface {
	private boolean _isVisible;
	private ProgressDialog _waitDialog;
	protected LayoutInflater mInflater;
	protected Toolbar mToolbar;
	protected int titleResId = R.string.app_name;
	protected boolean mCheckLogin = true;
	protected TextView mActionBarTitleView;


	@Override
	protected void onDestroy() {
		super.onDestroy();
		ButterKnife.unbind(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		Bundle bundle = getIntent().getExtras();
		if(!hasActionBar()){
			supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		onBeforeSetContentLayout();
		if(getLayoutId() != 0){
			setContentView(getLayoutId());
			ButterKnife.bind(this);
		}
		mInflater = getLayoutInflater();
		if(hasActionBar()){
			mToolbar = (Toolbar)findViewById(R.id.toolbar);
			mActionBarTitleView = (TextView) mToolbar.findViewById(R.id.toolbar_title);
			setActionBarTitle(getActionBarTitle());
			setSupportActionBar(mToolbar);
			getSupportActionBar().setDisplayUseLogoEnabled(false);
			initActionBar();
		}
		init(savedInstanceState);
		initView();
		initData();
		_isVisible = true;
	}

	protected boolean isCheckUserLogin(){
		return mCheckLogin;
	}

	protected void onBeforeSetContentLayout() {}

	protected boolean hasActionBar() {
		return true;
	}

	protected int getLayoutId() {
		return 0;
	}
	protected View inflateView(int resId) {
		return mInflater.inflate(resId, null);
	}
	protected int getActionBarTitle() {
		return R.string.app_name;
	}
	protected boolean hasBackButton() {
		return true;
	}

	protected void init(Bundle savedInstanceState) {}

	protected void initActionBar(){
		if(mToolbar == null) return;
		if (hasBackButton()) {
			mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
			mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
	}

	public void hideBackButton() {
		if (mToolbar != null) {
			mToolbar.setNavigationIcon(null);

		}
	}

	public void setActionBarTitle(int resId) {
		if (resId != 0) {
			setActionBarTitle(getString(resId));
		}
	}


	public void setActionBarTitle(String title) {
		if (StringUtils.isEmpty(title)) {
			title = getString(R.string.app_name);
		}
		if (hasActionBar() && mToolbar != null) {
			if(mActionBarTitleView != null){
				mActionBarTitleView.setText(title);
				mToolbar.setTitle("");
			}else {
				mToolbar.setTitle(title);
			}
			setSupportActionBar(mToolbar);
			initActionBar();
		}
	}

	public void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener onMenuItemClickListener){
		if(hasActionBar() && mToolbar != null && onMenuItemClickListener != null){
			mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPause(this);
	}

	@Override
	public ProgressDialog showWaitDialog() {
		return showWaitDialog(R.string.loading);
	}

	@Override
	public ProgressDialog showWaitDialog(int resid) {
		return showWaitDialog(getString(resid));
	}

	@Override
	public ProgressDialog showWaitDialog(String message) {
		if (_isVisible) {
			if (_waitDialog == null) {
				_waitDialog = DialogHelp.getWaitDialog(this, message);
			}
			if (_waitDialog != null) {
				_waitDialog.setMessage(message);
				_waitDialog.show();
			}
			return _waitDialog;
		}
		return null;
	}

	@Override
	public void hideWaitDialog() {
		if (_isVisible && _waitDialog != null) {
			try {
				_waitDialog.dismiss();
				_waitDialog = null;
			} catch (Exception ex) {
				TLog.log("hideWaitDialog:"+ex.getMessage());
			}
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// setOverflowIconVisible(featureId, menu);
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public void onClick(View v) {
			finish();
	}


}
