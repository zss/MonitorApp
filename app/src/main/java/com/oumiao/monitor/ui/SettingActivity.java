package com.oumiao.monitor.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oumiao.monitor.R;
import com.oumiao.monitor.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Description:
 * Author:zss
 * Date:17/2/8
 */

public class SettingActivity extends BaseActivity {
    @Bind(R.id.iv_name_arrow_right)
    ImageView ivNameArrowRight;
    @Bind(R.id.lay_account)
    RelativeLayout layAccount;
    @Bind(R.id.iv_number_arrow_right)
    ImageView ivNumberArrowRight;
    @Bind(R.id.lay_msg)
    RelativeLayout layMsg;
    @Bind(R.id.iv_gender_arrow_right)
    ImageView ivGenderArrowRight;
    @Bind(R.id.lay_common)
    RelativeLayout layCommon;
    @Bind(R.id.lay_about_us)
    RelativeLayout layAboutUs;
    @Bind(R.id.btn_logout)
    TextView btnLogout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
       setActionBarTitle("设置");
    }


    @OnClick({R.id.lay_account, R.id.lay_msg, R.id.lay_common, R.id.lay_about_us, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_account:
                break;
            case R.id.lay_msg:

                break;
            case R.id.lay_common:

                break;
            case R.id.lay_about_us:

                break;
            case R.id.btn_logout:
                break;
        }
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
}
