package com.oumiao.monitor.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.oumiao.monitor.MonitorApp;
import com.oumiao.monitor.R;
import com.oumiao.monitor.utils.UIHelper;


/**
 * Description: loading页
 * Author:zss
 * Date:17/2/5
 */

public class SplashActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
    }


    private void initData(){
        //存储设备宽高信息
        MonitorApp.getInstance().saveDisplaySize(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UIHelper.showActivity(SplashActivity.this,LoginActivity.class);
                finish();
            }
        },2000);
    }
}
