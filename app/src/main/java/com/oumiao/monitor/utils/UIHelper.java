package com.oumiao.monitor.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.oumiao.monitor.MonitorApp;
import com.oumiao.monitor.R;
import com.oumiao.monitor.fragment.BrowserFragment;
import com.oumiao.monitor.model.SimpleBackPage;
import com.oumiao.monitor.ui.LoginActivity;
import com.oumiao.monitor.ui.SimpleBackActivity;

import java.io.File;


/**
 * 界面帮助类
 * 
 *
 */
public class UIHelper {
    public static final int SELECT_CAMERA_REQ_CODE = 1000;//选择相机
    public static final int SELECT_PICTURE_REQ_CODE = 1001;//选择图库

    public static final int REQ_CODE_FOR_WRITE_STORE_PERMISSION = 1002;//请求获取本地sd卡读写权限
    public static final int REQ_CODE_FOR_CAMERA_PERMISSION = 1003;//请求获取拍照权限

    public static final int REQ_CODE_FOR_SEARCH_COMPANY = 1004;//搜索公司


    /**
     * 显示登录界面
     *
     * @param context
     */
    public static void showLoginActivity(Context context) {
        showLoginActivity(context, Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public static void showLoginActivity(Context context, int flag) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (flag != -1) intent.addFlags(flag);
        context.startActivity(intent);
    }

    /**
     * 打开系统中的浏览器
     *
     * @param context
     * @param url
     */
    public static void openSysBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            MonitorApp.showToastShort("无法浏览此网页");
        }
    }


    /**
     * 组合动态的回复文本
     *
     * @param name
     * @param body
     * @return
     */
    public static SpannableStringBuilder parseActiveReply(String name,
                                                          String body) {
        Spanned span = Html.fromHtml(body.trim());
        SpannableStringBuilder sp = new SpannableStringBuilder(name + "：");
        sp.append(span);
        // 设置用户名字体加粗、高亮
        // sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
        // name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")), 0,
                name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    /**
     * 发送App异常崩溃报告
     *
     * @param context
     */
    public static void sendAppCrashReport(final Context context) {
//        DialogHelp.getConfirmDialog(context, "程序发生异常", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // 退出
//                System.exit(-1);
//            }
//        }).show();
    }


    /**
     * 清除app缓存
     *
     * @param activity
     */
    public static void clearAppCache(Activity activity) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    MonitorApp.showToastShort("缓存清除成功");
                } else {
                    MonitorApp.showToastShort("缓存清除失败");
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    MonitorApp.getInstance().clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 打开内置浏览器
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {

//        if (StringUtils.isImgUrl(url)) {
//            ImagePreviewActivity.showImagePrivew(context, 0,
//                    new String[] { url });
//            return;
//        }
//
//        if (url.startsWith("http://www.oschina.net/tweet-topic/")) {
//            Bundle bundle = new Bundle();
//            int i = url.lastIndexOf("/");
//            if (i != -1) {
//                bundle.putString("topic",
//                        URLDecoder.decode(url.substring(i + 1)));
//            }
//            UIHelper.showSimpleBack(context, SimpleBackPage.TWEET_TOPIC_LIST,
//                    bundle);
//            return;
//        }
        try {
            // 启用外部浏览器
            // Uri uri = Uri.parse(url);
            // Intent it = new Intent(Intent.ACTION_VIEW, uri);
            // context.startActivity(it);
            Bundle bundle = new Bundle();
            bundle.putString(BrowserFragment.BROWSER_KEY, url);
            showSimpleBack(context, SimpleBackPage.BROWSER, bundle);
        } catch (Exception e) {
            e.printStackTrace();
            MonitorApp.showToastShort("无法浏览此网页");
        }
    }

    public static void showSimpleBackForResult(Fragment fragment,
                                               int requestCode, SimpleBackPage page){
        showSimpleBackForResult(fragment,requestCode,page,null);
    }

    public static void showSimpleBackForResult(Fragment fragment,
                                               int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(fragment.getActivity(),
                SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        if(args != null){
            intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
//            if(args.containsKey(Constants.INTENT_KEY_CHECK_LOGIN)){
//                intent.putExtra(Constants.INTENT_KEY_CHECK_LOGIN,args.getBoolean(Constants.INTENT_KEY_CHECK_LOGIN));
//            }
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Activity context,
                                               int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        if(args != null){
            intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
//            if(args.containsKey(Constants.INTENT_KEY_CHECK_LOGIN)){
//                intent.putExtra(Constants.INTENT_KEY_CHECK_LOGIN,args.getBoolean(Constants.INTENT_KEY_CHECK_LOGIN));
//            }
        }
        context.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Activity context,
                                               int requestCode, SimpleBackPage page) {
        showSimpleBackForResult(context,requestCode,page,null);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page) {
        showSimpleBack(context,page,null);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page,
                                      Bundle args) {
        context.startActivity(getSimpleBack(context, page, args, -1));
    }

    public static void showSimpleBack(Context context, SimpleBackPage page,
                                      Bundle args, int flags) {
        context.startActivity(getSimpleBack(context, page, args, flags));
    }

    public static Intent getSimpleBack(Context context, SimpleBackPage page, Bundle args) {
        return getSimpleBack(context, page, args, -1);
    }

    public static Intent getSimpleBack(Context context, SimpleBackPage page, Bundle args, int flags) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        if(args != null){
            intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
//            if(args.containsKey(Constants.INTENT_KEY_CHECK_LOGIN)){
//                intent.putExtra(Constants.INTENT_KEY_CHECK_LOGIN,args.getBoolean(Constants.INTENT_KEY_CHECK_LOGIN));
//            }
        }
        if (flags != -1) intent.setFlags(flags);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());

        return intent;
    }

    public static void showActivity(Context context, Class c, Bundle args){
       showActivity(context,c,args,-1);
    }

    public static void showActivity(Context context, Class c, Bundle args, int flag){
        Intent intent = new Intent(context, c);
        if(args != null) intent.putExtras(args);
        if(flag != -1) intent.addFlags(flag);
        context.startActivity(intent);
    }

    public static void showActivity(Context context, Class c){
        showActivity(context, c, null);
    }

    public static void showActivityForResult(Activity context, Class cl, Bundle args, int reqestCode){
        Intent intent = new Intent(context, cl);
        if(args != null) intent.putExtras(args);
        context.startActivityForResult(intent, reqestCode);
    }

    public static void showActivityForResult(Fragment fragment, Class cl, Bundle args, int reqestCode){
        Intent intent = new Intent(fragment.getActivity(), cl);
        if(args != null) intent.putExtras(args);
        fragment.startActivityForResult(intent, reqestCode);
    }

    public static void showActivityForResult(Activity context, Class cl, int reqestCode){
        showActivityForResult(context, cl,null, reqestCode);
    }

    public static void showActivityForResult(Fragment fragment, Class cl, int reqestCode){
        showActivityForResult(fragment, cl,null, reqestCode);
    }

//    public static void showSelectImageForResult(Activity context, Bundle args, int reqestCode){
//        Intent intent = new Intent(context, SelectImageAcrtivity.class);
//        if(args != null) intent.putExtras(args);
//        context.startActivityForResult(intent, reqestCode);
//    }
//
//    public static void showSelectImageForResult(Activity context, int reqestCode){
//        showSelectImageForResult(context, null, reqestCode);
//    }
//
//    public static void showSelectImageForResult(Fragment fragment, Bundle args, int reqestCode){
//        Intent intent = new Intent(fragment.getActivity(), SelectImageAcrtivity.class);
//        if(args != null) intent.putExtras(args);
//        fragment.startActivityForResult(intent, reqestCode);
//    }
//
//    public static void showSelectImageForResult(Fragment context, int reqestCode){
//        showSelectImageForResult(context, null, reqestCode);
//    }

    public static void postOnAnimation(View view, Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            SDK16.postOnAnimation(view, runnable);
        } else {
            view.postDelayed(runnable, 16);
        }
    }

    public static void setBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT >= 16) {
            SDK16.setBackground(view, background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static void setBackground(View view, int resId) {
        Drawable background = view.getContext().getResources().getDrawable(resId);
        if (Build.VERSION.SDK_INT >= 16) {
            SDK16.setBackground(view, background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static void setLayerType(View view, int layerType) {
        if (Build.VERSION.SDK_INT >= 11) {
            SDK11.setLayerType(view, layerType);
        }
    }

    @TargetApi(11)
    static class SDK11 {
        public static void setLayerType(View view, int layerType) {
            view.setLayerType(layerType, null);
        }
    }

    @TargetApi(16)
    static class SDK16 {

        public static void postOnAnimation(View view, Runnable runnable) {
            view.postOnAnimation(runnable);
        }

        public static void setBackground(View view, Drawable background) {
            view.setBackground(background);
        }

    }

    /**
     * 创建桌面快捷方式
     */
    public static void sendShotCut(Context context){
        try{
            boolean status = MonitorApp.getInstance().get(PreConstants.PRE_KEY_CREATE_SHOPCUT,false);
            if(status) return;
            Intent targetIntent = new Intent();
            targetIntent.setAction(Intent.ACTION_MAIN);
            targetIntent.addCategory("android.intent.category.LAUNCHER");
            ComponentName componentName = new ComponentName(context.getPackageName(), context.getClass().getName());
            targetIntent.setComponent(componentName);
            Intent intent = new Intent();
            //install_shortcut action
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
            Parcelable iconResource = Intent.ShortcutIconResource
                    .fromContext(context, R.mipmap.ic_launcher);
            //设置快捷方式图标
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,  iconResource);
            //不能重复多个
            intent.putExtra("duplicate", false);
            context.sendBroadcast(intent);
            MonitorApp.getInstance().set(PreConstants.PRE_KEY_CREATE_SHOPCUT, true);
        }catch(Exception e){
            TLog.log("shotcut icon create failed!");
        }
    }

    /**
     * 打开相机
     * @param context
     */
    public static void showCameraAction(Fragment context, File tempFile){
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(context.getActivity().getPackageManager()) != null){
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            context.startActivityForResult(cameraIntent, SELECT_CAMERA_REQ_CODE);
        }else{
            MonitorApp.getInstance().showToast(R.string.tip_no_camera);
        }
    }

    public static void showCameraAction(Activity context, File tempFile){
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(context.getPackageManager()) != null){
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            context.startActivityForResult(cameraIntent, SELECT_CAMERA_REQ_CODE);
        }else{
            MonitorApp.getInstance().showToast(R.string.tip_no_camera);
        }
    }

//	/**
//     * 设置textview显示富文本，超链接，email，表情特别显示
//     * @param context
//     * @param view
//     * @param content
//     */
//    public static void setContentHtml(Context context,TextView view,String content){
//        if(StringUtils.isNotEmpty(content)) {
//            content = content.replace(System.getProperty("line.separator"),"<br/>").replace("&gt;",">").replace("&lt;","<");
//            Spanned span = Html.fromHtml(content);
//            span = InputHelper.displayEmoji(context.getResources(), span);
//            view.setText(span);
//            MyURLSpan.parseLinkText(view, span);
//        }else{
//            view.setText("");
//        }
//    }

    public static Bundle getUseCoordLayoutBundle(boolean isUse) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(SimpleBackActivity.BUNDLE_KEY_USE_COORD_LAYOUT, isUse);
        return bundle;
    }

    /**
     * 设置view选中样式
     *
     * @param context
     * @param view
     */
    public static void setLayItemBg(Context context, View view) {
        Drawable drawable = null;
        if (Build.VERSION.SDK_INT >= 21) {
            drawable = context.getResources().getDrawable(R.drawable.ripple_bg, null);
        } else {
            drawable = context.getResources().getDrawable(R.drawable.day_list_item_background);
        }
        setBackground(view, drawable);
    }







}

