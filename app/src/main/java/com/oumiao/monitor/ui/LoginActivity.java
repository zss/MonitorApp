package com.oumiao.monitor.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.oumiao.monitor.R;
import com.oumiao.monitor.base.BaseActivity;
import com.oumiao.monitor.utils.UIHelper;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Description: 登录页面
 * Author:zss
 */

public class LoginActivity extends BaseActivity {

    @Bind(R.id.btn_back)
    TextView btnBack;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_login)
    TextView btnLogin;
    @Bind(R.id.btn_register)
    TextView btnRegister;
    @Bind(R.id.btn_qq)
    TextView btnQq;
    @Bind(R.id.btn_wx)
    TextView btnWx;
    @Bind(R.id.btn_weibo)
    TextView btnWeibo;
    @Bind(R.id.btn_forgot_password)
    TextView btnForgotPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.btn_back, R.id.btn_login, R.id.btn_register, R.id.btn_qq, R.id.btn_wx, R.id.btn_weibo, R.id.btn_forgot_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_login:
                UIHelper.showActivity(LoginActivity.this,MainActivity.class);
                break;
            case R.id.btn_register:
                UIHelper.showActivity(LoginActivity.this,RegisterActivity.class);
                break;
            case R.id.btn_qq:
                break;
            case R.id.btn_wx:
                break;
            case R.id.btn_weibo:
                break;
            case R.id.btn_forgot_password:
                break;
        }
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }
}
