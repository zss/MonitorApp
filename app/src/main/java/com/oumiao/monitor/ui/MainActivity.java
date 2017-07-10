package com.oumiao.monitor.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.oumiao.monitor.R;
import com.oumiao.monitor.base.BaseActivity;
import com.oumiao.monitor.fragment.FanStatusFragment;
import com.oumiao.monitor.fragment.HistoryDataFragment;
import com.oumiao.monitor.fragment.SettingFragment;
import com.oumiao.monitor.fragment.RealDataFragment;
import com.oumiao.monitor.fragment.ThreSholdFragment;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.container)
    FrameLayout container;
    @Bind(R.id.bottomBar)
    BottomNavigationBar bottomBar;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;

    private RealDataFragment realDataFragment;
    private HistoryDataFragment historyDataFragment;
    private ThreSholdFragment threSholdFragment;
    private FanStatusFragment fanStatusFragment;
    private SettingFragment settingFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        final BottomNavigationItem realTimeTabItem = new BottomNavigationItem(R.drawable.ic_home_grey_400_24dp,R.string.main_tab_real_time);
        final BottomNavigationItem historyTabItem = new BottomNavigationItem(R.drawable.ic_home_grey_400_24dp,R.string.main_tab_history);
        BottomNavigationItem thresholdTabItem = new BottomNavigationItem(R.drawable.ic_home_grey_400_24dp,R.string.main_tab_threshold);
        BottomNavigationItem fanStatusTabItem = new BottomNavigationItem(R.drawable.ic_home_grey_400_24dp,R.string.main_tab_fan_status);
        BottomNavigationItem settingTabItem = new BottomNavigationItem(R.drawable.ic_home_grey_400_24dp,R.string.main_tab_setting);
        bottomBar.setInActiveColor(R.color.infoTextColor);
        bottomBar.setActiveColor(R.color.colorPrimary);


        bottomBar.addItem(realTimeTabItem);
        bottomBar.addItem(historyTabItem);
        bottomBar.addItem(thresholdTabItem);
        bottomBar.addItem(fanStatusTabItem);
        bottomBar.addItem(settingTabItem);
        bottomBar.setFirstSelectedPosition(0);
        bottomBar.initialise();
        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (position){
                    case 0:
                        if(realDataFragment == null){
                            realDataFragment = new RealDataFragment();
                        }
                        fragmentTransaction.replace(R.id.container,realDataFragment);
                        setActionBarTitle(R.string.app_name);
                        break;
                    case 1:
                        if(historyDataFragment == null){
                            historyDataFragment = new HistoryDataFragment();
                        }
                        fragmentTransaction.replace(R.id.container,historyDataFragment);
                        setActionBarTitle(R.string.main_tab_history);
                        break;

                    case 2:
                        if(threSholdFragment == null){
                            threSholdFragment = new ThreSholdFragment();
                        }
                        fragmentTransaction.replace(R.id.container,threSholdFragment);
                        setActionBarTitle(R.string.main_tab_threshold);
                        break;
                    case 3:
                        if(fanStatusFragment == null){
                            fanStatusFragment = new FanStatusFragment();
                        }
                        fragmentTransaction.replace(R.id.container,fanStatusFragment);
                        setActionBarTitle(R.string.main_tab_fan_status);
                        break;
                    case 4:
                        if(settingFragment == null){
                            settingFragment = new SettingFragment();
                        }
                        fragmentTransaction.replace(R.id.container,settingFragment);
                        setActionBarTitle(R.string.main_tab_setting);
                        break;
                }
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        setDefaultFragment();
    }

    private void setDefaultFragment(){
        setActionBarTitle(R.string.app_name);
        realDataFragment = new RealDataFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container,realDataFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void initActionBar() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean hasBackButton() {
        return false;
    }
}
