package com.oumiao.monitor;

import com.oumiao.monitor.base.BaseApplication;

/**
 * Description:
 * Author:zss
 * Date:17/7/8
 */

public class MonitorApp extends BaseApplication{
    private static MonitorApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MonitorApp getInstance(){
        return mInstance;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param versionCode
     * @return
     */
    public static boolean isMethodsCompat(int versionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= versionCode;
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        //TODO 清除APP缓存
    }
}
