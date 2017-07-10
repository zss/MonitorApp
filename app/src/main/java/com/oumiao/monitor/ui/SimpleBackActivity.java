package com.oumiao.monitor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.oumiao.monitor.R;
import com.oumiao.monitor.base.BaseActivity;
import com.oumiao.monitor.base.BaseFragment;
import com.oumiao.monitor.model.SimpleBackPage;

import java.lang.ref.WeakReference;

import butterknife.Bind;


public class SimpleBackActivity extends BaseActivity {
    public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
    public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";
    public final static String BUNDLE_KEY_TITLE = "BUNDLE_KEY_TITLE";
    public final static String BUNDLE_KEY_USE_COORD_LAYOUT = "BUNDLE_KEY_USE_COORD_LAYOUT";//是否使用CollapsingToolbarLayout，当滑动时，自动隐藏titlebar
    private static final String TAG = "FLAG_TAG";
    protected WeakReference<Fragment> mFragments;
    protected int mPageValue = -1;
    protected boolean isUseCoordLayout = false;
    private Fragment mFragment;
    @Bind(R.id.bottom_lay)LinearLayout mBottomLay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getIntent().getBundleExtra(BUNDLE_KEY_ARGS);
        if (args != null) {
            isUseCoordLayout = args.getBoolean(BUNDLE_KEY_USE_COORD_LAYOUT, false);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return isUseCoordLayout ? R.layout.activity_new_simple_fragment : R.layout.activity_simple_fragment;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    public LinearLayout getBottomLayout(){
        return mBottomLay;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (mPageValue == -1) {
            mPageValue = getIntent().getIntExtra(BUNDLE_KEY_PAGE, 0);
        }
        initFromIntent(mPageValue, getIntent());
    }

    protected void initFromIntent(int pageValue, Intent data) {
        if (data == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }
        SimpleBackPage page = SimpleBackPage.getPageByValue(pageValue);
        if (page == null) {
            throw new IllegalArgumentException("can not find page by value:"
                    + pageValue);
        }

        setActionBarTitle(page.getTitle());

        try {
//            Fragment fragment = (Fragment) page.getClz().newInstance();

            mFragment = Fragment.instantiate(this, page.getClz().getName());

            Bundle args = data.getBundleExtra(BUNDLE_KEY_ARGS);
            if (args != null) {
                mFragment.setArguments(args);
                if(args.containsKey(BUNDLE_KEY_TITLE)){
                    setActionBarTitle(args.getString(BUNDLE_KEY_TITLE));
                }
            }

            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.container, mFragment, TAG);
            trans.commitAllowingStateLoss();

            mFragments = new WeakReference<Fragment>(mFragment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "generate fragment error. by value:" + pageValue);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {
        if (mFragments != null && mFragments.get() != null
                && mFragments.get() instanceof BaseFragment) {
            BaseFragment bf = (BaseFragment) mFragments.get();
            if (!bf.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN
                && mFragments.get() instanceof BaseFragment) {
            ((BaseFragment) mFragments.get()).onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if(mFragment != null){
            mFragment.onActivityResult(arg0,arg1,arg2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mFragment != null)
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView() {}

    @Override
    public void initData() {}
}
