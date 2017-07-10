package com.oumiao.monitor.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.oumiao.monitor.R;
import com.oumiao.monitor.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Description:
 * Author:zss
 */

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.btn_back)
    TextView btnBack;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_ver_code)
    EditText etVerCode;
    @Bind(R.id.btn_get_code)
    TextView btnGetCode;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_register)
    TextView btnRegister;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_register;
    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.btn_back, R.id.btn_get_code, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_get_code:
                break;
            case R.id.btn_register:

                break;
        }
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }
}
