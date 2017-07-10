package com.oumiao.monitor.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oumiao.monitor.R;
import com.oumiao.monitor.base.BaseFragment;
import com.oumiao.monitor.ui.SettingActivity;
import com.oumiao.monitor.utils.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Author:zss
 * Date:17/7/8
 */

public class SettingFragment extends BaseFragment {
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.lay_user_info)
    RelativeLayout layUserInfo;
    @Bind(R.id.iv_mine_ver_card)
    ImageView ivMineVerCard;
    @Bind(R.id.lay_mine_ver_card)
    RelativeLayout layMineVerCard;
    @Bind(R.id.iv_mine_comment)
    ImageView ivMineComment;
    @Bind(R.id.lay_comment)
    RelativeLayout layComment;
    @Bind(R.id.iv_mine_msg)
    ImageView ivMineMsg;
    @Bind(R.id.lay_msg)
    RelativeLayout layMsg;
    @Bind(R.id.iv_mine_setting)
    ImageView ivMineSetting;
    @Bind(R.id.lay_setting)
    RelativeLayout laySetting;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.lay_user_info, R.id.lay_mine_ver_card, R.id.lay_comment, R.id.lay_msg, R.id.lay_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lay_user_info:
                break;
            case R.id.lay_mine_ver_card:
                break;
            case R.id.lay_comment:
                break;
            case R.id.lay_msg:
                break;
            case R.id.lay_setting:
                UIHelper.showActivity(getActivity(), SettingActivity.class);
                break;
        }
    }
}
